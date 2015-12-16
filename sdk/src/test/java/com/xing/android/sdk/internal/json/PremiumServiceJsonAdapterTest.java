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
