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

import com.xing.api.oauth.XingOauthActivity;
import com.xing.api.sample.BuildConfig;
import com.xing.api.sample.R;
import com.xing.api.sample.Prefs;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

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
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                XingOauthActivity.startOauthProcess(this, BuildConfig.OAUTH_CONSUMER_KEY,
                      BuildConfig.OAUTH_CONSUMER_SECRET);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case XingOauthActivity.REQUEST_CODE: {
                onLoginActivityResult(resultCode, data);
                break;
            }
        }
    }

    public void onLoginActivityResult(int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                showToast("ok");
                Bundle extras = data.getExtras();
                initializeXingController(extras);
                startActivity(new Intent(this, ProfileActivity.class).
                      setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                finish();
                break;

            case RESULT_CANCELED:
                showToast("error");
                break;

            case XingOauthActivity.RESULT_BACK:
                //Back pressed
                break;
        }
    }

    private void initializeXingController(Bundle extras) {
        String token = extras.getString(XingOauthActivity.TOKEN, "");
        String tokenSecret = extras.getString(XingOauthActivity.TOKEN_SECRET, "");
        Prefs.getInstance(this).setOauthToken(token);
        Prefs.getInstance(this).setOauthSecret(tokenSecret);
    }
}
