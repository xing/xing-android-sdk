/*
 * Copyright (С) 2015 XING AG (http://xing.com/)
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

/** Exception for an unexpected, non-2xx HTTP response. */
public final class HttpException extends Exception {
    private final int code;
    private final String message;
    private final Object error;
    private final transient com.squareup.okhttp.Response rawResponse;

    public HttpException(Response<?, ?> response) {
        super("HTTP " + response.code() + " " + response.message());
        this.code = response.code();
        this.message = response.message();
        this.error = response.error();
        this.rawResponse = response.raw();
    }

    /** HTTP status code. */
    public int code() {
        return code;
    }

    /** HTTP status message. */
    public String message() {
        return message;
    }

    /** The full HTTP response. */
    public com.squareup.okhttp.Response response() {
        return rawResponse;
    }

    /** Error body object representative. */
    public Object error() {
        return error;
    }
}
