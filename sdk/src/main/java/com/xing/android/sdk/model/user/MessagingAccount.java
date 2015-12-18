/*
 * Copyright (С) 2015 XING AG (http://xing.com/)
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
package com.xing.android.sdk.model.user;

import android.support.annotation.NonNull;

import com.xing.android.sdk.model.JsonEnum;

/**
 * Possible instant messaging accounts.
 *
 * @author serj.lotutovici
 * @see <a href="https://dev.xing.com/docs/get/users/me">Instant Messaging Accounts</a>
 */
public enum MessagingAccount implements JsonEnum {
    SKYPE("skype"),
    ICQ("icq"),
    MSN("msn"),
    YAHOO("yahoo"),
    AIM("aim"),
    JABBER("jabber"),
    GOOGLE_TALK("googletalk");

    /** The name value received from the json response. */
    private final String mName;

    MessagingAccount(@NonNull String accountName) {
        mName = accountName;
    }

    @Override
    public String getJsonValue() {
        return mName;
    }
}
