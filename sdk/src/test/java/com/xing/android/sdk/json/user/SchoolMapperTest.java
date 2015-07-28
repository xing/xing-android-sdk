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
import com.xing.android.sdk.model.user.School;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author serj.lotutovici
 */
@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = Config.NONE
)
public class SchoolMapperTest extends ParserUnitTestBase {

    private static final String TEST_SCHOOL_1 = "{\n" +
            "            \"id\": \"42_abcdef\",\n" +
            "            \"name\": \"Carl-von-Ossietzky Universtät Schellenburg\",\n" +
            "            \"degree\": \"MSc CE/CS\",\n" +
            "            \"notes\": null,\n" +
            "            \"subject\": null,\n" +
            "            \"begin_date\": \"1998-08\",\n" +
            "            \"end_date\": \"2005-02\"\n" +
            "          }";

    private static final String TEST_SCHOOLS = "[" +
            TEST_SCHOOL_1 + ", " +
            TEST_SCHOOL_1 + ", " +
            TEST_SCHOOL_1 + "]";

    @Test
    public void parseSchool() throws Exception {
        School school = SchoolMapper.parseSchool(getReaderForJson(TEST_SCHOOL_1));
        assertNotNull(school);
        assertEquals("42_abcdef", school.getId());
        assertEquals("Carl-von-Ossietzky Universtät Schellenburg", school.getName());
        assertEquals("MSc CE/CS", school.getDegree());
        assertNull(school.getNotes());
        assertNull(school.getSubject());

        assertTrue(school.getBeginDate().isSet(Calendar.YEAR));
        assertTrue(school.getBeginDate().isSet(Calendar.MONTH));
        assertFalse(school.getBeginDate().isSet(Calendar.DAY_OF_MONTH));
        assertEquals(1998, school.getBeginDate().get(Calendar.YEAR));
        assertEquals(Calendar.AUGUST, school.getBeginDate().get(Calendar.MONTH));

        assertTrue(school.getEndDate().isSet(Calendar.YEAR));
        assertTrue(school.getEndDate().isSet(Calendar.MONTH));
        assertFalse(school.getEndDate().isSet(Calendar.DAY_OF_MONTH));
        assertEquals(2005, school.getEndDate().get(Calendar.YEAR));
        assertEquals(Calendar.FEBRUARY, school.getEndDate().get(Calendar.MONTH));
    }

    @Test
    public void parseListOfSchools() throws Exception {
        List<School> schools = SchoolMapper.parseSchoolList(getReaderForJson(TEST_SCHOOLS));
        assertNotNull(schools);
        assertEquals(schools.size(), 3);
    }

    @Test
    public void parseSchoolAndPassItThroughParcelFlow() throws Exception {
        School school = SchoolMapper.parseSchool(getReaderForJson(TEST_SCHOOL_1));
        assertNotNull(school);

        // Create an object from parcel and compare with initial one
        School awardFromParcel = createNewObjectViaParcelFlow(school, School.CREATOR);
        assertEquals(school.hashCode(), awardFromParcel.hashCode());
        assertEquals(school, awardFromParcel);
    }
}
