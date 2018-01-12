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
package com.xing.api.data.contact

import com.squareup.moshi.Json
import java.io.Serializable

/**
 * Represents a pending contact request of the authorizing user.
 *
 * @see ['Pending Contact Request' resource page.](https://dev.xing.com/docs/get/users/:user_id/contact_requests/sent)
 */
data class PendingContactRequest(
        /** Returns the id of the user who sent the contact request.  */
        @Json(name = "sender_id")
        val senderId: String,

        /** Returns the contact request recipient.  */
        @Json(name = "recipient_id")
        val recipientId: String
) : Serializable {

    companion object {
        private const val serialVersionUID = 2L
    }
}
