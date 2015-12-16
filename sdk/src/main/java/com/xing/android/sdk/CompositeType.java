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

import com.squareup.moshi.Types;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * Defines the type of responses, while adding the possibility to add a root, as well as defining if the object is a
 * list or just a single object.
 * The {@link CompositeType} can be used when defining the response type when building the CallSpec for a Resource.
 * <p>
 * This can be used as follows:
 * <pre>
 * {@code
 * CompositeType type = Resource.single(YourReturnObject.class, [List of roots where your object can be found]);
 * // or for a list
 * CompositeType type = Resource.list(YourReturnObject.class, [List of roots where your object can be found]);
 *
 * //Example: Usage inside the UserProfilesResource
 * public CallSpec<XingUser, Object> getUsersById(String id) {
 *  return this.<XingUser, Object>newGetSpec("/v1/users/{id}")
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
