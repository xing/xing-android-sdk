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
import com.squareup.moshi.Moshi;
import com.xing.api.model.SafeCalendar;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Currently supporting the next formats:
 * <li>"yyyy"</li>
 * <li>"yyyy-MM"</li>
 * <li>"yyyy-MM-dd"</li>
 * <li>"yyyy-MM-dd'T'HH:mmZ"</li>
 * <p>
 *
 * @author serj.lotutovici
 * @author daniel.hartwich
 */
public class SafeCalendarJsonAdapterTest {
    private static Locale preTestLocale;

    @BeforeClass
    public static void setUp() throws Exception {
        // Save pre test locale so that we don't screw up other tests
        preTestLocale = Locale.getDefault();
        // Pre set the defaults to germany
        Locale.setDefault(Locale.GERMANY);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        // Return default values to pre test values
        Locale.setDefault(preTestLocale);
    }

    private final Moshi moshi = new Moshi.Builder().build();

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
    public void nullForEmptyOrUnknown() throws Exception {
        assertNull(calendarAdapter().fromJson("\"null\""));
        assertNull(calendarAdapter().fromJson("\"\""));
        assertNull(calendarAdapter().fromJson("\"2010-MAY-13\""));
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
        // Need to reset time zone.
        TimeZone preTestTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

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
        assertThat(fromJson.get(Calendar.HOUR_OF_DAY)).isEqualTo(21);
        assertThat(fromJson.get(Calendar.MINUTE)).isEqualTo(0);
        assertThat(fromJson.get(Calendar.SECOND)).isEqualTo(23);

        // Return pre test time zone.
        TimeZone.setDefault(preTestTimeZone);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Nullable
    private <T extends Calendar> JsonAdapter<T> calendarAdapter() {
        return (JsonAdapter<T>) SafeCalendarJsonAdapter.FACTORY.create(
              SafeCalendar.class, Collections.<Annotation>emptySet(), moshi).lenient();
    }
}
