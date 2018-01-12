/*
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

/**
 * Callback adapter contract, that will decorate/adapt the passed callback to be executed on the appropriate
 * executor.
 */
interface CallbackAdapter {
    /** Adapts the passed callback. */
    <RT, ET> Callback<RT, ET> adapt(Callback<RT, ET> callback);

    /** Adapts the passed auth callback. */
    AuthErrorCallback adapt(AuthErrorCallback callback);

    /** Default callback adapter. DOESN'T alter the passed callbacks. */
    final class Default implements CallbackAdapter {
        @Override
        public <RT, ET> Callback<RT, ET> adapt(Callback<RT, ET> callback) {
            return callback;
        }

        @Override
        public AuthErrorCallback adapt(AuthErrorCallback callback) {
            return callback;
        }
    }
}
