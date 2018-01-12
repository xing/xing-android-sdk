/*
 * Copyright (C) 2018 XING SE (http://xing.com/)
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
package com.xing.api.data.messages

import com.squareup.moshi.Json
import com.xing.api.data.SafeCalendar
import com.xing.api.data.profile.XingUser
import java.io.Serializable

/**
 * A Message in a Conversation.
 *
 * @see <a href="https://dev.xing.com/docs/resources#messages">Messages Resource</a>
 */
data class ConversationMessage(
        @Json(name = "id")
        val messageId: String,

        @Json(name = "created_at")
        val createdAt: SafeCalendar,

        @Json(name = "content")
        val content: String,

        @Json(name = "read")
        val isRead: Boolean,

        @Json(name = "sender")
        val sender: XingUser,

        @Json(name = "attachments")
        val attachments: List<MessageAttachment>?
) : Serializable {

    override fun equals(other: Any?): Boolean = other is ConversationMessage && messageId == other.messageId

    override fun hashCode(): Int = messageId.hashCode()

    companion object {
        private const val serialVersionUID = -4307893362880528921L
    }
}
