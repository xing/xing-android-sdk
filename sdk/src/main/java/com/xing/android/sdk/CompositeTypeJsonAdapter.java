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
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.xing.android.sdk.Utils.ioError;

/**
 * Json adapter that parses {@link CompositeType}.
 *
 * @author serj.lotutovici
 */
final class CompositeTypeJsonAdapter<T> extends JsonAdapter<T> {
    static final JsonAdapter.Factory FACTORY = new Factory() {
        @Override
        public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
            if (!annotations.isEmpty()) return null;
            if (!(type instanceof CompositeType)) return null;
            return new CompositeTypeJsonAdapter<>(moshi, (CompositeType) type).nullSafe();
        }
    };

    private final JsonAdapter<?> adapter;
    private final CompositeType.Structure structure;
    private final String[] roots;

    public CompositeTypeJsonAdapter(Moshi moshi, CompositeType type) {
        this.adapter = moshi.adapter(type.parseType());
        this.roots = type.roots();
        this.structure = type.structure();
    }

    @Override
    public T fromJson(JsonReader reader) throws IOException {
        Object result = readRootLeafs(adapter, reader, roots, 0);

        if (structure == CompositeType.Structure.FIRST) {
            //noinspection unchecked This puts full responsibility on the caller.
            List<T> list = (List<T>) result;
            return list != null && !list.isEmpty() ? list.get(0) : null;
        }

        //noinspection unchecked
        return (T) result;
    }

    @Override
    public void toJson(JsonWriter writer, T value) throws IOException {
        // CompositeType is a simplification for parsing XWS responses. No need to write it as a json.
    }

    /**
     * Recursive method that goes through the JSON, finds the given root and returns the objects with the provided
     * roots.
     */
    @Nullable
    private static <T> T readRootLeafs(JsonAdapter<?> adapter, @NonNull JsonReader reader, String[] roots, int index)
          throws IOException {
        if (roots == null || index == roots.length) {
            //noinspection unchecked This puts full responsibility on the caller.
            return (T) adapter.fromJson(reader);
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
                reader.endObject();
            }
            throw ioError("Json does not match expected structure for roots %s.", Arrays.asList(roots));
        }
    }
}
