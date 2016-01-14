/*
 * Copyright (С) 2015 XING AG (http://xing.com/)
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
import com.xing.api.data.SafeCalendar;
import com.xing.api.data.profile.XingUser;

import java.io.Serializable;

/**
 * Represents a contact request.
 *
 * @author ciprian.ursu
 * @see <a href="https://dev.xing.com/docs/get/users/:user_id/contact_requests">'Contact Request' resource page.</a>
 */
@SuppressWarnings("unused")
public class ContactRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID of sender. */
    @Json(name = "sender_id")
    private String senderId;
    /** Sender user object. */
    @Json(name = "sender")
    private XingUser sender;
    /** Message from sender. */
    @Json(name = "message")
    private String message;
    /** Date of contact request. */
    @Json(name = "received_at")
    private SafeCalendar receivedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactRequest otherRequest = (ContactRequest) o;

        return (senderId != null ? senderId.equals(otherRequest.senderId)
              : otherRequest.senderId == null) && (sender != null ? sender.equals(otherRequest.sender)
              : otherRequest.sender == null) && (message != null ? message.equals(otherRequest.message)
              : otherRequest.message == null) && (receivedAt != null ? receivedAt.equals(otherRequest.receivedAt)
              : otherRequest.receivedAt == null);
    }

    @Override
    public int hashCode() {
        int result = senderId != null ? senderId.hashCode() : 0;
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (receivedAt != null ? receivedAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ContactRequest{"
              + "senderId='" + senderId + '\''
              + ", sender=" + sender
              + ", message='" + message + '\''
              + ", receivedAt=" + receivedAt
              + '}';
    }

    /** Returns the sender id. */
    public String senderId() {
        return senderId;
    }

    /** Sets the sender id. */
    public ContactRequest senderId(String id) {
        senderId = id;
        return this;
    }

    /** Returns the date the contact request was sent. */
    public SafeCalendar receivedAt() {
        return receivedAt;
    }

    /** Sets the date the contact request was received. */
    public ContactRequest receivedAt(SafeCalendar receivedAt) {
        this.receivedAt = receivedAt;
        return this;
    }

    /** Returns the contact request message. */
    public String message() {
        return message;
    }

    /** Sets the contact request message. */
    public ContactRequest message(String message) {
        this.message = message;
        return this;
    }

    /** Returns the contact request sender profile. */
    public XingUser sender() {
        return sender;
    }

    /** Sets the contact request sender profile. */
    public ContactRequest sender(XingUser sender) {
        this.sender = sender;
        return this;
    }
}
