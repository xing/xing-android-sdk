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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * @author serj.lotutovici
 */
@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = Config.NONE
)
public class XingControllerTest {

    @Before
    public void setUp() throws Exception {
        XingController.flush();
    }

    @Test(expected = XingController.InitializationException.class)
    public void controllerNotInitializedThrows() throws Exception {
        XingController.getInstance();
    }

    @Test(expected = IllegalArgumentException.class)
    public void enableCacheWithNoContextThrows() throws Exception {
        XingController.setup().cacheEnabled(null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setupControllerWithEmptyRequestExecutorThrows() throws Exception {
        XingController.setup().setExecutor(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setupWithNoParametersThrows() throws Exception {
        XingController.setup().init();
    }

    @Test(expected = IllegalArgumentException.class)
    public void setupWithEmptyConsumerSecreteThrows() throws Exception {
        XingController.setup()
                .setConsumerKey("test_key")
                .init();
    }

    @Test(expected = IllegalArgumentException.class)
    public void setupWithEmptyTokenThrows() throws Exception {
        XingController.setup()
                .setConsumerKey("test_key")
                .setConsumerSecret("test_secret")
                .init();
    }

    @Test(expected = IllegalArgumentException.class)
    public void setupWithEmptyTokenSecretThrows() throws Exception {
        XingController.setup()
                .setConsumerKey("test_key")
                .setConsumerSecret("test_secret")
                .setToken("test_token")
                .init();
    }

    @Test
    public void setupWithWithNonEmptyParamsSetsSingletonInstance() throws Exception {
        XingController.setup()
                .setConsumerKey("test_key")
                .setConsumerSecret("test_secret")
                .setToken("test_token")
                .setTokenSecret("test_token_secret")
                .init();
        assertNotNull(XingController.getInstance());
    }

    @Test(expected = IllegalStateException.class)
    public void tryingToSetupControllerTwiceThrows() throws Exception {
        XingController.setup()
                .setExecutor(mock(RequestExecutor.class))
                .setLoggedOut(true)
                .init();
        XingController.setup()
                .setExecutor(mock(RequestExecutor.class))
                .setLoggedOut(true)
                .init();
    }
}
