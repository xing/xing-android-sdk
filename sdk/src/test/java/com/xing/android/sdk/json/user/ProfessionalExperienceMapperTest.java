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

import android.os.Build;

import com.xing.android.sdk.json.ParserUnitTestBase;
import com.xing.android.sdk.model.user.ProfessionalExperience;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = Config.NONE)
public class ProfessionalExperienceMapperTest extends ParserUnitTestBase {

    public static final String TEST_EXPERIENCE = "{\n" +
            "        \"primary_company\": {\n" +
            "          \"id\": \"1_abcdef\",\n" +
            "          \"name\": \"XING AG\",\n" +
            "          \"title\": \"Softwareentwickler\",\n" +
            "          \"company_size\": \"201-500\",\n" +
            "          \"tag\": null,\n" +
            "          \"url\": \"http://www.xing.com\",\n" +
            "          \"career_level\": \"PROFESSIONAL_EXPERIENCED\",\n" +
            "          \"begin_date\": \"2010-01\",\n" +
            "          \"description\": null,\n" +
            "          \"end_date\": null,\n" +
            "          \"industry\": \"AEROSPACE\",\n" +
            "          \"form_of_employment\": \"FULL_TIME_EMPLOYEE\",\n" +
            "          \"until_now\": true\n" +
            "        },\n" +
            "        \"companies\": [\n" +
            "          {\n" +
            "            \"id\": \"1_abcdef\",\n" +
            "            \"name\": \"XING AG\",\n" +
            "            \"title\": \"Softwareentwickler\",\n" +
            "            \"company_size\": \"201-500\",\n" +
            "            \"tag\": null,\n" +
            "            \"url\": \"http://www.xing.com\",\n" +
            "            \"career_level\": \"PROFESSIONAL_EXPERIENCED\",\n" +
            "            \"begin_date\": \"2010-01\",\n" +
            "            \"description\": null,\n" +
            "            \"end_date\": null,\n" +
            "            \"industry\": \"AEROSPACE\",\n" +
            "            \"form_of_employment\": \"FULL_TIME_EMPLOYEE\",\n" +
            "            \"until_now\": true\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"24_abcdef\",\n" +
            "            \"name\": \"Ninja Ltd.\",\n" +
            "            \"title\": \"DevOps\",\n" +
            "            \"company_size\": null,\n" +
            "            \"tag\": \"NINJA\",\n" +
            "            \"url\": \"http://www.ninja-ltd.co.uk\",\n" +
            "            \"career_level\": null,\n" +
            "            \"begin_date\": \"2009-04\",\n" +
            "            \"description\": null,\n" +
            "            \"end_date\": \"2010-07\",\n" +
            "            \"industry\": \"ALTERNATIVE_MEDICINE\",\n" +
            "            \"form_of_employment\": \"OWNER\",\n" +
            "            \"until_now\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"45_abcdef\",\n" +
            "            \"name\": null,\n" +
            "            \"title\": \"Wiss. Mitarbeiter\",\n" +
            "            \"company_size\": null,\n" +
            "            \"tag\": \"OFFIS\",\n" +
            "            \"url\": \"http://www.uni.de\",\n" +
            "            \"career_level\": null,\n" +
            "            \"begin_date\": \"2007\",\n" +
            "            \"description\": null,\n" +
            "            \"end_date\": \"2008\",\n" +
            "            \"industry\": \"APPAREL_AND_FASHION\",\n" +
            "            \"form_of_employment\": \"PART_TIME_EMPLOYEE\",\n" +
            "            \"until_now\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"176_abcdef\",\n" +
            "            \"name\": null,\n" +
            "            \"title\": \"TEST NINJA\",\n" +
            "            \"company_size\": \"201-500\",\n" +
            "            \"tag\": \"TESTCOMPANY\",\n" +
            "            \"url\": null,\n" +
            "            \"career_level\": \"ENTRY_LEVEL\",\n" +
            "            \"begin_date\": \"1998-12\",\n" +
            "            \"description\": null,\n" +
            "            \"end_date\": \"1999-05\",\n" +
            "            \"industry\": \"ARTS_AND_CRAFTS\",\n" +
            "            \"form_of_employment\": \"INTERN\",\n" +
            "            \"until_now\": false\n" +
            "          }\n" +
            "        ],\n" +
            "        \"awards\": [\n" +
            "          {\n" +
            "            \"name\": \"Awesome Dude Of The Year\",\n" +
            "            \"date_awarded\": 2007,\n" +
            "            \"url\": null\n" +
            "          }\n" +
            "        ]\n" +
            "      }";

    @Test
    public void parseProfessionalExperienceFromJson() throws Exception {
        ProfessionalExperience professionalExperience =
                ProfessionalExperienceMapper.parseProfessionalExperience(getReaderForJson(TEST_EXPERIENCE));
        assertNotNull(professionalExperience);
        assertNotNull(professionalExperience.getPrimaryCompany());

        //  doesn't check the professional experience company because it's checked in ExperienceCompanyParserTest
        assertNotNull(professionalExperience.getCompanies());
        assertEquals(4, professionalExperience.getCompanies().size());

        assertNotNull(professionalExperience.getAwards());
        assertEquals(1, professionalExperience.getAwards().size());
    }

    @Test
    public void parsePrefessionalExperienceAndPassItThroughParcelFlow() throws Exception {
        ProfessionalExperience experience =
                ProfessionalExperienceMapper.parseProfessionalExperience(getReaderForJson(TEST_EXPERIENCE));
        assertNotNull(experience);

        // Create copy object via parcel flow
        ProfessionalExperience experienceFromParcel =
                createNewObjectViaParcelFlow(experience, ProfessionalExperience.CREATOR);
        assertEquals(experience.hashCode(), experienceFromParcel.hashCode());
        assertEquals(experience, experienceFromParcel);
    }
}
