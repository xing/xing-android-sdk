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
package com.xing.android.sdk.network;

import android.os.Build;
import android.util.Pair;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author david.gonzalez
 */
@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = Config.NONE)
public class DefaultRequestConfigTest {

    @Test
    public void loggedOutConfigWithoutParameter() throws Exception {
        DefaultRequestConfig.Builder builder = new DefaultRequestConfig.Builder();
        builder.setLoggedOut(true);
        DefaultRequestConfig defaultRequestConfig = builder.build();
        assertNotNull(defaultRequestConfig);
        assertNull(defaultRequestConfig.getOauthSigner());
    }

    @Test
    public void loggedOutConfigWithOneParameter() throws Exception {
        DefaultRequestConfig.Builder builder = new DefaultRequestConfig.Builder();
        builder.setLoggedOut(true);
        builder.addParam("key1", "value1");

        DefaultRequestConfig defaultRequestConfig = builder.build();
        assertNotNull(defaultRequestConfig);

        List<Pair<String, String>> commonParams = defaultRequestConfig.getCommonParams();
        assertNotNull(commonParams);
        assertEquals(1, commonParams.size());

        Pair<String, String> param = commonParams.get(0);
        assertEquals("key1", param.first);
        assertEquals("value1", param.second);
    }

    @Test
    public void loggedOutConfigWithTwoParameters() throws Exception {
        DefaultRequestConfig.Builder builder = new DefaultRequestConfig.Builder();
        builder.setLoggedOut(true);
        List<Pair<String, String>> params = new ArrayList<>(2);
        params.add(new Pair<>("key1", "value1"));
        params.add(new Pair<>("key2", "value2"));
        builder.addParams(params);

        DefaultRequestConfig defaultRequestConfig = builder.build();

        assertNotNull(defaultRequestConfig);

        List<Pair<String, String>> commonParams = defaultRequestConfig.getCommonParams();
        assertNotNull(commonParams);
        assertEquals(2, commonParams.size());
        assertArrayEquals(params.toArray(), commonParams.toArray());
    }
}
