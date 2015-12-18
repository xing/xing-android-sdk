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

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.xing.api.model.XingCalendar;

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
