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

package com.xing.android.sdk.network.request;

import android.net.Uri;
import android.os.Build;
import android.util.Pair;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * @author david.gonzalez
 */
@SuppressWarnings("MagicNumber")
@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = Config.NONE)
public class RequestUtilsTest {

    @Test
    public void appendParamsToBuilder() throws Exception {
        Uri uriBase = new Uri.Builder().scheme("test").authority("test").build();

        Uri.Builder builder = uriBase.buildUpon();

        // Check nothing happens
        RequestUtils.appendParamsToBuilder(builder, null);
        assertEquals(uriBase.toString(), builder.build().toString());

        // Check param with empty key will not be added
        List<Pair<String, String>> pairs = new ArrayList<>();
        pairs.add(new Pair<String, String>(null, null));

        RequestUtils.appendParamsToBuilder(builder, pairs);
        assertEquals(uriBase.toString(), builder.build().toString());

        // Check param with empty value will not be added
        pairs.clear();
        pairs.add(new Pair<String, String>("test", null));

        RequestUtils.appendParamsToBuilder(builder, pairs);
        assertEquals(uriBase.toString(), builder.build().toString());

        // Check actual param is added
        pairs.clear();
        pairs.add(new Pair<>("a", "a"));

        RequestUtils.appendParamsToBuilder(builder, pairs);
        assertEquals(uriBase + "?a=a", builder.build().toString());

        // Check next 2 params will be appended
        pairs.clear();
        pairs.add(new Pair<>("b", "b"));
        pairs.add(new Pair<String, String>(null, "d"));
        pairs.add(new Pair<String, String>("f", null));
        pairs.add(new Pair<>("c", "c"));

        RequestUtils.appendParamsToBuilder(builder, pairs);
        assertEquals(uriBase + "?a=a&b=b&c=c", builder.build().toString());
    }

    @Test
    public void inputStreamToString() throws Exception {
        InputStream firsAssertStream = IOUtils.toInputStream("test");
        String str = RequestUtils.inputStreamToString(firsAssertStream);
        assertEquals("test", str);

        InputStream secondAssertStream = IOUtils.toInputStream("");
        str = RequestUtils.inputStreamToString(secondAssertStream);
        assertEquals("", str);

        try {
            RequestUtils.inputStreamToString(null);
            fail("Should throw exception");
        } catch (Exception expected) {
            assertNotNull(expected);
        }
    }

    @Test
    public void appendSegmentToUri() throws Exception {
        // Assert that null will be returned
        assertNull(RequestUtils.appendSegmentToUri(null, null));

        Uri referenceUri = new Uri.Builder().scheme("test").authority("test").build();

        // Assert that uri wont be altered
        Uri uri = RequestUtils.appendSegmentToUri(referenceUri, null);
        assertEquals(referenceUri.toString(), uri.toString());

        // Assert segment will be added
        uri = RequestUtils.appendSegmentToUri(referenceUri, "test");
        assertEquals(referenceUri + "/test", uri.toString());
    }

    @Test
    public void createCommaSeparatedStringFromStringList() throws Exception {
        // Null and empty lists produce empty strings
        assertEquals("", RequestUtils.createCommaSeparatedStringFromStringList(null));
        assertEquals("", RequestUtils.createCommaSeparatedStringFromStringList(Collections.<String>emptyList()));

        // Create example list
        List<String> list = Arrays.asList("", "test", null, "test2", "test3");

        // Assert we get a string formed only from non null strings
        String result = RequestUtils.createCommaSeparatedStringFromStringList(list);
        assertEquals("test,test2,test3", result);
    }
}
