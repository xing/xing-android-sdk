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

import java.io.Serializable;

/**
 * Represents a pending contact request of the authorizing user.
 *
 * @author daniel.hartwich
 * @see <a href="https://dev.xing.com/docs/get/users/:user_id/contact_requests/sent">'Pending Contact Request' resource
 * page.</a>
 */
@AutoValue
public abstract class PendingContactRequest implements Serializable {
    private static final long serialVersionUID = 2L;

    public static JsonAdapter<PendingContactRequest> jsonAdapter(Moshi moshi) {
        return AutoValue_PendingContactRequest.jsonAdapter(moshi);
    }

    public static PendingContactRequest create(String senderId, String recipientId) {
        return new AutoValue_PendingContactRequest(senderId, recipientId);
    }

    /** Returns the id of the user who sent the contact request. */
    @Json(name = "sender_id")
    public abstract String senderId();

    /** Returns the contact request recipient. */
    @Json(name = "recipient_id")
    public abstract String recipientId();
}
