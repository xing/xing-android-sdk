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

package com.xing.android.sdk.sample.prefs;

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
