/*
 * Copyright (C) 2015 XING AG (http://xing.com/)
 * Copyright (C) 2015 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xing.api;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okio.Buffer;
import okio.ByteString;

import static com.xing.api.Utils.checkNotNull;
import static com.xing.api.Utils.stateNotNull;

/**
 * Request {@link Interceptor} that handel's Oauth1 request signing.
 */
@SuppressWarnings("ClassNamingConvention")
final class OAuth1SigningInterceptor implements Interceptor {
    private static final Pattern CHARACTER_PATTERN = Pattern.compile("\\W");
    private static final int NUANCE_BYTES = 32;
    private static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
    private static final String OAUTH_NONCE = "oauth_nonce";
    private static final String OAUTH_SIGNATURE = "oauth_signature";
    private static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
    private static final String OAUTH_SIGNATURE_METHOD_VALUE = "HMAC-SHA1";
    private static final String OAUTH_TIMESTAMP = "oauth_timestamp";
    private static final String OAUTH_ACCESS_TOKEN = "oauth_token";
    private static final String OAUTH_VERSION = "oauth_version";
    private static final String OAUTH_VERSION_VALUE = "1.0";
    private static final String ENCRYPTION_TYPE = "HmacSHA1";
    private static final MediaType FORM_ENCODED_CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded");

    private final String consumerKey;
    private final String consumerSecret;
    private final String accessToken;
    private final String accessSecret;
    private final Random random;
    private final Clock clock;

    /**
     * Creates a new instance of <strong>this</strong> interceptor. {@linkplain XingApi} should use only one OAuth
     * interceptor.
     */
    OAuth1SigningInterceptor(String consumerKey, String consumerSecret, String accessToken, String accessSecret,
          Random random, Clock clock) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessToken = accessToken;
        this.accessSecret = accessSecret;
        this.random = random;
        this.clock = clock;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(signRequest(chain.request()));
    }

    /**
     * Returns the same request with an 'Authorization' header.
     *
     * @throws IOException If the request body could not be read.
     */
    Request signRequest(Request request) throws IOException {
        byte[] nonce = new byte[NUANCE_BYTES];
        random.nextBytes(nonce);
        String oauthNonce = CHARACTER_PATTERN.matcher(ByteString.of(nonce).base64()).replaceAll("");
        String oauthTimestamp = clock.millis();

        String consumerKeyValue = UrlEscapeUtils.escape(consumerKey);
        String accessTokenValue = UrlEscapeUtils.escape(accessToken);

        SortedMap<String, String> parameters = new TreeMap<>();
        parameters.put(OAUTH_CONSUMER_KEY, consumerKeyValue);
        parameters.put(OAUTH_ACCESS_TOKEN, accessTokenValue);
        parameters.put(OAUTH_NONCE, oauthNonce);
        parameters.put(OAUTH_TIMESTAMP, oauthTimestamp);
        parameters.put(OAUTH_SIGNATURE_METHOD, OAUTH_SIGNATURE_METHOD_VALUE);
        parameters.put(OAUTH_VERSION, OAUTH_VERSION_VALUE);

        HttpUrl url = request.httpUrl();
        for (int i = 0; i < url.querySize(); i++) {
            parameters.put(UrlEscapeUtils.escape(url.queryParameterName(i)),
                  UrlEscapeUtils.escape(url.queryParameterValue(i)));
        }

        Buffer body = new Buffer();
        RequestBody requestBody = request.body();
        if (requestBody != null && FORM_ENCODED_CONTENT_TYPE.equals(requestBody.contentType())) {
            requestBody.writeTo(body);
        }

        while (!body.exhausted()) {
            long keyEnd = body.indexOf((byte) '=');
            if (keyEnd == -1) throw new IllegalStateException("Key with no value: " + body.readUtf8());

            String key = body.readUtf8(keyEnd);
            body.skip(1); // Equals.

            long valueEnd = body.indexOf((byte) '&');
            String value = valueEnd == -1 ? body.readUtf8() : body.readUtf8(valueEnd);
            if (valueEnd != -1) body.skip(1); // Ampersand.

            parameters.put(key, value);
        }

        Buffer base = new Buffer();
        String method = request.method();
        base.writeUtf8(method);
        base.writeByte('&');
        base.writeUtf8(UrlEscapeUtils.escape(request.httpUrl().newBuilder().query(null).build().toString()));
        base.writeByte('&');

        boolean first = true;
        //noinspection SSBasedInspection
        for (Entry<String, String> entry : parameters.entrySet()) {
            if (!first) base.writeUtf8(UrlEscapeUtils.escape("&"));
            first = false;
            base.writeUtf8(UrlEscapeUtils.escape(entry.getKey()));
            base.writeUtf8(UrlEscapeUtils.escape("="));
            base.writeUtf8(UrlEscapeUtils.escape(entry.getValue()));
        }

        String signingKey = UrlEscapeUtils.escape(consumerSecret) + '&' + UrlEscapeUtils.escape(accessSecret);
        SecretKeySpec keySpec = new SecretKeySpec(signingKey.getBytes(), ENCRYPTION_TYPE);
        Mac mac;
        try {
            mac = Mac.getInstance(ENCRYPTION_TYPE);
            mac.init(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException(e);
        }
        byte[] result = mac.doFinal(base.readByteArray());
        String signature = ByteString.of(result).base64();

        String authorization = "OAuth "
              + OAUTH_CONSUMER_KEY + "=\"" + consumerKeyValue + "\", "
              + OAUTH_NONCE + "=\"" + oauthNonce + "\", "
              + OAUTH_SIGNATURE + "=\"" + UrlEscapeUtils.escape(signature) + "\", "
              + OAUTH_SIGNATURE_METHOD + "=\"" + OAUTH_SIGNATURE_METHOD_VALUE + "\", "
              + OAUTH_TIMESTAMP + "=\"" + oauthTimestamp + "\", "
              + OAUTH_ACCESS_TOKEN + "=\"" + accessTokenValue + "\", "
              + OAUTH_VERSION + "=\"" + OAUTH_VERSION_VALUE + '"';

        return request.newBuilder().addHeader("Authorization", authorization).build();
    }

    /** Simple builder class, to simplify interceptor initialization. */
    @SuppressWarnings({"DuplicateStringLiteralInspection", "JavaDoc"})
    public static final class Builder {
        private String consumerKey;
        private String consumerSecret;
        private String accessToken;
        private String accessSecret;
        private Random random = new SecureRandom();
        private Clock clock = new Clock();

        public Builder consumerKey(String consumerKey) {
            this.consumerKey = checkNotNull(consumerKey, "consumerKey == null");
            return this;
        }

        public Builder consumerSecret(String consumerSecret) {
            this.consumerSecret = checkNotNull(consumerSecret, "consumerSecret == null");
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = checkNotNull(accessToken, "accessToken == null");
            return this;
        }

        public Builder accessSecret(String accessSecret) {
            this.accessSecret = checkNotNull(accessSecret, "accessSecret == null");
            return this;
        }

        public Builder random(Random random) {
            this.random = checkNotNull(random, "random == null");
            return this;
        }

        /** Set clock, this is required mainly for testing (Or when we will have full Java 8 support). */
        public Builder clock(Clock clock) {
            this.clock = checkNotNull(clock, "clock == null");
            return this;
        }

        public OAuth1SigningInterceptor build() {
            stateNotNull(consumerKey, "consumerKey not set");
            stateNotNull(consumerSecret, "consumerSecret not set");
            stateNotNull(accessToken, "accessToken not set");
            stateNotNull(accessSecret, "accessSecret not set");
            return new OAuth1SigningInterceptor(consumerKey, consumerSecret, accessToken, accessSecret, random, clock);
        }
    }

    /** Simple clock like class, to allow time mocking. */
    @SuppressWarnings({"MethodMayBeStatic", "MagicNumber"}) // Required for mocking.
    static class Clock {
        /** Returns the current time in milliseconds divided by 1K. */
        public String millis() {
            return Long.toString(System.currentTimeMillis() / 1000L);
        }
    }
}
