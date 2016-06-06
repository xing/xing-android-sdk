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

/**
 * {@link JsonAdapter} that parses all types of dates received form XWS and converts them into a {@link SafeCalendar}.
 *
 * @author daniel.hartwich
 * @author serj.lotutovici
 */
public final class SafeCalendarJsonAdapter<T extends Calendar> extends JsonAdapter<T> {
    private static final String REG_EX_YEAR = "^(19|20)\\d{2}";
    private static final String REG_EX_YEAR_MONTH = "^(19|20)\\d{2}-\\d{2}$";
    private static final String REG_EX_YEAR_MONTH_DAY = "^(19|20)\\d{2}-\\d{2}-\\d{2}$";
    private static final String REG_EX_ISO_DATE_Z = "^(19|20)\\d{2}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z$";
    private static final String REG_EX_ISO_DATE_TIME = "^(19|20)\\d{2}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[+]\\d{4}$";
    private static final String REG_EX_THREE_LETTER_ISO8601_DATE_FORMAT =
          "^(19|20)\\d{2}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[+]\\d{2}:\\d{2}$";
    private static final String REG_EX_ISO_DATE_WEIRD = "^(19|20)\\d{2}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}Z$";

    private static final String ISO_DATE_FORMAT_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final String ISO_DATE_FORMAT_WEIRD = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String YEAR_DATE_FORMAT = "yyyy";
    private static final String YEAR_MONTH_DATE_FORMAT = "yyyy-MM";
    private static final String YEAR_MONTH_DAY_DATE_FORMAT = "yyyy-MM-dd";

    private static final NumberFormat TWO_DIGITS_FORMATTER = new DecimalFormat("00");
    private static final Map<String, DateFormat> DATE_FORMAT_MAP = new LinkedHashMap<>(5);

    static {
        DATE_FORMAT_MAP.put(REG_EX_YEAR, new SimpleDateFormat(YEAR_DATE_FORMAT, Locale.ENGLISH));
        DATE_FORMAT_MAP.put(REG_EX_YEAR_MONTH, new SimpleDateFormat(YEAR_MONTH_DATE_FORMAT, Locale.ENGLISH));
        DATE_FORMAT_MAP.put(REG_EX_YEAR_MONTH_DAY, new SimpleDateFormat(YEAR_MONTH_DAY_DATE_FORMAT, Locale.ENGLISH));
        DATE_FORMAT_MAP.put(REG_EX_ISO_DATE_Z, new SimpleDateFormat(ISO_DATE_FORMAT_Z, Locale.ENGLISH));
        DATE_FORMAT_MAP.put(REG_EX_ISO_DATE_TIME, new SimpleDateFormat(ISO_DATE_FORMAT, Locale.ENGLISH));
        DATE_FORMAT_MAP.put(REG_EX_THREE_LETTER_ISO8601_DATE_FORMAT,
              new SimpleDateFormat(ISO_DATE_FORMAT, Locale.ENGLISH) {
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
              });
        DATE_FORMAT_MAP.put(REG_EX_ISO_DATE_WEIRD, new SimpleDateFormat(ISO_DATE_FORMAT_WEIRD, Locale.ENGLISH));
    }

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

    SafeCalendarJsonAdapter() {
    }

    @Override
    public T fromJson(JsonReader reader) throws IOException {
        String dateStr = reader.nextString();
        if (dateStr.isEmpty()) return null;

        // Read the format entry
        DateFormat format = null;
        for (Map.Entry<String, DateFormat> entry : DATE_FORMAT_MAP.entrySet()) {
            if (dateStr.matches(entry.getKey())) {
                format = entry.getValue();
                break;
            }
        }

        // Throw if no supported format, so that we don't fail silently.
        if (format == null) {
            throw new AssertionError("Unsupported date format! Expecting ISO 8601, but found: " + dateStr);
        }

        try {
            // Try to parse the date
            Date date = format.parse(dateStr);
            // Create a calendar instance, clear it and set the received time
            Calendar calendar = new SafeCalendar();
            calendar.clear();
            calendar.setTime(date);

            // Clear unnecessary fields form calendar
            clearCalendarByRegEx(calendar, ((SimpleDateFormat) format).toPattern());

            //noinspection unchecked
            return (T) calendar;
        } catch (ParseException ex) {
            // This should not be reached.
            return null;
        }
    }

    @Override
    public void toJson(JsonWriter writer, T calendar) throws IOException {
        StringBuilder output = null;
        if (calendar.isSet(Calendar.YEAR)) {
            output = new StringBuilder();
            output.append(calendar.get(Calendar.YEAR));
            if (calendar.isSet(Calendar.MONTH)) {
                output.append('-');
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
                        output.append('Z');
                    }
                }
            }
        }

        writer.value(output != null ? output.toString() : null);
    }

    @Override
    public String toString() {
        return "JsonAdapter(" + SafeCalendar.class + ')';
    }

    /**
     * Clear unused calendar fields by regular expression.
     *
     * @param calendar The calendar to clear
     * @param regEx The reg ex
     */
    private static void clearCalendarByRegEx(Calendar calendar, String regEx) {
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
}
