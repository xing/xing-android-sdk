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
package com.xing.api.internal.json;

import android.support.annotation.Nullable;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.xing.api.model.SafeCalendar;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Set;

/** Adapter that parses a birthday object into a {@link SafeCalendar}. */
public final class BirthDateJsonAdapter extends JsonAdapter<SafeCalendar> {
    public static final JsonAdapter.Factory FACTORY = new Factory() {
        @Nullable
        @Override
        public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
            if (annotations.isEmpty() || annotations.size() != 1
                  // At this point we know that the set contains only one entry.
                  || annotations.iterator().next().annotationType() != BirthDate.class) {
                return null;
            }
            Class<?> rawType = Types.getRawType(type);
            if (rawType != SafeCalendar.class) return null;
            return new BirthDateJsonAdapter().nullSafe();
        }
    };

    BirthDateJsonAdapter() {
    }

    @Override
    public SafeCalendar fromJson(JsonReader reader) throws IOException {
        SafeCalendar calendar = new SafeCalendar();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if ("year".equals(name)) {
                calendar.set(Calendar.YEAR, reader.nextInt());
            } else if ("month".equals(name)) {
                calendar.set(Calendar.MONTH, reader.nextInt() - 1);
            } else if ("day".equals(name)) {
                calendar.set(Calendar.DAY_OF_MONTH, reader.nextInt());
            } else {
                throw new IOException("birthday should contain 'year', 'month' and/or 'day', found $" + name);
            }
        }
        reader.endObject();

        return calendar;
    }

    @Override
    public void toJson(JsonWriter writer, SafeCalendar value) throws IOException {
        writer.beginObject();
        if (value.isSet(Calendar.YEAR)) writer.name("year").value(value.get(Calendar.YEAR));
        if (value.isSet(Calendar.MONTH)) writer.name("month").value(value.get(Calendar.MONTH) + 1);
        if (value.isSet(Calendar.DAY_OF_MONTH)) writer.name("day").value(value.get(Calendar.DAY_OF_MONTH));
        writer.endObject();
    }
}
