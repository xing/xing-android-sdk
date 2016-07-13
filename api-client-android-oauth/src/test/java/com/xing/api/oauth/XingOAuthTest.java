/*
 * Copyright (C) 2016 XING AG (http://xing.com/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xing.api.oauth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public final class XingOAuthTest {
    @Test
    public void builderPreventsNullInput() throws Exception {
        XingOAuth.Builder builder = new XingOAuth.Builder();

        try {
            builder.consumerKey(null);
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessageContaining("consumerKey == null");
        }

        try {
            builder.consumerSecret(null);
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessageContaining("consumerSecret == null");
        }

        try {
            builder.callbackUrl(null);
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessageContaining("callbackUrl == null");
        }

        try {
            builder.oauthCallback(null);
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessageContaining("callback == null");
        }
    }

    @Test
    public void builderThrowsIfNotComplete() throws Exception {
        XingOAuth.Builder builder = new XingOAuth.Builder();

        try {
            builder.build();
            fail();
        } catch (IllegalStateException expected) {
            assertThat(expected).hasMessageContaining("consumerKey is not set");
        }

        builder.consumerKey("some_key");
        try {
            builder.build();
            fail();
        } catch (IllegalStateException expected) {
            assertThat(expected).hasMessageContaining("consumerSecret is not set");
        }

        builder.consumerSecret("some_key");
        try {
            builder.build();
            fail();
        } catch (IllegalStateException expected) {
            assertThat(expected).hasMessageContaining("callbackUrl is not set");
        }

        builder.callbackUrlDebug();
        try {
            builder.build();
            fail();
        } catch (IllegalStateException expected) {
            assertThat(expected).hasMessageContaining("oAuthCallback not set");
        }

        builder.oauthCallback(mock(XingOAuthCallback.class));
        assertThat(builder.build()).isNotNull();
    }

    @Test
    public void builderThrowsOnWrongCallbackUrl() throws Exception {
        XingOAuth.Builder builder = new XingOAuth.Builder();
        // Should expect prefix
        try {
            builder.callbackUrl("test.com");
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected).hasMessage("Expected input: [^[a-z]*\\:\\/\\/+[a-z0-9_\\-\\.]*], found: test.com");
        }
        // Prefix should be lover case
        try {
            builder.callbackUrl("TEST://test.com");
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected).hasMessage("Expected input: [^[a-z]*\\:\\/\\/+[a-z0-9_\\-\\.]*], found: TEST://test.com");
        }
    }

    @Test
    public void builderAcceptsValidCallbackUrls() throws Exception {
        XingOAuth.Builder builder = new XingOAuth.Builder();
        // We accept custom callbacks
        builder.callbackUrl("myapp://callback");
        // We accept urls
        builder.callbackUrl("http://domain.com");
        builder.callbackUrl("https://domain.com");
        // We accept urls with sub-domains
        builder.callbackUrl("https://our.domain.com");
    }
}
