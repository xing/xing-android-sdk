/*
 * Copyright (C) 2015 Square, Inc.
 * Copyright (С) 2018 XING SE (http://xing.com/)
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

import okhttp3.Headers;

import static com.xing.api.Utils.checkNotNull;

/**
 * Response from executing a request with {@linkplain CallSpec#execute()} or {@linkplain CallSpec#rawStream()}.
 *
 * @param <RT> Successful response body type.
 * @param <ET> Error response body type.
 */
public final class Response<RT, ET> {
    /** Returns a successful {@link Response} with a {@code null} error body. */
    static <RT, ET> Response<RT, ET> success(RT body, okhttp3.Response rawResponse) {
        return new Response<>(rawResponse, null, body, null);
    }

    /** Returns a successful {@link Response} with 'possibly' non null {@linkplain ContentRange}. */
    static <RT, ET> Response<RT, ET> success(RT body, ContentRange range, okhttp3.Response rawResponse) {
        return new Response<>(rawResponse, range, body, null);
    }

    /** Returns a error {@link Response} with a {@code null} response body. */
    static <RT, ET> Response<RT, ET> error(ET error, okhttp3.Response rawResponse) {
        return new Response<>(rawResponse, null, null, error);
    }

    private final okhttp3.Response rawResponse;
    private final ContentRange range;
    private final RT body;
    private final ET error;

    private Response(okhttp3.Response rawResponse, ContentRange range, RT body, ET error) {
        this.rawResponse = checkNotNull(rawResponse, "rawResponse == null");
        this.range = range;
        this.body = body;
        this.error = error;
    }

    /** The raw response from the HTTP client. */
    public okhttp3.Response raw() {
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
    public boolean isSuccessful() {
        return rawResponse.isSuccessful();
    }

    /**
     * The de-serialized response body of a {@linkplain #isSuccessful() successful} response
     * or null if the response was unsuccessful.
     */
    public RT body() {
        return body;
    }

    /**
     * The parsed error response of an {@linkplain #isSuccessful() unsuccessful} response
     * or null if the error contains no body.
     */
    public ET error() {
        return error;
    }

    /** The {@code Xing-Content-Rage} value of the response. Available only if the response is paginated. */
    public ContentRange range() {
        return range;
    }
}
