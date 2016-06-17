/*
 * Copyright (C) 2016 XING AG (http://xing.com/)
 * Copyright (C) 2016 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
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

import static com.xing.api.Utils.buffer;
import static com.xing.api.Utils.closeQuietly;
import static com.xing.api.Utils.stateError;

/** Implements {@linkplain CallSpec} providing the desired functionality. */
final class RealCallSpec<RT, ET> implements CallSpec<RT, ET> {
    final XingApi api;
    private final CallSpec.Builder<RT, ET> builder;
    private final Type responseType;
    private final Type errorType;

    private volatile Call rawCall;
    private boolean executed; // Guarded by this.
    private volatile boolean canceled;

    RealCallSpec(CallSpec.Builder<RT, ET> builder) {
        this.builder = builder;
        api = builder.api;
        responseType = builder.responseType;
        errorType = builder.errorType;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone") // This is a final type & this saves clearing state.
    @Override
    public CallSpec<RT, ET> clone() {
        // When called from CallSpec we don't need to go through the validation process.
        return new RealCallSpec<>(builder.newBuilder());
    }

    @Override
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

    @Override
    public void enqueue(final Callback<RT, ET> callback) {
        synchronized (this) {
            if (executed) throw stateError("Call already executed");
            executed = true;
        }

        Call rawCall = createRawCall();
        if (canceled) rawCall.cancel();
        this.rawCall = rawCall;

        rawCall.enqueue(new okhttp3.Callback() {
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
            public void onFailure(Call call, IOException e) {
                callFailure(e);
            }

            @Override
            public void onResponse(Call call, okhttp3.Response rawResponse) {
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

    @Override
    public Observable<Response<RT, ET>> rawStream() {
        return Observable.create(new SpecOnSubscribe<>(this));
    }

    @Override
    public Observable<RT> stream() {
        return rawStream().lift(OperatorMapResponseToBodyOrError.<RT, ET>instance());
    }

    @Override
    public synchronized boolean isExecuted() {
        return executed;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void cancel() {
        canceled = true;
        Call rawCall = this.rawCall;
        if (rawCall != null) rawCall.cancel();
    }

    @Override
    public CallSpec<RT, ET> queryParam(String name, Object value) {
        builder.queryParam(name, value);
        return this;
    }

    @Override
    public CallSpec<RT, ET> queryParam(String name, String value) {
        builder.queryParam(name, value);
        return this;
    }

    @Override
    public CallSpec<RT, ET> queryParam(String name, String... values) {
        builder.queryParam(name, values);
        return this;
    }

    @Override
    public CallSpec<RT, ET> queryParam(String name, List<String> values) {
        builder.queryParam(name, values);
        return this;
    }

    @Override
    public CallSpec<RT, ET> formField(String name, String value) {
        builder.formField(name, value);
        return this;
    }

    @Override
    public CallSpec<RT, ET> formField(String name, String... values) {
        builder.formField(name, values);
        return this;
    }

    @Override
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
    Response<RT, ET> parseResponse(okhttp3.Response rawResponse) throws IOException {
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
            } catch (EOFException eofe) {
                return Response.error(null, rawResponse);
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

        // No need to parse the response body since the response should not contain any content.
        if (code == 204 || code == 205) {
            return Response.success(null, rawResponse);
        }

        try {
            RT body = parseBody(responseType, catchingBody);
            String contentRange = rawResponse.header(ContentRange.HEADER_NAME);
            return Response.success(body, ContentRange.parse(contentRange), rawResponse);
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
        IOException thrownException;

        ExceptionCatchingRequestBody(ResponseBody delegate) {
            this.delegate = delegate;
        }

        @Override
        public MediaType contentType() {
            return delegate.contentType();
        }

        @Override
        public long contentLength() {
            return delegate.contentLength();
        }

        @Override
        public BufferedSource source() {
            BufferedSource delegateSource = delegate.source();
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
        public void close() {
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
