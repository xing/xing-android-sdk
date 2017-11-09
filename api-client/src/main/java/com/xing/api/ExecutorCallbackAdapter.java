/*
 * Copyright (C) 2016 XING SE (http://xing.com/)
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

import java.util.concurrent.Executor;

import okhttp3.ResponseBody;

/** Callback adapter that allows invoking callback methods on the set executor thread. */
final class ExecutorCallbackAdapter implements CallbackAdapter {
    private final Executor executor;

    public ExecutorCallbackAdapter(Executor executor) {
        this.executor = executor;
    }

    @Override
    public <RT, ET> Callback<RT, ET> adapt(final Callback<RT, ET> callback) {
        return new Callback<RT, ET>() {
            @Override
            public void onResponse(final Response<RT, ET> response) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(response);
                    }
                });
            }

            @Override
            public void onFailure(final Throwable t) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(t);
                    }
                });
            }
        };
    }

    @Override
    public AuthErrorCallback adapt(final AuthErrorCallback callback) {
        return new AuthErrorCallback() {
            @Override
            public void onAuthError(final Response<?, ResponseBody> errorResponse) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onAuthError(errorResponse);
                    }
                });
            }
        };
    }
}
