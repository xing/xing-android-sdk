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

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.xing.android.sdk.model.user.LanguageSkill;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import okio.Buffer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author daniel.hartwich
 */
public class LanguageSkillJsonAdapterTest {
    private Moshi moshi;

    @Before
    public void setUp() throws Exception {
        moshi = new Moshi.Builder()
              .add(LanguageSkillJsonAdapter.FACTORY)
              .build();
    }

    @After
    public void tearDown() throws Exception {
        moshi = null;
    }

    @Test
    public void testFromJson() throws Exception {
        JsonAdapter<LanguageSkillHelper> adapter =  moshi.adapter(LanguageSkillHelper.class);
        assertThat(adapter.fromJson("{\"language_skill\" : \"BASIC\"}").languageSkill).isEqualTo(LanguageSkill.BASIC);
        assertThat(adapter.fromJson("{\"language_skill\" : \"GOOD\"}").languageSkill).isEqualTo(LanguageSkill.GOOD);
        assertThat(adapter.fromJson("{\"language_skill\" : \"FLUENT\"}").languageSkill).isEqualTo(LanguageSkill.FLUENT);
        assertThat(adapter.fromJson("{\"language_skill\" : \"NATIVE\"}").languageSkill).isEqualTo(LanguageSkill.NATIVE);
        assertThat(adapter.fromJson("{\"language_skill\" : \"BESTONEARTHSTYLE\"}").languageSkill).isNull();

    }

    @Test
    public void testToJson() throws Exception {
        Buffer buffer = new Buffer();
        JsonWriter writer = JsonWriter.of(buffer);
        writer.setLenient(true);
        moshi.adapter(LanguageSkill.class).toJson(writer, LanguageSkill.NATIVE);
        writer.flush();
        String bufferedString = buffer.readUtf8();
        assertThat(bufferedString).isEqualTo('"' + LanguageSkill.NATIVE.getJsonValue() + '"');
    }

    static class LanguageSkillHelper {
        @Json(name = "language_skill")
        public LanguageSkill languageSkill;
    }
}
