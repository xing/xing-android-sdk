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
 * Java representation of a Message in a Conversation.
 */
public class ConversationMessage implements Serializable {
    private static final long serialVersionUID = -4307893362880528921L;

    @Json(name = "id")
    private String messageId;
    @Json(name = "created_at")
    private SafeCalendar createdAt;
    @Json(name = "content")
    private String content;
    @Json(name = "read")
    private boolean isRead;
    @Json(name = "sender")
    private XingUser sender;
    @Json(name = "attachments")
    private List<MessageAttachment> attachments;

    @Override
    public boolean equals(Object obj) {
        ConversationMessage m;
        if (obj instanceof ConversationMessage) {
            m = (ConversationMessage) obj;
        } else {
            return false;
        }

        return (messageId.equals(m.messageId));
    }

    @Override
    public int hashCode() {
        return messageId != null ? messageId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ConversationMessage{"
              + "messageId='" + messageId + '\''
              + ", createdAt='" + createdAt + '\''
              + ", content='" + content + '\''
              + ", isRead='" + isRead + '\''
              + ", sender='" + sender + '\''
              + ", attachments='" + attachments + '\''
              + '}';
    }

    public String messageId() {
        return messageId;
    }

    public ConversationMessage messageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public SafeCalendar createdAt() {
        return createdAt;
    }

    public ConversationMessage createdAt(SafeCalendar createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String content() {
        return content;
    }

    public ConversationMessage content(String content) {
        this.content = content;
        return this;
    }

    public boolean isRead() {
        return isRead;
    }

    public ConversationMessage isRead(boolean isRead) {
        this.isRead = isRead;
        return this;
    }

    public XingUser sender() {
        return sender;
    }

    public ConversationMessage sender(XingUser sender) {
        this.sender = sender;
        return this;
    }

    public List<MessageAttachment> attachments() {
        return attachments;
    }

    public ConversationMessage attachments(List<MessageAttachment> attachments) {
        this.attachments = attachments;
        return this;
    }
}
