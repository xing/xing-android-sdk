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
import android.preference.PreferenceManager;

/**
 * @author ciprian.ursu
 */
public final class Prefs {
    private static final String FILENAME = "SamplePrefs";
    private static final String KEY_AUTH_TOKEN = "oauthtoken";
    public static final String KEY_TOKEN_SECRET = "oauthsecret";
    public static final String KEY_USER_ID = "id";
    private static Prefs mInstance;
    private final SharedPreferences mPrefs;
    private final Context ctx;

    private Prefs(Context context) {
        this.ctx = context.getApplicationContext();
        mPrefs = ctx.getSharedPreferences(FILENAME, Context.MODE_MULTI_PROCESS);
    }

    public static Prefs getInstance(Context context) {
        if (mInstance == null || mInstance.mPrefs == null) {
            mInstance = new Prefs(context);
        }
        return mInstance;
    }

    private void edit(String key, String value) {
        mPrefs.edit().putString(key, value).apply();
    }

    public String getOauthToken() {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString(KEY_AUTH_TOKEN, null);
    }

    public String getOauthSecret() {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString(KEY_TOKEN_SECRET, null);
    }

    public void setOauthToken(String token) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.commit();
    }

    public void setOauthSecret(String tokenSecret) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        editor.putString(KEY_TOKEN_SECRET, tokenSecret);
        editor.commit();
    }

    public void logout() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        editor.remove(KEY_AUTH_TOKEN);
        editor.remove(KEY_TOKEN_SECRET);
        editor.apply();
        mPrefs.edit().remove(KEY_USER_ID).apply();
    }

    public void setUserId(String userId) {
        edit(KEY_USER_ID, userId);
    }
}
