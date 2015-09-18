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

import android.support.annotation.NonNull;
import android.util.JsonReader;
import android.util.JsonToken;

import com.xing.android.sdk.json.ParserUtils;
import com.xing.android.sdk.model.CalendarUtils;
import com.xing.android.sdk.model.user.ExperienceCompany;
import com.xing.android.sdk.model.user.Industry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser that Parses the ExperienceCompany object
 *
 * @author david.gonzalez
 * */
@SuppressWarnings("unused")
public final class ExperienceCompanyMapper {
    /**
     * Parses the ExperienceCompany
     *
     * @param reader The JsonReader object
     * @return A ExperienceCompany object
     * @throws IOException
     * */
    public static ExperienceCompany parseExperienceCompany(JsonReader reader) throws IOException {
        ExperienceCompany experienceCompany = new ExperienceCompany();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "name": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        experienceCompany.setName(reader.nextString());
                    }
                    break;
                }
                case "id": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        experienceCompany.setId(reader.nextString());
                    }
                    break;
                }
                case "begin_date": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        experienceCompany.setBeginDate(CalendarUtils.parseCalendarFromString(reader.nextString()));
                    }
                    break;
                }
                case "career_level": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        experienceCompany.setCareerLevel(reader.nextString());
                    }
                    break;
                }
                case "company_size": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        experienceCompany.setCompanySize(reader.nextString());
                    }
                    break;
                }
                case "description": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        experienceCompany.setDescription(reader.nextString());
                    }
                    break;
                }
                case "end_date": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        experienceCompany.setEndDate(CalendarUtils.parseCalendarFromString(reader.nextString()));
                    }
                    break;
                }
                case "form_of_employment": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        experienceCompany.setFormOfEmployment(reader.nextString());
                    }
                    break;
                }
                case "industries": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        parseIndustry(reader, experienceCompany);
                    }
                    break;
                }
                case "title": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        experienceCompany.setTitle(reader.nextString());
                    }
                    break;
                }
                case "until_now": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        experienceCompany.setUntilNow(reader.nextBoolean());
                    }
                    break;
                }
                case "url": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        experienceCompany.setUrl(ParserUtils.stringToUri(reader.nextString()));
                    }
                    break;
                }
                case "tag": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        experienceCompany.setTag(reader.nextString());
                    }
                    break;
                }
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return experienceCompany;
    }

    private static void parseIndustry(@NonNull JsonReader reader, @NonNull ExperienceCompany experienceCompany) throws
            IOException {
        reader.beginArray();

        if (reader.peek() == JsonToken.BEGIN_OBJECT) {
            int industryId;
            String industryType;

            reader.beginObject();
            reader.nextName();
            industryId = reader.nextInt();
            reader.nextName();
            industryType = reader.nextString();
            experienceCompany.setIndustry(new Industry(industryId, industryType));
            reader.endObject();
        }

        reader.endArray();
    }

    /**
     * Parse the ExperienceCompany same as {@link ExperienceCompanyMapper#parseExperienceCompany(JsonReader)}
     * but instead retuning a list
     *
     * @param reader The JsonReader object
     * @return A list of ExperienceCompany objects
     * @throws IOException
     * */
    public static List<ExperienceCompany> parseExperienceCompanyList(JsonReader reader) throws IOException {
        List<ExperienceCompany> experienceCompanyList = new ArrayList<>(0);
        reader.beginArray();
        while (reader.hasNext()) {
            experienceCompanyList.add(parseExperienceCompany(reader));
        }
        reader.endArray();
        return experienceCompanyList;
    }
}
