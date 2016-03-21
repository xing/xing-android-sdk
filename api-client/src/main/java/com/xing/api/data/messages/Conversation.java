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

import com.squareup.moshi.Json;
import com.xing.api.data.SafeCalendar;
import com.xing.api.data.profile.XingUser;

import java.io.Serializable;
import java.util.List;

/**
 * Java representation of a Conversation.
 */
public class Conversation implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "id")
    private String id;
    @Json(name = "subject")
    private String subject;
    @Json(name = "message_count")
    private int totalMsgCount;
    @Json(name = "unread_message_count")
    private int unreadMessageCount;
    @Json(name = "updated_at")
    private SafeCalendar updatedAt;
    @Json(name = "read_only")
    private boolean isReadOnly;
    @Json(name = "participants")
    private List<XingUser> participants;
    @Json(name = "latest_messages")
    public List<ConversationMessage> latestMessages;

    @Override
    public boolean equals(Object obj) {
        Conversation c;
        if (obj instanceof Conversation) {
            c = (Conversation) obj;
        } else {
            return false;
        }

        return (id.equals(c.id));
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Conversation{"
              + "id='" + id + '\''
              + ", subject='" + subject + '\''
              + ", totalMsgCount='" + totalMsgCount + '\''
              + ", unreadMessageCount='" + unreadMessageCount + '\''
              + ", updatedAt='" + updatedAt + '\''
              + ", isReadOnly='" + isReadOnly + '\''
              + ", participants='" + participants + '\''
              + ", latestMessages='" + latestMessages + '\''
              + '}';
    }

    public String id() {
        return id;
    }

    public Conversation id(String id) {
        this.id = id;
        return this;
    }

    public String subject() {
        return subject;
    }

    public Conversation subject(String subject) {
        this.subject = subject;
        return this;
    }

    public int totalMsgCount() {
        return totalMsgCount;
    }

    public Conversation totalMsgCount(int totalMsgCount) {
        this.totalMsgCount = totalMsgCount;
        return this;
    }

    public int unreadMessageCount() {
        return unreadMessageCount;
    }

    public Conversation unreadMessageCount(int unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
        return this;
    }

    public SafeCalendar updatedAt() {
        return updatedAt;
    }

    public Conversation updatedAt(SafeCalendar updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public Conversation isReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
        return this;
    }

    public List<XingUser> participants() {
        return participants;
    }

    public Conversation participants(List<XingUser> participants) {
        this.participants = participants;
        return this;
    }

    public boolean readOnly() {
        return isReadOnly;
    }

    public Conversation readOnly(boolean readOnly) {
        isReadOnly = readOnly;
        return this;
    }

    public List<ConversationMessage> latestMessages() {
        return latestMessages;
    }

    public Conversation latestMessages(List<ConversationMessage> latestMessages) {
        this.latestMessages = latestMessages;
        return this;
    }
}
