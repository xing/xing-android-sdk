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

package com.xing.android.sdk;

import android.os.Build.VERSION_CODES;

import com.squareup.okhttp.HttpUrl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = VERSION_CODES.LOLLIPOP,
        manifest = Config.NONE)
public class XingApiTest {
    private XingApi api;

    @Before
    public void setUp() {
        api = new XingApi.Builder().loggedOut().build();
    }

    @Test
    public void getRightResourceImpl() throws Exception {
        Resource resource = api.resource(LegalTestResource.class);
        assertThat(resource).isInstanceOf(LegalTestResource.class);

        LegalTestResource testResource = api.resource(LegalTestResource.class);
        assertThat(testResource).isSameAs(resource);
    }

    @Test
    public void throwsForNonStaticResourceClass() throws Exception {
        try {
            api.resource(IllegalTestResource.class);
            fail("Should fail on non-static class.");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class).hasMessage("Resource class must be static.");
        }
    }

    @Test
    public void throwsForMethodInnerResourceClass() throws Exception {
        class InnerTestResource extends Resource {
            protected InnerTestResource(XingApi api) {
                super(api);
            }
        }

        try {
            api.resource(InnerTestResource.class);
            fail("Should fail on non-static class.");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class).hasMessage("Resource class must be static.");
        }
    }

    @Test
    public void builder() throws Exception {
        XingApi.Builder builder = new XingApi.Builder();

        try {
            builder.apiEndpoint((String) null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("apiEndpoint == null");
        }

        try {
            builder.apiEndpoint((HttpUrl) null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("apiEndpoint == null");
        }

        try {
            builder.apiEndpoint("http/invalid-url.org");
            fail("Builder should throw on invalid endpoints.");
        } catch (IllegalArgumentException expected) {
            assertThat(expected.getMessage()).isEqualTo("Illegal endpoint URL: http/invalid-url.org");
        }

        try {
            builder.accessSecret(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("accessSecret == null");
        }

        try {
            builder.accessToken(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("accessToken == null");
        }

        try {
            builder.consumerKey(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("consumerKey == null");
        }

        try {
            builder.consumerSecret(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("consumerSecret == null");
        }

        try {
            builder.client(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("client == null");
        }

        try {
            builder.build();
            fail("Build should throw if oauth is not set.");
        } catch (IllegalStateException expected) {
            assertThat(expected.getMessage()).contains("not set");
        }
    }

    static class LegalTestResource extends Resource {
        protected LegalTestResource(XingApi api) {
            super(api);
        }
    }

    class IllegalTestResource extends Resource {
        protected IllegalTestResource(XingApi api) {
            super(api);
        }
    }
}
