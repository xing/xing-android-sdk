/*
 * Copyright (С) 2016 XING AG (http://xing.com/)
 * Copyright (C) 2011 Google Inc.
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
package com.xing.api.internal.json;

import android.support.annotation.Nullable;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
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

/**
 * Json adapter for all {@link Enum<T>}. This should overrule Moshi's default adapter for enums and allow parsing
 * enum constants annotated with {@linkplain Json}.
 * <p>
 * Borrowed from Gson. See Gson's <a
 * href="https://github.com/google/gson/blob/master/gson/src/main/java/com/google/gson/internal/bind/TypeAdapters.java">
 * TypeAdapters.java</a>
 */
@SuppressWarnings("CollectionWithoutInitialCapacity")
public final class EnumJsonAdapter<T extends Enum<T>> extends JsonAdapter<T> {
    public static final JsonAdapter.Factory FACTORY = new JsonAdapter.Factory() {
        @Nullable
        @Override
        public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
            if (!annotations.isEmpty()) return null;
            Class<?> rawType = Types.getRawType(type);
            if (!rawType.isEnum()) return null;
            //noinspection unchecked We know that this is an enum.
            return new EnumJsonAdapter<>((Class<? extends Enum>) rawType).nullSafe();
        }
    };

    private final Class<T> enumType;
    private final Map<String, T> nameToConstant = new LinkedHashMap<>();
    private final Map<T, String> constantToName = new LinkedHashMap<>();

    EnumJsonAdapter(Class<T> enumType) {
        this.enumType = enumType;
        try {
            T[] constants = enumType.getEnumConstants();
            for (T constant : constants) {
                Json annotation = enumType.getField(constant.name()).getAnnotation(Json.class);
                String name = annotation != null ? annotation.name() : constant.name();
                nameToConstant.put(name, constant);
                constantToName.put(constant, name);
            }
        } catch (NoSuchFieldException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public T fromJson(JsonReader reader) throws IOException {
        String name = reader.nextString();
        T constant = nameToConstant.get(name);
        if (constant != null) return constant;
        throw new JsonDataException("Expected one of "
              + nameToConstant.keySet() + " but was " + name + " at path "
              + reader.getPath());
    }

    @Override
    public void toJson(JsonWriter writer, T value) throws IOException {
        writer.value(constantToName.get(value));
    }

    @Override
    public String toString() {
        return "JsonAdapter(" + enumType.getName() + ')';
    }
}
