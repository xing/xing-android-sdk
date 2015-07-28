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

import java.net.HttpURLConnection;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;

/**
 * Allows to sign requests. It has to be initialized first.
 */
@SuppressWarnings("ClassWithOnlyPrivateConstructors")
public class OauthSigner {
    private final OAuthConsumer mOauthConsumer;
    private static OauthSigner sInstance;

    /**
     * Initializes the instance of the OauthSigner.
     * @param consumerKey
     * @param consumerSecret
     * @param token
     * @param tokenSecret
     */
    public static void init(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        sInstance = new OauthSigner(consumerKey, consumerSecret, token, tokenSecret);
    }

    private OauthSigner(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        mOauthConsumer = new DefaultOAuthConsumer(consumerKey, consumerSecret);
        mOauthConsumer.setTokenWithSecret(token, tokenSecret);
    }

    public static OauthSigner getInstance() {
        return sInstance;
    }

    public void sign(HttpURLConnection connection) throws XingOauthException {
        try {
            mOauthConsumer.sign(connection);
        } catch (Exception exception) {
            throw new XingOauthException(exception);
        }
    }

    public static class XingOauthException extends Exception {

        private static final long serialVersionUID = -6242176995187584643L;

        private final String mMessage;

        public XingOauthException(Exception ex) {
            mMessage = ex.getMessage();
        }

        @Override
        public String getMessage() {
            return mMessage;
        }
    }
}
