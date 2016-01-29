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
package com.xing.api.sample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xing.api.oauth.XingOAuthCallback;
import com.xing.api.oauth.XingOAuth;
import com.xing.api.sample.BuildConfig;
import com.xing.api.sample.Prefs;
import com.xing.api.sample.R;

public class LoginActivity extends BaseActivity implements View.OnClickListener, XingOAuthCallback {
    private XingOAuth xingOAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.button_login);
        loginButton.setOnClickListener(this);

        if (TextUtils.isEmpty(BuildConfig.OAUTH_CONSUMER_KEY) || TextUtils.isEmpty(BuildConfig.OAUTH_CONSUMER_SECRET)) {
            TextView missingCredentialsTV = (TextView) findViewById(R.id.missingCredentials);
            loginButton.setEnabled(false);
            missingCredentialsTV.setVisibility(View.VISIBLE);
            showToast(R.string.missing_credentials);
        } else {
            xingOAuth = new XingOAuth.Builder()
                  .consumerKey(BuildConfig.OAUTH_CONSUMER_KEY)
                  .consumerSecret(BuildConfig.OAUTH_CONSUMER_SECRET)
                  .oauthCallback(this)
                  .callbackUrlDebug()
                  .build();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                xingOAuth.loginWithXing(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        xingOAuth.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(String token, String tokenSecret) {
        showToast("ok");

        Prefs.getInstance(this).setOauthToken(token);
        Prefs.getInstance(this).setOauthSecret(tokenSecret);

        startActivity(new Intent(this, ProfileActivity.class).setFlags(
              Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        finish();
    }

    @Override
    public void onError() {
        showToast("error");
    }
}
