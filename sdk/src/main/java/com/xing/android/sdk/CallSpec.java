/*
 * Copyright (c) 2015 XING AG (http://xing.com/)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.xing.android.sdk;

import com.squareup.moshi.JsonAdapter;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.xing.android.sdk.internal.HttpMethod;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okio.Buffer;
import rx.Observable;

import static com.xing.android.sdk.Utils.assertionError;
import static com.xing.android.sdk.Utils.checkNotNull;
import static com.xing.android.sdk.Utils.stateNotNull;

/**
 * TODO docs.
 *
 * @author serj.lotutovici
 */
public final class CallSpec<RT, ET> {
    private final XingApi api;
    private final Builder<RT, ET> builder;
    // private final CompoundType compoundType;

    private CallSpec(Builder<RT, ET> builder) {
        api = builder.api;
        this.builder = builder;
    }

    public Response<RT, ET> execute() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void enqueue(Callback<RT> callback) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public Observable<Response<RT, ET>> stream() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    //TODO (DanielH) Provide conversion methods for responses.

    /**
     * TODO docs.
     */
    public static final class Builder<RT, ET> {
        private static final char[] HEX_DIGITS =
              {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        private static final String PATH_SEGMENT_ENCODE_SET = " \"<>^`{}|/\\?#";
        // Upper and lower characters, digits, underscores, and hyphens, starting with a character.
        private static final String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";
        private static final Pattern PARAM_NAME_REGEX = Pattern.compile(PARAM);
        private static final Pattern PARAM_URL_REGEX = Pattern.compile("\\{(" + PARAM + ")\\}");

        private final XingApi api;
        private final String method;
        private final HttpUrl apiEndpoint;
        private final Request.Builder requestBuilder;
        private final Set<String> resourcePathParams;
        private final boolean hasBody;

        private String resourcePath;
        private HttpUrl.Builder urlBuilder;
        private FormEncodingBuilder formEncodingBuilder;
        private RequestBody body;

        private Class<RT> responseCls;
        private Class<ET> errorCls;

        // For now block the possibility to build outside this package.
        Builder(XingApi api, @HttpMethod String method, String resourcePath, boolean hasBody, boolean isFormEncoded) {
            this.api = api;
            this.method = checkNotNull(method, "method == null");
            this.resourcePath = checkNotNull(resourcePath, "resourcePath == null");
            this.hasBody = hasBody;
            resourcePathParams = parseResourcePathParams(resourcePath);
            apiEndpoint = api.apiEndpoint;
            requestBuilder = new Request.Builder();

            if (isFormEncoded) {
                formEncodingBuilder = new FormEncodingBuilder();
            }
        }

        public Builder<RT, ET> pathParam(String name, String value) {
            stateNotNull(resourcePath, "Path params must be set before query params.");
            validatePathParam(name);
            resourcePath = resourcePath.replace('{' + name + '}', canonicalize(value));
            resourcePathParams.remove(name);
            return this;
        }

        public Builder<RT, ET> queryParam(String name, String value) {
            if (resourcePath != null) {
                // Do a one-time combination of the built relative URL and the base URL.
                urlBuilder = apiEndpoint.resolve(resourcePath).newBuilder();
                resourcePath = null;
            }

            urlBuilder.addQueryParameter(name, value);
            return this;
        }

        public Builder<RT, ET> formField(String name, String value) {
            formEncodingBuilder.add(name, value);
            return this;
        }

        public Builder<RT, ET> body(RequestBody body) {
            this.body = body;
            return this;
        }

        //TODO Avoid converting response body on main thread?
        public <U> Builder<RT, ET> body(Class<U> cls, U body) {
            JsonAdapter<U> jsonAdapter = api.converter.adapter(cls);
            return body(RequestBody.create(MediaType.parse("application/json"), jsonAdapter.toJson(body)));
        }

        //TODO (DanielH) Add support for a generic tree type. This is mandatory.
        public Builder<RT, ET> responseType(Class<RT> responseType) {
            responseCls = checkNotNull(responseType, "responseType == null");
            return this;
        }

        //TODO (DanielH) set a generic error type response if this is not set.
        public Builder<RT, ET> errorType(Class<ET> errorType) {
            errorCls = checkNotNull(errorType, "errorType == null");
            return this;
        }

        public CallSpec<RT, ET> build() {
            // TODO (SerjLtt) Validate that CallSpec has everything necessary for request execution.
            return new CallSpec<>(this);
        }

        //TODO (SerjLtt) Build request.
        Request request() {
            return null;
        }

        static String canonicalize(String input) {
            int codePoint;
            for (int i = 0, limit = input.length(); i < limit; i += Character.charCount(codePoint)) {
                codePoint = input.codePointAt(i);
                //noinspection MagicNumber
                if (codePoint < 0x20 || codePoint >= 0x7f || PATH_SEGMENT_ENCODE_SET.indexOf(codePoint) != -1
                      || codePoint == '%') {
                    // Slow path: the character at i requires encoding!
                    Buffer out = new Buffer();
                    out.writeUtf8(input, 0, i);
                    canonicalize(out, input, i, limit);
                    return out.readUtf8();
                }
            }

            // Fast path: no characters required encoding.
            return input;
        }

        static void canonicalize(Buffer out, String input, int pos, int limit) {
            Buffer utf8Buffer = null; // Lazily allocated.
            int codePoint;
            for (int i = pos; i < limit; i += Character.charCount(codePoint)) {
                codePoint = input.codePointAt(i);
                //noinspection StatementWithEmptyBody
                if (codePoint == '\t' || codePoint == '\n' || codePoint == '\f' || codePoint == '\r') {
                    // Skip this character.
                } else //noinspection MagicNumber
                    if (codePoint < 0x20 || codePoint >= 0x7f || PATH_SEGMENT_ENCODE_SET.indexOf(codePoint) != -1
                          || codePoint == '%') {
                        // Percent encode this character.
                        if (utf8Buffer == null) {
                            utf8Buffer = new Buffer();
                        }
                        utf8Buffer.writeUtf8CodePoint(codePoint);
                        while (!utf8Buffer.exhausted()) {
                            int b = utf8Buffer.readByte() & 0xff;
                            out.writeByte('%');
                            out.writeByte(HEX_DIGITS[(b >> 4) & 0xf]);
                            out.writeByte(HEX_DIGITS[b & 0xf]);
                        }
                    } else {
                        // This character doesn't need encoding. Just copy it over.
                        out.writeUtf8CodePoint(codePoint);
                    }
            }
        }

        /**
         * Gets the set of unique path parameters used in the given URI. If a parameter is used twice
         * in the URI, it will only show up once in the set.
         */
        static Set<String> parseResourcePathParams(String resourcePath) {
            Matcher matcher = PARAM_URL_REGEX.matcher(resourcePath);
            //noinspection CollectionWithoutInitialCapacity
            Set<String> patterns = new LinkedHashSet<>();
            while (matcher.find()) {
                patterns.add(matcher.group(1));
            }
            return patterns;
        }

        private void validatePathParam(String name) {
            if (!PARAM_NAME_REGEX.matcher(name).matches()) {
                throw assertionError("Path parameter name must match %s. Found: %s", PARAM_URL_REGEX.pattern(), name);
            }
            // Verify URL replacement name is actually present in the URL path.
            if (!resourcePathParams.contains(name)) {
                throw assertionError(
                      "Resource path \"%s\" does not contain \"{%s}\". Or the path parameter has ben already set.",
                      resourcePath, name);
            }
        }
    }
}
