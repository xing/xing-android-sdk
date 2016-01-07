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
package com.xing.api.internal.json;

import android.support.annotation.Nullable;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.xing.api.data.profile.Language;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * @author daniel.hartwich
 */
public class LanguageJsonAdapter extends EnumJsonAdapter<Language> {
    public static final Factory FACTORY = new Factory() {
        @Nullable
        @Override
        public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
            if (!annotations.isEmpty()) return null;
            Class<?> rawType = Types.getRawType(type);
            if (rawType != Language.class) return null;
            return new LanguageJsonAdapter().nullSafe();
        }
    };

    LanguageJsonAdapter() {
    }

    @Nullable
    @Override
    public Language fromJson(JsonReader reader) throws IOException {
        String language = reader.nextString();
        switch (language) {
            case "en":
                return Language.EN;
            case "de":
                return Language.DE;
            case "es":
                return Language.ES;
            case "fi":
                return Language.FI;
            case "fr":
                return Language.FR;
            case "hu":
                return Language.HU;
            case "it":
                return Language.IT;
            case "ja":
                return Language.JA;
            case "ko":
                return Language.KO;
            case "nl":
                return Language.NL;
            case "pl":
                return Language.PL;
            case "pt":
                return Language.PT;
            case "ru":
                return Language.RU;
            case "sv":
                return Language.SV;
            case "tr":
                return Language.TR;
            case "zh":
                return Language.ZH;
            case "ro":
                return Language.RO;
            case "no":
                return Language.NO;
            case "cs":
                return Language.CS;
            case "el":
                return Language.EL;
            case "da":
                return Language.DA;
            case "ar":
                return Language.AR;
            case "he":
                return Language.HE;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return "JsonAdapter(" + Language.class + ')';
    }
}
