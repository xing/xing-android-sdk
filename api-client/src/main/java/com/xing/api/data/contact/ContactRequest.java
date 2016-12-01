/*
 * Copyright (С) 2016 XING AG (http://xing.com/)
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
import com.xing.api.data.SafeCalendar;
import com.xing.api.data.profile.XingUser;

import java.io.Serializable;

/**
 * Represents a contact request.
 *
 * @author ciprian.ursu
 * @see <a href="https://dev.xing.com/docs/get/users/:user_id/contact_requests">'Contact Request' resource page.</a>
 */
@AutoValue
public abstract class ContactRequest implements Serializable {
    private static final long serialVersionUID = 2L;

    public static JsonAdapter<ContactRequest> jsonAdapter(Moshi moshi) {
        return AutoValue_ContactRequest.jsonAdapter(moshi);
    }

    /** Returns the sender id. */
    @Json(name = "sender_id")
    public abstract String senderId();

    /** Returns the contact request sender profile. */
    @Json(name = "sender")
    public abstract XingUser sender();

    /** Returns the contact request message. */
    @Json(name = "message")
    public abstract String message();

    /** Returns the date the contact request was sent. */
    @Json(name = "received_at")
    public abstract SafeCalendar receivedAt();

    public static Builder builder() {
        return new AutoValue_ContactRequest.Builder();
    }

    @AutoValue.Builder
    abstract static class Builder {
        abstract Builder senderId(String senderId);

        abstract Builder sender(XingUser sender);

        abstract Builder message(String message);

        abstract Builder receivedAt(SafeCalendar receivedAt);

        abstract ContactRequest build();
    }
}
