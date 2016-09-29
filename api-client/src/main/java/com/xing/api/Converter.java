/*
 * Copyright (C) 2016 XING AG (http://xing.com/)
 * Copyright (C) 2016 Square, Inc.
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

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;

import static com.xing.api.Utils.buffer;
import static com.xing.api.Utils.closeQuietly;

/**
 * Facade over {@link Moshi} that is responsible for the creation and caching of custom adapters that provide required
 * unwrapping functionality.
 */
final class Converter {
    static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    /** Returns a {@link Type} that will expect a single object in the root json tree. */
    static Type single(Type searchFor, String... roots) {
        return new CompositeType(searchFor, roots);
    }

    /** Returns a {@link Type} that will expect a list of objects in the root json tree. */
    static Type list(Type searchFor, String... roots) {
        return new CompositeType(new ListTypeImpl(searchFor), roots);
    }

    /**
     * Returns a {@link Type} that will expect an element of {@code searchFor} as the first element in a json list in
     * the root json tree.
     */
    static Type first(Type searchFor, String... roots) {
        return new CompositeType(new ListTypeImpl(searchFor, true), roots);
    }

    @SuppressWarnings("CollectionWithoutInitialCapacity") // We are happy with the initial capacity and the load factor.
    private final Map<Object, JsonAdapter<?>> adapterCache = new LinkedHashMap<>();
    private final Moshi moshi;

    public Converter(Moshi moshi) {
        this.moshi = moshi;
    }

    /** Returns the encapsulated {@link Moshi} object. */
    public Moshi moshi() {
        return moshi;
    }

    /** Converts the contents of the {@link ResponseBody} to the expected java {@code type}. */
    @SuppressWarnings("unchecked") // Type matching is the caller's responsibility.
    public <R> R convertFromBody(Type type, ResponseBody body) throws IOException {
        if (body == null) return null;

        try {
            // Don't parse the response body, if the caller doesn't expect a json.
            if (type == Void.class) return null;
            if (type == String.class) return (R) body.string();
            if (type == ResponseBody.class) return (R) buffer(body);

            JsonAdapter<R> adapter = findAdapter(type);
            JsonReader reader = JsonReader.of(body.source());
            return adapter.fromJson(reader);
        } finally {
            closeQuietly(body);
        }
    }

    /** Converts the java {@code type} to a {@link RequestBody} with a "application/json" {@link MediaType}. */
    public <B> RequestBody convertToBody(Type type, B body) {
        Buffer buffer = new Buffer();
        JsonAdapter<B> jsonAdapter = findAdapter(type);
        try {
            jsonAdapter.toJson(buffer, body);
        } catch (IOException ignored) {
            // Doesn't need to be handled. Buffer should not throw in this case.
        }
        return RequestBody.create(MEDIA_TYPE_JSON, buffer.readByteArray());
    }

    @SuppressWarnings("unchecked")
    public <T> JsonAdapter<T> findAdapter(Type type) {
        if (type instanceof CompositeType) {
            CompositeType compositeType = (CompositeType) type;
            synchronized (adapterCache) {
                JsonAdapter<?> adapter = adapterCache.get(compositeType);
                if (adapter != null) return (JsonAdapter<T>) adapter;
            }

            JsonAdapter<T> delegate = findAdapter(compositeType.searchFor);
            JsonAdapter<T> adapter = new CompositeJsonAdapter<>(delegate, compositeType.roots);
            synchronized (adapterCache) {
                adapterCache.put(compositeType, adapter);
            }

            return adapter;
        }

        if (type instanceof ListTypeImpl) {
            ListTypeImpl listType = (ListTypeImpl) type;
            synchronized (adapterCache) {
                JsonAdapter<?> adapter = adapterCache.get(listType);
                if (adapter != null) return (JsonAdapter<T>) adapter;
            }

            JsonAdapter<?> delegate = findAdapter(listType.type);
            JsonAdapter<T> adapter = (JsonAdapter<T>) new ListTypeImplJsonAdapter<>(delegate, listType.isFirst);
            synchronized (adapterCache) {
                adapterCache.put(listType, adapter);
            }

            return adapter;
        }

        // Moshi has it's own adapter cache, no need to do anything here
        return moshi.adapter(type);
    }

    /** Represents a type that contains a certain structure and path to the expected json object. */
    private static final class CompositeType implements Type {
        private static final String[] NO_ROOTS = new String[0];

        final Type searchFor;
        final String[] roots;

        CompositeType(Type searchFor, String... roots) {
            this.searchFor = searchFor;
            this.roots = roots != null ? roots : NO_ROOTS;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            CompositeType compareTo = (CompositeType) obj;

            if (searchFor != null ? !searchFor.equals(compareTo.searchFor) : compareTo.searchFor != null) {
                return false;
            }
            // We need a strict order comparison.
            return Arrays.equals(roots, compareTo.roots);
        }

        @Override
        public int hashCode() {
            int result = searchFor != null ? searchFor.hashCode() : 0;
            result = 31 * result + (roots != null ? Arrays.hashCode(roots) : 0);
            return result;
        }

        @Override
        public String toString() {
            return "CompositeType(" + searchFor + ')';
        }
    }

    /** JsonAdapter for the {@link CompositeType}. */
    private static final class CompositeJsonAdapter<T> extends JsonAdapter<T> {
        private final JsonAdapter<T> adapter;
        private final String[] roots;

        CompositeJsonAdapter(JsonAdapter<T> adapter, String[] roots) {
            this.adapter = adapter;
            this.roots = roots;
        }

        @Override
        public T fromJson(JsonReader reader) throws IOException {
            return readRootLeafs(adapter, reader, roots, 0);
        }

        /** Recursively goes through the JSON and finds the given root. Returns the object(s) found in provided roots. */
        private static <T> T readRootLeafs(JsonAdapter<T> adapter, JsonReader reader, String[] roots, int index)
              throws IOException {
            if (roots == null || index == roots.length) {
                //noinspection unchecked This puts full responsibility on the caller.
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
                    // If the json has an additional key, that was not red, we ignore it.
                    while (reader.hasNext()) {
                        reader.skipValue();
                    }
                    reader.endObject();
                }
                throw new IOException(String.format(
                      "Json does not match expected structure for roots %s.",
                      Arrays.asList(roots)));
            }
        }

        @Override
        public void toJson(JsonWriter writer, T value) {
            try {
                writeRootLeafs(adapter, writer, value, roots, 0);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        /**
         * Recursively writes the respective roots forming a json object that resembles the {@code roots} and {@code
         * structure} of the {@linkplain CompositeType}.
         */
        private static <T> void writeRootLeafs(JsonAdapter<T> adapter, JsonWriter writer, T value,
              String[] roots, int index) throws IOException {
            if (roots == null || index == roots.length) {
                adapter.toJson(writer, value);
            } else {
                writer.beginObject();
                writeRootLeafs(adapter, writer.name(roots[index]), value, roots, ++index);
                writer.endObject();
            }
        }

        @Override
        public String toString() {
            return adapter + String.format(".compositeAt(%s)", Arrays.asList(roots));
        }
    }

    /**
     * Helps to avoid going through Moshi's internal {@link ParameterizedType}
     * so that {@code type} is not lost if it's a {@link CompositeType}.
     */
    private static final class ListTypeImpl implements Type {
        final Type type;
        final boolean isFirst;

        ListTypeImpl(Type type) {
            this(type, false);
        }

        ListTypeImpl(Type type, boolean isFirst) {
            this.type = type;
            this.isFirst = isFirst;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ListTypeImpl listType = (ListTypeImpl) o;
            return type.equals(listType.type)
                  && isFirst == listType.isFirst;
        }

        @Override
        public int hashCode() {
            int result = type != null ? type.hashCode() : 0;
            result = 31 * result + (isFirst ? 1 : 0);
            return result;
        }

        @Override
        public String toString() {
            return "ListTypeImpl(" + type + ')' + (isFirst ? ".first()" : "");
        }
    }

    /** JsonAdapter for the custom ListTypeImpl, to avoid going through Moshi. */
    private static final class ListTypeImplJsonAdapter<T> extends JsonAdapter<T> {
        private final JsonAdapter<T> elementAdapter;
        private final boolean isFirst;

        ListTypeImplJsonAdapter(JsonAdapter<T> elementAdapter, boolean isFirst) {
            this.elementAdapter = elementAdapter;
            this.isFirst = isFirst;
        }

        @Override
        public T fromJson(JsonReader reader) throws IOException {
            // We can't know the size of the array at this point.
            //noinspection CollectionWithoutInitialCapacity
            List<Object> result = new ArrayList<>();
            reader.beginArray();
            while (reader.hasNext()) {
                result.add(elementAdapter.fromJson(reader));
            }
            reader.endArray();

            if (isFirst) {
                //noinspection unchecked
                return !result.isEmpty() ? (T) result.get(0) : null;
            }

            //noinspection unchecked
            return (T) result;
        }

        @Override
        public void toJson(JsonWriter writer, T value) throws IOException {
            writer.beginArray();

            List<?> list;
            if (value instanceof List) {
                //noinspection unchecked
                list = (List<Object>) value;
            } else {
                list = Collections.singletonList(value);
            }

            for (int i = 0, size = list.size(); i < size; i++) {
                Object element = list.get(i);
                //noinspection unchecked
                elementAdapter.toJson(writer, (T) element);
            }

            writer.endArray();
        }

        @Override
        public String toString() {
            return elementAdapter + ".listTypeImpl()" + (isFirst ? ".first()" : "");
        }
    }
}
