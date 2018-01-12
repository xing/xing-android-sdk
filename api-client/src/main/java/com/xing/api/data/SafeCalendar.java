/*
 * Copyright (C) 2018 XING SE (http://xing.com/)
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
package com.xing.api.data;

import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A safer implementation of {@link java.util.Calendar}. The reason for this class is that {@link java.util.Calendar}
 * fills all un-set fields calling the internal method {@link java.util.Calendar#complete()}, which results in
 * unexpected behavior. This implementation overrides {@link java.util.Calendar#complete()} by scanning the stack trace
 * to ensure that {@code compile()} is called only once during the object initialization.
 * <p>
 * <b>DISCLAIMER: </b> Although this class is {@link java.io.Serializable} there is no guaranty that it will be
 * serialized/de-serialized properly.
 */
@SuppressWarnings("unused") // Public api.
public final class SafeCalendar extends GregorianCalendar {
    private static final long serialVersionUID = 1L;
    public static final SafeCalendar EMPTY = new SafeCalendar();

    /** Creates an empty calendar, all the fields are not set. */
    public SafeCalendar() {
        clear();
    }

    /** Creates a calendar with only year set. */
    public SafeCalendar(int year) {
        this();
        set(YEAR, year);
    }

    /** Creates a calendar with only year and month set. */
    public SafeCalendar(int year, int month) {
        this(year);
        set(MONTH, month);
    }

    /** Creates a calendar with year, month and day set. */
    public SafeCalendar(int year, int month, int day) {
        this(year, month);
        set(DAY_OF_MONTH, day);
    }

    /** Creates a calendar with year, month, day, hour and minute set. */
    public SafeCalendar(int year, int month, int day, int hour, int minute) {
        this(year, month, day);
        set(HOUR_OF_DAY, hour);
        set(MINUTE, minute);
    }

    /** Creates a calendar with year, month, day, hour, minute and seconds set. */
    public SafeCalendar(int year, int month, int day, int hour, int minute, int second) {
        this(year, month, day, hour, minute);
        set(SECOND, second);
    }

    /** Creates a calendar with the desired local. The time will be set to {@link System#currentTimeMillis()}. */
    public SafeCalendar(Locale locale) {
        this(TimeZone.getDefault(), locale);
    }

    /** Creates a calendar with the desired local. The time will be set to {@link System#currentTimeMillis()}. */
    public SafeCalendar(TimeZone timezone) {
        this(timezone, Locale.getDefault());
    }

    /**
     * Creates a calendar with the desired local and time zone.
     * The time will be set to {@link System#currentTimeMillis()}.
     */
    public SafeCalendar(TimeZone timezone, Locale locale) {
        super(timezone, locale);
        clear();
    }

    @Override
    public String toString() {
        return "SafeCalendar["
              + "\"year\": " + (isSet(YEAR) ? get(YEAR) : -1) + ", "
              + "\"month\": " + (isSet(MONTH) ? get(MONTH) : -1) + ", "
              + "\"day\": " + (isSet(DAY_OF_MONTH) ? get(DAY_OF_MONTH) : -1) + ", "
              + "\"hour\": " + (isSet(HOUR_OF_DAY) ? get(HOUR_OF_DAY) : -1) + ", "
              + "\"minute\": " + (isSet(MINUTE) ? get(MINUTE) : -1) + ", "
              + "\"second\": " + (isSet(SECOND) ? get(SECOND) : -1)
              + ']';
    }

    public boolean isEmpty() {
        return equals(EMPTY)
              && !isSet(YEAR)
              && !isSet(MONTH)
              && !isSet(DAY_OF_MONTH)
              && !isSet(HOUR_OF_DAY)
              && !isSet(MINUTE)
              && !isSet(SECOND);
    }

    @Override
    public void set(int field, int value) {
        super.set(field, value);
        if (field == DAY_OF_YEAR) super.complete();
    }

    /**
     * {@inheritDoc}
     * <p>
     * We hack the complete method to avoid it being executed by most methods, which can make the calendar data
     * unstable and unreliable.
     */
    @Override
    protected void complete() {
        // Get the stack trace of calling methods and access the 4th trace element.
        // This makes this class very inefficient, due to the fact that we have to access the stack trace each time we
        // set a field value.
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement element = stackTraceElements[3];
        String methodName = element.getMethodName();
        // Check if method should be ignored
        if ("setTimeInMillis".equals(methodName)) {
            super.complete();
        }
    }
}
