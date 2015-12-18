/*
 * Copyright (c) 2015 XING AG (http://xing.com/)
 * Copyright (C) 2015 Square, Inc.
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

import android.support.annotation.Nullable;

import com.squareup.okhttp.Headers;

import static com.google.gdata.util.common.base.Preconditions.checkNotNull;

/**
 * TODO docs.
 */
public final class Response<RT, ET> {
    /** Returns a successful {@link Response} with a {@code null} error body. */
    static <RT, ET> Response<RT, ET> success(@Nullable RT body, com.squareup.okhttp.Response rawResponse) {
        return new Response<>(rawResponse, body, null);
    }

    /** Returns a error {@link Response} with a {@code null} response body. */
    static <RT, ET> Response<RT, ET> error(@Nullable ET error, com.squareup.okhttp.Response rawResponse) {
        return new Response<>(rawResponse, null, error);
    }

    private final com.squareup.okhttp.Response rawResponse;
    private final RT body;
    private final ET error;

    private Response(com.squareup.okhttp.Response rawResponse, @Nullable RT body, @Nullable ET error) {
        this.rawResponse = checkNotNull(rawResponse, "rawResponse == null");
        this.body = body;
        this.error = error;
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

    /** {@code true} if {@link #code()} is in the range [200..300]. */
    public boolean isSuccess() {
        return rawResponse.isSuccessful();
    }

    /** The de-serialized response body of a {@linkplain #isSuccess() successful} response. */
    @Nullable
    public RT body() {
        return body;
    }

    /** The parsed error response of an {@linkplain #isSuccess() unsuccessful} response. */
    @Nullable
    public ET error() {
        return error;
    }
}
