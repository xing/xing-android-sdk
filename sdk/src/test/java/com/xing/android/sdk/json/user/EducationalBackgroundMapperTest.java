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
import com.xing.android.sdk.model.user.EducationalBackground;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author serj.lotutovici
 */
@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = Config.NONE)
public class EducationalBackgroundMapperTest extends ParserUnitTestBase {

    private static final String TEST_BACKGROUND = "{\n" +
            "        \"degree\": \"MSc CE/CS\",\n" +
            "        \"primary_school\": {\n" +
            "          \"id\": \"42_abcdef\",\n" +
            "          \"name\": \"Carl-von-Ossietzky Universtät Schellenburg\",\n" +
            "          \"degree\": \"MSc CE/CS\",\n" +
            "          \"notes\": null,\n" +
            "          \"subject\": null,\n" +
            "          \"begin_date\": \"1998-08\",\n" +
            "          \"end_date\": \"2005-02\"\n" +
            "        },\n" +
            "        \"schools\": [\n" +
            "          {\n" +
            "            \"id\": \"42_abcdef\",\n" +
            "            \"name\": \"Carl-von-Ossietzky Universtät Schellenburg\",\n" +
            "            \"degree\": \"MSc CE/CS\",\n" +
            "            \"notes\": null,\n" +
            "            \"subject\": null,\n" +
            "            \"begin_date\": \"1998-08\",\n" +
            "            \"end_date\": \"2005-02\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"qualifications\": [\n" +
            "          \"TOEFLS\",\n" +
            "          \"PADI AOWD\"\n" +
            "        ]\n" +
            "      }";

    @Test
    public void parseEducationalBackground() throws Exception {
        EducationalBackground background =
                EducationalBackgroundMapper.parseEducationalBackground(getReaderForJson(TEST_BACKGROUND));
        assertNotNull(background);
        assertEquals(background.getDegree(), "MSc CE/CS");

        // We assume that the school object parser should be tested in another test
        // just checking for null values
        assertNotNull(background.getSchools());
        assertEquals(background.getSchools().size(), 1);

        // Test primary school obtained
        assertNotNull(background.getPrimarySchool());
        assertEquals(background.getPrimarySchool().getId(), "42_abcdef");

        // Test qualifications
        assertNotNull(background.getQualifications());
        assertTrue(background.getQualifications().contains("TOEFLS"));
        assertTrue(background.getQualifications().contains("PADI AOWD"));
    }

    @Test
    public void parseEducationalBackgroundAndPassItThroughParcelFlow() throws Exception {
        EducationalBackground background =
                EducationalBackgroundMapper.parseEducationalBackground(getReaderForJson(TEST_BACKGROUND));
        assertNotNull(background);

        // Create an object from parcel and compare with initial one
        EducationalBackground backgroundFromParcel =
                createNewObjectViaParcelFlow(background, EducationalBackground.CREATOR);
        assertEquals(background.hashCode(), backgroundFromParcel.hashCode());
        assertEquals(background, backgroundFromParcel);
    }
}
