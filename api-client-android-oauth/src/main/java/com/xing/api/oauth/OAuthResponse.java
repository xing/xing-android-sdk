/*
 * Copyright (C) 2016 XING SE (http://xing.com/)
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

/**
 * Represents the OAuth response. <strong>This</strong> bundles the users {@code token} and {@code tokenSecret} that
 * should be stored by the consumer.
 */
public final class OAuthResponse {
    /** Builds a successful {@linkplain OAuthResponse}. */
    static OAuthResponse success(String token, String tokenSecret) {
        return new OAuthResponse(token, tokenSecret, null);
    }

    /** Builds an error {@linkplain OAuthResponse}. */
    static OAuthResponse error(String errorReason) {
        return new OAuthResponse(null, null, errorReason);
    }

    private final String token;
    private final String tokenSecret;

    // TODO: (2.1.0) allow consumers to read the error reason.
    private final String errorReason;

    OAuthResponse(String token, String tokenSecret, String errorReason) {
        this.token = token;
        this.tokenSecret = tokenSecret;
        this.errorReason = errorReason;
    }

    public String token() {
        return token;
    }

    public String tokenSecret() {
        return tokenSecret;
    }

    /** Returns {@code true} if the authentication was successful. */
    public boolean isSuccessful() {
        return errorReason == null;
    }
}
