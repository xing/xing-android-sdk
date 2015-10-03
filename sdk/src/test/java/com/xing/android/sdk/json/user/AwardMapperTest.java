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
import com.xing.android.sdk.model.user.Award;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Serj Lotutovici
 */
@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = Config.NONE)
public class AwardMapperTest extends ParserUnitTestBase {

    private static final String TEST_AWARD_1 = "{\n" +
            "            \"name\": \"Awesome Dude Of The Year\",\n" +
            "            \"date_awarded\": 2007,\n" +
            "            \"url\": null\n" +
            "          }\n";

    private static final String TEST_AWARD_2 = "{\n" +
            "            \"name\": \"Jeddy that defeated Darf Vader\",\n" +
            "            \"date_awarded\": null,\n" +
            "            \"url\": \"A galaxy far far away\"\n" +
            "          }\n";

    private static final String TEST_AWARDS = "[" +
            TEST_AWARD_1 + ", \n" +
            TEST_AWARD_2 + "]";

    @Test
    public void parseAwardWithEmptyUrl() throws Exception {
        Award award = AwardMapper.parseAward(getReaderForJson(TEST_AWARD_1));
        assertNotNull(award);
        assertEquals(award.getName(), "Awesome Dude Of The Year");
        assertEquals(award.getYearOfDateAwarded(), 2007);
        assertEquals(award.getYearOfDateAwardedAsString(), "2007");
        assertNull(award.getUrl());
        assertNull(award.getUrlAsString());
    }

    @Test
    public void parseAwardWithEmptyDate() throws Exception {
        Award award = AwardMapper.parseAward(getReaderForJson(TEST_AWARD_2));
        assertNotNull(award);
        assertEquals(award.getName(), "Jeddy that defeated Darf Vader");
        assertEquals(award.getYearOfDateAwarded(), -1);
        assertNull(award.getYearOfDateAwardedAsString());
        assertEquals(award.getUrlAsString(), "A galaxy far far away");
    }

    @Test
    public void parseAListOfAwards() throws Exception {
        List<Award> awards = AwardMapper.parseAwardList(getReaderForJson(TEST_AWARDS));
        assertNotNull(awards);
        assertEquals(awards.size(), 2);
        assertEquals(awards.get(0).getName(), "Awesome Dude Of The Year");
    }

    @Test
    public void parseAwardAndPassItThroughParcelFlow() throws Exception {
        Award award = AwardMapper.parseAward(getReaderForJson(TEST_AWARD_1));
        assertNotNull(award);

        // Create an object from parcel and compare with initial one
        Award awardFromParcel = createNewObjectViaParcelFlow(award, Award.CREATOR);
        assertEquals(award.hashCode(), awardFromParcel.hashCode());
        assertEquals(award, awardFromParcel);
    }
}