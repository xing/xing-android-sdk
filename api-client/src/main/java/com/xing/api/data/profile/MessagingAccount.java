/*
 * Copyright (C) 2018 XING SE (http://xing.com/)
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
package com.xing.api.data.profile;

import com.squareup.moshi.Json;

/**
 * Possible instant messaging account values returned/accepted by the XING API.
 *
 * @see <a href="https://dev.xing.com/docs/get/users/:id">User Profile Resource</a>
 */
public enum MessagingAccount {
    @Json(name = "skype")
    SKYPE("skype"),
    @Json(name = "icq")
    ICQ("icq"),
    @Json(name = "msn")
    MSN("msn"),
    @Json(name = "yahoo")
    YAHOO("yahoo"),
    @Json(name = "aim")
    AIM("aim"),
    @Json(name = "jabber")
    JABBER("jabber"),
    @Json(name = "googletalk")
    GOOGLE_TALK("googletalk"),
    @Json(name = "lync")
    LYNC("lync");

    /** The name value received from the json response. */
    private final String value;

    MessagingAccount(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
