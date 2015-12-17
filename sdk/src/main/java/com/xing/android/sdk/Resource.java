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

import java.lang.reflect.Type;

/**
 * TODO docs.
 *
 * @author serj.lotutovici
 */
public abstract class Resource {
    protected final XingApi api;

    /** Creates a resource instance. This should be the only constructor declared by child classes. */
    protected Resource(XingApi api) {
        this.api = api;
    }

    /** Returns a {@link CallSpec.Builder} for a GET request. */
    protected static <RT, ET> CallSpec.Builder<RT, ET> newGetSpec(XingApi api, String resourcePath) {
        return new CallSpec.Builder<>(api, HttpMethod.GET, resourcePath, false);
    }

    /** Returns a {@link CallSpec.Builder} for a POST request. */
    protected static <RT, ET> CallSpec.Builder<RT, ET> newPostSpec(XingApi api, String resourcePath,
          boolean isFormEncoded) {
        return new CallSpec.Builder<>(api, HttpMethod.POST, resourcePath, isFormEncoded);
    }

    /** Returns a {@link CallSpec.Builder} for a PUT request. */
    protected static <RT, ET> CallSpec.Builder<RT, ET> newPutSpec(XingApi api, String resourcePath,
          boolean isFormEncoded) {
        return new CallSpec.Builder<>(api, HttpMethod.PUT, resourcePath, isFormEncoded);
    }

    /** Returns a {@link CallSpec.Builder} for a DELETE request. */
    protected static <RT, ET> CallSpec.Builder<RT, ET> newDeleteSpec(XingApi api, String resourcePath) {
        return new CallSpec.Builder<>(api, HttpMethod.DELETE, resourcePath, false);
    }

    /** Returns a {@link CompositeType} that will expect a single object in the root tree. */
    protected static CompositeType single(Type classType, String... roots) {
        return new CompositeType(classType, CompositeType.Structure.SINGLE, roots);
    }

    /** Returns a {@link CompositeType} that will expect a list of objects in the root tree. */
    protected static CompositeType list(Type classType, String... roots) {
        return new CompositeType(classType, CompositeType.Structure.LIST, roots);
    }

    /**
     * Returns a {@link CompositeType} that will expect an element of {@code classType} as the first element in a list.
     */
    protected static CompositeType first(Type classType, String... roots) {
        return new CompositeType(classType, CompositeType.Structure.FIRST, roots);
    }
}
