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
package com.xing.android.sdk;

import com.squareup.moshi.Types;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * Defines the type of responses, while adding the possibility to add a root, as well as defining if the object is a
 * list or just a single object.
 * The {@link CompositeType} is used when defining the response type of a {@link com.xing.android.sdk.CallSpec} defined
 * in a {@link com.xing.android.sdk.Resource}.
 * <p>
 * This can be used as follows:
 * <pre>
 * {@code
 * CompositeType type = Resource.single(YourReturnObject.class, [List of roots where your object can be found]);
 * // or for a list
 * CompositeType type = Resource.list(YourReturnObject.class, [List of roots where your object can be found]);
 *
 * //Example: Usage inside the UserProfilesResource
 * public CallSpec<XingUser, HttpError> getUsersById(String id) {
 *  return this.<XingUser, HttpError>newGetSpec("/v1/users/{id}")
 *      .pathParam("id", id)
 *      .responseAsList(XingUser.class, "users")
 *      .build();
 * }
 * }
 * </pre>
 *
 * @author daniel.hartwich
 * @author serj.lotutovici
 */
public final class CompositeType implements Type {
    /**
     * Defines the possible response types a user can expect.
     * <p>
     * Currently supported:
     * <li>SINGLE - A single object</li>
     * <li>LIST - A list of objects</li>
     * <li>FIRST - A single object wrapped in a list structure (happens often in profile resources)</li>
     */
    protected enum Structure {
        SINGLE,
        LIST,
        FIRST
    }

    private final Type parseType;
    private final String[] roots;
    private final Structure structure;

    CompositeType(Type parseType, Structure structure, String... roots) {
        this.parseType = parseType;
        this.structure = structure;
        this.roots = roots;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        CompositeType that = (CompositeType) obj;

        if (parseType != null ? !parseType.equals(that.parseType) : that.parseType != null) return false;
        // We need a strict order comparison.
        return Arrays.equals(roots, that.roots) && structure == that.structure;
    }

    @Override
    public int hashCode() {
        int result = parseType != null ? parseType.hashCode() : 0;
        result = 31 * result + (roots != null ? Arrays.hashCode(roots) : 0);
        result = 31 * result + (structure != null ? structure.hashCode() : 0);
        return result;
    }

    /** Return the actual parse type. */
    Type parseType() {
        return structure == Structure.SINGLE ? parseType : Types.newParameterizedType(List.class, parseType);
    }

    /** Return the composite structure. */
    Structure structure() {
        return structure;
    }

    /** Roots the parse type is located. */
    String[] roots() {
        return roots;
    }
}
