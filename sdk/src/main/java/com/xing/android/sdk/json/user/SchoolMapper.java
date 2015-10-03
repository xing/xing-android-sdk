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

import com.xing.android.sdk.model.CalendarUtils;
import com.xing.android.sdk.model.user.School;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This parser takes a JsonReader and gets the school information.
 *
 * @author david.gonzalez
 */
public final class SchoolMapper {
    /**
     * Extracts the detail information about a school.
     *
     * @param reader A JsonReader object containing the detail information about a school
     * @return A School object
     *
     * @throws IOException
     */
    public static School parseSchool(JsonReader reader) throws IOException {
        School school = new School();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "id": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        school.setId(reader.nextString());
                    }
                    break;
                }
                case "name": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        school.setName(reader.nextString());
                    }
                    break;
                }
                case "degree": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        school.setDegree(reader.nextString());
                    }
                    break;
                }
                case "notes": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        school.setNotes(reader.nextString());
                    }
                    break;
                }
                case "subject": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        school.setSubject(reader.nextString());
                    }
                    break;
                }
                case "begin_date": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        school.setBeginDate(CalendarUtils.parseCalendarFromString(reader.nextString()));
                    }
                    break;
                }
                case "end_date": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        school.setEndDate(CalendarUtils.parseCalendarFromString(reader.nextString()));
                    }
                    break;
                }
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return school;
    }

    /**
     * Parses information about the schools.
     *
     * @param reader The JsonReader
     * @return A list of School objects
     */
    public static List<School> parseSchoolList(JsonReader reader) throws IOException {
        List<School> schoolList = new ArrayList<>(0);
        reader.beginArray();
        while (reader.hasNext()) {
            schoolList.add(parseSchool(reader));
        }
        reader.endArray();
        return schoolList;
    }

    private SchoolMapper() {
        throw new AssertionError("No instances.");
    }
}
