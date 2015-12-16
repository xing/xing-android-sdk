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

package com.xing.android.sdk.internal.json;

import android.support.annotation.Nullable;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Rfc3339DateJsonAdapter;
import com.squareup.moshi.Types;
import com.xing.android.sdk.model.XingCalendar;

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
