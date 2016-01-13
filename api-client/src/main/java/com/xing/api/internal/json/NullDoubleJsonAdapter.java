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
package com.xing.api.internal.json;

import android.support.annotation.Nullable;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

/** Adapter for double values annotated with {@linkplain NullDouble}. */
public final class NullDoubleJsonAdapter extends JsonAdapter<Double> {
    public static final Factory FACTORY = new Factory() {
        @Nullable
        @Override
        public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
            if (annotations.isEmpty() || annotations.size() != 1
                  // At this point we know that the set contains only one entry.
                  || annotations.iterator().next().annotationType() != NullDouble.class) {
                return null;
            }
            Class<?> rawType = Types.getRawType(type);
            if (rawType == double.class || rawType == Double.class) return new NullDoubleJsonAdapter();
            return null;
        }
    };

    NullDoubleJsonAdapter() {
    }

    @Override
    public Double fromJson(JsonReader reader) throws IOException {
        if (reader.peek() == JsonReader.Token.NULL) {
            reader.skipValue(); // Consume the next token.
            return -1D;
        } else {
            return reader.nextDouble();
        }
    }

    @Override
    public void toJson(JsonWriter writer, Double value) throws IOException {
        if (value == null || value == -1) {
            writer.nullValue();
        } else {
            writer.value(value.doubleValue());
        }
    }

    @Override
    public String toString() {
        return "JsonAdapter(NullDouble)";
    }
}
