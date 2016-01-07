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
import com.xing.api.data.profile.MessagingAccount;

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
        assertThat(bufferedString).isEqualTo('"' + MessagingAccount.GOOGLE_TALK.toString() + '"');
    }

    static class MessagingAccountHelper {
        @Json(name = "messaging_account")
        public MessagingAccount messagingAccount;
    }
}
