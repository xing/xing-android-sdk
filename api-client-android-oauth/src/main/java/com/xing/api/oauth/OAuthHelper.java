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
package com.xing.api.oauth;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;

/**
 * @author david.gonzalez
 * @author daniel.hartwich
 */
@SuppressWarnings("ClassNamingConvention")
final class OAuthHelper {
    private static final String REQUEST_TOKEN_URL = "https://api.xing.com/v1/request_token";
    private static final String ACCESS_TOKEN_URL = "https://api.xing.com/v1/access_token";
    private static final String AUTHORIZE_URL = "https://api.xing.com/v1/authorize";

    private final OAuthConsumer consumer;
    private final OAuthProvider provider;
    private final String callbackUrl;

    /**
     * Initializes the instance of OAuthAuthenticatorHelper.
     *
     * @param consumerKey Consumer key of the app in the server.
     * @param consumerSecret Consumer secret of the app in the server.
     */
    public OAuthHelper(@NonNull String consumerKey, @NonNull String consumerSecret, @NonNull String callbackUrl) {
        //TODO Replace this with the OKHttp implementation.
        this.callbackUrl = callbackUrl;
        consumer = new DefaultOAuthConsumer(consumerKey, consumerSecret);
        provider = new DefaultOAuthProvider(REQUEST_TOKEN_URL, ACCESS_TOKEN_URL, AUTHORIZE_URL);
    }

    /**
     * Retrieve the url with request token.
     *
     * @throws Exception signpost exception.
     */
    public String retrieveRequestTokenUrl() throws Exception {
        return provider.retrieveRequestToken(consumer, callbackUrl);
    }

    /**
     * @param uri Uri that contains the oauth_verifier, necessary for retrieve the access token.
     * @throws Exception signpost exception.
     */
    public void retrieveAccessToken(Uri uri) throws Exception {
        try {
            String oauthVerifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
            provider.retrieveAccessToken(consumer, oauthVerifier);
        } catch (Exception ex) {
            clean();
        }
    }

    public boolean overrideRedirect(String url) {
        return !TextUtils.isEmpty(url) && url.startsWith(callbackUrl);
    }

    public String getToken() {
        return consumer.getToken();
    }

    public String getTokenSecret() {
        return consumer.getTokenSecret();
    }

    public void clean() {
        consumer.setTokenWithSecret(null, null);
    }
}
