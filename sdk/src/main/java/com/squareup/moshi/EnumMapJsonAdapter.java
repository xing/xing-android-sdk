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
package com.squareup.moshi;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * @author daniel.hartwich
 */
public final class EnumMapJsonAdapter<K extends Enum<K>, V> extends JsonAdapter<EnumMap<K, V>> {
    public static final Factory FACTORY = new Factory() {
        @Override public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations,
              Moshi moshi) {
            if (!annotations.isEmpty()) return null;
            Class<?> rawType = Types.getRawType(type);
            if (rawType != EnumMap.class) return null;
            Type[] keyAndValue = Types.mapKeyAndValueTypes(type, rawType);
            return new EnumMapJsonAdapter<>(moshi, keyAndValue[0], keyAndValue[1]).nullSafe();
        }
    };

    private final Type keyType;
    private final JsonAdapter<K> keyAdapter;
    private final JsonAdapter<V> valueAdapter;

    public EnumMapJsonAdapter(Moshi moshi, Type keyType, Type valueType) {
        this.keyType = keyType;
        this.keyAdapter = moshi.adapter(keyType);
        this.valueAdapter = moshi.adapter(valueType);
    }

    @Override public EnumMap<K, V> fromJson(JsonReader reader) throws IOException {
        // We can ignore the cast warning since the type is for sure an enum.
        @SuppressWarnings("unchecked") EnumMap<K, V> result = new EnumMap<>((Class<K>) keyType);
        reader.beginObject();
        while (reader.hasNext()) {
            reader.promoteNameToValue();
            K key = keyAdapter.fromJson(reader);
            V value = valueAdapter.fromJson(reader);
            V replaced = result.put(key, value);
            if (replaced != null) {
                throw new JsonDataException("EnumMap key '" + key + "' has multiple values at path "
                      + reader.getPath());
            }
        }
        reader.endObject();
        return result;
    }

    @Override public void toJson(JsonWriter writer, EnumMap<K, V> map) throws IOException {
        writer.beginObject();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getKey() == null) {
                throw new JsonDataException("Map key is null at path " + writer.getPath());
            }
            writer.promoteNameToValue();
            keyAdapter.toJson(writer, entry.getKey());
            valueAdapter.toJson(writer, entry.getValue());
        }
        writer.endObject();
    }
}

