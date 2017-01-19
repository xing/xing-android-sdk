/*
 * Copyright (С) 2016 XING AG (http://xing.com/)
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
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.xing.api.data.SafeCalendar;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link JsonAdapter} that parses all types of dates received form the XING API and converts them into a
 * {@link SafeCalendar}.
 */
public final class SafeCalendarJsonAdapter<T extends Calendar> extends JsonAdapter<T> {

    public static final Factory FACTORY = new Factory() {
        @Override
        public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
            if (!annotations.isEmpty()) return null;
            Class<?> rawType = Types.getRawType(type);
            if (rawType == SafeCalendar.class || rawType == GregorianCalendar.class
                  || rawType == Calendar.class) {
                return new SafeCalendarJsonAdapter().nullSafe();
            }
            return null;
        }
    };

    static final TimeZone ZULU_TIME_ZONE = TimeZone.getTimeZone("UTC");

    private static final Pattern REG_EX_YEAR = Pattern.compile("^(19|20)\\d{2}");
    private static final Pattern REG_EX_YEAR_MONTH = Pattern.compile("^(19|20)\\d{2}-\\d{2}$");
    private static final Pattern REG_EX_MONTH_DAY = Pattern.compile("\\d{2}-\\d{2}$");
    private static final Pattern REG_EX_YEAR_MONTH_DAY = Pattern.compile("^(19|20)\\d{2}-\\d{2}-\\d{2}$");
    private static final Pattern REG_EX_ISO_DATE_Z = Pattern.compile("^(19|20)\\d{2}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z$");
    private static final Pattern REG_EX_ISO_DATE_TIME =
          Pattern.compile("^(19|20)\\d{2}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[+]\\d{4}$");
    private static final Pattern REG_EX_THREE_LETTER_ISO8601_DATE_FORMAT =
          Pattern.compile("^(19|20)\\d{2}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[+|-]\\d{2}:\\d{2}$");
    private static final Pattern REG_EX_ISO_DATE_WEIRD =
          Pattern.compile("^(19|20)\\d{2}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}Z$");
    private static final Pattern REG_EX_ISO_DATE_WEIRD_AND_ZONE =
          Pattern.compile("^(19|20)\\d{2}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}[+|-]\\d{2}:\\d{2}$");

    private static final String ISO_DATE_FORMAT_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final String YEAR_DATE_FORMAT = "yyyy";
    private static final String YEAR_MONTH_DATE_FORMAT = "yyyy-MM";
    private static final String MONTH_DAY_DATE_FORMAT = "MM-dd";
    private static final String YEAR_MONTH_DAY_DATE_FORMAT = "yyyy-MM-dd";
    private static final String ISO_DATE_FORMAT_WEIRD = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String ISO_DATE_FORMAT_WEIRD_AND_ZONE = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    private static final NumberFormat TWO_DIGITS_FORMATTER = new DecimalFormat("00");
    private static final Map<Pattern, ThreadLocal<SimpleDateFormat>> DATE_FORMAT_MAP = new LinkedHashMap<>(5);

    static {
        DATE_FORMAT_MAP.put(REG_EX_YEAR,
                asThreadLocal(new SimpleDateFormat(YEAR_DATE_FORMAT, Locale.ENGLISH)));
        DATE_FORMAT_MAP.put(REG_EX_YEAR_MONTH,
                asThreadLocal(new SimpleDateFormat(YEAR_MONTH_DATE_FORMAT, Locale.ENGLISH)));
        DATE_FORMAT_MAP.put(REG_EX_MONTH_DAY,
                asThreadLocal(new SimpleDateFormat(MONTH_DAY_DATE_FORMAT, Locale.ENGLISH)));
        DATE_FORMAT_MAP.put(REG_EX_YEAR_MONTH_DAY,
                asThreadLocal(new SimpleDateFormat(YEAR_MONTH_DAY_DATE_FORMAT, Locale.ENGLISH)));
        DATE_FORMAT_MAP.put(REG_EX_ISO_DATE_Z,
                asThreadLocal(new ZuluDateFormat(ISO_DATE_FORMAT_Z, Locale.ENGLISH)));
        DATE_FORMAT_MAP.put(REG_EX_ISO_DATE_TIME,
                asThreadLocal(new SimpleDateFormat(ISO_DATE_FORMAT, Locale.ENGLISH)));
        DATE_FORMAT_MAP.put(REG_EX_THREE_LETTER_ISO8601_DATE_FORMAT,
                asThreadLocal(new ThreeLetterDateFormat(ISO_DATE_FORMAT, Locale.ENGLISH)));
        DATE_FORMAT_MAP.put(REG_EX_ISO_DATE_WEIRD,
                asThreadLocal(new ZuluDateFormat(ISO_DATE_FORMAT_WEIRD, Locale.ENGLISH)));
        DATE_FORMAT_MAP.put(REG_EX_ISO_DATE_WEIRD_AND_ZONE,
                asThreadLocal(new SimpleDateFormat(ISO_DATE_FORMAT_WEIRD_AND_ZONE, Locale.ENGLISH)));
    }

    static ThreadLocal<SimpleDateFormat> asThreadLocal(final SimpleDateFormat simpleDateFormat) {
        return new ThreadLocal<SimpleDateFormat>() {
            @Override
            protected SimpleDateFormat initialValue() {
                return simpleDateFormat;
            }
        };
    }

    SafeCalendarJsonAdapter() {
    }

    public static void resolveTimeZone(Calendar calendar, String chosenPattern) {
        boolean isZuluTimeZone = ISO_DATE_FORMAT_Z.equals(chosenPattern)
              || ISO_DATE_FORMAT_WEIRD.equals(chosenPattern);

        if (!isZuluTimeZone) return;

        calendar.setTimeZone(ZULU_TIME_ZONE);
    }

    /**
     * Clear unused calendar fields by regular expression.
     *
     * @param calendar The calendar to clear
     * @param regEx The reg ex
     */
    @SuppressWarnings("fallthrough")
    private static void clearCalendarByRegEx(Calendar calendar, String regEx) {
        switch (regEx) {
            case YEAR_DATE_FORMAT: {
                calendar.clear(Calendar.MONTH);
                calendar.clear(Calendar.DAY_OF_MONTH);
                clearTime(calendar);
                break;
            }
            case YEAR_MONTH_DATE_FORMAT: {
                calendar.clear(Calendar.DAY_OF_MONTH);
                clearTime(calendar);
                break;
            }
            case MONTH_DAY_DATE_FORMAT: {
                calendar.clear(Calendar.YEAR);
                clearTime(calendar);
                break;
            }
            case YEAR_MONTH_DAY_DATE_FORMAT: {
                clearTime(calendar);
                break;
            }
        }
    }

    private static void clearTime(Calendar calendar) {
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
    }

    /**
     * Checks if a {@link SafeCalendar} has all the necessary fields set to generate a timestamp.
     * These fields are: year, month, day of month, hour of day, minute and second.
     *
     * @return True if has all the fields set, false otherwise.
     */
    private static boolean isFilledToTime(Calendar calendar) {
        return calendar.isSet(Calendar.HOUR_OF_DAY)
              && calendar.isSet(Calendar.MINUTE)
              && calendar.isSet(Calendar.SECOND);
    }

    @Override
    public T fromJson(JsonReader reader) throws IOException {
        String dateStr = reader.nextString();
        if (dateStr.isEmpty()) return null;

        // Read the format entry
        DateFormat format = null;
        for (Map.Entry<Pattern, ThreadLocal<SimpleDateFormat>> entry : DATE_FORMAT_MAP.entrySet()) {
            Matcher matcher = entry.getKey().matcher(dateStr);
            if (matcher.matches()) {
                format = entry.getValue().get();
                break;
            }
        }

        // Throw if no supported format, so that we don't fail silently.
        if (format == null) {
            throw new AssertionError("Unsupported date format! Expecting ISO 8601, but found: "
                  + dateStr);
        }

        try {
            // Try to parse the date
            Date date = format.parse(dateStr);
            // Create a calendar instance, clear it and set the received time
            String chosenPattern = ((SimpleDateFormat) format).toPattern();
            Calendar calendar = new SafeCalendar();
            resolveTimeZone(calendar, chosenPattern);
            calendar.clear();
            calendar.setTime(date);

            // Clear unnecessary fields form calendar
            clearCalendarByRegEx(calendar, chosenPattern);

            //noinspection unchecked
            return (T) calendar;
        } catch (ParseException ex) {
            // This should not be reached.
            return null;
        }
    }

    @Override
    public void toJson(JsonWriter writer, T calendar) throws IOException {
        StringBuilder output = new StringBuilder();
        if (calendar.isSet(Calendar.YEAR)) {
            output.append(calendar.get(Calendar.YEAR));
        }
        if (calendar.isSet(Calendar.MONTH)) {
            if (calendar.isSet(Calendar.YEAR)) {
                output.append('-');
            }
            output.append(TWO_DIGITS_FORMATTER.format(calendar.get(Calendar.MONTH) + 1));
            if (calendar.isSet(Calendar.DAY_OF_MONTH)) {
                output.append('-');
                output.append(TWO_DIGITS_FORMATTER.format(calendar.get(Calendar.DAY_OF_MONTH)));
                if (isFilledToTime(calendar)) {
                    output.append('T');
                    output.append(TWO_DIGITS_FORMATTER.format(calendar.get(Calendar.HOUR_OF_DAY)));
                    output.append(':');
                    output.append(TWO_DIGITS_FORMATTER.format(calendar.get(Calendar.MINUTE)));
                    output.append(':');
                    output.append(TWO_DIGITS_FORMATTER.format(calendar.get(Calendar.SECOND)));
                    if (ZULU_TIME_ZONE.equals(calendar.getTimeZone())) {
                        output.append('Z');
                    } else {
                        TimeZone timeZone = calendar.getTimeZone();
                        // This will return the initial time milliseconds + offset milliseconds
                        long millisWithOffset = timeZone.getOffset(calendar.getTimeInMillis());
                        String offset = String.format("%02d:%02d",
                              // Get the amount of hours form the offset
                              Math.abs(millisWithOffset / 3600000),
                              // Get the amount of minutes form the offset
                              Math.abs((millisWithOffset / 60000) % 60));
                        output.append((millisWithOffset >= 0 ? "+" : "-")).append(offset);
                    }
                }
            }
        }

        writer.value(output.length() > 0 ? output.toString() : null);
    }

    @Override
    public String toString() {
        return "JsonAdapter(" + SafeCalendar.class + ')';
    }

    static class ThreeLetterDateFormat extends SimpleDateFormat {

        public ThreeLetterDateFormat(String isoDateFormat, Locale locale) {
            super(isoDateFormat, locale);
        }

        @Override
        public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
            StringBuffer dateString = super.format(date, toAppendTo, pos);
            return dateString.insert(dateString.length() - 2, ':');
        }

        @Override
        public Date parse(String text, ParsePosition pos) {
            int index = text.length() - 3;
            text = text.substring(0, index) + text.substring(index + 1);
            return super.parse(text, pos);
        }
    }

    static class ZuluDateFormat extends SimpleDateFormat {

        public ZuluDateFormat(String isoDateFormat, Locale locale) {
            super(isoDateFormat, locale);
            setTimeZone(ZULU_TIME_ZONE);
        }
    }
}
