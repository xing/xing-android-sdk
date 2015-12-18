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
