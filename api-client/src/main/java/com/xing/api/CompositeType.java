/*
 * Copyright (С) 2016 XING AG (http://xing.com/)
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
final class CompositeType implements Type {
    // TODO(2.1.0) we need our own way of caching for CompositeType adapters.

    /**
     * Finds the appropriate adapter for the provided type.
     * <p>
     * Note: This bypasses Moshi's adapter construction if the input is a {@link CompositeType}. This is required
     * du to the fact that Moshi doesn't accept custom types.
     */
    static <T> JsonAdapter<T> findAdapter(Moshi moshi, Type type) {
        if (type instanceof CompositeType) {
            CompositeType compositeType = (CompositeType) type;
            JsonAdapter<T> delegate = findAdapter(moshi, compositeType.toFind());
            CompositeType.Structure structure = compositeType.structure();
            String[] roots = compositeType.roots();

            return new CompositeJsonAdapter<>(delegate, structure, roots);
        }

        if (type instanceof ListTypeImpl) {
            ListTypeImpl listType = (ListTypeImpl) type;
            JsonAdapter<?> delegate = findAdapter(moshi, listType.getActualTypeArguments()[0]);
            //noinspection unchecked
            return (JsonAdapter<T>) new ListTypeImplJsonAdapter<>(delegate);
        }

        return moshi.adapter(type);
    }

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

    private final Type searchFor;
    private final String[] roots;
    private final Structure structure;

    CompositeType(Type searchFor, Structure structure, String... roots) {
        this.searchFor = searchFor;
        this.structure = structure;
        this.roots = roots;
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
              ? searchFor
              : new ListTypeImpl(searchFor);
    }

    /** Return the composite structure. */
    Structure structure() {
        return structure;
    }

    /** Roots where {@link #toFind()} is located. */
    String[] roots() {
        return roots != null ? roots : NO_ROOTS;
    }

    /** Helps to avoid going through Moshi's ParameterizedTypeImpl so that {@code argType} is not wrapped by it. */
    private static final class ListTypeImpl implements ParameterizedType {
        private final Type[] typeArguments;

        ListTypeImpl(Type argType) {
            typeArguments = new Type[]{argType};
        }

        @Override
        public Type[] getActualTypeArguments() {
            return typeArguments;
        }

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

    private static final class ListTypeImplJsonAdapter<T> extends JsonAdapter<List<T>> {
        private final JsonAdapter<T> elementAdapter;

        ListTypeImplJsonAdapter(JsonAdapter<T> elementAdapter) {
            this.elementAdapter = elementAdapter;
        }

        @Override
        public List<T> fromJson(JsonReader reader) throws IOException {
            //noinspection CollectionWithoutInitialCapacity We don't know the size of the array.
            List<T> result = new ArrayList<>();
            reader.beginArray();
            while (reader.hasNext()) {
                result.add(elementAdapter.fromJson(reader));
            }
            reader.endArray();
            return result;
        }

        @Override
        public void toJson(JsonWriter writer, List<T> value) throws IOException {
            writer.beginArray();
            for (int i = 0; i < value.size(); i++) {
                T element = value.get(i);
                elementAdapter.toJson(writer, element);
            }
            writer.endArray();
        }
    }

    private static final class CompositeJsonAdapter<T> extends JsonAdapter<T> {
        private final JsonAdapter<T> adapter;
        private final CompositeType.Structure structure;
        private final String[] roots;

        CompositeJsonAdapter(JsonAdapter<T> adapter, CompositeType.Structure structure, String[] roots) {
            this.adapter = adapter;
            this.structure = structure;
            this.roots = roots;
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
        public void toJson(JsonWriter writer, T value) {
            try {
                writeRootLeafs(adapter, writer, value, roots, 0);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        @Override
        public String toString() {
            return String.format("JsonAdapter[%s][%s](%s)", structure, Arrays.asList(roots), adapter);
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
    }
}
