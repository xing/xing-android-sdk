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

package com.xing.android.sdk.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xing.android.sdk.login.LoginHelper;
import com.xing.android.sdk.login.OauthCallbackActivity;
import com.xing.android.sdk.network.oauth.OauthAuthenticatorHelper;
import com.xing.android.sdk.sample.prefs.Prefs;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button loginButton = (Button) findViewById(R.id.button_login);
        loginButton.setOnClickListener(this);

        if (TextUtils.isEmpty(BuildConfig.OAUTH_CONSUMER_KEY) ||
                TextUtils.isEmpty(BuildConfig.OAUTH_CONSUMER_SECRET)) {
            final TextView missingCredentialsTV = (TextView) findViewById(R.id.missingCredentials);
            loginButton.setEnabled(false);
            missingCredentialsTV.setVisibility(View.VISIBLE);
            showToast(R.string.missing_credentials);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                handleLogin();
                break;
        }
    }

    private void handleLogin() {
        LoginHelper.login(BuildConfig.OAUTH_CONSUMER_KEY, BuildConfig.OAUTH_CONSUMER_SECRET, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OauthCallbackActivity.REQUEST_CODE: {
                onLoginActivityResult(resultCode, data);
                break;
            }
        }
    }

    public void onLoginActivityResult(int resultCode, Intent data) {
        switch (resultCode) {
            case Activity.RESULT_OK:
                showToast("ok");
                Bundle extras = data.getExtras();
                Prefs prefs = Prefs.getInstance(getApplicationContext());
                prefs.setOauthToken(extras.getString(OauthAuthenticatorHelper.TOKEN));
                prefs.setOauthSecret(extras
                        .getString(OauthAuthenticatorHelper.TOKEN_SECRET));
                SdkSampleApplication.getInstance().onUserLoggedIn();
                startActivity(new Intent(this, ProfileActivity.class).
                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_SINGLE_TOP));
                finish();
                break;

            case Activity.RESULT_CANCELED:
                showToast("error");
                break;

            case OauthCallbackActivity.RESULT_BACK:
                //Back pressed
                break;
        }
    }
}
