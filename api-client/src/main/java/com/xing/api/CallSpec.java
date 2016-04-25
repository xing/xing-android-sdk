/*
 * Copyright (C) 2016 XING AG (http://xing.com/)
 * Copyright (C) 2016 Square, Inc.
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

import com.squareup.moshi.JsonAdapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import rx.Observable;

import static com.xing.api.UrlEscapeUtils.escape;
import static com.xing.api.Utils.assertionError;
import static com.xing.api.Utils.checkNotNull;
import static com.xing.api.Utils.stateError;
import static com.xing.api.Utils.stateNotNull;

/**
 * An invocation of a {@linkplain Resource resource method} that sends a request to XWS and returns a response.
 * Each call yields its own HTTP request and response pair. Use {@link #clone} to make multiple
 * calls with the same parameters to the same {@linkplain Resource resource call}; this may be used to implement polling or
 * to retry a failed call.
 *
 * <p>Calls may be executed synchronously with {@link #execute}, asynchronously with {@link
 * #enqueue}, or as asynchronous observable streams with {@link #stream()} or {@link #rawStream()} (<b>Note:</b> that a
 * dependency to RxJava is required). In either case the call can be canceled at any time with {@link #cancel}. A call that
 * is busy writing its request or reading its response may receive a {@link IOException}; this is working as designed.
 *
 * @param <RT> Successful response body type.
 * @param <ET> Error response body type.
 */
public interface CallSpec<RT, ET> extends Cloneable {
    /**
     * Synchronously executes the request and returns it's response.
     *
     * @throws IOException If a problem occurred while talking to the server.
     * @throws RuntimeException If an unexpected error occurred during execution or while parsing response.
     */
    Response<RT, ET> execute() throws IOException;

    /**
     * Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred talking to the server, creating the request, or processing the response.
     *
     * <p>This method is <i>null-safe</i>, which means that there will be no failure or error propagated if
     * {@code callback} methods will throw an error.
     *
     * <p>Note that the {@code callback} will be dropped after the call execution.
     */
    void enqueue(Callback<RT, ET> callback);

    /**
     * Executes the underlying call as an observable. The observable will try to return an
     * {@link Response} object from which the http result may be obtained.
     */
    Observable<Response<RT, ET>> rawStream();

    /**
     * Executes the underlying call as an observable. This method will try to populate the success response object of
     * a {@link Response}. In case of an error an {@link HttpException} will be thrown.
     * For a more richer and controllable api consider calling {@link #rawStream()}.
     */
    Observable<RT> stream();

    /**
     * Returns true if this call has been either {@linkplain #execute() executed} or {@linkplain #enqueue(Callback)
     * enqueued}. It is an error to execute or enqueue a call more than once.
     */
    boolean isExecuted();

    /** True if {@link #cancel()} was called. */
    boolean isCanceled();

    /**
     * Cancel this call. An attempt will be made to cancel in-flight calls, and if the call has not yet been executed
     * it never will be.
     */
    void cancel();

    /** Appends a query parameter to the query string of the underlying request's url. */
    CallSpec<RT, ET> queryParam(String name, Object value);

    /** Appends a query parameter to the query string of the underlying request's url. */
    CallSpec<RT, ET> queryParam(String name, String value);

    /** Appends a query parameter as a csv list to the query string of the underlying request's url. */
    CallSpec<RT, ET> queryParam(String name, String... values);

    /** Appends a query parameter as a csv list to the query string of the underlying request's url. */
    CallSpec<RT, ET> queryParam(String name, List<String> values);

    /**
     * Adds a form field to the underlying request's form body.
     *
     * <p>This will throw an {@linkplain NullPointerException} if the form body supprt was not specified during spec
     * creation.
     */
    CallSpec<RT, ET> formField(String name, String value);

    /**
     * Adds a form field as a csv list to the underlying request's form body.
     *
     * <p>This will throw an {@linkplain NullPointerException} if the form body supprt was not specified during spec
     * creation.
     */
    CallSpec<RT, ET> formField(String name, String... values);

    /**
     * Adds a form field as a csv list to the underlying request's form body.
     *
     * <p>This will throw an {@linkplain NullPointerException} if the form body supprt was not specified during spec
     * creation.
     */
    CallSpec<RT, ET> formField(String name, List<String> values);

    /** Creates and returns a copy of <strong>this</strong> object losing the executable state. */
    CallSpec<RT, ET> clone();

    /**
     * TODO docs.
     */
    final class Builder<RT, ET> {
        // Upper and lower characters, digits, underscores, and hyphens, starting with a character.
        private static final String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";
        private static final Pattern PARAM_NAME_REGEX = Pattern.compile(PARAM);
        private static final Pattern PARAM_URL_REGEX = Pattern.compile("\\{(" + PARAM + ")\\}");

        static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

        private final HttpMethod httpMethod;
        private final HttpUrl apiEndpoint;
        private final Request.Builder requestBuilder;
        private final Set<String> resourcePathParams;
        private String resourcePath;

        private HttpUrl.Builder urlBuilder;
        private FormBody.Builder formBodyBuilder;
        private RequestBody body;

        final XingApi api;
        Type responseType;
        Type errorType;

        // For now block the possibility to build outside this package.
        Builder(XingApi api, HttpMethod httpMethod, String resourcePath, boolean isFormEncoded) {
            this.api = api;
            this.httpMethod = checkNotNull(httpMethod, "httpMethod == null");
            this.resourcePath = checkNotNull(resourcePath, "resourcePath == null");

            resourcePathParams = parseResourcePathParams(resourcePath);
            apiEndpoint = api.apiEndpoint();
            requestBuilder = new Request.Builder().header("Accept", "application/json");

            if (isFormEncoded) formBodyBuilder = new FormBody.Builder();
        }

        private Builder(Builder<RT, ET> builder) {
            api = builder.api;
            httpMethod = builder.httpMethod;
            apiEndpoint = builder.apiEndpoint;
            requestBuilder = builder.requestBuilder;
            resourcePathParams = builder.resourcePathParams;
            resourcePath = builder.resourcePath;
            urlBuilder = builder.urlBuilder;
            formBodyBuilder = builder.formBodyBuilder;
            body = builder.body;
            responseType = builder.responseType;
            errorType = builder.errorType;
        }

        /** Replaces path parameter {@code name} with provided {@code values}. */
        public Builder<RT, ET> pathParam(String name, String value) {
            return pathParam(name, value, false);
        }

        /**
         * Replaces path parameter {@code name} with provided {@code values}.
         *
         * <p>The string values will not be 'escaped' to meet the url formatting. If required this action has to be
         * performed by the caller before submitting the values.
         */
        public Builder<RT, ET> pathParam(String name, String... values) {
            return pathParam(name, toCsv(values, false), true);
        }

        /**
         * Replaces path parameter {@code name} with provided {@code values}.
         *
         * <p>The string values will not be 'escaped' to meet the url formatting. If required this action has to be
         * performed by the caller before submitting the values.
         */
        public Builder<RT, ET> pathParam(String name, List<String> values) {
            return pathParam(name, toCsv(values, false), true);
        }

        private Builder<RT, ET> pathParam(String name, String value, boolean encoded) {
            stateNotNull(resourcePath, "Path params must be set before query params.");
            validatePathParam(name);
            resourcePath = resourcePath.replace('{' + name + '}', encoded ? value : escape(value));
            resourcePathParams.remove(name);
            return this;
        }

        public Builder<RT, ET> queryParam(String name, Object value) {
            if (resourcePath != null) buildUrlBuilder();
            urlBuilder.addEncodedQueryParameter(name, escape(String.valueOf(value)));
            return this;
        }

        public Builder<RT, ET> queryParam(String name, String... values) {
            return queryParam(name, toCsv(values, false));
        }

        public Builder<RT, ET> queryParam(String name, List<String> values) {
            return queryParam(name, toCsv(values, false));
        }

        public Builder<RT, ET> formField(String name, Object value) {
            stateNotNull(formBodyBuilder, "form fields are not accepted by this request.");
            formBodyBuilder.add(name, String.valueOf(value));
            return this;
        }

        public Builder<RT, ET> formField(String name, String... values) {
            return formField(name, toCsv(values, true));
        }

        public Builder<RT, ET> formField(String name, List<String> values) {
            return formField(name, toCsv(values, true));
        }

        public Builder<RT, ET> body(RequestBody body) {
            this.body = body;
            return this;
        }

        //TODO Avoid converting response body on main thread?
        public <U> Builder<RT, ET> body(Type type, U body) {
            Buffer buffer = new Buffer();
            JsonAdapter<U> jsonAdapter = api.converter().adapter(type);
            try {
                jsonAdapter.toJson(buffer, body);
            } catch (IOException ignored) {
                // Doesn't need to be handled. Buffer should not throw in this case.
            }
            return body(RequestBody.create(MEDIA_TYPE_JSON, buffer.readByteArray()));
        }

        public Builder<RT, ET> header(String name, String value) {
            requestBuilder.header(name, value);
            return this;
        }

        // TODO Document that setting response as Void will always avoid parsing the body.
        public Builder<RT, ET> responseAs(Class<RT> type) {
            return responseAs((Type) type);
        }

        public Builder<RT, ET> responseAs(Type type) {
            responseType = checkNotNull(type, "type == null");
            return this;
        }

        //This is needed for XING internal APIs that return the error message in a custom format.
        public Builder<RT, ET> errorAs(Class<ET> type) {
            return errorAs((Type) type);
        }

        public Builder<RT, ET> errorAs(Type type) {
            errorType = checkNotNull(type, "type == null");
            return this;
        }

        public CallSpec<RT, ET> build() {
            if (!resourcePathParams.isEmpty()) {
                throw stateError("Not all path params where set. Found %d unsatisfied parameter(s)",
                      resourcePathParams.size());
            }

            if (urlBuilder == null) buildUrlBuilder();
            if (responseType == null) throw stateError("Response type is not set.");
            if (errorType == null) errorType = HttpError.class;

            return new RealCallSpec<>(this);
        }

        Request request() {
            if (urlBuilder == null) throw stateError("#request() can be called only after #build()");
            HttpUrl url = urlBuilder.build();

            RequestBody body = this.body;
            if (body == null) {
                // Try to pull from one of the builders.
                if (formBodyBuilder != null) {
                    body = formBodyBuilder.build();
                } else if (httpMethod.hasBody()) {
                    // Body is absent, make an empty body.
                    //noinspection ZeroLengthArrayAllocation
                    body = RequestBody.create(null, new byte[0]);
                }
            }

            return requestBuilder
                  .url(url)
                  .method(httpMethod.method(), body)
                  .build();
        }

        /** Creates a new builder from the existing one. */
        Builder<RT, ET> newBuilder() {
            return new Builder<>(this);
        }

        /** Do a one-time combination of the built relative URL and the base URL. */
        private void buildUrlBuilder() {
            urlBuilder = apiEndpoint.resolve(resourcePath).newBuilder();
            resourcePath = null;
        }

        private void validatePathParam(String name) {
            if (!PARAM_NAME_REGEX.matcher(name).matches()) {
                throw assertionError("Path parameter name must match %s. Found: %s", PARAM_URL_REGEX.pattern(), name);
            }
            // Verify URL replacement name is actually present in the URL path.
            if (!resourcePathParams.contains(name)) {
                throw assertionError(
                      "Resource path \"%s\" does not contain \"{%s}\". Or the path parameter has been already set.",
                      resourcePath, name);
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

        /**
         * Converts varargs into a single string with coma separated values. If the varargs are {@code null}
         * or empty an empty string will be returned.
         */
        static String toCsv(Object[] values, boolean withSpace) {
            return toCsv(values != null ? Arrays.asList(values) : Collections.emptyList(), withSpace);
        }

        /**
         * Converts a list into a string with coma separated values. If the list is {@code null} or empty an
         * empty string will be returned.
         *
         * <p><b>NOTE:</b> The values contained in the {@link List} should not be null, otherwise a {@code "null"}
         * will be put in it's place.
         *
         * <p><b>NOTE:</b> For path params we need to avoid the whitespace after the comma, otherwise the url will be
         * considered as malformed.
         */
        static String toCsv(List<?> values, boolean withSpace) {
            StringBuilder sb = new StringBuilder();
            if (values != null && !values.isEmpty()) {
                int size = values.size();
                if (size > 1) {
                    boolean firstTime = true;
                    for (int index = 0; index < size; index++) {
                        if (firstTime) {
                            firstTime = false;
                        } else {
                            sb.append(withSpace ? ", " : ',');
                        }
                        sb.append(values.get(index));
                    }
                } else {
                    sb.append(values.get(0));
                }
            }
            return sb.toString();
        }
    }
}
