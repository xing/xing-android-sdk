/*
 * Copyright (C) 2016 XING AG (http://xing.com/)
 * Copyright (C) 2014 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xing.api.internal.json;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/** Null safe json adapter for enums. This adapter will not fail if there is no value to map in a programmed enum. */
public final class SafeEnumJsonAdapter<T extends Enum<T>> extends JsonAdapter<T> {
    public static final JsonAdapter.Factory FACTORY = new JsonAdapter.Factory() {
        @Override
        public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
            if (!annotations.isEmpty()) return null;
            Class<?> rawType = Types.getRawType(type);
            if (rawType.isEnum()) {
                //noinspection unchecked
                return new SafeEnumJsonAdapter<>((Class<? extends Enum>) rawType).nullSafe();
            }
            return null;
        }
    };

    private final Class<T> enumType;
    private final Map<String, T> nameConstantMap;
    private final String[] nameStrings;

    SafeEnumJsonAdapter(Class<T> enumType) {
        this.enumType = enumType;
        try {
            T[] constants = enumType.getEnumConstants();
            //noinspection CollectionWithoutInitialCapacity
            nameConstantMap = new LinkedHashMap<>();
            nameStrings = new String[constants.length];
            for (int i = 0, size = constants.length; i < size; i++) {
                T constant = constants[i];
                Json annotation = enumType.getField(constant.name()).getAnnotation(Json.class);
                String name = annotation != null ? annotation.name() : constant.name();
                nameConstantMap.put(name, constant);
                nameStrings[i] = name;
            }
        } catch (NoSuchFieldException e) {
            throw new AssertionError("Missing field in " + enumType.getName(), e);
        }
    }

    @Override
    public T fromJson(JsonReader reader) throws IOException {
        String name = reader.nextString();
        return nameConstantMap.get(name);
    }

    @Override
    public void toJson(JsonWriter writer, T value) throws IOException {
        writer.value(nameStrings[value.ordinal()]);
    }

    @Override
    public String toString() {
        return "SafeEnumJsonAdapter(" + enumType.getName() + ')';
    }
}
