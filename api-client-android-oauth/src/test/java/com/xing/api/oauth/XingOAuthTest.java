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

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowActivity.IntentForResult;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public final class XingOAuthTest {
    private static final String DUMMY_CONSUMER_KEY = "consumerKey";
    private static final String DUMMY_CONSUMER_SECRET = "consumerSecret";

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

    @Config(sdk = 21) // FIXME: Mover this to instrumentation tests
    @Test
    public void loginWithXingCalledFromActivity() throws Exception {
        Activity activity = Robolectric.buildActivity(Activity.class).create().start().visible().get();
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);

        XingOAuth xingOAuth = buildTestXingOauth();
        xingOAuth.loginWithXing(activity);

        IntentForResult intentForResult = shadowActivity.peekNextStartedActivityForResult();
        assertStartedIntent(intentForResult.intent, activity);
    }

    @Config(sdk = 21) // FIXME: Mover this to instrumentation tests
    @Test
    public void loginWithXingCalledFromFragment() throws Exception {
        Fragment fragment = Robolectric.buildFragment(Fragment.class).create().attach().start().visible().get();

        XingOAuth xingOAuth = buildTestXingOauth();
        xingOAuth.loginWithXing(fragment);

        Intent nextStartedActivity = ShadowApplication.getInstance().getNextStartedActivity();
        assertStartedIntent(nextStartedActivity, fragment.getActivity());
    }

    @Config(sdk = 21) // FIXME: Mover this to instrumentation tests
    @Test
    public void loginWithXingCalledFromSupportFragment() throws Exception {
        android.support.v4.app.Fragment fragment = new android.support.v4.app.Fragment();
        SupportFragmentTestUtil.startFragment(fragment);

        XingOAuth xingOAuth = buildTestXingOauth();
        xingOAuth.loginWithXing(fragment);

        Intent nextStartedActivity = ShadowApplication.getInstance().getNextStartedActivity();
        assertStartedIntent(nextStartedActivity, fragment.getActivity());
    }

    @Test
    public void startersThrowsOnNonSupportedObject() throws Exception {
        try {
            XingOAuth.unwrapContext("this_should_throw");
            fail();
        } catch (IllegalArgumentException ex) {
            assertThat(ex).hasMessage("caller should be an instance of Activity or Fragment, got: java.lang.String");
        }

        try {
            XingOAuth.startActivityForResult(1, null);
            fail();
        } catch (IllegalArgumentException ex) {
            assertThat(ex).hasMessage("caller should be an instance of Activity or Fragment, got: java.lang.Integer");
        }
    }

    @Test
    public void onActivityResultHandlesSuccess() throws Exception {
        XingOAuth xingOAuth = buildTestXingOauth();

        Intent data = new Intent();
        data.putExtra(Shared.TOKEN, "test");
        data.putExtra(Shared.TOKEN_SECRET, "test_test");
        OAuthResponse response = xingOAuth.onActivityResult(Shared.REQUEST_CODE, Activity.RESULT_OK, data);

        assertThat(response).isNotNull();
        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.token()).isEqualTo("test");
        assertThat(response.tokenSecret()).isEqualTo("test_test");
    }

    @Test
    public void onActivityResultHandlesError() throws Exception {
        XingOAuth xingOAuth = buildTestXingOauth();

        Intent data = new Intent();
        OAuthResponse response = xingOAuth.onActivityResult(Shared.REQUEST_CODE, Activity.RESULT_CANCELED, data);

        assertThat(response).isNotNull();
        assertThat(response.isSuccessful()).isFalse();
        assertThat(response.token()).isNullOrEmpty();
        assertThat(response.tokenSecret()).isNullOrEmpty();
    }

    @Test
    public void onActivityResultIgnoresOtherResults() throws Exception {
        XingOAuth xingOAuth = buildTestXingOauth();

        Intent data = new Intent();
        OAuthResponse response = xingOAuth.onActivityResult(345, Activity.RESULT_CANCELED, data);

        assertThat(response).isNull();
    }

    private void assertStartedIntent(Intent nextStartedActivity, Activity activity) {
        assertThat(nextStartedActivity.getComponent())
              .isEqualTo(new ComponentName(activity, XingOAuthActivity.class));
        assertThat(nextStartedActivity.getStringExtra(Shared.CONSUMER_KEY)).isEqualTo(DUMMY_CONSUMER_KEY);
        assertThat(nextStartedActivity.getStringExtra(Shared.CONSUMER_SECRET)).isEqualTo(DUMMY_CONSUMER_SECRET);
        assertThat(nextStartedActivity.getStringExtra(Shared.CALLBACK_URL)).isEqualTo("debug://callback");
    }

    private static XingOAuth buildTestXingOauth() {
        return new XingOAuth.Builder()
              .consumerKey(DUMMY_CONSUMER_KEY)
              .consumerSecret(DUMMY_CONSUMER_SECRET)
              .callbackUrlDebug()
              .build();
    }
}
