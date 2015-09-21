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

package com.xing.android.sdk.model;

import com.xing.android.sdk.model.user.Industry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.os.Build;
import android.util.JsonReader;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author angelo.marchesin
 */
@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = Config.NONE
)

public class IndustriesLoaderTest {

    public static final String INDUSTRY_NAME = "Architecture and planning";
    public static final String SEGMENT_NAME = "Architecture";
    public static final int INDUSTRY_ID = 10000;
    public static final int SEGMENT_ID = 10400;

    @Test
    public void testLoadIndustriesList() throws Exception {
        JsonReader jsonReader = new JsonReader(new StringReader(IndustriesJsonStub.INDUSTRIES_LIST));
        jsonReader.setLenient(true);
        List<Industry> industryList = IndustriesLoader.readJson(jsonReader);
        assertNotNull(industryList);
        assertEquals(industryList.size(), 1);
        Industry industry = new Industry(INDUSTRY_ID, INDUSTRY_NAME);
        assertNotNull(industryList);
        assertNotNull(industryList.get(0));
        assertEquals(industryList.get(0), industry);
        assertNotNull(industryList.get(0).getSegments());
        assertEquals(industryList.get(0).getSegments().size(), 5);
    }

    @Test
    public void testSingleSegmentParsing() throws Exception {
        JsonReader jsonReader = new JsonReader(new StringReader(IndustriesJsonStub.INDUSTRIES_SEGMENT));
        jsonReader.setLenient(true);
        List<Industry.Segment> segmentList = IndustriesLoader.readSegments(jsonReader);
        assertNotNull(segmentList);
        assertEquals(segmentList.size(), 5);
        assertNotNull(segmentList.get(0));
        assertEquals(segmentList.get(0), new Industry.Segment(SEGMENT_ID, SEGMENT_NAME));
    }
}
