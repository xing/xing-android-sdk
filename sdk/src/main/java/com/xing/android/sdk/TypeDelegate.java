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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import okio.BufferedSource;

/**
 * Defines the type of responses, while adding the possibility to add a root, as well as defining if the object is a
 * list or just a single object.
 * The TypeDelegate can be used when defining the response type when building the CallSpec for a Resource
 * This can be used as follows:
 * <pre>
 * {@code
 *  TypeDelegate delegate = TypeDelegate.single(YourReturnObject.class, [Root or List of roots where your object can be
 *  found])
 *  // or for a list
 *  TypeDelegate delegate = TypeDelegate.list(YourReturnObject.class, [Root or List of roots where your object can be
 *  found])
 *
 * //Example: Usage inside the UserProfilesResource
 * public CallSpec<XingUser, Object> getUsersById(String id) {
 *  return this.<XingUser, Object>newGetSpec("/v1/users/{id}")
 *      .pathParam("id", id)
 *      .responseType(TypeDelegate.list(XingUser.class, "users")
 *      .build();
 * }
 * }
 *
 * </pre>
 *
 * @author daniel.hartwich
 * @author serj.lotutovici
 */
public final class TypeDelegate {
    public static TypeDelegate single(Class<?> classType, String... roots) {
        return new TypeDelegate(classType, false, roots);
    }

    public static TypeDelegate list(Class<?> classType, String... roots) {
        return new TypeDelegate(classType, true, roots);
    }

    private final Type classType;
    private final String[] roots;
    private final boolean isListType;

    private TypeDelegate(Class<?> classType, boolean isListType, String... roots) {
        this.classType = classType;
        this.isListType = isListType;
        this.roots = roots;
    }

    /**
     * Parses the ResponseBody, finds the provided root and returns the object.
     */
    @Nullable
    public <T> T from(Moshi moshi, ResponseBody body) throws IOException {
        Type type = getType();
        BufferedSource source = body.source();

        JsonAdapter<T> adapter = moshi.adapter(type);
        try {
            if (roots != null && roots.length != 0) {
                return readRootLeafs(adapter, JsonReader.of(source), roots, 0);
            } else {
                return adapter.fromJson(source);
            }
        } finally {
            if (source != null) {
                try {
                    source.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    /**
     * Helper that gives you the Type, making a difference between list and "standalone" object.
     *
     * @return Type - The type that should be returned after parsing the given object.
     */
    public Type getType() {
        return isListType ? Types.newParameterizedType(List.class, classType) : classType;
    }

    /**
     * Recursive method that goes through the JSON, finds the given root and returns the objects with the provided
     * roots.
     */
    @Nullable
    private static <T> T readRootLeafs(@NonNull JsonAdapter<T> adapter, @NonNull JsonReader reader,
          @NonNull String[] roots, int index) throws IOException {
        if (index == roots.length) {
            return adapter.fromJson(reader);
        } else {
            reader.beginObject();
            try {
                String root = roots[index];
                while (reader.hasNext()) {
                    if (reader.nextName().equals(root)) {
                        if (reader.peek() == JsonReader.Token.NULL) {
                            return reader.nextNull();
                        }

                        return readRootLeafs(adapter, reader, roots, ++index);
                    } else {
                        reader.skipValue();
                    }
                }
            } finally {
                try {
                    reader.endObject();
                } catch (IOException ioe) {
                    // Ignore, in case we are closing early.
                }
            }
            throw new IOException(String.format("Json does not match expected structure for roots %s.",
                  Arrays.asList(roots)));
        }
    }
}
