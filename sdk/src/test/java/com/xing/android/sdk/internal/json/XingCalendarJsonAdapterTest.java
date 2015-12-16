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

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.xing.android.sdk.model.XingCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Calendar;

import okio.Buffer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author daniel.hartwich
 */
public class XingCalendarJsonAdapterTest {
    private Moshi moshi;
    private XingCalendar comparisonCalendar;

    @SuppressWarnings("MagicNumber")
    @Before
    public void setUp() throws Exception {
        moshi = new Moshi.Builder()
              .add(XingCalendarJsonAdapter.FACTORY)
              .build();
        comparisonCalendar = new XingCalendar(2015, Calendar.FEBRUARY, 15);
    }

    @After
    public void tearDown() throws Exception {
        moshi = null;
        comparisonCalendar = null;
    }

    @Test
    public void testFromJson() throws Exception {
        JsonAdapter<XingCalendarHelper> adapter = moshi.adapter(XingCalendarHelper.class);

        assertThat(adapter.fromJson("{\"calendar\" : \"2015-02-15\"}").xingCalendar.get(Calendar.MONTH))
              .isEqualTo(XingCalendar.FEBRUARY);
        assertThat(adapter.fromJson("{\"calendar\" : \"2015-02-15\"}").xingCalendar.get(Calendar.DAY_OF_MONTH))
              .isEqualTo(comparisonCalendar.get(Calendar.DAY_OF_MONTH));
        assertThat(adapter.fromJson("{\"calendar\" : \"2015-02-15\"}").xingCalendar.get(Calendar.YEAR))
              .isEqualTo(comparisonCalendar.get(Calendar.YEAR));
    }

    @Ignore //TODO Fix Flaky Test
    @Test
    public void testToJson() throws Exception {
        Buffer buffer = new Buffer();
        JsonWriter writer = JsonWriter.of(buffer);
        writer.setLenient(true);
        moshi.adapter(XingCalendar.class).toJson(writer, comparisonCalendar);
        writer.flush();
        String bufferedString = buffer.readUtf8();

        assertThat(bufferedString).isEqualTo('"' + "2015-02-14T23:00:00.000Z" + '"');
    }

    static class XingCalendarHelper {
        @Json(name = "calendar")
        public XingCalendar xingCalendar;
    }
}
