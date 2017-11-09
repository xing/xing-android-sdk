/*
 * Copyright (С) 2016 XING SE (http://xing.com/)
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

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonReader.Token;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.xing.api.data.GeoCode;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * Adapter for {@linkplain GeoCode} objects. When parsing a {@linkplain GeoCode} will return {@code null} if the
 * provided values are invalid.
 */
public final class GeoCodeJsonAdapter extends JsonAdapter<GeoCode> {
    public static final JsonAdapter.Factory FACTORY = new JsonAdapter.Factory() {
        @Override
        public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
            if (!annotations.isEmpty()) return null;
            Class<?> rawType = Types.getRawType(type);
            if (rawType != GeoCode.class) return null;
            return new GeoCodeJsonAdapter().nullSafe();
        }
    };

    GeoCodeJsonAdapter() {
    }

    @Override
    public GeoCode fromJson(JsonReader reader) throws IOException {
        // Using nulls to ensure the values where set.
        Double latitude = null;
        Double longitude = null;
        int accuracy = 0;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (reader.peek() == Token.NULL) {
                reader.skipValue();
                continue;
            }

            if ("accuracy" .equals(name)) {
                accuracy = reader.nextInt();
            } else if ("latitude" .equals(name)) {
                latitude = reader.nextDouble();
            } else if ("longitude" .equals(name)) {
                longitude = reader.nextDouble();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        if (latitude == null || longitude == null) return null;
        return new GeoCode(accuracy, latitude, longitude);
    }

    @Override
    public void toJson(JsonWriter writer, GeoCode geoCode) throws IOException {
        writer.beginObject();
        writer.name("accuracy").value(geoCode.accuracy());
        writer.name("latitude").value(geoCode.latitude());
        writer.name("longitude").value(geoCode.longitude());
        writer.endObject();
    }

    @Override
    public String toString() {
        return "JsonAdapter(GeoCode)";
    }
}
