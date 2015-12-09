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

