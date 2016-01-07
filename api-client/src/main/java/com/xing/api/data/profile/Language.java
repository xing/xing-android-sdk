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
package com.xing.api.data.profile;

/**
 * Possible language values returned/accepted by XWS.
 *
 * @see <a href="https://dev.xing.com/docs/put/users/me/languages/:language">Languages Resource</a>
 */
public enum Language {
    EN("en"),
    DE("de"),
    ES("es"),
    FI("fi"),
    FR("fr"),
    HU("hu"),
    IT("it"),
    JA("ja"),
    KO("ko"),
    NL("nl"),
    PL("pl"),
    PT("pt"),
    RU("ru"),
    SV("sv"),
    TR("tr"),
    ZH("zh"),
    RO("ro"),
    NO("no"),
    CS("cs"),
    EL("el"),
    DA("da"),
    AR("ar"),
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
