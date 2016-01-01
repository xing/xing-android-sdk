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

import android.support.annotation.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * Defines the type of responses, while adding the possibility to add a root, as well as defining if the object is a
 * list or just a single object.
 * The {@link CompositeType} is used when defining the response type of a {@link CallSpec} defined
 * in a {@link Resource}.
 * <p>
 * This can be used as follows:
 * <pre>
 * {@code
 * CompositeType type = Resource.single(YourReturnObject.class, [List of roots where your object can be found]);
 * // or for a list
 * CompositeType type = Resource.list(YourReturnObject.class, [List of roots where your object can be found]);
 *
 * //Example: Usage inside the UserProfilesResource
 * public CallSpec<XingUser, HttpError> getUserById(String id) {
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
final class CompositeType implements ParameterizedType {
    static final String[] NO_ROOTS = new String[0];

    /**
     * Defines the possible response structures a caller may search for.
     * <p>
     * Currently supported:
     * <li>SINGLE - A single object</li>
     * <li>LIST - A list of objects</li>
     * <li>FIRST - A single object wrapped in a list structure (happens often in profile resources)</li>
     */
    enum Structure {
        SINGLE,
        LIST,
        FIRST
    }

    final Type ownerType;
    final Type searchFor;
    final String[] roots;
    final Structure structure;

    CompositeType(Type ownerType, Type searchFor, Structure structure, String... roots) {
        this.ownerType = ownerType;
        this.searchFor = searchFor;
        this.structure = structure;
        this.roots = roots;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return new Type[]{toFind()};
    }

    @Override
    public Type getOwnerType() {
        return ownerType;
    }

    @Override
    public Type getRawType() {
        return CompositeType.class;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        CompositeType compareTo = (CompositeType) obj;

        if (searchFor != null ? !searchFor.equals(compareTo.searchFor) : compareTo.searchFor != null) return false;
        // We need a strict order comparison.
        return Arrays.equals(roots, compareTo.roots) && structure == compareTo.structure;
    }

    @Override
    public int hashCode() {
        int result = searchFor != null ? searchFor.hashCode() : 0;
        result = 31 * result + (roots != null ? Arrays.hashCode(roots) : 0);
        result = 31 * result + (structure != null ? structure.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CompositeType[" + toFind() + ']';
    }

    /** Return the actual parse type. */
    Type toFind() {
        return structure == Structure.SINGLE
              ? normalize(searchFor, this)
              : new ListTypeImpl(normalize(searchFor, List.class));
    }

    /** Return the composite structure. */
    Structure structure() {
        return structure;
    }

    /** Roots where {@link #toFind()} is located. */
    String[] roots() {
        return roots != null ? roots : NO_ROOTS;
    }

    /** Make sure that the required type will be processed as expected. */
    private static Type normalize(Type type, Type ownerType) {
        if (type instanceof CompositeType) {
            CompositeType composite = (CompositeType) type;
            // The caller is not aware of type ownership.
            if (composite.ownerType != ownerType) {
                return new CompositeType(ownerType, composite.searchFor, composite.structure, composite.roots);
            }
        }

        return type;
    }

    /** Helps to avoid going through Moshi's ParameterizedTypeImpl so that {@code argType} is not wrapped by it. */
    static final class ListTypeImpl implements ParameterizedType {
        private final Type[] typeArguments;

        ListTypeImpl(Type argType) {
            typeArguments = new Type[]{argType};
        }

        @Override
        public Type[] getActualTypeArguments() {
            return typeArguments;
        }

        @Nullable
        @Override
        public Type getOwnerType() {
            return null;
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public String toString() {
            return "ParameterizedType[List[" + typeArguments[0] + "]]";
        }
    }
}
