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

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.xing.api.data.SafeCalendar;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import static com.xing.api.internal.json.SafeCalendarJsonAdapter.ZULU_TIME_ZONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * Test for {@linkplain SafeCalendarJsonAdapter}.
 * <p>
 * Currently supporting the next formats:
 * <li>"yyyy"</li>
 * <li>"yyyy-MM"</li>
 * <li>"yyyy-MM-dd"</li>
 * <li>"yyyy-MM-dd'T'HH:mm.ssZ"</li>
 * <li>"yyyy-MM-dd'T'HH:mm.ss.SSSZ"</li>
 * <li>"yyyy-MM-dd'T'HH:mm:ssXXX"</li>
 * </p>
 */
public final class SafeCalendarJsonAdapterTest {
    /** Run all tests in Central European Time. */
    @Rule
    public final TimeZoneRule rule = new TimeZoneRule(TimeZone.getTimeZone("CET"));
    public final Moshi moshi = new Moshi.Builder().build();

    @Test
    public void ignoresOtherTypes() throws Exception {
        JsonAdapter<?> adapter1 = SafeCalendarJsonAdapter.FACTORY.create(
              String.class, Collections.<Annotation>emptySet(), moshi);
        assertNull(adapter1);

        Set<Annotation> annotations = new LinkedHashSet<>(1);
        annotations.add(mock(Annotation.class));
        JsonAdapter<?> adapter2 = SafeCalendarJsonAdapter.FACTORY.create(
              Calendar.class, annotations, moshi);
        assertNull(adapter2);
    }

    @Test
    public void nullIfEmpty() throws Exception {
        assertNull(calendarAdapter().fromJson("\"\""));
    }

    @Test
    public void throwsUnknown() throws Exception {
        try {
            calendarAdapter().fromJson("\"2010-MAY-13\"");
            fail();
        } catch (Throwable e) {
            assertThat(e).isInstanceOf(AssertionError.class)
                  .hasMessage("Unsupported date format! Expecting ISO 8601, but found: 2010-MAY-13");
        }
    }

    @Test
    public void yearOnly() throws Exception {
        Calendar calendar = new SafeCalendar();
        calendar.set(Calendar.YEAR, 2031);

        String toJson = calendarAdapter().toJson(calendar);
        assertThat(toJson).isEqualTo("\"2031\"");

        Calendar fromJson = calendarAdapter().fromJson("\"1956\"");
        assertNotNull(fromJson);

        assertFalse(fromJson.isSet(Calendar.DAY_OF_MONTH));
        assertFalse(fromJson.isSet(Calendar.MONTH));
        assertTrue(fromJson.isSet(Calendar.YEAR));

        assertThat(fromJson.get(Calendar.YEAR)).isEqualTo(1956);
    }

    @Test
    public void yearAndMonthOnly() throws Exception {
        Calendar calendar = new SafeCalendar();
        calendar.set(Calendar.YEAR, 1987);
        calendar.set(Calendar.MONTH, Calendar.AUGUST);

        String toJson = calendarAdapter().toJson(calendar);
        assertThat(toJson).isEqualTo("\"1987-08\"");

        Calendar fromJson = calendarAdapter().fromJson("\"1945-05\"");
        assertNotNull(fromJson);

        assertFalse(fromJson.isSet(Calendar.DAY_OF_MONTH));
        assertTrue(fromJson.isSet(Calendar.MONTH));
        assertTrue(fromJson.isSet(Calendar.YEAR));

        assertThat(fromJson.get(Calendar.YEAR)).isEqualTo(1945);
        assertThat(fromJson.get(Calendar.MONTH)).isEqualTo(Calendar.MAY);
    }

    @Test
    public void monthAndDayOnly() throws Exception {
        Calendar calendar = new SafeCalendar();
        calendar.set(Calendar.MONTH, Calendar.OCTOBER);
        calendar.set(Calendar.DAY_OF_MONTH, 14);

        String toJson = calendarAdapter().toJson(calendar);
        assertThat(toJson).isEqualTo("\"10-14\"");

        Calendar fromJson = calendarAdapter().fromJson("\"03-05\"");
        assertNotNull(fromJson);

        assertTrue(fromJson.isSet(Calendar.DAY_OF_MONTH));
        assertTrue(fromJson.isSet(Calendar.MONTH));
        assertFalse(fromJson.isSet(Calendar.YEAR));

        assertThat(fromJson.get(Calendar.MONTH)).isEqualTo(Calendar.MARCH);
        assertThat(fromJson.get(Calendar.DAY_OF_MONTH)).isEqualTo(5);
    }

    @Test
    public void yearMonthAndDay() throws Exception {
        Calendar calendar = new SafeCalendar();
        calendar.set(Calendar.YEAR, 1991);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);

        String toJson = calendarAdapter().toJson(calendar);
        assertThat(toJson).isEqualTo("\"1991-12-31\"");

        Calendar fromJson = calendarAdapter().fromJson("\"1988-03-04\"");
        assertNotNull(fromJson);

        assertTrue(fromJson.isSet(Calendar.DAY_OF_MONTH));
        assertTrue(fromJson.isSet(Calendar.MONTH));
        assertTrue(fromJson.isSet(Calendar.YEAR));

        assertThat(fromJson.get(Calendar.YEAR)).isEqualTo(1988);
        assertThat(fromJson.get(Calendar.MONTH)).isEqualTo(Calendar.MARCH);
        assertThat(fromJson.get(Calendar.DAY_OF_MONTH)).isEqualTo(4);
    }

    @Test
    public void iso8601() throws Exception {
        Calendar calendar = new SafeCalendar();
        calendar.setTimeZone(SafeCalendarJsonAdapter.ZULU_TIME_ZONE);
        calendar.set(Calendar.YEAR, 2011);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 23);
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 20);
        calendar.set(Calendar.SECOND, 17);

        String toJson = calendarAdapter().toJson(calendar);
        assertThat(toJson).isEqualTo("\"2011-01-23T20:20:17Z\"");

        Calendar fromJson = calendarAdapter().fromJson("\"2000-02-27T23:00:23Z\"");
        assertNotNull(fromJson);

        assertTrue(fromJson.isSet(Calendar.SECOND));
        assertTrue(fromJson.isSet(Calendar.MINUTE));
        assertTrue(fromJson.isSet(Calendar.HOUR_OF_DAY));
        assertTrue(fromJson.isSet(Calendar.DAY_OF_MONTH));
        assertTrue(fromJson.isSet(Calendar.MONTH));
        assertTrue(fromJson.isSet(Calendar.YEAR));

        assertThat(fromJson.get(Calendar.YEAR)).isEqualTo(2000);
        assertThat(fromJson.get(Calendar.MONTH)).isEqualTo(Calendar.FEBRUARY);
        assertThat(fromJson.get(Calendar.DAY_OF_MONTH)).isEqualTo(27);
        assertThat(fromJson.get(Calendar.HOUR_OF_DAY)).isEqualTo(23);
        assertThat(fromJson.get(Calendar.MINUTE)).isEqualTo(0);
        assertThat(fromJson.get(Calendar.SECOND)).isEqualTo(23);
    }

    @Test
    public void iso8601withTimeZone() throws Exception {
        Calendar fromJson = calendarAdapter().fromJson("\"2000-02-27T23:00:23+0200\"");
        assertNotNull(fromJson);

        assertTrue(fromJson.isSet(Calendar.SECOND));
        assertTrue(fromJson.isSet(Calendar.MINUTE));
        assertTrue(fromJson.isSet(Calendar.HOUR));
        assertTrue(fromJson.isSet(Calendar.DAY_OF_MONTH));
        assertTrue(fromJson.isSet(Calendar.MONTH));
        assertTrue(fromJson.isSet(Calendar.YEAR));

        assertThat(fromJson.get(Calendar.YEAR)).isEqualTo(2000);
        assertThat(fromJson.get(Calendar.MONTH)).isEqualTo(Calendar.FEBRUARY);
        assertThat(fromJson.get(Calendar.DAY_OF_MONTH)).isEqualTo(27);
        assertThat(fromJson.get(Calendar.HOUR_OF_DAY)).isEqualTo(22);
        assertThat(fromJson.get(Calendar.MINUTE)).isEqualTo(0);
        assertThat(fromJson.get(Calendar.SECOND)).isEqualTo(23);
        assertThat(fromJson.getTimeZone()).isEqualTo(TimeZone.getTimeZone("CET"));
    }

    @Test
    public void threeLetterIso8601withTimeZone() throws Exception {
        Calendar fromJson1 = calendarAdapter().fromJson("\"2003-04-21T16:42:11+02:00\"");
        Calendar fromJson2 = calendarAdapter().fromJson("\"2016-07-21T17:59:59-04:00\"");

        assertNotNull(fromJson1);
        assertNotNull(fromJson2);

        assertTrue(fromJson1.isSet(Calendar.SECOND));
        assertTrue(fromJson1.isSet(Calendar.MINUTE));
        assertTrue(fromJson1.isSet(Calendar.HOUR));
        assertTrue(fromJson1.isSet(Calendar.DAY_OF_MONTH));
        assertTrue(fromJson1.isSet(Calendar.MONTH));
        assertTrue(fromJson1.isSet(Calendar.YEAR));

        assertTrue(fromJson2.isSet(Calendar.SECOND));
        assertTrue(fromJson2.isSet(Calendar.MINUTE));
        assertTrue(fromJson2.isSet(Calendar.HOUR));
        assertTrue(fromJson2.isSet(Calendar.DAY_OF_MONTH));
        assertTrue(fromJson2.isSet(Calendar.MONTH));
        assertTrue(fromJson2.isSet(Calendar.YEAR));

        assertThat(fromJson1.get(Calendar.YEAR)).isEqualTo(2003);
        assertThat(fromJson1.get(Calendar.MONTH)).isEqualTo(Calendar.APRIL);
        assertThat(fromJson1.get(Calendar.DAY_OF_MONTH)).isEqualTo(21);
        assertThat(fromJson1.get(Calendar.HOUR_OF_DAY)).isEqualTo(16);
        assertThat(fromJson1.get(Calendar.MINUTE)).isEqualTo(42);
        assertThat(fromJson1.get(Calendar.SECOND)).isEqualTo(11);
        assertThat(fromJson1.getTimeZone()).isEqualTo(TimeZone.getTimeZone("CET"));

        assertThat(fromJson2.get(Calendar.YEAR)).isEqualTo(2016);
        assertThat(fromJson2.get(Calendar.MONTH)).isEqualTo(Calendar.JULY);
        assertThat(fromJson2.get(Calendar.DAY_OF_MONTH)).isEqualTo(21);
        assertThat(fromJson2.get(Calendar.HOUR_OF_DAY)).isEqualTo(23);
        assertThat(fromJson2.get(Calendar.MINUTE)).isEqualTo(59);
        assertThat(fromJson2.get(Calendar.SECOND)).isEqualTo(59);
        assertThat(fromJson1.getTimeZone()).isEqualTo(TimeZone.getTimeZone("CET"));
    }

    @Test
    public void iso8601withMilliseconds() throws Exception {
        Calendar fromJson = calendarAdapter().fromJson("\"2016-01-15T07:42:01.000Z\"");
        assertNotNull(fromJson);

        assertTrue(fromJson.isSet(Calendar.SECOND));
        assertTrue(fromJson.isSet(Calendar.MINUTE));
        assertTrue(fromJson.isSet(Calendar.HOUR));
        assertTrue(fromJson.isSet(Calendar.DAY_OF_MONTH));
        assertTrue(fromJson.isSet(Calendar.MONTH));
        assertTrue(fromJson.isSet(Calendar.YEAR));

        assertThat(fromJson.get(Calendar.YEAR)).isEqualTo(2016);
        assertThat(fromJson.get(Calendar.MONTH)).isEqualTo(Calendar.JANUARY);
        assertThat(fromJson.get(Calendar.DAY_OF_MONTH)).isEqualTo(15);
        assertThat(fromJson.get(Calendar.HOUR_OF_DAY)).isEqualTo(7);
        assertThat(fromJson.get(Calendar.MINUTE)).isEqualTo(42);
        assertThat(fromJson.get(Calendar.SECOND)).isEqualTo(1);
    }

    @Test
    public void iso8601withMillisecondsAndOffset() throws Exception {
        Calendar fromJson = calendarAdapter().fromJson("\"2014-04-30T10:53:48.000+02:00\"");
        assertThat(fromJson).isNotNull();

        assertTrue(fromJson.isSet(Calendar.MILLISECOND));
        assertTrue(fromJson.isSet(Calendar.SECOND));
        assertTrue(fromJson.isSet(Calendar.MINUTE));
        assertTrue(fromJson.isSet(Calendar.HOUR));
        assertTrue(fromJson.isSet(Calendar.DAY_OF_MONTH));
        assertTrue(fromJson.isSet(Calendar.MONTH));
        assertTrue(fromJson.isSet(Calendar.YEAR));

        assertThat(fromJson.get(Calendar.YEAR)).isEqualTo(2014);
        assertThat(fromJson.get(Calendar.MONTH)).isEqualTo(Calendar.APRIL);
        assertThat(fromJson.get(Calendar.DAY_OF_MONTH)).isEqualTo(30);
        // FIXME on travis
        // FIXME this currently fails due to the fact that travis servers have different time zones
        //assertThat(fromJson.get(Calendar.HOUR_OF_DAY)).isEqualTo(10);
        assertThat(fromJson.get(Calendar.MINUTE)).isEqualTo(53);
        assertThat(fromJson.get(Calendar.SECOND)).isEqualTo(48);
        assertThat(fromJson.getTimeZone()).isEqualTo(TimeZone.getTimeZone("CET"));
    }

    @Test
    public void parsedZuluTimestampShouldHaveZuluTimeZone() throws IOException {
        List<TimeZone> parsedTimeZones = new ArrayList<>();
        parsedTimeZones.add(parseTimeZone("\"2016-06-27T11:35:21.120Z\""));
        parsedTimeZones.add(parseTimeZone("\"2016-06-27T11:35:21Z\""));

        //noinspection SSBasedInspection
        for (TimeZone currentTimeZone : parsedTimeZones) {
            assertTrue(ZULU_TIME_ZONE.equals(currentTimeZone));
        }
    }

    @Test
    public void safeCalendarAdapterRespectsTimezoneOffset() throws Exception {
        Calendar fromJson1 = calendarAdapter().fromJson("\"2003-04-21T16:42:11+02:00\"");
        Calendar fromJson2 = calendarAdapter().fromJson("\"2003-04-21T14:42:11Z\"");
        Calendar fromJson3 = calendarAdapter().fromJson("\"2016-07-21T17:59:59-04:00\"");

        assertThat(fromJson1.getTimeInMillis()).isEqualTo(fromJson2.getTimeInMillis());

        String toJson1 = calendarAdapter().toJson(fromJson1);
        assertThat(toJson1).isEqualTo("\"2003-04-21T16:42:11+02:00\"");

        String toJson2 = calendarAdapter().toJson(fromJson2);
        assertThat(toJson2).isEqualTo("\"2003-04-21T14:42:11Z\"");

        String toJson3 = calendarAdapter().toJson(fromJson3);
        assertThat(toJson3).isEqualTo("\"2016-07-21T23:59:59+02:00\"");
    }

    private TimeZone parseTimeZone(String dateString) throws IOException {
        Calendar calendar = calendarAdapter().fromJson(dateString);
        return calendar.getTimeZone();
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private <T extends Calendar> JsonAdapter<T> calendarAdapter() {
        return (JsonAdapter<T>) SafeCalendarJsonAdapter.FACTORY.create(
              SafeCalendar.class, Collections.<Annotation>emptySet(), moshi).lenient();
    }
}
