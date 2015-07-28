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

package com.xing.android.sdk.network;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Pair;

import com.xing.android.sdk.network.info.Optional;
import com.xing.android.sdk.network.oauth.OauthSigner;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DefaultRequestConfig implements RequestConfig {

    private final OauthSigner mOauthSigner;
    private final Uri mUri;
    private final List<Pair<String, String>> mHeaders;
    private final List<Pair<String, String>> mParams;

    /**
     * Create an instance of the default {@link RequestConfig}
     *
     * @param builder The builder object to build with
     */
    DefaultRequestConfig(@NonNull DefaultRequestConfig.Builder builder) {
        if (!builder.loggedOut) {
            // Prepare oauth signer
            OauthSigner.init(
                    builder.consumerKey,
                    builder.consumerSecret,
                    builder.token,
                    builder.tokenSecret
            );
            mOauthSigner = OauthSigner.getInstance();
        } else {
            mOauthSigner = null;
        }

        // Read headers
        mHeaders = builder.headers;

        mParams = builder.params;

        // Prepare Uri
        mUri = new Uri.Builder().scheme("https").authority("api.xing.com").build();
    }

    @Override
    public List<Pair<String, String>> getCommonHeaders() {
        return mHeaders;
    }

    @Override
    public Uri getBaseUri() {
        return mUri;
    }

    @Override
    public OauthSigner getOauthSigner() {
        return mOauthSigner;
    }

    @Override
    public List<Pair<String, String>> getCommonParams() {
        return mParams;
    }

    public static class Builder {

        private String consumerKey;
        private String consumerSecret;
        private String token;
        private String tokenSecret;
        private List<Pair<String, String>> headers;
        private List<Pair<String, String>> params;
        private boolean loggedOut = false;


        /**
         * Set consumer key
         */
        public Builder setConsumerKey(String consumerKey) {
            this.consumerKey = consumerKey;
            return this;
        }

        /**
         * Set consumer secret
         */
        public Builder setConsumerSecret(String consumerSecret) {
            this.consumerSecret = consumerSecret;
            return this;
        }

        /**
         * Set token
         */
        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        /**
         * Set token secret
         */
        public Builder setTokenSecret(String tokenSecret) {
            this.tokenSecret = tokenSecret;
            return this;
        }

        /**
         * Set loggedOut. This avoids the check of oauth values.
         */
        public Builder setLoggedOut(boolean loggedOut) {
            this.loggedOut = loggedOut;
            return this;
        }

        /**
         * Add global param
         */
        @Optional
        public Builder addParam(String key, String value) {
            if (params == null) {
                params = new ArrayList<>();
            }
            params.add(new Pair<>(key, value));

            return this;
        }

        /**
         * Add global params
         */
        @Optional
        public Builder addParams(List<Pair<String, String>> params) {
            if (this.params == null) {
                this.params = params;
            } else {
                this.params.addAll(params);
            }

            return this;
        }

        /**
         * Add global header
         */
        @Optional
        public Builder addHeader(String key, String value) {
            if (headers == null) {
                headers = new ArrayList<>();
            }
            headers.add(new Pair<>(key, value));

            return this;
        }

        /**
         * Add global headers
         */
        @Optional
        public Builder addHeaders(List<Pair<String, String>> headers) {
            if (this.headers == null) {
                this.headers = headers;
            } else {
                this.headers.addAll(headers);
            }

            return this;
        }

        /**
         * Check if all required parameters are set
         */
        private void validateBuildParameters() {
            if (TextUtils.isEmpty(consumerKey)) {
                throw new IllegalArgumentException("CONSUMER_KEY not set");
            }

            if (TextUtils.isEmpty(consumerSecret)) {
                throw new IllegalArgumentException("CONSUMER_SECRET not set");
            }

            if (TextUtils.isEmpty(token)) {
                throw new IllegalArgumentException("TOKEN not set, the user must be logged it");
            }

            if (TextUtils.isEmpty(tokenSecret)) {
                throw new IllegalArgumentException("TOKEN_SECRET not set, the user must be logged it");
            }
        }

        public DefaultRequestConfig build() {
            if (!loggedOut) {
                validateBuildParameters();
            }

            return new DefaultRequestConfig(this);
        }
    }
}
