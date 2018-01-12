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
import java.io.Serializable

/**
 * An Attachment of the Message.
 *
 * @see <a href="https://dev.xing.com/docs/resources#messages">Messages Resource</a>
 */
data class MessageAttachment(
        @Json(name = "id")
        val id: String,

        @Json(name = "created_at")
        val createdAt: SafeCalendar,

        @Json(name = "filename")
        val fileName: String,

        @Json(name = "mime_type")
        val mimeType: String,

        @Json(name = "size")
        val fileSize: Int
) : Serializable {

    override fun equals(other: Any?): Boolean = other is MessageAttachment && id == other.id

    override fun hashCode(): Int = id.hashCode()

    companion object {
        private const val serialVersionUID = 1L
    }
}
