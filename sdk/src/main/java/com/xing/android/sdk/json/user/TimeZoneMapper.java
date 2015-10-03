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

package com.xing.android.sdk.json.user;

import android.util.JsonReader;
import android.util.JsonToken;

import com.xing.android.sdk.model.user.TimeZone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser that gets the Time Zone from a JsonReader.
 *
 * @author david.gonzalez
 */
@SuppressWarnings("unused")
public final class TimeZoneMapper {
    /**
     * Extracts the TimeZone out of the JsonReader.
     *
     * @param reader The JsonReader containing the TimeZone
     * @return The TimeZone object
     *
     * @throws IOException
     */
    public static TimeZone parseTimeZone(JsonReader reader) throws IOException {
        TimeZone timezone = new TimeZone();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "name": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        timezone.setName(reader.nextString());
                    }
                    break;
                }
                case "utc_offset": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        timezone.setUtcOffset((float) reader.nextDouble());
                    }
                    break;
                }
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return timezone;
    }

    /**
     * Same as {@link TimeZoneMapper#parseTimeZone(JsonReader)} but for multiple TimeZones.
     *
     * @param reader The JsonReader
     * @return A list with TimeZone objects
     *
     * @throws IOException
     */
    public static List<TimeZone> parseTimeZoneList(JsonReader reader) throws IOException {
        List<TimeZone> timezoneList = new ArrayList<>(0);
        reader.beginArray();
        while (reader.hasNext()) {
            timezoneList.add(parseTimeZone(reader));
        }
        reader.endArray();
        return timezoneList;
    }

    private TimeZoneMapper() {
        throw new AssertionError("No instances.");
    }
}
