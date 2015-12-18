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
import com.squareup.moshi.Rfc3339DateJsonAdapter;
import com.squareup.moshi.Types;
import com.xing.api.model.XingCalendar;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Set;

/**
 * @author daniel.hartwich
 */
public class XingCalendarJsonAdapter extends JsonAdapter<XingCalendar> {
    public static final Factory FACTORY = new Factory() {
        @Nullable
        @Override
        public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
            if (!annotations.isEmpty()) return null;
            Class<?> rawType = Types.getRawType(type);
            if (rawType != XingCalendar.class) return null;
            return new XingCalendarJsonAdapter(new Rfc3339DateJsonAdapter()).nullSafe();
        }
    };

    private final Rfc3339DateJsonAdapter delegate;

    XingCalendarJsonAdapter(Rfc3339DateJsonAdapter adapter) {
        delegate = adapter;
    }

    @Override
    public XingCalendar fromJson(JsonReader reader) throws IOException {
        Date date = delegate.fromJson(reader);
        XingCalendar calendar = new XingCalendar();
        calendar.setTime(date);
        return calendar;
    }

    @Override
    public void toJson(JsonWriter writer, XingCalendar value) throws IOException {
        Date date = value.getTime();
        delegate.toJson(writer, date);
    }
}
