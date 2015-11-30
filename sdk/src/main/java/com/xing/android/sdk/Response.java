/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xing.android.sdk;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.ResponseBody;

import static com.google.gdata.util.common.base.Preconditions.checkNotNull;

/**
 * TODO docs.
 */
public final class Response<RT, ET> {
    // TODO (SerjLtt) Make response reflect error type.

    /**
     * TODO docs.
     */
    public static <RT> Response<RT, Object> success(RT body) {
        return success(body, new com.squareup.okhttp.Response.Builder() //
              .code(200)
              .protocol(Protocol.HTTP_1_1)
              .request(new com.squareup.okhttp.Request.Builder().url(HttpUrl.parse("http://localhost")).build())
              .build());
    }

    /**
     * TODO docs.
     */
    public static <RT> Response<RT, Object> success(RT body, com.squareup.okhttp.Response rawResponse) {
        return new Response<>(rawResponse, body, null);
    }

    /**
     * TODO docs.
     */
    public static <RT> Response<RT, Object> error(int code, ResponseBody body) {
        return error(body, new com.squareup.okhttp.Response.Builder() //
              .code(code)
              .protocol(Protocol.HTTP_1_1)
              .request(new com.squareup.okhttp.Request.Builder().url(HttpUrl.parse("http://localhost")).build())
              .build());
    }

    /**
     * TODO docs.
     */
    public static <RT, ET> Response<RT, ET> error(ResponseBody body, com.squareup.okhttp.Response rawResponse) {
        return new Response<>(rawResponse, null, body);
    }

    private final com.squareup.okhttp.Response rawResponse;
    private final RT body;
    private final ResponseBody errorBody;

    private Response(com.squareup.okhttp.Response rawResponse, RT body, ResponseBody errorBody) {
        this.rawResponse = checkNotNull(rawResponse, "rawResponse == null");
        this.body = body;
        this.errorBody = errorBody;
    }

    /** The raw response from the HTTP client. */
    public com.squareup.okhttp.Response raw() {
        return rawResponse;
    }

    /** HTTP status code. */
    public int code() {
        return rawResponse.code();
    }

    /** HTTP status message. */
    public String message() {
        return rawResponse.message();
    }

    public Headers headers() {
        return rawResponse.headers();
    }

    /** {@code true} if {@link #code()} is in the range [200..300). */
    public boolean isSuccess() {
        return rawResponse.isSuccessful();
    }

    /** The deserialized response body of a {@linkplain #isSuccess() successful} response. */
    public RT body() {
        return body;
    }

    /** The raw response body of an {@linkplain #isSuccess() unsuccessful} response. */
    public ResponseBody errorBody() {
        return errorBody;
    }
}
