/*
 * Copyright (С) 2015 XING AG (http://xing.com/)
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

/**
 * Method to use in a {@linkplain CallSpec}.
 * Each method accept the method name and a boolean indicating whether it has a body or not
 */
enum HttpMethod {
    GET("GET", false),

    POST("POST", true),

    PUT("PUT", true),

    DELETE("DELETE", false);

    private final String method;
    private final boolean hasBody;

    HttpMethod(String method, boolean hasBody) {
        this.method = method;
        this.hasBody = hasBody;
    }

    public String method() {
        return method;
    }

    public boolean hasBody() {
        return hasBody;
    }
}
