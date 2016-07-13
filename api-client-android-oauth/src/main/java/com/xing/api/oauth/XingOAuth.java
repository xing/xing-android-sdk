/*
 * Copyright (C) 2016 XING AG (http://xing.com/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xing.api.oauth;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import static com.xing.api.oauth.Shared.checkNotNull;
import static com.xing.api.oauth.Shared.stateNotNull;
import static com.xing.api.oauth.Shared.validate;

/**
 * Helper class that allows to start (and react to) the user authentication process.
 *
 * @author Serj Lotutovici
 */
@SuppressWarnings("ClassNamingConvention")
public final class XingOAuth {
    /** Unwraps the context from the calling object. */
    static Context unwrapContext(Object caller) {
        if (caller instanceof Activity) return ((Activity) caller);
        if (caller instanceof Fragment) return ((Fragment) caller).getActivity();
        throw new IllegalArgumentException("caller should be an instance of Activity or Fragment");
    }

    /** Starts another activity for result via respective method, based on the objects type. */
    static void startActivityForResult(Object caller, Intent intent) {
        if (caller instanceof Activity) {
            ((Activity) caller).startActivityForResult(intent, Shared.REQUEST_CODE);
            return;
        }
        if (caller instanceof Fragment) {
            ((Fragment) caller).startActivityForResult(intent, Shared.REQUEST_CODE);
            return;
        }
        throw new IllegalArgumentException("caller should be an instance of Activity or Fragment");
    }

    private final String consumerKey;
    private final String consumerSecret;
    private final String callbackUrl;
    private final XingOAuthCallback callback;

    XingOAuth(String consumerKey, String consumerSecret, String callbackUrl, XingOAuthCallback callback) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.callbackUrl = callbackUrl;
        this.callback = callback;
    }

    /**
     * Starts the "Login with XING" process, by showing an auth screen.
     * <p>
     * This calls the {@linkplain Activity#startActivityForResult} method.
     */
    public void loginWithXing(Activity activity) {
        startLoginForCaller(activity);
    }

    /**
     * Starts the "Login with XING" process, by showing an auth screen.
     * <p>
     * This calls the {@linkplain Fragment#startActivityForResult} method.
     */
    public void loginWithXing(Fragment fragment) {
        startLoginForCaller(fragment);
    }

    /**  */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Shared.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                callback.onSuccess(extras.getString(Shared.TOKEN, ""), extras.getString(Shared.TOKEN_SECRET, ""));
            } else if (resultCode == Activity.RESULT_CANCELED) {
                callback.onError();
            }
        }
    }

    /** Builds the intent and passes it to the auth activity via respective callback. */
    void startLoginForCaller(Object caller) {
        Intent intent = new Intent(unwrapContext(caller), XingOAuthActivity.class);
        intent.putExtra(Shared.CONSUMER_KEY, consumerKey);
        intent.putExtra(Shared.CONSUMER_SECRET, consumerSecret);
        intent.putExtra(Shared.CALLBACK_URL, callbackUrl);
        startActivityForResult(caller, intent);
    }

    /** Builds and validates the {@linkplain XingOAuth} helper. */
    public static final class Builder {
        private static final String CALLBACK_PATTERN = "^[a-z]*\\:\\/\\/+[a-z0-9_\\-\\.]*";

        private String consumerKey;
        private String consumerSecret;
        private String callbackUrl;
        private XingOAuthCallback xingOAuthCallback;

        /** Sets the consumer key. */
        public Builder consumerKey(String consumerKey) {
            this.consumerKey = checkNotNull(consumerKey, "consumerKey == null");
            return this;
        }

        /** Sets the consumer secret. */
        public Builder consumerSecret(String consumerSecret) {
            this.consumerSecret = checkNotNull(consumerSecret, "consumerSecret == null");
            return this;
        }

        /**
         * Sets the callback urls.
         * <p>
         * This is the callback url that is specified by the consumer when requesting production keys. In debug
         * mode this is not mandatory and {@linkplain #callbackUrlDebug()} should be called instead.
         * <p>
         * The passed value must satisfy the standard url format or {@code [prefix]://callback}, where
         * <strong>prefix</strong> consists only of lower case characters.
         */
        public Builder callbackUrl(String callbackUrl) {
            this.callbackUrl = validate(checkNotNull(callbackUrl, "callbackUrl == null"), CALLBACK_PATTERN);
            return this;
        }

        /** Sets the callback url to debug mode. */
        public Builder callbackUrlDebug() {
            callbackUrl = "debug://callback";
            return this;
        }

        /** Required callback for the auth process. */
        public Builder oauthCallback(XingOAuthCallback callback) {
            xingOAuthCallback = checkNotNull(callback, "callback == null");
            return this;
        }

        /** Builds an instance of {@linkplain XingOAuth}. */
        public XingOAuth build() {
            stateNotNull(consumerKey, "consumerKey is not set");
            stateNotNull(consumerSecret, "consumerSecret is not set");
            stateNotNull(callbackUrl, "callbackUrl is not set. Call callbackUrlDebug if in debug mode");
            stateNotNull(xingOAuthCallback, "oAuthCallback not set");
            return new XingOAuth(consumerKey, consumerSecret, callbackUrl, xingOAuthCallback);
        }
    }
}
