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

import java.lang.reflect.Type;

/**
 * TODO docs.
 *
 * @author serj.lotutovici
 */
public abstract class Resource {
    protected static final String ME = "me";
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

    /** Returns a {@link Type} that will expect a single object in the root json tree. */
    protected static Type single(Type searchFor, String... roots) {
        return new CompositeType(null, searchFor, CompositeType.Structure.SINGLE, roots);
    }

    /** Returns a {@link Type} that will expect a list of objects in the root json tree. */
    protected static Type list(Type searchFor, String... roots) {
        return new CompositeType(null, searchFor, CompositeType.Structure.LIST, roots);
    }

    /**
     * Returns a {@link Type} that will expect an element of {@code searchFor} as the first element in a json list in
     * the root json tree.
     */
    protected static Type first(Type searchFor, String... roots) {
        return new CompositeType(null, searchFor, CompositeType.Structure.FIRST, roots);
    }
}
