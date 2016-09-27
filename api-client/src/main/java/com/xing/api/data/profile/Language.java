/*
 * Copyright (С) 2016 XING AG (http://xing.com/)
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
 * Possible language values returned/accepted by XWS.
 *
 * @see <a href="https://dev.xing.com/docs/put/users/me/languages/:language">Languages Resource</a>
 */
public enum Language {
    @Json(name = "en")
    EN("en"),
    @Json(name = "de")
    DE("de"),
    @Json(name = "es")
    ES("es"),
    @Json(name = "fi")
    FI("fi"),
    @Json(name = "fr")
    FR("fr"),
    @Json(name = "hu")
    HU("hu"),
    @Json(name = "it")
    IT("it"),
    @Json(name = "ja")
    JA("ja"),
    @Json(name = "ko")
    KO("ko"),
    @Json(name = "nl")
    NL("nl"),
    @Json(name = "pl")
    PL("pl"),
    @Json(name = "pt")
    PT("pt"),
    @Json(name = "ru")
    RU("ru"),
    @Json(name = "sv")
    SV("sv"),
    @Json(name = "tr")
    TR("tr"),
    @Json(name = "zh")
    ZH("zh"),
    @Json(name = "ro")
    RO("ro"),
    @Json(name = "no")
    NO("no"),
    @Json(name = "cs")
    CS("cs"),
    @Json(name = "el")
    EL("el"),
    @Json(name = "da")
    DA("da"),
    @Json(name = "ar")
    AR("ar"),
    @Json(name = "he")
    HE("he");

    private final String value;

    Language(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
