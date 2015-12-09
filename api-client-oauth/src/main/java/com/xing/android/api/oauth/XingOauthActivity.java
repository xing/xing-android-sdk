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

package com.xing.android.api.oauth;

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
 */
public class XingOauthActivity extends Activity {

    public static final int REQUEST_CODE = 600;
    public static final int RESULT_BACK = 601;
    public static final String TOKEN = "token";
    public static final String TOKEN_SECRET = "tokenSecret";

    private static final String CONSUMER_KEY = "consumerKey";
    private static final String CONSUMER_SECRET = "consumerSecret";
    private WebView webView;
    private OauthHelper helper;


    public static void startOauthProcess(Activity activity, String consumerKey, String consumerSecret) {
        Intent intent = new Intent(activity, XingOauthActivity.class);
        intent.putExtra(CONSUMER_KEY, consumerKey);
        intent.putExtra(CONSUMER_SECRET, consumerSecret);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * Clear all cookies.
     */
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
        initializeOAuthAuthenticatorHelper();
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
                if (!TextUtils.isEmpty(url) && url.startsWith(getString(R.string.xing_sdk))) {
                    new RetrieveAccessTokenTask(XingOauthActivity.this, helper).
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
        setResult(RESULT_BACK);
        super.onBackPressed();
    }

    /**
     * Initializes the OAuthAuthenticationHelper with the values that reads from the extras received.
     */
    public void initializeOAuthAuthenticatorHelper() {
        Bundle extras = getIntent().getExtras();
        helper = new OauthHelper(
              extras.getString(CONSUMER_KEY),
              extras.getString(CONSUMER_SECRET));
    }

    /**
     * Invokes in background the oauthAuthenticationHelper to retrieve the request token.
     */
    private static class OauthRequestTokenTask extends AsyncTask<Void, Void, String> {

        private final OauthHelper helper;
        private final WeakReference<XingOauthActivity> activityRef;

        OauthRequestTokenTask(XingOauthActivity activity, OauthHelper helper) {
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
            XingOauthActivity activity = activityRef.get();
            if (TextUtils.isEmpty(url)) {
                helper.clean();
                if (activity != null) {
                    activity.setResult(Activity.RESULT_CANCELED);
                    activity.finishActivity(REQUEST_CODE);
                    activity.finish();
                }
            } else {
                if (activity != null) {
                    activity.webView.loadUrl(url);
                }
            }
        }
    }

    /**
     * Invokes in background the oauthAuthenticationHelper to retrieve the access token.
     */
    private static class RetrieveAccessTokenTask extends AsyncTask<Uri, Void, Boolean> {

        private final OauthHelper helper;
        private final WeakReference<XingOauthActivity> activityRef;

        RetrieveAccessTokenTask(XingOauthActivity activity, OauthHelper helper) {
            activityRef = new WeakReference<>(activity);
            this.helper = helper;
        }

        @Override
        protected Boolean doInBackground(Uri... uris) {
            Boolean ret = Boolean.TRUE;
            try {
                helper.retrieveAccessToken(uris[0]);
            } catch (Exception ex) {
                ret = Boolean.FALSE;
            }
            return ret;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Intent data = null;
            int activityResult;
            XingOauthActivity activity = activityRef.get();

            String token = helper.getToken();
            String tokenSecret = helper.getTokenSecret();

            if (TextUtils.isEmpty(token) || TextUtils.isEmpty(tokenSecret)) {
                helper.clean();
                activityResult = RESULT_CANCELED;
            } else {
                data = new Intent();
                data.putExtra(TOKEN, token);
                data.putExtra(TOKEN_SECRET, tokenSecret);
                activityResult = RESULT_OK;
            }
            if (activity != null) {
                activity.setResult(activityResult, data);
                activity.finishActivity(REQUEST_CODE);
                activity.finish();
            }
        }
    }
}
