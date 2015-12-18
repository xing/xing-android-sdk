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
import com.xing.android.sdk.model.user.PremiumService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import okio.Buffer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author daniel.hartwich
 */
public class PremiumServiceJsonAdapterTest {
    private Moshi moshi;

    @Before
    public void setUp() throws Exception {
        moshi = new Moshi.Builder()
              .add(PremiumServiceJsonAdapter.FACTORY)
              .build();
    }

    @After
    public void tearDown() throws Exception {
        moshi = null;
    }

    @Test
    public void testFromJson() throws Exception {
        JsonAdapter<PremiumServiceHelper> adapter = moshi.adapter(PremiumServiceHelper.class);
        assertThat(adapter.fromJson("{\"premium_service\" : \"SEARCH\"}").premiumService)
              .isEqualTo(PremiumService.SEARCH);
        assertThat(adapter.fromJson("{\"premium_service\" : \"PRIVATEMESSAGES\"}").premiumService)
              .isEqualTo(PremiumService.PRIVATE_MESSAGES);
        assertThat(adapter.fromJson("{\"premium_service\" : \"NOADVERTISING\"}").premiumService)
              .isEqualTo(PremiumService.NO_ADVERTISING);
        assertThat(adapter.fromJson("{\"premium_service\" : \"MASTER\"}").premiumService).isNull();
    }

    @Test
    public void testToJson() throws Exception {
        Buffer buffer = new Buffer();
        JsonWriter writer = JsonWriter.of(buffer);
        writer.setLenient(true);
        moshi.adapter(PremiumService.class).toJson(writer, PremiumService.SEARCH);
        writer.flush();
        String bufferedString = buffer.readUtf8();
        assertThat(bufferedString).isEqualTo('"' + PremiumService.SEARCH.getJsonValue() + '"');
    }

    static class PremiumServiceHelper {
        @Json(name = "premium_service")
        public PremiumService premiumService;
    }
}
