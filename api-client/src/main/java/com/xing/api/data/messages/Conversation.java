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
 * Java representation of a Conversation.
 */
@AutoValue
public abstract class Conversation implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "id")
    public abstract String id();

    @Json(name = "subject")
    public abstract String subject();

    @Json(name = "message_count")
    public abstract int totalMsgCount();

    @Json(name = "unread_message_count")
    public abstract int unreadMessageCount();

    @Json(name = "updated_at")
    public abstract SafeCalendar updatedAt();

    @Json(name = "read_only")
    public abstract boolean isReadOnly();

    @Json(name = "participants")
    public abstract List<XingUser> participants();

    @Json(name = "latest_messages")
    @Nullable
    public abstract List<ConversationMessage> latestMessages();

    static Builder builder() {
        return new AutoValue_Conversation.Builder();
    }

    @AutoValue.Builder
    abstract static class Builder {
        abstract Builder id(String id);

        abstract Builder subject(String subject);

        abstract Builder totalMsgCount(int totalMsgCount);

        abstract Builder unreadMessageCount(int unreadMessageCount);

        abstract Builder updatedAt(SafeCalendar updatedAt);

        abstract Builder isReadOnly(boolean isReadOnly);

        abstract Builder participants(List<XingUser> participants);

        abstract Builder latestMessages(List<ConversationMessage> latestMessages);

        abstract Conversation build();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Conversation)) {
            return false;
        }

        Conversation c = (Conversation) obj;
        return id().equals(c.id());
    }

    @Override
    public int hashCode() {
        return id().hashCode();
    }

    public static JsonAdapter<Conversation> jsonAdapter(Moshi moshi) {
        return new AutoValue_Conversation.MoshiJsonAdapter(moshi);
    }
}
