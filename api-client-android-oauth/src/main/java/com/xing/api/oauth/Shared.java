/*
 * Copyright (C) 2016 XING AG (http://xing.com/)
 * Copyright (C) 2007 The Guava Authors
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
package com.xing.api.oauth;

/** Utility class for basic assertion methods and shared constants. */
final class Shared {
    static final String CONSUMER_KEY = "consumerKey";
    static final String CONSUMER_SECRET = "consumerSecret";
    static final String CALLBACK_URL = "callbackUrl";

    static final String TOKEN = "token";
    static final String TOKEN_SECRET = "tokenSecret";

    static final int REQUEST_CODE = 600;
    static final int RESULT_BACK = 601;

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

    static String validate(String input, String pattern) {
        if (!pattern.matches(input)) {
            throw new IllegalArgumentException("Expected input: [" + pattern + "], found: " + input);
        }
        return input;
    }

    /** No instances. */
    private Shared() {
    }
}
