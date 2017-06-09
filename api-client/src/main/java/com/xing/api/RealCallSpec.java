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

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;

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
    private int connectTimeout = -1;
    private int readTimeout = -1;
    private int writeTimeout = -1;

    RealCallSpec(CallSpec.Builder<RT, ET> builder) {
        this.builder = builder;
        api = builder.api;
        responseType = builder.responseType;
        errorType = builder.errorType;
        readTimeout = builder.readTimeout;
        connectTimeout = builder.connectTimeout;
        writeTimeout = builder.writeTimeout;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone") // This is a final type & this saves clearing state.
    @Override
    public CallSpec<RT, ET> clone() {
        // When called from CallSpec we don't need to go through the validation process.
        return new RealCallSpec<>(builder.newBuilder());
    }

    @Override
    public Request request() {
        return builder.request();
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
    public rx.Observable<Response<RT, ET>> rawStream() {
        return rx.Observable.fromCallable(new ResponseCallable<>(this));
    }

    @Override
    public rx.Observable<RT> stream() {
        ResponseCallable<RT, ET> responseCallable = new ResponseCallable<>(this);
        return rx.Observable.fromCallable(new BodyCallable<>(responseCallable));
    }

    @Override
    public rx.Single<RT> singleStream() {
        return stream().toSingle();
    }

    @Override
    public rx.Completable completableStream() {
        return stream().toCompletable();
    }

    @Override
    public Single<Response<RT, ET>> singleRawResponse() {
        return Single.fromCallable(new ResponseCallable<>(this));
    }

    @Override
    public Single<RT> singleResponse() {
        ResponseCallable<RT, ET> responseCallable = new ResponseCallable<>(this);
        return Single.fromCallable(new BodyCallable<>(responseCallable));
    }

    @Override
    public Completable completableResponse() {
        ResponseCallable<RT, ET> responseCallable = new ResponseCallable<>(this);
        return Completable.fromCallable(new BodyCallable<>(responseCallable));
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
    public CallSpec<RT, ET> header(String name, String value) {
        builder.header(name, value);
        return this;
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
    public CallSpec<RT, ET> formField(String name, String value, boolean encode) {
        builder.formField(name, value, encode);
        return this;
    }

    @Override
    public CallSpec<RT, ET> formField(String name, String value) {
        builder.formField(name, value);
        return this;
    }

    @Override
    public CallSpec<RT, ET> formField(String name, Object value) {
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

    @Override
    public CallSpec<RT, ET> connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    @Override
    public CallSpec<RT, ET> readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    @Override
    public CallSpec<RT, ET> writeTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    /** Returns a raw {@link Call} pre-building the targeted request. */
    private Call createRawCall() {
        OkHttpClient client = api.client();
        client = applyTimeouts(client);
        return client.newCall(builder.request());
    }

    private OkHttpClient applyTimeouts(OkHttpClient client) {
        if (connectTimeout >= 0 || readTimeout >= 0 || writeTimeout >= 0) {
            OkHttpClient.Builder builder = client.newBuilder();
            if (connectTimeout >= 0) {
                builder.connectTimeout(connectTimeout, TimeUnit.SECONDS);
            }
            if (readTimeout >= 0) {
                builder.readTimeout(readTimeout, TimeUnit.SECONDS);
            }
            if (writeTimeout >= 0) {
                builder.writeTimeout(writeTimeout, TimeUnit.SECONDS);
            }
            return builder.build();
        }
        return client;
    }

    /** Parsers the OkHttp raw response and returns an response ready to be consumed by the caller. */
    @SuppressWarnings("MagicNumber")
    // These codes are specific to this method and to the http protocol.
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
                ET errorBody = api.converter().convertFromBody(errorType, catchingBody);
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
            RT body = api.converter().convertFromBody(responseType, catchingBody);
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

    /** Callable that returns the successful response body or throws an {@linkplain HttpException}. */
    static final class BodyCallable<RT, ET> implements Callable<RT> {
        private final Callable<Response<RT, ET>> callable;

        BodyCallable(Callable<Response<RT, ET>> callable) {
            this.callable = callable;
        }

        @Override
        public RT call() throws Exception {
            Response<RT, ET> response = callable.call();
            if (response.isSuccessful()) {
                return response.body();
            }
            throw new HttpException(response);
        }
    }

    /** Callable that executes the call spec and yields the response. */
    static final class ResponseCallable<RT, ET> implements Callable<Response<RT, ET>> {
        private final CallSpec<RT, ET> callSpec;

        ResponseCallable(CallSpec<RT, ET> callSpec) {
            this.callSpec = callSpec;
        }

        @Override
        public Response<RT, ET> call() throws IOException {
            // Since CallSpec is a one-shot type, clone it for each new caller.
            return callSpec.clone().execute();
        }
    }
}
