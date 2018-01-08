/*
 * Copyright (С) 2016 XING SE (http://xing.com/)
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
import com.xing.api.data.profile.XingUser
import java.io.Serializable

/**
 * Represents the invitation stats response for the invite by mail call.
 *
 * @author daniel.hartwich
 * @see ['Send Invitations' resource page.](https://dev.xing.com/docs/post/users/invite)
 */
data class InvitationStats(
        /** Returns the total number of addresses received by the api.  */
        @Json(name = "total_addresses")
        val totalAddresses: Int,

        /** Returns the number of invitation sent.  */
        @Json(name = "invitations_sent")
        val invitationsSent: Int,

        /** Returns the list of emails of users that are already invited.  */
        @Json(name = "already_invited")
        val alreadyInvited: List<String>,

        /** Returns the list of users that are already XING members.  */
        @Json(name = "already_member")
        val alreadyMember: List<XingUser>,

        /** Returns the a list of invalid emails.  */
        @Json(name = "invalid_addresses")
        val invalidAddresses: List<String>
) : Serializable {

    companion object {
        private const val serialVersionUID = 2L
    }
}
