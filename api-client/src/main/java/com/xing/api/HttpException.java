/*
 * Copyright (C) 2015 Square, Inc.
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

/** Exception for an unexpected, non-2xx HTTP response. */
public class HttpException extends Exception {
    private final int code;
    private final String message;
    private final Object error;
    private final transient okhttp3.Response rawResponse;

    public HttpException(Response<?, ?> response) {
        super("HTTP " + response.code() + ' ' + response.message());
        code = response.code();
        message = response.message();
        error = response.error();
        rawResponse = response.raw();
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
    public okhttp3.Response response() {
        return rawResponse;
    }

    /** Error body object representative. */
    public Object error() {
        return error;
    }
}
