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
package com.xing.api.model.user;

import com.squareup.moshi.Json;
import com.xing.api.model.SafeCalendar;

import java.io.Serializable;

/**
 * Represents a contact request.
 *
 * TODO Move this to appropriate package.
 *
 * @author ciprian.ursu
 * @see <a href="https://dev.xing.com/docs/get/users/:user_id/contact_requests">Contact Request</a>
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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String id) {
        senderId = id;
    }

    public SafeCalendar getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(SafeCalendar receivedAt) {
        this.receivedAt = receivedAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public XingUser getSender() {
        return sender;
    }

    public void setSender(XingUser sender) {
        this.sender = sender;
    }
}
