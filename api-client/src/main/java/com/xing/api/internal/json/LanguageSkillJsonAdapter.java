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
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.xing.api.model.user.LanguageSkill;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * @author daniel.hartwich
 */
public class LanguageSkillJsonAdapter extends JsonAdapter<LanguageSkill> {
    public static final Factory FACTORY = new Factory() {
        @Nullable
        @Override
        public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
            if (!annotations.isEmpty()) return null;
            Class<?> rawType = Types.getRawType(type);
            if (rawType != LanguageSkill.class) return null;
            return new LanguageSkillJsonAdapter().nullSafe();
        }
    };
    @Nullable
    @Override
    public LanguageSkill fromJson(JsonReader reader) throws IOException {

        String languageSkill = reader.nextString();
        switch (languageSkill) {
            case "BASIC":
                return LanguageSkill.BASIC;
            case "GOOD":
                return LanguageSkill.GOOD;
            case "FLUENT":
                return LanguageSkill.FLUENT;
            case "NATIVE":
                return LanguageSkill.NATIVE;
            default:
                return null;
        }
    }

    @Override
    public void toJson(JsonWriter writer, LanguageSkill value) throws IOException {
        writer.value(value.getJsonValue());
    }
}
