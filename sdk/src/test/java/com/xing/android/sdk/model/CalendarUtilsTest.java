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

package com.xing.android.sdk.model;

import android.os.Build;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Calendar;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author serj.lotutovici
 */
@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = Config.NONE)
public class CalendarUtilsTest {

    private static Locale preTestLocale;

    @BeforeClass
    public static void setUp() throws Exception {
        // Save pre test locale so that we don't screw up other tests
        preTestLocale = Locale.getDefault();
        // Pre set the local to germany
        Locale.setDefault(Locale.GERMANY);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        // Return locale value to pre test value
        Locale.setDefault(preTestLocale);
    }

    @Test
    public void parseNullOrUnsupportedDates() throws Exception {
        assertNull(CalendarUtils.parseCalendarFromString(null));
        assertNull(CalendarUtils.parseCalendarFromString(""));
        assertNull(CalendarUtils.parseCalendarFromString("2010-MAY-13"));
    }

    @Test
    public void parseDateWithOnlyYearSet() throws Exception {
        Calendar calendar = CalendarUtils.parseCalendarFromString("1956");
        assertNotNull(calendar);
        assertFalse(calendar.isSet(Calendar.DAY_OF_MONTH));
        assertFalse(calendar.isSet(Calendar.MONTH));
        assertTrue(calendar.isSet(Calendar.YEAR));
        assertEquals(calendar.get(Calendar.YEAR), 1956);
    }

    @Test
    public void parseDateWithOnlyYearAndMonthSet() throws Exception {
        Calendar calendar = CalendarUtils.parseCalendarFromString("1945-05");
        assertNotNull(calendar);
        assertFalse(calendar.isSet(Calendar.DAY_OF_MONTH));
        assertTrue(calendar.isSet(Calendar.MONTH));
        assertTrue(calendar.isSet(Calendar.YEAR));
        assertEquals(calendar.get(Calendar.YEAR), 1945);
        assertEquals(calendar.get(Calendar.MONTH), Calendar.MAY);
    }

    @Test
    public void parseDateWithYearMonthAndDateSet() throws Exception {
        Calendar calendar = CalendarUtils.parseCalendarFromString("1988-03-04");
        assertNotNull(calendar);
        assertTrue(calendar.isSet(Calendar.DAY_OF_MONTH));
        assertTrue(calendar.isSet(Calendar.MONTH));
        assertTrue(calendar.isSet(Calendar.YEAR));
        assertEquals(calendar.get(Calendar.YEAR), 1988);
        assertEquals(calendar.get(Calendar.MONTH), Calendar.MARCH);
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), 4);
    }

    @Test
    public void parseDateInISOFormatWithZ() throws Exception {
        Calendar calendar = CalendarUtils.parseCalendarFromString("2000-02-27T23:00:23Z");
        assertNotNull(calendar);
        assertTrue(calendar.isSet(Calendar.SECOND));
        assertTrue(calendar.isSet(Calendar.MINUTE));
        assertTrue(calendar.isSet(Calendar.HOUR_OF_DAY));
        assertTrue(calendar.isSet(Calendar.DAY_OF_MONTH));
        assertTrue(calendar.isSet(Calendar.MONTH));
        assertTrue(calendar.isSet(Calendar.YEAR));
        assertEquals(calendar.get(Calendar.YEAR), 2000);
        assertEquals(calendar.get(Calendar.MONTH), Calendar.FEBRUARY);
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), 27);
        assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 23);
        assertEquals(calendar.get(Calendar.MINUTE), 0);
        assertEquals(calendar.get(Calendar.SECOND), 23);
    }

    @Test
    @Ignore // FIXME Fails on travis-ci
    public void parseDateInISOFormatWithTimeZoneOffset() throws Exception {
        Calendar calendar = CalendarUtils.parseCalendarFromString("2000-02-27T23:00:23+0100");
        assertNotNull(calendar);
        assertTrue(calendar.isSet(Calendar.SECOND));
        assertTrue(calendar.isSet(Calendar.MINUTE));
        assertTrue(calendar.isSet(Calendar.HOUR));
        assertTrue(calendar.isSet(Calendar.DAY_OF_MONTH));
        assertTrue(calendar.isSet(Calendar.MONTH));
        assertTrue(calendar.isSet(Calendar.YEAR));
        assertEquals(calendar.get(Calendar.YEAR), 2000);
        assertEquals(calendar.get(Calendar.MONTH), Calendar.FEBRUARY);
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), 27);
        assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 23);
        assertEquals(calendar.get(Calendar.MINUTE), 0);
        assertEquals(calendar.get(Calendar.SECOND), 23);
    }
}
