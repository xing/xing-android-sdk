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
