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

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Data format utilities that help convert received dates form json to calendar instances
 * <p/>
 * Currently supporting the next formats:
 * <li>"yyyy-MM"</li>
 * <li>"yyyy"</li>
 * <li>"yyyy-MM-dd"</li>
 * <li>"yyyy-MM-dd'T'HH:mmZ"</li>
 * <p/>
 * <p/>
 * <p/>
 *
 * @author serj.lotutovici
 */
@SuppressLint("SimpleDateFormat")
public final class CalendarUtils {

    private static final String REG_EX_YEAR = "^(19|20)\\d{2}";
    private static final String REG_EX_YEAR_MONTH = "^(19|20)\\d{2}-\\d{2}$";
    private static final String REG_EX_YEAR_MONTH_DAY = "^(19|20)\\d{2}-\\d{2}-\\d{2}$";
    private static final String REG_EX_ISO_DATE_Z = "^(19|20)\\d{2}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z$";
    private static final String REG_EX_ISO_DATE_TIME = "^(19|20)\\d{2}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[+]\\d{4}$";

    private static final String ISO_DATE_FORMAT_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final String YEAR_DATE_FORMAT = "yyyy";
    private static final String YEAR_MONTH_DATE_FORMAT = "yyyy-MM";
    private static final String YEAR_MONTH_DAY_DATE_FORMAT = "yyyy-MM-dd";


    private static final NumberFormat TWO_DIGITS_FORMATTER = new DecimalFormat("00");

    /**
     * A processing map for the date string to calendar conversion
     */
    private static final Map<String, DateFormat> DATE_FORMAT_MAP = new HashMap<>(5);

    static {
        DATE_FORMAT_MAP.put(REG_EX_YEAR, new SimpleDateFormat(YEAR_DATE_FORMAT));
        DATE_FORMAT_MAP.put(REG_EX_YEAR_MONTH, new SimpleDateFormat(YEAR_MONTH_DATE_FORMAT));
        DATE_FORMAT_MAP.put(REG_EX_YEAR_MONTH_DAY, new SimpleDateFormat(YEAR_MONTH_DAY_DATE_FORMAT));
        DATE_FORMAT_MAP.put(REG_EX_ISO_DATE_Z, new SimpleDateFormat(ISO_DATE_FORMAT_Z));
        DATE_FORMAT_MAP.put(REG_EX_ISO_DATE_TIME, new SimpleDateFormat(ISO_DATE_FORMAT));
    }


    /**
     * Private constructor for utility class
     */
    private CalendarUtils() {
    }

    /**
     * Parse the string to a calendar instance
     *
     * @param dateStr The string to parse
     * @return A calendar instance with the encoded date, or null if nothing can be extracted
     */
    @Nullable
    public static XingCalendar parseCalendarFromString(@Nullable String dateStr) {

        // Return null if the date string is empty
        if (TextUtils.isEmpty(dateStr)) {
            return null;
        }

        // Read the format entry
        DateFormat format = null;
        for (Map.Entry<String, DateFormat> entry : DATE_FORMAT_MAP.entrySet()) {
            if (dateStr.matches(entry.getKey())) {
                format = entry.getValue();
                break;
            }
        }

        // No supported format
        if (format == null) {
            return null;
        }

        try {
            // Try to parse the date
            Date date = format.parse(dateStr);
            // Create a calendar instance, clear it and set the received time
            XingCalendar calendar = new XingCalendar();
            calendar.clear();
            calendar.setTime(date);

            // Clear unnecessary fields form calendar
            clearCalendarByRegEx(calendar, ((SimpleDateFormat) format).toPattern());

            return calendar;

        } catch (ParseException ex) {
            return null;
        }
    }

    /**
     * Clear unused calendar fields by regular expression
     *
     * @param calendar The calendar to clear
     * @param regEx    The reg ex
     */
    private static void clearCalendarByRegEx(XingCalendar calendar, String regEx) {
        switch (regEx) {
            case YEAR_DATE_FORMAT: {
                calendar.clear(Calendar.MONTH);
            }
            case YEAR_MONTH_DATE_FORMAT: {
                calendar.clear(Calendar.DAY_OF_MONTH);
            }
            case YEAR_MONTH_DAY_DATE_FORMAT: {
                calendar.clear(Calendar.HOUR);
                calendar.clear(Calendar.MINUTE);
                calendar.clear(Calendar.SECOND);
                calendar.clear(Calendar.MILLISECOND);
            }
        }
    }

    /**
     * Converts a XingCalendar object into a String with the format "yyyy-MM-ddThh:mm:ssZ". Useful
     * to save dates into the database, easily convertible to XingCalendar again with
     * com.xing.android.sdk.model.DateUtils.parseCalendarFromString(String );
     * It is possible to do the same with a DateFormat, but due to the issues on DateFormat on API 15
     * and the simplicity of the implementation, it's better to use our own one.
     *
     * @param calendar The XingCalendar to convert.
     * @return The String with the timestamp.
     */
    public static String calendarToTimestamp(@NonNull XingCalendar calendar) {
        String output = null;

        if (calendar.isSet(Calendar.YEAR)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(calendar.get(Calendar.YEAR));
            if (calendar.isSet(Calendar.MONTH)) {
                stringBuilder.append('-');
                stringBuilder.append(TWO_DIGITS_FORMATTER.format(calendar.get(Calendar.MONTH) + 1));
                if (calendar.isSet(Calendar.DAY_OF_MONTH)) {
                    stringBuilder.append('-');
                    stringBuilder.append(TWO_DIGITS_FORMATTER.format(calendar.get(Calendar.DAY_OF_MONTH)));
                    if (isFilledToTime(calendar)) {
                        stringBuilder.append('T');
                        stringBuilder.append(TWO_DIGITS_FORMATTER.format(calendar.get(Calendar.HOUR_OF_DAY)));
                        stringBuilder.append(':');
                        stringBuilder.append(TWO_DIGITS_FORMATTER.format(calendar.get(Calendar.MINUTE)));
                        stringBuilder.append(':');
                        stringBuilder.append(TWO_DIGITS_FORMATTER.format(calendar.get(Calendar.SECOND)));
                        stringBuilder.append('Z');
                    }
                }
            }

            output = stringBuilder.toString();
        }

        return output;
    }

    /**
     * Checks if a XingCalendar object has all the necessary fields set to generate a timeStamp.
     * These fields are: year, month, day of month, hour of day, minute and second.
     *
     * @param calendar The XingCalendar to check.
     * @return True if has all the fields set, false otherwise.
     */
    private static boolean isFilledToTime(@NonNull XingCalendar calendar) {
        return calendar.isSet(Calendar.HOUR_OF_DAY) && calendar.isSet(Calendar.MINUTE) && calendar.isSet(Calendar.SECOND);
    }
}

