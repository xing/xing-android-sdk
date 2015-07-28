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

package com.xing.android.sdk.json.user;import android.util.JsonReader;
import android.util.JsonToken;

import com.xing.android.sdk.model.user.ProfessionalExperience;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser that provides methods to get the professional experience of a user
 *
 * @author david.gonzalez
 * */
@SuppressWarnings("unused")
public final class ProfessionalExperienceMapper {
    /**
     * Parses the Professional Experience of a user
     *
     * @param reader  A JsonReader object that contains the educational background
     * @return The professional experience of a user
     * @throws IOException
     */
    public static ProfessionalExperience parseProfessionalExperience(JsonReader reader) throws IOException {
        ProfessionalExperience professionalexperience = new ProfessionalExperience();
        reader.beginObject();
        while(reader.hasNext()) {
            switch(reader.nextName()) {
                case "primary_company":{
                    if(reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        professionalexperience.setPrimaryCompany(ExperienceCompanyMapper.parseExperienceCompany(reader));
                    }
                    break;
                }
                case "companies":{
                    if(reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        professionalexperience.setCompanies(ExperienceCompanyMapper.parseExperienceCompanyList(reader));
                    }
                    break;
                }
                case "awards":{
                    if(reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        professionalexperience.setAwards(AwardMapper.parseAwardList(reader));
                    }
                    break;
                }
                default: reader.skipValue();
            }
        }
        reader.endObject();
        return professionalexperience;
    }

    /**
     * Gets a list of Professional experiences
     *
     * @param reader  A JsonReader object that contains the education backgrounds
     * @return A list of ProfessionalExperience objects
     * @throws IOException
     */
    public static List<ProfessionalExperience> parseProfessionalExperienceList (JsonReader reader) throws IOException {
        List<ProfessionalExperience> professionalExperienceList = new ArrayList<>(0);
        reader.beginArray();
        while(reader.hasNext()) {
            professionalExperienceList.add(parseProfessionalExperience(reader));
        }
        reader.endArray();
        return professionalExperienceList;
    }
}
