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
package com.xing.api.data.contact;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.xing.api.data.profile.XingUser;

import java.io.Serializable;
import java.util.List;

/**
 * Represents the invitation stats response for the invite by mail call.
 *
 * @author daniel.hartwich
 * @see <a href="https://dev.xing.com/docs/post/users/invite">'Send Invitations' resource page.</a>
 */
@AutoValue
public abstract class InvitationStats implements Serializable {
    private static final long serialVersionUID = 2L;

    public static JsonAdapter<InvitationStats> jsonAdapter(Moshi moshi) {
        return AutoValue_InvitationStats.jsonAdapter(moshi);
    }

    /** Returns the total number of addresses received by the api. */
    @Json(name = "total_addresses")
    public abstract int totalAddresses();

    /** Returns the number of invitation sent. */
    @Json(name = "invitations_sent")
    public abstract int invitationsSent();

    /** Returns the list of emails of users that are already invited. */
    @Json(name = "already_invited")
    public abstract List<String> alreadyInvited();

    /** Returns the list of users that are already XING members. */
    @Json(name = "already_member")
    public abstract List<XingUser> alreadyMember();

    /** Returns the a list of invalid emails. */
    @Json(name = "invalid_addresses")
    public abstract List<String> invalidAddresses();
}
