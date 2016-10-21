/*
 * Copyright (c) 2016 XING AG (http://xing.com/)
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

package com.xing.api.data.messages;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.xing.api.data.SafeCalendar;
import com.xing.api.data.profile.XingUser;
import com.xing.api.internal.Nullable;

import java.io.Serializable;
import java.util.List;

/**
 * Java representation of a Message in a Conversation.
 */
@AutoValue
public abstract class ConversationMessage implements Serializable {
    private static final long serialVersionUID = -4307893362880528921L;

    @Json(name = "id")
    public abstract String messageId();
    @Json(name = "created_at")
    public abstract SafeCalendar createdAt();
    @Json(name = "content")
    public abstract String content();
    @Json(name = "read")
    public abstract boolean isRead();
    @Json(name = "sender")
    public abstract XingUser sender();
    @Json(name = "attachments") @Nullable
    public abstract List<MessageAttachment> attachments();

    @Override
    public boolean equals(Object obj) {
        ConversationMessage m;
        if (obj instanceof ConversationMessage) {
            m = (ConversationMessage) obj;
        } else {
            return false;
        }

        return (messageId().equals(m.messageId()));
    }

    @Override
    public int hashCode() {
        return messageId() != null ? messageId().hashCode() : 0;
    }

    public static JsonAdapter<ConversationMessage> jsonAdapter(Moshi moshi) {
        return new AutoValue_ConversationMessage.MoshiJsonAdapter(moshi);
    }
}
