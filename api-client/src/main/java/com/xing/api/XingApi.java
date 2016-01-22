/*
 * Copyright (С) 2015 XING AG (http://xing.com/)
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

import com.squareup.moshi.Moshi;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.xing.api.internal.GeoCodeJsonAdapter;
import com.xing.api.internal.json.BirthDateJsonAdapter;
import com.xing.api.internal.json.ContactPathJsonAdapter;
import com.xing.api.internal.json.CsvCollectionJsonAdapter;
import com.xing.api.internal.json.NullDoubleJsonAdapter;
import com.xing.api.internal.json.NullIntJsonAdapter;
import com.xing.api.internal.json.PhoneJsonAdapter;
import com.xing.api.internal.json.SafeCalendarJsonAdapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.xing.api.Utils.checkNotNull;
import static com.xing.api.Utils.stateNull;

/**
 * TODO docs.
 *
 * @author serj.lotutovici
 */
public final class XingApi {
    @SuppressWarnings("CollectionWithoutInitialCapacity")
    private final Map<Class<? extends Resource>, Resource> resourcesCache = new LinkedHashMap<>();

    private final OkHttpClient client;
    private final HttpUrl apiEndpoint;
    private final Moshi converter;

    XingApi(OkHttpClient client, HttpUrl apiEndpoint, Moshi converter) {
        this.client = client;
        this.apiEndpoint = apiEndpoint;
        this.converter = converter;
    }

    /** Return a {@link Resource} instance specified by the provided class. */
    @SuppressWarnings("unchecked")
    public <T extends Resource> T resource(Class<T> resource) {
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

    /** Returns the api endpoint for <strong>this</strong> client instance. */
    public HttpUrl apiEndpoint() {
        return apiEndpoint;
    }

    /**
     * Returns the json converter ({@linkplain Moshi} instance) associated with <strong>this</strong> client
     * instance.
     */
    public Moshi converter() {
        return converter;
    }

    OkHttpClient client() {
        return client;
    }

    /** Throws an exception if class was declared non-static or non-final. */
    private static void checkResourceClassDeclaration(Class<? extends Resource> resource) {
        int modifiers = resource.getModifiers();
        if (!Modifier.isFinal(modifiers)) throw new IllegalArgumentException("Resource class must be declared final.");
        if (resource.isLocalClass() || (resource.isMemberClass() && !Modifier.isStatic(modifiers))) {
            throw new IllegalArgumentException("Resource class must be declared static.");
        }
    }

    /**
     * TODO docs.
     */
    public static final class Builder {
        private final Oauth1SigningInterceptor.Builder oauth1Builder;
        private OkHttpClient client;
        private HttpUrl apiEndpoint;
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
            loggedOut = true;
            return this;
        }

        public Builder client(OkHttpClient client) {
            this.client = checkNotNull(client, "client == null");
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

        public Builder apiEndpoint(HttpUrl baseUrl) {
            apiEndpoint = checkNotNull(baseUrl, "apiEndpoint == null");
            return this;
        }

        /**
         * Adds a moshi instance to build on <b>top</b>.
         * <p>
         * <b>DISCLAIMER: </b> This will extract all custom adapter factories declared in the provided instance
         * and create a builder to which internal declared factories will be added. Keep in mind that for {@linkplain
         * Moshi} the order of custom factories maters and the resulting object will favor factories from the
         * provided {@linkplain Moshi moshi}, which can brake expected behaviour. This means that by adding your own
         * {@link Moshi} you may override adapters declared internally by {@link XingApi}, but you should do that at
         * your own risk.
         * <p>
         * <b>NOTE: </b>This method can be called only once, otherwise an exception will be thrown.
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
            if (this.client != null) {
                client = this.client;
            } else {
                client = new OkHttpClient();
            }

            // If the api is build in logged out mode, no need to build oauth interceptor.
            if (!loggedOut) {
                // This makes sure that signing is always first in line.
                client.interceptors().add(0, oauth1Builder.build());
            }

            // Add the custom JSON Adapters to Moshi
            if (moshiBuilder == null) moshiBuilder = new Moshi.Builder();
            moshiBuilder.add(CompositeTypeJsonAdapter.FACTORY);
            moshiBuilder.add(ContactPathJsonAdapter.FACTORY);
            moshiBuilder.add(NullIntJsonAdapter.FACTORY);
            moshiBuilder.add(NullDoubleJsonAdapter.FACTORY);
            moshiBuilder.add(BirthDateJsonAdapter.FACTORY);
            moshiBuilder.add(SafeCalendarJsonAdapter.FACTORY);
            moshiBuilder.add(PhoneJsonAdapter.FACTORY);
            moshiBuilder.add(CsvCollectionJsonAdapter.FACTORY);
            moshiBuilder.add(GeoCodeJsonAdapter.FACTORY);

            return new XingApi(client, apiEndpoint, moshiBuilder.build());
        }
    }
}
