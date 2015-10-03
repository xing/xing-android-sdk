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

package com.xing.android.sdk.login;

import android.app.Activity;
import android.content.Intent;

import com.xing.android.sdk.network.oauth.OauthAuthenticatorHelper;

/**
 * Allows to perform authentication using OAuth 1.
 *
 * @author david.gonzalez
 */
public final class LoginHelper {
    /**
     * Performs login using OAuth 1.
     *
     * @param oauthConsumerKey Consumer key for the OAuth authentication process.
     * @param oauthConsumerSecret Consumer secret for the OAuth authentication process.
     * @param activity Activity that will receive the result of the login. In case of success, token and token secret
     * will be included on an intent.
     */
    public static void login(String oauthConsumerKey, String oauthConsumerSecret, Activity activity) {
        Intent intent = new Intent(activity, OauthCallbackActivity.class);
        intent.putExtra(OauthAuthenticatorHelper.CONSUMER_KEY, oauthConsumerKey);
        intent.putExtra(OauthAuthenticatorHelper.CONSUMER_SECRET, oauthConsumerSecret);
        activity.startActivityForResult(intent, OauthCallbackActivity.REQUEST_CODE);
    }

    private LoginHelper() {
        throw new AssertionError("No instances.");
    }
}
