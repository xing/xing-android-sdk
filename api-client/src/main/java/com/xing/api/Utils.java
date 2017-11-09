/*
 * Copyright (C) 2016 XING SE (http://xing.com/)
 * Copyright (C) 2012 Square, Inc.
 * Copyright (C) 2007 The Guava Authors
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

import java.io.Closeable;
import java.io.IOException;

import okhttp3.ResponseBody;
import okio.Buffer;

final class Utils {
    /**
     * Check if the object instance is not null. This will method throws a NPE if case the condition is not met,
     * otherwise the same (non-altered) instance of the object will be returned.
     */
    static <T> T checkNotNull(T object, String message) {
        if (object == null) throw new NullPointerException(message);
        return object;
    }

    /**
     * Checks if the object instance is not null. This will throw an {@link IllegalStateException} if the condition is
     * not met.
     */
    static <T> void stateNotNull(T object, String message) {
        if (object == null) throw new IllegalStateException(message);
    }

    /**
     * Checks if the object is in null state. This will throw an {@link IllegalStateException} if the condition is
     * not met.
     */
    static <T> void stateNull(T object, String message) {
        if (object != null) throw stateError(message);
    }

    static void closeQuietly(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException ignored) {
        }
    }

    /** Buffers the response body, allowing to close the original source. */
    static ResponseBody buffer(ResponseBody body) throws IOException {
        Buffer buffer = new Buffer();
        body.source().readAll(buffer);
        return ResponseBody.create(body.contentType(), body.contentLength(), buffer);
    }

    /** Returns a {@link RuntimeException} with a formatted error message. */
    static RuntimeException assertionError(String message, Object... args) {
        return assertionError(null, message, args);
    }

    /** Returns a {@link RuntimeException} with a formatted error message. */
    static RuntimeException assertionError(Throwable cause, String message, Object... args) {
        message = String.format(message, args);
        IllegalArgumentException error = new IllegalArgumentException(message);
        error.initCause(cause);
        return error;
    }

    /** Returns a state {@link RuntimeException} with a formatted error message. */
    static RuntimeException stateError(String message, Object... args) {
        return stateError(null, message, args);
    }

    /** Returns a state {@link RuntimeException} with a formatted error message. */
    static RuntimeException stateError(Throwable cause, String message, Object... args) {
        message = String.format(message, args);
        IllegalStateException error = new IllegalStateException(message);
        error.initCause(cause);
        return error;
    }

    private Utils() {
        // No instances.
    }
}
