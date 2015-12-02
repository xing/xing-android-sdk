/*
 * Copyright (c) 2015 XING AG (http://xing.com/)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.xing.android.sdk;

import com.xing.android.sdk.internal.Http;

/**
 * TODO docs.
 *
 * @author serj.lotutovici
 */
public abstract class Resource {
    final XingApi api;

    /** Creates a resource instance. This should be the only constructor declared by child classes. */
    protected Resource(XingApi api) {
        this.api = api;
    }

    // TODO (DanielH) Implement all internal methods required for building specs.
    protected <RT, ET> CallSpec.Builder<RT, ET> newGetSpec(String resourcePath) {
        return new CallSpec.Builder<>(api, Http.HTTP_GET, resourcePath, false, false);
    }

    protected <RT, ET> CallSpec.Builder<RT, ET> newPostSpec(String resourcePath) {
        return new CallSpec.Builder<>(api, Http.HTTP_POST, resourcePath, true, true);
    }

    protected <RT, ET> CallSpec.Builder<RT, ET> newPutSpec(String resourcePath) {
        return new CallSpec.Builder<>(api, Http.HTTP_PUT, resourcePath, true, false);
    }

    protected <RT, ET> CallSpec.Builder<RT, ET> newDeleteSpec(String resourcePath) {
        return new CallSpec.Builder<>(api, Http.HTTP_DELETE, resourcePath, false, false);
    }
}
