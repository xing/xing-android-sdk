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
package com.xing.android.sdk.model;

import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * This class has to be used instead of the normal Calendar. The reason is because when a Calendar
 * object has filled some fields (year and month, for example) but not others (day), calling the method
 * calendar.get(Calendar.YEAR) automatically fills the rest of the fields calling the internal method
 * complete(). This class avoids the call to the complete method, just returning the value.
 * <p/>
 *
 * @author david.gonzalez
 */
public class XingCalendar extends GregorianCalendar {

    private static final long serialVersionUID = 1571039856535190157L;

    /**
     * Creates an empty calendar, all the fields are not set.
     */
    public XingCalendar() {
        clear();
    }

    /**
     * Creates a calendar.
     *
     * @param year year.
     * @param month month.
     * @param day day.
     */
    public XingCalendar(int year, int month, int day) {
        super(year, month, day);
    }

    /**
     * Creates a calendar.
     *
     * @param year year.
     * @param month month.
     * @param day day.
     * @param hour hour.
     * @param minute minute.
     */
    public XingCalendar(int year, int month, int day, int hour, int minute) {
        super(year, month, day, hour, minute);
    }

    /**
     * Creates a calendar.
     *
     * @param year year.
     * @param month month.
     * @param day day.
     * @param hour hour.
     * @param minute minute.
     * @param second second.
     */
    public XingCalendar(int year, int month, int day, int hour, int minute, int second) {
        super(year, month, day, hour, minute, second);
    }

    /**
     * Creates a calendar.
     *
     * @param locale locale.
     */
    public XingCalendar(Locale locale) {
        super(locale);
    }

    /**
     * Creates a calendar.
     *
     * @param timezone timezone.
     */
    public XingCalendar(TimeZone timezone) {
        super(timezone);
    }

    /**
     * Creates a calendar.
     *
     * @param timezone timezone.
     * @param locale locale.
     */
    public XingCalendar(TimeZone timezone, Locale locale) {
        super(timezone, locale);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * <p/>
     * We hack the complete method to avoid it being executed by most methods, which can make
     * the calendar data unstable and unreliable.
     */
    @Override
    protected void complete() {
        // Get the stack trace of calling methods
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        // Get the calling stack trace element
        StackTraceElement element = stackTraceElements[3];
        String methodName = element.getMethodName();
        // Check if method should be ignored
        if ("setTimeInMillis".equals(methodName)) {
            super.complete();
        }
    }
}
