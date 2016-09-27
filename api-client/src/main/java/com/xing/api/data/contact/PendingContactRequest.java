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

import com.squareup.moshi.Json;

import java.io.Serializable;

/**
 * Represents a pending contact request of the authorizing user.
 *
 * @author daniel.hartwich
 * @see <a href="https://dev.xing.com/docs/get/users/:user_id/contact_requests/sent">'Pending Contact Request' resource
 * page.</a>
 */
public class PendingContactRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "sender_id")
    private final String senderId;
    @Json(name = "recipient_id")
    private final String recipientId;

    public PendingContactRequest(String senderId, String recipientId) {
        this.senderId = senderId;
        this.recipientId = recipientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PendingContactRequest other = (PendingContactRequest) o;

        return (senderId != null ? senderId.equals(other.senderId) : other.senderId == null)
              && (recipientId != null ? recipientId.equals(other.recipientId) : other.recipientId == null);
    }

    @Override
    public int hashCode() {
        int result = senderId != null ? senderId.hashCode() : 0;
        result = 31 * result + (recipientId != null ? recipientId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PendingContactRequest{"
              + "senderId='" + senderId + '\''
              + ", recipientId='" + recipientId + '\''
              + '}';
    }

    /** Returns the id of the user who sent the contact request. */
    public String senderId() {
        return senderId;
    }

    /** Returns the contact request recipient. */
    public String recipientId() {
        return recipientId;
    }
}
