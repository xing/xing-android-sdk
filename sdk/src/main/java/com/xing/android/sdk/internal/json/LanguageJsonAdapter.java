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

package com.xing.android.sdk.internal.json;

import android.support.annotation.Nullable;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.xing.android.sdk.model.user.Language;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * @author daniel.hartwich
 */
public class LanguageJsonAdapter extends JsonAdapter<Language> {
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
    public void toJson(JsonWriter writer, Language value) throws IOException {
        writer.value(value.getJsonValue());
    }
}
