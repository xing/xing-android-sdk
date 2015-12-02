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

import com.squareup.moshi.Moshi;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.xing.android.sdk.Utils.checkNotNull;
import static com.xing.android.sdk.Utils.stateNull;

/**
 * TODO docs.
 *
 * @author serj.lotutovici
 */
public final class XingApi {
    private final Map<Class<? extends Resource>, Resource> resourcesCache = new LinkedHashMap<>();
    final OkHttpClient okHttpClient;
    final Moshi converter;
    final HttpUrl apiEndpoint;

    private XingApi(OkHttpClient okHttpClient, HttpUrl apiEndpoint, Moshi converter) {
        this.okHttpClient = okHttpClient;
        this.apiEndpoint = apiEndpoint;
        this.converter = converter;
    }

    /** Return a {@link Resource} instance that specified by the provided class name. */
    @SuppressWarnings("unchecked")
    public <T extends Resource> T resource(Class<? extends Resource> resource) {
        Resource res = resourcesCache.get(checkNotNull(resource, "resource == null"));
        if (res == null) {
            checkResourceClassDeclaration(resource);
            try {
                Constructor<? extends Resource> constructor = resource.getDeclaredConstructor(XingApi.class);
                constructor.setAccessible(true);
                res = constructor.newInstance(this);
                resourcesCache.put(resource, res);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Resource class malformed.", ex);
            }
        }
        return (T) res;
    }

    /** Throws an exception if class was declared non-static. */
    private static void checkResourceClassDeclaration(Class<? extends Resource> resource) {
        if (resource.isLocalClass() || (resource.isMemberClass() && !Modifier.isStatic(resource.getModifiers()))) {
            throw new IllegalArgumentException("Resource class must be static.");
        }
    }

    /**
     * TODO docs.
     */
    public static final class Builder {
        private OkHttpClient okHttpClient;
        private HttpUrl apiEndpoint;
        private Oauth1SigningInterceptor.Builder oauth1Builder;
        private Moshi.Builder moshiBuilder;
        private boolean loggedOut;

        public Builder() {
            apiEndpoint = HttpUrl.parse("https://api.xing.com/");
            oauth1Builder = new Oauth1SigningInterceptor.Builder();
            loggedOut = false;
        }

        public Builder consumerKey(String consumerKey) {
            oauth1Builder.consumerKey(consumerKey);
            return this;
        }

        public Builder consumerSecret(String consumerSecret) {
            oauth1Builder.consumerSecret(consumerSecret);
            return this;
        }

        public Builder accessToken(String accessToken) {
            oauth1Builder.accessToken(accessToken);
            return this;
        }

        public Builder accessSecret(String accessSecret) {
            oauth1Builder.accessSecret(accessSecret);
            return this;
        }

        public Builder loggedOut() {
            this.loggedOut = true;
            return this;
        }

        public Builder client(OkHttpClient client) {
            this.okHttpClient = checkNotNull(client, "client == null");
            return this;
        }

        public Builder apiEndpoint(String apiEndpoint) {
            checkNotNull(apiEndpoint, "apiEndpoint == null");
            HttpUrl httpUrl = HttpUrl.parse(apiEndpoint);
            if (httpUrl == null) {
                throw new IllegalArgumentException("Illegal endpoint URL: " + apiEndpoint);
            }
            return apiEndpoint(httpUrl);
        }

        Builder apiEndpoint(HttpUrl baseUrl) {
            this.apiEndpoint = checkNotNull(baseUrl, "apiEndpoint == null");
            return this;
        }

        /**
         * Adds a moshi instance to build on <b>top</b>.
         * <p>
         * <b>DISCLAIMER: </b> This will extract all custom adapter factories declared in the provided instance
         * and create a builder to which internal declared factories will be added. Keep in mind that for {@link
         * Moshi} the order of custom factories maters and the resulting object will favor factories from the
         * provided {@linkplain Moshi moshi}, which can brake expected behaviour. This means that by adding your own
         * {@link Moshi} you may override adapters declared internally by {@link XingApi}, but you should do that at
         * your own risk.
         * <p>
         * <b>NOTE: </b> This method can be called only once, otherwise an exception will be thrown.
         *
         * @throws java.lang.IllegalStateException If the internal builder was already initialized.
         */
        public Builder moshi(Moshi moshi) {
            stateNull(moshiBuilder, "Only one instance of Moshi is allowed");
            moshiBuilder = checkNotNull(moshi, "moshi == null").newBuilder();
            return this;
        }

        public XingApi build() {
            // Setup the network client.
            OkHttpClient client;
            if (okHttpClient != null) {
                client = okHttpClient;
            } else {
                client = new OkHttpClient();
            }

            // If the api is build in logged out mode, no need to build oauth interceptor.
            if (!loggedOut) {
                client.networkInterceptors().add(oauth1Builder.build());
            }

            if (moshiBuilder == null) {
                moshiBuilder = new Moshi.Builder();
            }

            return new XingApi(client, apiEndpoint, moshiBuilder.build());
        }
    }
}
