/*
 * Copyright (С) 2015 XING AG (http://xing.com/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xing.android.sdk.model;

import android.os.Build;
import android.util.JsonReader;

import com.xing.android.sdk.model.user.Industry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

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
