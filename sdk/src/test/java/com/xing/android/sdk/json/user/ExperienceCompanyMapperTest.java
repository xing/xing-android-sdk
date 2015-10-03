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
import com.xing.android.sdk.model.user.CareerLevel;
import com.xing.android.sdk.model.user.CompanySize;
import com.xing.android.sdk.model.user.ExperienceCompany;
import com.xing.android.sdk.model.user.FormOfEmployment;
import com.xing.android.sdk.model.user.Industry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = Config.NONE)
public class ExperienceCompanyMapperTest extends ParserUnitTestBase {

    private static final String TEST_COMPANY = "{\n" +
            "          \"id\": \"1_abcdef\",\n" +
            "          \"name\": \"XING AG\",\n" +
            "          \"title\": \"Softwareentwickler\",\n" +
            "          \"company_size\": \"201-500\",\n" +
            "          \"tag\": \"XINGAG\",\n" +
            "          \"url\": \"http://www.xing.com\",\n" +
            "          \"career_level\": \"PROFESSIONAL_EXPERIENCED\",\n" +
            "          \"begin_date\": \"2010-01\",\n" +
            "          \"description\": null,\n" +
            "          \"end_date\": null,\n" +
            "          \"industries\": [{\"id\": 10400, \"localized_name\": \"Architecture\" }],\n" +
            "          \"form_of_employment\": \"FULL_TIME_EMPLOYEE\",\n" +
            "          \"until_now\": true\n" +
            "        }";
    public static final int INDUSTRY_ID = 10400;
    public static final String INDUSTRY_NAME = "Architecture";

    @Test
    public void parseExperienceCompany() throws Exception {
        ExperienceCompany experienceCompany =
                ExperienceCompanyMapper.parseExperienceCompany(getReaderForJson(TEST_COMPANY));
        assertEquals("1_abcdef", experienceCompany.getId());
        assertEquals("XING AG", experienceCompany.getName());
        assertEquals("Softwareentwickler", experienceCompany.getTitle());
        assertEquals(CompanySize.SIZE_201_500, experienceCompany.getCompanySize());
        assertEquals("http://www.xing.com", experienceCompany.getUrl().toString());
        assertEquals(CareerLevel.PROFESSIONAL_EXPERIENCED, experienceCompany.getCareerLevel());
        assertTrue(experienceCompany.getBeginDate().isSet(Calendar.YEAR));
        assertTrue(experienceCompany.getBeginDate().isSet(Calendar.MONTH));
        assertFalse(experienceCompany.getBeginDate().isSet(Calendar.DAY_OF_MONTH));
        assertEquals("XINGAG", experienceCompany.getTag());
        assertEquals(null, experienceCompany.getDescription());
        assertEquals(null, experienceCompany.getEndDate());
        assertEquals(new Industry(INDUSTRY_ID, INDUSTRY_NAME), experienceCompany.getIndustry());
        assertEquals(FormOfEmployment.FULL_TIME_EMPLOYEE, experienceCompany.getFormOfEmployment());
        assertTrue(experienceCompany.getUntilNow());
    }

    @Test
    public void parseExperienceCompanyAndPassItThroughParcelFlow() throws Exception {
        ExperienceCompany company = ExperienceCompanyMapper.parseExperienceCompany(getReaderForJson(TEST_COMPANY));
        assertNotNull(company);

        // Create copy object via the parcel flow
        ExperienceCompany companyFormParcel = createNewObjectViaParcelFlow(company, ExperienceCompany.CREATOR);
        assertEquals(company.hashCode(), companyFormParcel.hashCode());
        assertEquals(company, companyFormParcel);
    }
}