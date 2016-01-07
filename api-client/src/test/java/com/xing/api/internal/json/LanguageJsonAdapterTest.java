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

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.xing.api.data.profile.Language;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import okio.Buffer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author daniel.hartwich
 */
public class LanguageJsonAdapterTest {
    private Moshi moshi;

    @Before
    public void setUp() throws Exception {
        moshi = new Moshi.Builder()
              .add(LanguageJsonAdapter.FACTORY)
              .build();
    }

    @After
    public void tearDown() throws Exception {
        moshi = null;
    }

    @Test
    public void testFromJson() throws Exception {
        JsonAdapter<LanguageHelper> adapter = moshi.adapter(LanguageHelper.class);

        assertThat(adapter.fromJson("{ \"language\" : \"en\" }").language).isEqualTo(Language.EN);
        assertThat(adapter.fromJson("{ \"language\" : \"de\" }").language).isEqualTo(Language.DE);
        assertThat(adapter.fromJson("{ \"language\" : \"es\" }").language).isEqualTo(Language.ES);
        assertThat(adapter.fromJson("{ \"language\" : \"fi\" }").language).isEqualTo(Language.FI);
        assertThat(adapter.fromJson("{ \"language\" : \"fr\" }").language).isEqualTo(Language.FR);
        assertThat(adapter.fromJson("{ \"language\" : \"hu\" }").language).isEqualTo(Language.HU);
        assertThat(adapter.fromJson("{ \"language\" : \"it\" }").language).isEqualTo(Language.IT);
        assertThat(adapter.fromJson("{ \"language\" : \"ja\" }").language).isEqualTo(Language.JA);
        assertThat(adapter.fromJson("{ \"language\" : \"ko\" }").language).isEqualTo(Language.KO);
        assertThat(adapter.fromJson("{ \"language\" : \"nl\" }").language).isEqualTo(Language.NL);
        assertThat(adapter.fromJson("{ \"language\" : \"pl\" }").language).isEqualTo(Language.PL);
        assertThat(adapter.fromJson("{ \"language\" : \"pt\" }").language).isEqualTo(Language.PT);
        assertThat(adapter.fromJson("{ \"language\" : \"ru\" }").language).isEqualTo(Language.RU);
        assertThat(adapter.fromJson("{ \"language\" : \"sv\" }").language).isEqualTo(Language.SV);
        assertThat(adapter.fromJson("{ \"language\" : \"tr\" }").language).isEqualTo(Language.TR);
        assertThat(adapter.fromJson("{ \"language\" : \"zh\" }").language).isEqualTo(Language.ZH);
        assertThat(adapter.fromJson("{ \"language\" : \"ro\" }").language).isEqualTo(Language.RO);
        assertThat(adapter.fromJson("{ \"language\" : \"no\" }").language).isEqualTo(Language.NO);
        assertThat(adapter.fromJson("{ \"language\" : \"cs\" }").language).isEqualTo(Language.CS);
        assertThat(adapter.fromJson("{ \"language\" : \"el\" }").language).isEqualTo(Language.EL);
        assertThat(adapter.fromJson("{ \"language\" : \"da\" }").language).isEqualTo(Language.DA);
        assertThat(adapter.fromJson("{ \"language\" : \"ar\" }").language).isEqualTo(Language.AR);
        assertThat(adapter.fromJson("{ \"language\" : \"he\" }").language).isEqualTo(Language.HE);
        assertThat(adapter.fromJson("{ \"language\" : \"klingon\" }").language).isNull();
    }

    @Test
    public void testToJson() throws Exception {
        Buffer buffer = new Buffer();
        JsonWriter writer = JsonWriter.of(buffer);
        writer.setLenient(true);
        moshi.adapter(Language.class).toJson(writer, Language.EN);
        writer.flush();
        String bufferedString = buffer.readUtf8();
        assertThat(bufferedString).isEqualTo('"' + Language.EN.toString() + '"');
    }

    static class LanguageHelper {
        @Json(name = "language")
        public Language language;
    }
}
