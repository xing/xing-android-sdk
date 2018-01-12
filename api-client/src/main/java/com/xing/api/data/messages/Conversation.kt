/*
 * Copyright (С) 2018 XING SE (http://xing.com/)
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
 * A Conversation.
 *
 * @see <a href="https://dev.xing.com/docs/resources#messages">Messages Resource</a>
 */
data class Conversation(
        @Json(name = "id")
        val id: String,

        @Json(name = "subject")
        val subject: String,

        @Json(name = "message_count")
        val totalMsgCount: Int,

        @Json(name = "unread_message_count")
        val unreadMessageCount: Int,

        @Json(name = "updated_at")
        val updatedAt: SafeCalendar,

        @Json(name = "read_only")
        val isReadOnly: Boolean,

        @Json(name = "participants")
        val participants: List<XingUser>,

        @Json(name = "latest_messages")
        val latestMessages: List<ConversationMessage>?
) : Serializable {

    override fun equals(other: Any?): Boolean = other is Conversation && id == other.id

    override fun hashCode(): Int = id.hashCode()

    companion object {
        private const val serialVersionUID = 1L
    }
}
