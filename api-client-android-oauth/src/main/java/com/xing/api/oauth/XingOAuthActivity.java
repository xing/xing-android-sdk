/*
 * Copyright 2015 XING AG (http://xing.com/)
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
package com.xing.api.oauth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.ref.WeakReference;

/**
 * Activity for the first step of the OAuth authentication process.
 *
 * @author david.gonzalez
 * @author daniel.hartwich
 * @author serj.lotutovici
 */
@SuppressWarnings("ClassNamingConvention")
public final class XingOAuthActivity extends Activity {
    WebView webView;
    OAuthHelper helper;

    /** Clear all cookies. */
    @SuppressWarnings("deprecation")
    private static void clearCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(null);
        } else {
            cookieManager.removeAllCookie();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth_callback);

        Bundle extras = getIntent().getExtras();
        //noinspection ConstantConditions The asumption is that this activity is started through a null safe mechanism.
        helper = new OAuthHelper(
              extras.getString(Shared.CONSUMER_KEY),
              extras.getString(Shared.CONSUMER_SECRET),
              extras.getString(Shared.CALLBACK_URL));

        webView = (WebView) findViewById(R.id.webView);
        webView.clearCache(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setDatabaseEnabled(false);
        webView.getSettings().setSaveFormData(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (helper.overrideRedirect(url)) {
                    new RetrieveAccessTokenTask(XingOAuthActivity.this, helper).
                          executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Uri.parse(url));
                    return true;
                } else {
                    return false;
                }
            }
        });
        clearCookies();
        new OauthRequestTokenTask(this, helper).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onBackPressed() {
        setResult(Shared.RESULT_BACK);
        super.onBackPressed();
    }

    /** Invokes in background the oauthAuthenticationHelper to retrieve the request token. */
    static final class OauthRequestTokenTask extends AsyncTask<Void, Void, String> {
        private final OAuthHelper helper;
        private final WeakReference<XingOAuthActivity> activityRef;

        OauthRequestTokenTask(XingOAuthActivity activity, OAuthHelper helper) {
            activityRef = new WeakReference<>(activity);
            this.helper = helper;
        }

        @Nullable
        @Override
        protected String doInBackground(Void... voids) {
            String url;
            try {
                url = helper.retrieveRequestTokenUrl();
            } catch (Exception ex) {
                url = null;
            }
            return url;
        }

        @Override
        protected void onPostExecute(String url) {
            super.onPostExecute(url);
            XingOAuthActivity activity = activityRef.get();
            if (TextUtils.isEmpty(url)) {
                helper.clean();
                if (activity != null) {
                    activity.setResult(Activity.RESULT_CANCELED);
                    activity.finishActivity(Shared.REQUEST_CODE);
                    activity.finish();
                }
            } else {
                if (activity != null) {
                    activity.webView.loadUrl(url);
                }
            }
        }
    }

    /** Invokes in background the oauthAuthenticationHelper to retrieve the access token. */
    static final class RetrieveAccessTokenTask extends AsyncTask<Uri, Void, Boolean> {
        private final OAuthHelper helper;
        private final WeakReference<XingOAuthActivity> activityRef;

        RetrieveAccessTokenTask(XingOAuthActivity activity, OAuthHelper helper) {
            activityRef = new WeakReference<>(activity);
            this.helper = helper;
        }

        @Override
        protected Boolean doInBackground(Uri... uris) {
            boolean result = true;
            try {
                helper.retrieveAccessToken(uris[0]);
            } catch (Exception ex) {
                result = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Intent data = null;
            int activityResult;
            Activity activity = activityRef.get();

            String token = helper.getToken();
            String tokenSecret = helper.getTokenSecret();

            if (TextUtils.isEmpty(token) || TextUtils.isEmpty(tokenSecret)) {
                helper.clean();
                activityResult = RESULT_CANCELED;
            } else {
                data = new Intent();
                data.putExtra(Shared.TOKEN, token);
                data.putExtra(Shared.TOKEN_SECRET, tokenSecret);
                activityResult = RESULT_OK;
            }

            if (activity != null) {
                activity.setResult(activityResult, data);
                activity.finishActivity(Shared.REQUEST_CODE);
                activity.finish();
            }
        }
    }
}
