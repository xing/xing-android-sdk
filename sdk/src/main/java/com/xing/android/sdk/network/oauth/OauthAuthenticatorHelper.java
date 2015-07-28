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

package com.xing.android.sdk.network.oauth;

import android.content.Context;
import android.net.Uri;

import com.xing.android.sdk.R;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;


/**
 * @author david.gonzalez
 */
public final class OauthAuthenticatorHelper {
    public static final String CONSUMER_KEY = "consumerKey";
    public static final String CONSUMER_SECRET = "consumerSecret";
    public static final String TOKEN = "token";
    public static final String TOKEN_SECRET = "tokenSecret";
    private final OAuthConsumer mConsumer;
    private final OAuthProvider mProvider;
    private final String mCallbackUrl;

    /**
     * Initializes the instance of OAuthAuthenticatorHelper.
     *
     * @param context        Context that allows the class to create the callback url from resources.
     * @param consumerKey    Consumer key of the app in the server.
     * @param consumerSecret Consumer secret of the app in the server.
     */
    public OauthAuthenticatorHelper(Context context, String consumerKey, String consumerSecret) {
        mConsumer = new DefaultOAuthConsumer(consumerKey, consumerSecret);
        mProvider = new DefaultOAuthProvider(
                context.getString(R.string.requestTokenUrl),
                context.getString(R.string.accessTokenUrl),
                context.getString(R.string.authorizeUrl));
        mCallbackUrl = buildCallback(context);
    }


    /**
     * Creates the url that will be used by the server as callback.
     *
     * @param context Context of the app.
     * @return Url used by the server as a callback.
     */
    private static String buildCallback(Context context) {
        return context.getString(R.string.xingsdk) + "://" + context.getString(R.string.callback);
    }


    /**
     * Retrive the url with request token
     *
     * @throws Exception signpost exception.
     */
    public String retriveRequestTokenUrl() throws Exception {
        return mProvider.retrieveRequestToken(mConsumer, mCallbackUrl);
    }


    /**
     * @param uri Uri that contains the oauth_verifier, necessary for retrieve the access token.
     * @throws Exception signpost exception.
     */
    public void retrieveAccessToken(Uri uri) throws Exception {
        try {
            String oauthVerifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);

            mProvider.retrieveAccessToken(mConsumer, oauthVerifier);
        } catch (Exception ex) {
            clean();
        }
    }

    public String getToken() {
        return mConsumer.getToken();
    }

    public String getTokenSecret() {
        return mConsumer.getTokenSecret();
    }

    public void clean() {
        mConsumer.setTokenWithSecret(null, null);
    }
}
