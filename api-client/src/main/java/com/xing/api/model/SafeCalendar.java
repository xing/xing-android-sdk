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
package com.xing.api.model;

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
 *
 * @author david.gonzalez
 */
@SuppressWarnings("unused") // Public api.
public final class SafeCalendar extends GregorianCalendar {
    private static final long serialVersionUID = 1L;

    /** Creates an empty calendar, all the fields are not set. */
    public SafeCalendar() {
        clear();
    }

    /** Creates a calendar with year, month and day set. */
    public SafeCalendar(int year, int month, int day) {
        super(year, month, day);
    }

    /** Creates a calendar with year, month, day, hour and minute set. */
    public SafeCalendar(int year, int month, int day, int hour, int minute) {
        super(year, month, day, hour, minute);
    }

    /** Creates a calendar with year, month, day, hour, minute and seconds set. */
    public SafeCalendar(int year, int month, int day, int hour, int minute, int second) {
        super(year, month, day, hour, minute, second);
    }

    /** Creates a calendar with the desired local. The time will be set to {@link System#currentTimeMillis()}. */
    public SafeCalendar(Locale locale) {
        super(locale);
    }

    /** Creates a calendar with the desired local. The time will be set to {@link System#currentTimeMillis()}. */
    public SafeCalendar(TimeZone timezone) {
        super(timezone);
    }

    /**
     * Creates a calendar with the desired local and time zone.
     * The time will be set to {@link System#currentTimeMillis()}.
     */
    public SafeCalendar(TimeZone timezone, Locale locale) {
        super(timezone, locale);
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
