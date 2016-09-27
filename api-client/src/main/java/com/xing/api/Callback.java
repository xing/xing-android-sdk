/*
 * Copyright (С) 2016 XING AG (http://xing.com/)
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

/**
 * Communicates responses from a server or offline requests. One and only one method will be
 * invoked in response to a given request.
 * <p>
 * Callback methods are executed using the {@link CallSpec} callback executor. All Callbacks are executed on the
 * application's main (UI) thread.
 *
 * @param <RT> Expected response type.
 * @param <ET> Expected error type.
 */
public interface Callback<RT, ET> {
    /** Successful HTTP response. */
    void onResponse(Response<RT, ET> response);

    /** Invoked when a network or unexpected exception occurred during the HTTP request. */
    void onFailure(Throwable t);
}
