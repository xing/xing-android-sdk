/*
 * Copyright (С) 2016 XING AG (http://xing.com/)
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
import com.xing.api.internal.json.BirthDateJsonAdapter;
import com.xing.api.internal.json.ContactPathJsonAdapter;
import com.xing.api.internal.json.CsvCollectionJsonAdapter;
import com.xing.api.internal.json.GeoCodeJsonAdapter;
import com.xing.api.internal.json.NullIntJsonAdapter;
import com.xing.api.internal.json.PhoneJsonAdapter;
import com.xing.api.internal.json.SafeCalendarJsonAdapter;
import com.xing.api.internal.json.SafeEnumJsonAdapter;
import com.xing.api.internal.json.TimeZoneJsonAdapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

import static com.xing.api.Utils.checkNotNull;
import static com.xing.api.Utils.stateNull;

/**
 * Main access point for the XING API. Creates and holds instances of {@linkplain Response resources} that provide access
 * points and response/error handling for XING APIs.
 * <p>
 * Usage:
 * <pre>{@code
 *           // Will instantiate ContactsResource.
 *          ContactsResource resource = xingApi.resource(ContactsResource.class);
 *     }</pre>
 * <p>
 * Two states of XingApi are supported:
 * <dl>
 * <li>Logged in - Requires a user's access token and token secret (See {@linkplain XingApi.Builder}.)</li>
 * <li>Logged out - See {@linkplain Builder#loggedOut()}</li>
 * </dl>
 *
 * @since 2.0.0
 */
public final class XingApi {
    @SuppressWarnings("CollectionWithoutInitialCapacity")
    private final Map<Class<? extends Resource>, Resource> resourcesCache = new LinkedHashMap<>();
    private final List<AuthErrorCallback> authErrorCallbacks = new LinkedList<>();

    private final OkHttpClient client;
    private final HttpUrl apiEndpoint;
    private final Moshi converter;
    private final CallbackAdapter callbackAdapter;
    private final Executor callbackExecutor;

    XingApi(OkHttpClient client, HttpUrl apiEndpoint, Moshi converter, CallbackAdapter callbackAdapter,
          Executor callbackExecutor) {
        this.client = client;
        this.apiEndpoint = apiEndpoint;
        this.converter = converter;
        this.callbackAdapter = callbackAdapter;
        this.callbackExecutor = callbackExecutor;
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

    /** Returns the executor throw which the callbacks will be invoked. */
    public Executor callbackExecutor() {
        return callbackExecutor;
    }

    /** Returns the {@linkplain OkHttpClient} associated with <strong>this</strong> instance. */
    public OkHttpClient client() {
        return client;
    }

    /** Adds an auth error callback that will be invoked each time the server responses with an auth failure. */
    public XingApi addAuthErrorCallback(AuthErrorCallback errorCallback) {
        authErrorCallbacks.add(errorCallback);
        return this;
    }

    /** Removes the callback from being notified when the server responds with an auth failure. */
    public XingApi removeAuthErrorCallback(AuthErrorCallback errorCallback) {
        authErrorCallbacks.remove(errorCallback);
        return this;
    }

    CallbackAdapter callbackAdapter() {
        return callbackAdapter;
    }

    /** Notify all callbacks that the server returned an auth error. */
    void notifyAuthError(Response<?, ResponseBody> rawResponse) {
        for (int i = 0, size = authErrorCallbacks.size(); i < size; i++) {
            AuthErrorCallback callback = authErrorCallbacks.get(i);
            if (callback != null) callbackAdapter.adapt(callback).onAuthError(rawResponse);
        }
    }

    /** Throws an exception if class was declared non-static or non-final. */
    private static void checkResourceClassDeclaration(Class<? extends Resource> resource) {
        int modifiers = resource.getModifiers();
        if (resource.isLocalClass() || (resource.isMemberClass() && !Modifier.isStatic(modifiers))) {
            throw new IllegalArgumentException("Resource class must be declared static.");
        }
    }

    /**
     * Build a new {@link XingApi}.
     * <p>
     * Calling {@link Builder#consumerKey(String)}, {@link Builder#consumerSecret(String)},
     * {@link Builder#accessSecret(String)} and {@link Builder#accessToken(String)} is required to allow access
     * to logged in only content. To create a {@link XingApi} in a logged out state call {@link Builder#loggedOut()}.
     * Other methods are optional.
     *
     * @since 2.0.0
     */
    public static final class Builder {
        private final OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        private final OAuth1SigningInterceptor.Builder oauth1Builder = new OAuth1SigningInterceptor.Builder();
        private HttpUrl apiEndpoint;
        private Moshi.Builder moshiBuilder;
        private boolean loggedOut;
        private Executor callbackExecutor;

        public Builder() {
            apiEndpoint = HttpUrl.parse("https://api.xing.com/");
            loggedOut = false;
        }

        /** Sets the consumer key. Value must not be {@code null}. */
        public Builder consumerKey(String consumerKey) {
            oauth1Builder.consumerKey(consumerKey);
            return this;
        }

        /** Sets the consumer secret. Value must not be {@code null}. */
        public Builder consumerSecret(String consumerSecret) {
            oauth1Builder.consumerSecret(consumerSecret);
            return this;
        }

        /** Sets the access token. Value must not be {@code null}. */
        public Builder accessToken(String accessToken) {
            oauth1Builder.accessToken(accessToken);
            return this;
        }

        /** Sets the access secret. Value must not be {@code null}. */
        public Builder accessSecret(String accessSecret) {
            oauth1Builder.accessSecret(accessSecret);
            return this;
        }

        /** Notifies the builder that the {@link XingApi} will run in logout mode. */
        public Builder loggedOut() {
            loggedOut = true;
            return this;
        }

        /** Adds an {@linkplain Interceptor interceptor} to the underlying {@linkplain OkHttpClient client}. */
        public Builder addInterceptor(Interceptor interceptor) {
            clientBuilder.addInterceptor(checkNotNull(interceptor, "interceptor == null"));
            return this;
        }

        /** Adds an {@linkplain Interceptor network interceptor} to the underlying {@linkplain OkHttpClient client}. */
        public Builder addNetworkInterceptor(Interceptor interceptor) {
            clientBuilder.addNetworkInterceptor(checkNotNull(interceptor, "interceptor == null"));
            return this;
        }

        /**
         * Change the api endpoint.
         * <p>
         * Should be used only for testing and if the <strong>consumer</strong>  has access to a staging endpoint.
         */
        public Builder apiEndpoint(String apiEndpoint) {
            HttpUrl httpUrl = HttpUrl.parse(checkNotNull(apiEndpoint, "apiEndpoint == null"));
            if (httpUrl == null) {
                throw new IllegalArgumentException("Illegal endpoint URL: " + apiEndpoint);
            }
            return apiEndpoint(httpUrl);
        }

        /**
         * Change the api endpoint.
         * <p>
         * Should be used only for testing and if the <strong>consumer</strong>  has access to a staging endpoint.
         */
        public Builder apiEndpoint(HttpUrl baseUrl) {
            apiEndpoint = checkNotNull(baseUrl, "apiEndpoint == null");
            return this;
        }

        /** Sets a {@link Cache} to the underlying  {@linkplain OkHttpClient client}. */
        public Builder cache(Cache cache) {
            clientBuilder.cache(checkNotNull(cache, "cache == null"));
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

        /** Sets the executor which will determine the thread on which callback will be invoked. */
        public Builder callbackExecutor(Executor callbackExecutor) {
            this.callbackExecutor = checkNotNull(callbackExecutor, "callbackExecutor == null");
            return this;
        }

        /** Create a {@link XingApi} instance using the provided values. */
        public XingApi build() {
            // If the api is build in logged out mode, no need to build oauth interceptor.
            if (!loggedOut) {
                clientBuilder.addInterceptor(oauth1Builder.build());
            }

            // Add the custom JSON Adapters to Moshi
            if (moshiBuilder == null) moshiBuilder = new Moshi.Builder();
            moshiBuilder.add(SafeEnumJsonAdapter.FACTORY);
            moshiBuilder.add(ContactPathJsonAdapter.FACTORY);
            moshiBuilder.add(NullIntJsonAdapter.FACTORY);
            moshiBuilder.add(BirthDateJsonAdapter.FACTORY);
            moshiBuilder.add(SafeCalendarJsonAdapter.FACTORY);
            moshiBuilder.add(PhoneJsonAdapter.FACTORY);
            moshiBuilder.add(CsvCollectionJsonAdapter.FACTORY);
            moshiBuilder.add(GeoCodeJsonAdapter.FACTORY);
            moshiBuilder.add(TimeZoneJsonAdapter.FACTORY);

            // Select adapter by platform.
            CallbackAdapter adapter = Platform.get().callbackAdapter(callbackExecutor);

            return new XingApi(clientBuilder.build(), apiEndpoint, moshiBuilder.build(), adapter, callbackExecutor);
        }
    }
}
