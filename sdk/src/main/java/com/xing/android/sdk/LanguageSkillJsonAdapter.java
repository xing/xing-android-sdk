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

package com.xing.android.sdk;

import android.support.annotation.Nullable;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonReader.Token;
import com.squareup.moshi.JsonWriter;
import com.xing.android.sdk.model.user.LanguageSkill;

import java.io.IOException;

/**
 * @author daniel.hartwich
 */
public class LanguageSkillJsonAdapter extends JsonAdapter<LanguageSkill> {
    @Nullable
    @Override
    public LanguageSkill fromJson(JsonReader reader) throws IOException {
        if (reader.peek() == Token.NULL) return reader.nextNull();

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
