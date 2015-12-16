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
import com.xing.android.sdk.model.user.MessagingAccount;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import okio.Buffer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author daniel.hartwich
 */
public class MessagingAccountJsonAdapterTest {
    private Moshi moshi;

    @Before
    public void setUp() throws Exception {
        moshi = new Moshi.Builder()
              .add(MessagingAccountJsonAdapter.FACTORY)
              .build();
    }

    @After
    public void tearDown() throws Exception {
        moshi = null;
    }

    @Test
    public void testFromJson() throws Exception {
        JsonAdapter<MessagingAccountHelper> adapter = moshi.adapter(MessagingAccountHelper.class);
        assertThat(adapter.fromJson("{\"messaging_account\" : \"aim\"}").messagingAccount).isEqualTo(MessagingAccount
              .AIM);
        assertThat(adapter.fromJson("{\"messaging_account\" : \"icq\"}").messagingAccount).isEqualTo(MessagingAccount
              .ICQ);
        assertThat(adapter.fromJson("{\"messaging_account\" : \"skype\"}").messagingAccount).isEqualTo(MessagingAccount
              .SKYPE);
        assertThat(adapter.fromJson("{\"messaging_account\" : \"msn\"}").messagingAccount).isEqualTo(MessagingAccount
              .MSN);
        assertThat(adapter.fromJson("{\"messaging_account\" : \"yahoo\"}").messagingAccount).isEqualTo(MessagingAccount
              .YAHOO);
        assertThat(adapter.fromJson("{\"messaging_account\" : \"jabber\"}").messagingAccount).isEqualTo(MessagingAccount
              .JABBER);
        assertThat(adapter.fromJson("{\"messaging_account\" : \"googletalk\"}").messagingAccount).isEqualTo
              (MessagingAccount.GOOGLE_TALK);
        assertThat(adapter.fromJson("{\"messaging_account\" : \"whatsapp\"}").messagingAccount).isNull();
    }

    @Test
    public void testToJson() throws Exception {
        Buffer buffer = new Buffer();
        JsonWriter writer = JsonWriter.of(buffer);
        writer.setLenient(true);
        moshi.adapter(MessagingAccount.class).toJson(writer, MessagingAccount.GOOGLE_TALK);
        writer.flush();
        String bufferedString = buffer.readUtf8();
        assertThat(bufferedString).isEqualTo('"' + MessagingAccount.GOOGLE_TALK.getJsonValue() + '"');
    }

    static class MessagingAccountHelper {
        @Json(name = "messaging_account")
        public MessagingAccount messagingAccount;
    }
}
