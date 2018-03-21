/*
 * Copyright (C) 2018 XING SE (http://xing.com/)
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

import com.serjltt.moshi.adapters.FallbackEnum;
import com.serjltt.moshi.adapters.FallbackOnNull;
import com.squareup.moshi.Moshi;
import com.xing.api.Resource.Factory;
import com.xing.api.internal.Experimental;
import com.xing.api.internal.json.BirthDateJsonAdapter;
import com.xing.api.internal.json.ContactPathJsonAdapter;
import com.xing.api.internal.json.CsvCollectionJsonAdapter;
import com.xing.api.internal.json.GeoCodeJsonAdapter;
import com.xing.api.internal.json.PhoneJsonAdapter;
import com.xing.api.internal.json.SafeCalendarJsonAdapter;
import com.xing.api.internal.json.SafeEnumJsonAdapter;
import com.xing.api.internal.json.TimeZoneJsonAdapter;
import com.xing.api.resources.BookmarksResource;
import com.xing.api.resources.ContactsResource;
import com.xing.api.resources.GroupsResource;
import com.xing.api.resources.JobsResource;
import com.xing.api.resources.MessagesResource;
import com.xing.api.resources.MiscellaneousResource;
import com.xing.api.resources.ProfileEditingResource;
import com.xing.api.resources.ProfileVisitsResource;
import com.xing.api.resources.RecommendationsResource;
import com.xing.api.resources.UserProfilesResource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

import okhttp3.HttpUrl;
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
    /** A list of built in factories for resources shipped with the library. */
    private static final List<Resource.Factory> BUILT_IN_FACTORIES = new ArrayList<>();

    static {
        BUILT_IN_FACTORIES.add(BookmarksResource.FACTORY);
        BUILT_IN_FACTORIES.add(ContactsResource.FACTORY);
        BUILT_IN_FACTORIES.add(GroupsResource.FACTORY);
        BUILT_IN_FACTORIES.add(JobsResource.FACTORY);
        BUILT_IN_FACTORIES.add(MessagesResource.FACTORY);
        BUILT_IN_FACTORIES.add(MiscellaneousResource.FACTORY);
        BUILT_IN_FACTORIES.add(ProfileEditingResource.FACTORY);
        BUILT_IN_FACTORIES.add(ProfileVisitsResource.FACTORY);
        BUILT_IN_FACTORIES.add(RecommendationsResource.FACTORY);
        BUILT_IN_FACTORIES.add(UserProfilesResource.FACTORY);
    }

    @SuppressWarnings("CollectionWithoutInitialCapacity")
    private final Map<Class<? extends Resource>, Resource> resourcesCache = new LinkedHashMap<>();
    private final List<AuthErrorCallback> authErrorCallbacks = new LinkedList<>();

    private final Set<Resource.Factory> resourceFactories;
    private final OkHttpClient client;
    private final HttpUrl apiEndpoint;
    private final Converter converter;
    private final CallbackAdapter callbackAdapter;
    private final Executor callbackExecutor;

    XingApi(OkHttpClient client, HttpUrl apiEndpoint, Converter converter, CallbackAdapter callbackAdapter,
          Executor callbackExecutor, List<Resource.Factory> resourceFactories) {
        this.client = client;
        this.apiEndpoint = apiEndpoint;
        this.converter = converter;
        this.callbackAdapter = callbackAdapter;
        this.callbackExecutor = callbackExecutor;

        /* Initialise the factories and add custom ones. */
        this.resourceFactories = new LinkedHashSet<>(resourceFactories);
        this.resourceFactories.addAll(BUILT_IN_FACTORIES);
    }

    /** Return a {@link Resource} instance specified by the provided class. */
    @SuppressWarnings("unchecked")
    public <T extends Resource> T resource(Class<T> resource) {
        Resource res = resourcesCache.get(checkNotNull(resource, "resource == null"));
        if (res == null) {
            // First try to create the resource via a factory
            for (Resource.Factory factory : resourceFactories) {
                res = factory.create(resource, this);

                if (res != null) { // yay!
                    resourcesCache.put(resource, res);
                    return (T) res;
                }
            }

            // Fallback to the reflection path
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

    /** Returns the json {@linkplain Moshi} instance associated with <strong>this</strong> client instance. */
    public Moshi moshi() {
        return converter.moshi();
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

    Converter converter() {
        return converter;
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
     * TODO
     *
     * @since 2.0.0
     */
    public static final class Builder {
        public OAuth1Step oauth1() {
            return new OAuth1Step();
        }

        public LoggedOutStep loggedOut() {
            return new LoggedOutStep();
        }

        @Experimental
        public CustomStep custom() {
            return new CustomStep();
        }
    }

    /**
     * // TODO
     *
     * Calling {@link OAuth1Step#consumerKey(String)}, {@link OAuth1Step#consumerSecret(String)},
     * {@link OAuth1Step#accessSecret(String)} and {@link OAuth1Step#accessToken(String)} is required to allow access
     * to logged in only content.
     *
     * @since 2.1.0
     */
    public static final class OAuth1Step extends BuildStep<OAuth1Step> {
        private final OAuth1SigningInterceptor.Builder oauth1Builder = new OAuth1SigningInterceptor.Builder();

        OAuth1Step() {
        }

        /** Sets the consumer key. Value must not be {@code null}. */
        public OAuth1Step consumerKey(String consumerKey) {
            oauth1Builder.consumerKey(consumerKey);
            return this;
        }

        /** Sets the consumer secret. Value must not be {@code null}. */
        public OAuth1Step consumerSecret(String consumerSecret) {
            oauth1Builder.consumerSecret(consumerSecret);
            return this;
        }

        /** Sets the access token. Value must not be {@code null}. */
        public OAuth1Step accessToken(String accessToken) {
            oauth1Builder.accessToken(accessToken);
            return this;
        }

        /** Sets the access secret. Value must not be {@code null}. */
        public OAuth1Step accessSecret(String accessSecret) {
            oauth1Builder.accessSecret(accessSecret);
            return this;
        }

        @Override
        OkHttpClient.Builder clientBuilder() {
            OkHttpClient.Builder builder = super.clientBuilder();
            builder.addInterceptor(oauth1Builder.build());
            return builder;
        }
    }

    /**
     * TODO.
     *
     * @since 2.1.0
     */
    public static final class LoggedOutStep extends BuildStep<LoggedOutStep> {
        LoggedOutStep() {
        }
    }

    /**
     * TODO.
     *
     * @since 2.1.0
     */
    public static final class CustomStep extends BuildStep<CustomStep> {
        CustomStep() {
        }
    }

    /**
     * TODO.
     *
     * @since 2.1.0
     */
    public static class BuildStep<T extends BuildStep> {
        private final List<Factory> resourceFactory = new ArrayList<>();
        private OkHttpClient.Builder clientBuilder;
        private Moshi.Builder moshiBuilder;
        private Executor callbackExecutor;
        private HttpUrl apiEndpoint;

        BuildStep() {
            apiEndpoint = HttpUrl.parse("https://api.xing.com/");
        }

        /** Add a resource factory for a specific {@linkplain Resource resource}. */
        public final T addResourceFactory(Resource.Factory factory) {
            resourceFactory.add(checkNotNull(factory, "factory == null"));
            return self();
        }

        /**
         * Change the api endpoint.
         * <p>
         * Should be used only for testing and if the <strong>consumer</strong>  has access to a staging endpoint.
         */
        public final T apiEndpoint(String apiEndpoint) {
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
        public final T apiEndpoint(HttpUrl baseUrl) {
            apiEndpoint = checkNotNull(baseUrl, "apiEndpoint == null");
            return self();
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
        public final T moshi(Moshi moshi) {
            stateNull(moshiBuilder, "Only one instance of Moshi is allowed");
            moshiBuilder = checkNotNull(moshi, "moshi == null").newBuilder();
            return self();
        }

        /** Sets the executor which will determine the thread on which callback will be invoked. */
        public final T callbackExecutor(Executor callbackExecutor) {
            this.callbackExecutor = checkNotNull(callbackExecutor, "callbackExecutor == null");
            return self();
        }

        public final T client(OkHttpClient client) {
            clientBuilder = checkNotNull(client, "client == null").newBuilder();
            return self();
        }

        OkHttpClient.Builder clientBuilder() {
            return clientBuilder != null ? clientBuilder : new OkHttpClient.Builder();
        }

        private T self() {
            //noinspection unchecked Protected by class definition.
            return (T) this;
        }

        public final XingApi build() {
            // Add the custom JSON Adapters to Moshi
            if (moshiBuilder == null) moshiBuilder = new Moshi.Builder();
            moshiBuilder.add(FallbackOnNull.ADAPTER_FACTORY);
            moshiBuilder.add(FallbackEnum.ADAPTER_FACTORY);
            moshiBuilder.add(SafeEnumJsonAdapter.FACTORY);
            moshiBuilder.add(ContactPathJsonAdapter.FACTORY);
            moshiBuilder.add(BirthDateJsonAdapter.FACTORY);
            moshiBuilder.add(SafeCalendarJsonAdapter.FACTORY);
            moshiBuilder.add(PhoneJsonAdapter.FACTORY);
            moshiBuilder.add(CsvCollectionJsonAdapter.FACTORY);
            moshiBuilder.add(GeoCodeJsonAdapter.FACTORY);
            moshiBuilder.add(TimeZoneJsonAdapter.FACTORY);

            // Select adapter by platform.
            CallbackAdapter adapter = Platform.get().callbackAdapter(callbackExecutor);
            Converter converter = new Converter(moshiBuilder.build());

            return new XingApi(clientBuilder().build(), apiEndpoint, converter, adapter, callbackExecutor, resourceFactory);
        }
    }
}
