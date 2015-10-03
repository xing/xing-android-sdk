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

import com.xing.android.sdk.json.StringMapper;
import com.xing.android.sdk.model.user.EducationalBackground;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser that provides methods to get the educational background information of a user.
 *
 * @author david.gonzalez
 */
@SuppressWarnings("unused")
public final class EducationalBackgroundMapper {
    /**
     * Parses the Educational Background of a user.
     *
     * @param reader A JsonReader object that contains the educational background
     * @return The educational background of a user
     *
     * @throws IOException
     */
    public static EducationalBackground parseEducationalBackground(JsonReader reader) throws IOException {
        EducationalBackground educationalbackground = new EducationalBackground();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "degree": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        educationalbackground.setDegree(reader.nextString());
                    }
                    break;
                }
                case "primary_school": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        educationalbackground.setPrimarySchool(SchoolMapper.parseSchool(reader));
                    }
                    break;
                }
                case "schools": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        educationalbackground.setSchools(SchoolMapper.parseSchoolList(reader));
                    }
                    break;
                }
                case "qualifications": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        educationalbackground.setQualifications(StringMapper.parseStringList(reader));
                    }
                    break;
                }
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return educationalbackground;
    }

    /**
     * Gets a list of contact requests.
     *
     * @param reader A JsonReader object that contains the education backgrounds
     * @return A list of EducationalBackground objects
     *
     * @throws IOException
     */
    public static List<EducationalBackground> parseEducationalBackgroundList(JsonReader reader) throws IOException {
        List<EducationalBackground> educationalBackgroundList = new ArrayList<>(0);
        reader.beginArray();
        while (reader.hasNext()) {
            educationalBackgroundList.add(parseEducationalBackground(reader));
        }
        reader.endArray();
        return educationalBackgroundList;
    }

    private EducationalBackgroundMapper() {
        throw new AssertionError("No inspections.");
    }
}
