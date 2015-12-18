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
import com.xing.api.model.user.MessagingAccount;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * @author daniel.hartwich
 */
public class MessagingAccountJsonAdapter extends JsonAdapter<MessagingAccount> {
    public static final Factory FACTORY = new Factory() {
        @Nullable
        @Override
        public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
            if (!annotations.isEmpty()) return null;
            Class<?> rawType = Types.getRawType(type);
            if (rawType != MessagingAccount.class) return null;
            return new MessagingAccountJsonAdapter().nullSafe();
        }
    };

    @Nullable
    @Override
    public MessagingAccount fromJson(JsonReader reader) throws IOException {
        String messagingAccount = reader.nextString();
        switch (messagingAccount) {
            case "aim":
                return MessagingAccount.AIM;
            case "icq":
                return MessagingAccount.ICQ;
            case "skype":
                return MessagingAccount.SKYPE;
            case "msn":
                return MessagingAccount.MSN;
            case "yahoo":
                return MessagingAccount.YAHOO;
            case "jabber":
                return MessagingAccount.JABBER;
            case "googletalk":
                return MessagingAccount.GOOGLE_TALK;
            default:
                return null;
        }
    }

    @Override
    public void toJson(JsonWriter writer, MessagingAccount value) throws IOException {
        writer.value(value.getJsonValue());
    }
}
