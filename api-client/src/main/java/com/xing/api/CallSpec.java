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
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.Types;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import rx.Observable;
import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

import static com.xing.api.UrlEscapeUtils.escape;
import static com.xing.api.Utils.assertionError;
import static com.xing.api.Utils.buffer;
import static com.xing.api.Utils.checkNotNull;
import static com.xing.api.Utils.closeQuietly;
import static com.xing.api.Utils.stateError;
import static com.xing.api.Utils.stateNotNull;

/**
 * TODO docs.
 *
 * @author serj.lotutovici
 */
public final class CallSpec<RT, ET> implements Cloneable {
    private final XingApi api;
    private final Builder<RT, ET> builder;
    private final Type responseType;
    private final Type errorType;

    private volatile Call rawCall;
    private boolean executed; // Guarded by this.
    private volatile boolean canceled;

    private CallSpec(Builder<RT, ET> builder) {
        this.builder = builder;
        api = builder.api;
        responseType = builder.responseType;
        errorType = builder.errorType;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone") // This is a final type & this saves clearing state.
    @Override
    public CallSpec<RT, ET> clone() {
        // When called from CallSpec we don't need to go through the validation process.
        return new CallSpec<>(builder.newBuilder());
    }

    /**
     * Synchronously executes the request and returns it's response.
     *
     * @throws IOException If a problem occurred while talking to the server.
     * @throws RuntimeException If an unexpected error occurred during execution or while parsing response.
     */
    public Response<RT, ET> execute() throws IOException {
        synchronized (this) {
            if (executed) throw stateError("Call already executed");
            executed = true;
        }

        Call rawCall = createRawCall();
        if (canceled) rawCall.cancel();
        this.rawCall = rawCall;

        return parseResponse(rawCall.execute());
    }

    /**
     * Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred talking to the server, creating the request, or processing the response.
     * <p>
     * This method is <i>null-safe</i>, which means that there will be no failure or error propagated if
     * {@code callback} methods will throw an error.
     * <p>
     * Note that the {@code callback} will be dropped after the call execution.
     */
    public void enqueue(final Callback<RT, ET> callback) {
        synchronized (this) {
            if (executed) throw stateError("Call already executed");
            executed = true;
        }

        Call rawCall;
        try {
            rawCall = createRawCall();
        } catch (Throwable t) {
            callback.onFailure(t);
            return;
        }

        if (canceled) rawCall.cancel();
        this.rawCall = rawCall;

        rawCall.enqueue(new com.squareup.okhttp.Callback() {
            private void callFailure(Throwable e) {
                try {
                    api.callbackAdapter().adapt(callback).onFailure(e);
                } catch (Throwable t) {
                    // TODO add some logging
                }
            }

            private void callSuccess(Response<RT, ET> response) {
                try {
                    api.callbackAdapter().adapt(callback).onResponse(response);
                } catch (Throwable t) {
                    // TODO add some logging
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                callFailure(e);
            }

            @Override
            public void onResponse(com.squareup.okhttp.Response rawResponse) {
                Response<RT, ET> response;
                try {
                    response = parseResponse(rawResponse);
                } catch (Throwable e) {
                    callFailure(e);
                    return;
                }
                callSuccess(response);
            }
        });
    }

    /**
     * Executes the underlying call as an observable. The observable will try to return an
     * {@link Response} object from which the http result may be obtained.
     */
    public Observable<Response<RT, ET>> rawStream() {
        return Observable.create(new SpecOnSubscribe<>(this));
    }

    /**
     * Executes the underlying call as an observable. This method will try to populate the success response object of
     * a {@link Response}. In case of an error an {@link HttpException} will be thrown.
     * For a more richer and controllable api consider calling {@link #rawStream()}.
     */
    public Observable<RT> stream() {
        return rawStream().lift(OperatorMapResponseToBodyOrError.<RT, ET>instance());
    }

    /**
     * Returns true if this call has been either {@linkplain #execute() executed} or {@linkplain #enqueue(Callback)
     * enqueued}. It is an error to execute or enqueue a call more than once.
     */
    public synchronized boolean isExecuted() {
        return executed;
    }

    /** True if {@link #cancel()} was called. */
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * Cancel this call. An attempt will be made to cancel in-flight calls, and if the call has not yet been executed
     * it never will be.
     */
    public void cancel() {
        canceled = true;
        Call rawCall = this.rawCall;
        if (rawCall != null) rawCall.cancel();
    }

    public CallSpec<RT, ET> queryParam(String name, Object value) {
        builder.queryParam(name, value);
        return this;
    }

    public CallSpec<RT, ET> queryParam(String name, String value) {
        builder.queryParam(name, value);
        return this;
    }

    public CallSpec<RT, ET> queryParam(String name, String... values) {
        builder.queryParam(name, values);
        return this;
    }

    public CallSpec<RT, ET> queryParam(String name, List<String> values) {
        builder.queryParam(name, values);
        return this;
    }

    public CallSpec<RT, ET> formField(String name, String value) {
        builder.formField(name, value);
        return this;
    }

    public CallSpec<RT, ET> formField(String name, String... values) {
        builder.formField(name, values);
        return this;
    }

    public CallSpec<RT, ET> formField(String name, List<String> values) {
        builder.formField(name, values);
        return this;
    }

    /** Returns a raw {@link Call} pre-building the targeted request. */
    private Call createRawCall() {
        return api.client().newCall(builder.request());
    }

    /** Parsers the OkHttp raw response and returns an response ready to be consumed by the caller. */
    @SuppressWarnings("MagicNumber") // These codes are specific to this method and to the http protocol.
    private Response<RT, ET> parseResponse(com.squareup.okhttp.Response rawResponse) throws IOException {
        ResponseBody rawBody = rawResponse.body();

        // Remove the body's source (the only stateful object) so we can pass the response along.
        rawResponse = rawResponse.newBuilder()
              .body(new NoContentResponseBody(rawBody.contentType(), rawBody.contentLength()))
              .build();

        ExceptionCatchingRequestBody catchingBody = new ExceptionCatchingRequestBody(rawBody);
        int code = rawResponse.code();
        if (code < 200 || code >= 300) {
            try {

                // Bypass the body parsing, and return the raw data.
                if (code == 401) {
                    ResponseBody bufferedBody = buffer(catchingBody);
                    api.notifyAuthError(Response.error(bufferedBody, rawResponse));
                    throw new IOException("401 Unauthorized");
                }

                // Buffer the entire body to avoid future I/O.
                ET errorBody = parseBody(errorType, catchingBody);
                return Response.error(errorBody, rawResponse);
            } catch (RuntimeException e) {
                // If the underlying source threw an exception, propagate that, rather than indicating it was
                // a runtime exception.
                catchingBody.throwIfCaught();
                throw e;
            } finally {
                // FIXME I don't think we need to close the body twice.
                closeQuietly(catchingBody);
            }
        }

        // No need to parse the response body since the response should not contain a body.
        if (code == 204 || code == 205) {
            return Response.success(null, rawResponse);
        }

        try {
            RT body = parseBody(responseType, catchingBody);
            return Response.success(body, rawResponse);
        } catch (RuntimeException e) {
            // If the underlying source threw an exception, propagate that, rather than indicating it was
            // a runtime exception.
            catchingBody.throwIfCaught();
            throw e;
        } finally {
            // FIXME I don't think we need to close the body twice.
            closeQuietly(catchingBody);
        }
    }

    @SuppressWarnings("unchecked") // Type is declared on CallSpec creation. Type matching is the caller responsibility.
    private <PT> PT parseBody(Type type, ResponseBody body) throws IOException {
        if (body == null) return null;

        try {
            // Don't parse the response body, if the caller doesn't expect a json.
            Class<?> rawType = Types.getRawType(type);
            if (rawType == Void.class) return null;
            if (rawType == String.class) return (PT) body.string();
            if (rawType == ResponseBody.class) return (PT) buffer(body);

            JsonAdapter<PT> adapter = api.converter().adapter(type);
            JsonReader reader = JsonReader.of(body.source());
            return adapter.fromJson(reader);
        } finally {
            closeQuietly(body);
        }
    }

    /**
     * TODO docs.
     */
    public static final class Builder<RT, ET> {
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
        private FormEncodingBuilder formEncodingBuilder;
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

            if (isFormEncoded) formEncodingBuilder = new FormEncodingBuilder();
        }

        private Builder(Builder<RT, ET> builder) {
            api = builder.api;
            httpMethod = builder.httpMethod;
            apiEndpoint = builder.apiEndpoint;
            requestBuilder = builder.requestBuilder;
            resourcePathParams = builder.resourcePathParams;
            resourcePath = builder.resourcePath;
            urlBuilder = builder.urlBuilder;
            formEncodingBuilder = builder.formEncodingBuilder;
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
         * <p>
         * The string values will not be 'escaped' to meet the url formatting. If required this action has to be
         * performed by the caller before submitting the values.
         */
        public Builder<RT, ET> pathParam(String name, String... values) {
            return pathParam(name, toCsv(values, false), true);
        }

        /**
         * Replaces path parameter {@code name} with provided {@code values}.
         * <p>
         * The string values will not be 'escaped' to meet the url formatting. If required this action has to be
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
            stateNotNull(formEncodingBuilder, "form fields are not accepted by this request.");
            formEncodingBuilder.add(name, String.valueOf(value));
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

            return new CallSpec<>(this);
        }

        Request request() {
            if (urlBuilder == null) throw stateError("#request() can be called only after #build()");
            HttpUrl url = urlBuilder.build();

            RequestBody body = this.body;
            if (body == null) {
                // Try to pull from one of the builders.
                if (formEncodingBuilder != null) {
                    body = formEncodingBuilder.build();
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
         * <p>
         * <b>NOTE:</b> The values contained in the {@link List} should not be null, otherwise a {@code "null"}
         * will be put in it's place.
         * <p>
         * <b>NOTE:</b> For path params we need to avoid the whitespace after the comma, otherwise the url will be
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

    static final class NoContentResponseBody extends ResponseBody {
        private final MediaType contentType;
        private final long contentLength;

        NoContentResponseBody(MediaType contentType, long contentLength) {
            this.contentType = contentType;
            this.contentLength = contentLength;
        }

        @Override
        public MediaType contentType() {
            return contentType;
        }

        @Override
        public long contentLength() {
            return contentLength;
        }

        @Override
        public BufferedSource source() {
            throw new IllegalStateException("Cannot read raw response body of a parsed body.");
        }
    }

    static final class ExceptionCatchingRequestBody extends ResponseBody {
        private final ResponseBody delegate;
        private IOException thrownException;

        ExceptionCatchingRequestBody(ResponseBody delegate) {
            this.delegate = delegate;
        }

        @Override
        public MediaType contentType() {
            return delegate.contentType();
        }

        @Override
        public long contentLength() throws IOException {
            try {
                return delegate.contentLength();
            } catch (IOException e) {
                thrownException = e;
                throw e;
            }
        }

        @Override
        public BufferedSource source() throws IOException {
            BufferedSource delegateSource;
            try {
                delegateSource = delegate.source();
            } catch (IOException e) {
                thrownException = e;
                throw e;
            }
            return Okio.buffer(new ForwardingSource(delegateSource) {
                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    try {
                        return super.read(sink, byteCount);
                    } catch (IOException e) {
                        thrownException = e;
                        throw e;
                    }
                }
            });
        }

        @Override
        public void close() throws IOException {
            delegate.close();
        }

        void throwIfCaught() throws IOException {
            if (thrownException != null) throw thrownException;
        }
    }

    /**
     * {@link Observable.OnSubscribe} implementation that takes a {@linkplain CallSpec spec} and executes the request.
     * This class handles the subscriptions life cycle and completes the subscription as soon as the response is
     * delivered.
     */
    static final class SpecOnSubscribe<RT, ET> implements Observable.OnSubscribe<Response<RT, ET>> {
        private final CallSpec<RT, ET> originalSpec;

        /** Creates on instance of {@linkplain SpecOnSubscribe} for the provided spec. */
        SpecOnSubscribe(CallSpec<RT, ET> originalSpec) {
            this.originalSpec = originalSpec;
        }

        @Override
        public void call(Subscriber<? super Response<RT, ET>> subscriber) {
            // Since Call is a one-shot type, clone it for each new subscriber.
            CallSpec<RT, ET> spec = originalSpec.clone();

            // Wrap the call in a helper which handles both unsubscription and backpressure.
            RequestArbiter<RT, ET> requestArbiter = new RequestArbiter<>(spec, subscriber);
            subscriber.add(requestArbiter);
            subscriber.setProducer(requestArbiter);
        }
    }

    /** Helper/Arbiter that will honor request back-pressure on observable creation. */
    static final class RequestArbiter<RT, ET> extends AtomicBoolean implements Subscription, Producer {
        private final CallSpec<RT, ET> spec;
        private final Subscriber<? super Response<RT, ET>> subscriber;

        /** Creates an instance of {@linkplain RequestArbiter} for the provided spec and subscriber. */
        RequestArbiter(CallSpec<RT, ET> spec, Subscriber<? super Response<RT, ET>> subscriber) {
            this.spec = spec;
            this.subscriber = subscriber;
        }

        @Override
        public void request(long n) {
            if (n < 0) throw new IllegalArgumentException("n < 0: " + n);
            if (n == 0) return; // Nothing to do when requesting 0.
            if (!compareAndSet(false, true)) return; // Request was already triggered.

            try {
                Response<RT, ET> response = spec.execute();
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(response);
                }
            } catch (Throwable t) {
                Exceptions.throwIfFatal(t);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(t);
                }
                return;
            }

            if (!subscriber.isUnsubscribed()) {
                subscriber.onCompleted();
            }
        }

        @Override
        public void unsubscribe() {
            spec.cancel();
        }

        @Override
        public boolean isUnsubscribed() {
            return spec.isCanceled();
        }
    }

    /**
     * A version of {@link Observable#map(Func1)} which lets us trigger {@code onError} without having
     * to use {@link Observable#flatMap(Func1)} which breaks producer requests from propagating.
     */
    static final class OperatorMapResponseToBodyOrError<RT, ET> implements Operator<RT, Response<RT, ET>> {
        private static final OperatorMapResponseToBodyOrError<Object, Object> INSTANCE =
              new OperatorMapResponseToBodyOrError<>();

        /**
         * Returns a static instance of {@linkplain OperatorMapResponseToBodyOrError}.
         * This allows us to avoid multiple instantiations for each request (saves memory).
         */
        @SuppressWarnings("unchecked") // Safe because of erasure.
        static <RT, ET> OperatorMapResponseToBodyOrError<RT, ET> instance() {
            return (OperatorMapResponseToBodyOrError<RT, ET>) INSTANCE;
        }

        @Override
        public Subscriber<? super Response<RT, ET>> call(final Subscriber<? super RT> child) {
            return new Subscriber<Response<RT, ET>>(child) {
                @Override
                public void onNext(Response<RT, ET> response) {
                    if (response.isSuccessful()) {
                        child.onNext(response.body());
                    } else {
                        child.onError(new HttpException(response));
                    }
                }

                @Override
                public void onCompleted() {
                    child.onCompleted();
                }

                @Override
                public void onError(Throwable th) {
                    child.onError(th);
                }
            };
        }
    }
}
