/*
 * Copyright (C) 2016 XING SE (http://xing.com/)
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

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonReader.Token;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.xing.api.data.profile.TimeZone;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * Adapter for {@linkplain TimeZone} objects. When parsing a {@linkplain TimeZone} will return {@code null} if the
 * provided values are invalid.
 */
public final class TimeZoneJsonAdapter extends JsonAdapter<TimeZone> {
    public static final JsonAdapter.Factory FACTORY = new JsonAdapter.Factory() {
        @Override
        public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
            if (!annotations.isEmpty()) return null;
            Class<?> rawType = Types.getRawType(type);
            if (rawType != TimeZone.class) return null;
            return new TimeZoneJsonAdapter().nullSafe();
        }
    };

    TimeZoneJsonAdapter() {
    }

    @Override
    public TimeZone fromJson(JsonReader reader) throws IOException {
        String timeZoneName = null;
        Double utcOffset = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (reader.peek() == Token.NULL) {
                reader.skipValue();
                continue;
            }

            if ("name" .equals(name)) {
                timeZoneName = reader.nextString();
            } else if ("utc_offset" .equals(name)) {
                utcOffset = reader.nextDouble();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        if (timeZoneName != null && utcOffset != null) return new TimeZone(timeZoneName, utcOffset);
        return null;
    }

    @Override
    public void toJson(JsonWriter writer, TimeZone timeZone) throws IOException {
        writer.beginObject();
        writer.name("name").value(timeZone.name());
        writer.name("utc_offset").value(timeZone.utcOffset());
        writer.endObject();
    }

    @Override
    public String toString() {
        return "JsonAdapter(TimeZone)";
    }
}
