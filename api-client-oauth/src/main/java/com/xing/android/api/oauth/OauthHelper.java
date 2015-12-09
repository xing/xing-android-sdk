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

import android.net.Uri;
import android.support.annotation.Nullable;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;

/**
 * @author david.gonzalez
 * @author daniel.hartwich
 */
class OauthHelper {
    private static final String REQUEST_TOKEN_URL = "https://api.xing.com/v1/request_token";
    private static final String ACCESS_TOKEN_URL = "https://api.xing.com/v1/access_token";
    private static final String AUTHORIZE_URL = "https://api.xing.com/v1/authorize";
    private static final String CALLBACK_URL = "xingsdk://callback";

    private final OAuthConsumer consumer;
    private final OAuthProvider provider;

    /**
     * Initializes the instance of OAuthAuthenticatorHelper.
     *
     * @param consumerKey Consumer key of the app in the server.
     * @param consumerSecret Consumer secret of the app in the server.
     */
    public OauthHelper(@Nullable String consumerKey, @Nullable String consumerSecret) {
        //TODO Replace this with the OKHttp replacement
        consumer = new DefaultOAuthConsumer(consumerKey, consumerSecret);
        provider = new DefaultOAuthProvider(REQUEST_TOKEN_URL, ACCESS_TOKEN_URL, AUTHORIZE_URL);
    }

    /**
     * Retrieve the url with request token.
     *
     * @throws Exception signpost exception.
     */
    protected String retrieveRequestTokenUrl() throws Exception {
        return provider.retrieveRequestToken(consumer, CALLBACK_URL);
    }

    /**
     * @param uri Uri that contains the oauth_verifier, necessary for retrieve the access token.
     * @throws Exception signpost exception.
     */
    protected void retrieveAccessToken(Uri uri) throws Exception {
        try {
            String oauthVerifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
            provider.retrieveAccessToken(consumer, oauthVerifier);
        } catch (Exception ex) {
            clean();
        }
    }

    protected String getToken() {
        return consumer.getToken();
    }

    protected String getTokenSecret() {
        return consumer.getTokenSecret();
    }

    protected void clean() {
        consumer.setTokenWithSecret(null, null);
    }
}
