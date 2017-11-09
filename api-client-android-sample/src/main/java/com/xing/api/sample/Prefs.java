/*
 * Copyright 2016 XING SE (http://xing.com/)
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
package com.xing.api.sample;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

/**
 * @author ciprian.ursu
 */
public final class Prefs {
    private static final String FILE_NAME = "SamplePrefs";
    private static final String KEY_TOKEN_SECRET = "oauth_secret";
    private static final String KEY_AUTH_TOKEN = "oauth_token";
    private static final String KEY_USER_ID = "id";

    private static Prefs instance;

    private final SharedPreferences preferences;

    private Prefs(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public static Prefs getInstance(Context context) {
        if (instance == null || instance.preferences == null) {
            instance = new Prefs(context);
        }
        return instance;
    }

    private void edit(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    @Nullable
    public String getOauthToken() {
        return preferences.getString(KEY_AUTH_TOKEN, null);
    }

    public void setOauthToken(String token) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.apply();
    }

    @Nullable
    public String getOauthSecret() {
        return preferences.getString(KEY_TOKEN_SECRET, null);
    }

    public void setOauthSecret(String tokenSecret) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_TOKEN_SECRET, tokenSecret);
        editor.apply();
    }

    public void logout() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(KEY_AUTH_TOKEN);
        editor.remove(KEY_TOKEN_SECRET);
        editor.remove(KEY_USER_ID);
        editor.apply();
    }

    public void setUserId(String userId) {
        edit(KEY_USER_ID, userId);
    }
}
