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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Json adapter that parses {@link CompositeType}.
 *
 * @author serj.lotutovici
 */
final class CompositeTypeJsonAdapter<T> extends JsonAdapter<T> {
    static final JsonAdapter.Factory FACTORY = new Factory() {
        @Nullable
        @Override
        public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
            if (!annotations.isEmpty()) return null;
            Class<?> rawType = Types.getRawType(type);
            if (rawType != CompositeType.class) return null;
            return new CompositeTypeJsonAdapter<>(moshi, (CompositeType) type).nullSafe();
        }
    };

    private final JsonAdapter<?> adapter;
    private final CompositeType.Structure structure;
    private final String[] roots;

    public CompositeTypeJsonAdapter(Moshi moshi, CompositeType type) {
        adapter = moshi.adapter(type.toFind());
        roots = type.roots();
        structure = type.structure();
    }

    @Nullable
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
    public void toJson(JsonWriter writer, T value) {
        // CompositeType is a simplification for parsing XWS responses. No need to write it as a json.
    }

    @Override
    public String toString() {
        return String.format("JsonAdapter[%s][%s](%s)", structure, Arrays.asList(roots), adapter);
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
                // If the json has an additional key, that was not red, we ignore it.
                while (reader.hasNext()) reader.skipValue();
                reader.endObject();
            }
            throw Utils.ioError("Json does not match expected structure for roots %s.", Arrays.asList(roots));
        }
    }
}
