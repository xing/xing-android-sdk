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

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;
import com.xing.api.model.CalendarUtils;
import com.xing.api.model.XingCalendar;

import java.io.Serializable;

/**
 * Represents a contact request.
 *
 * @author ciprian.ursu
 * @see <a href="https://dev.xing.com/docs/get/users/:user_id/contact_requests">Contact Request</a>
 */
@SuppressWarnings("unused")
public class ContactRequest implements Serializable, Parcelable {
    private static final long serialVersionUID = 1792283724323281L;
    /** Creator object for the Parcelable contract. */
    public static final Creator<ContactRequest> CREATOR = new Creator<ContactRequest>() {
        @Override
        public ContactRequest createFromParcel(Parcel source) {
            return new ContactRequest(source);
        }

        @Override
        public ContactRequest[] newArray(int size) {
            return new ContactRequest[size];
        }
    };

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
    private XingCalendar receivedAt;

    /** Create a simple Contact object with empty fields. */
    public ContactRequest() {
    }

    /**
     * Create {@link ContactRequest} from {@link Parcel}.
     *
     * @param in Input {@link Parcel}
     */
    private ContactRequest(Parcel in) {
        senderId = in.readString();
        sender = (XingUser) in.readSerializable();
        message = in.readString();
        receivedAt = (XingCalendar) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(senderId);
        dest.writeSerializable(sender);
        dest.writeString(message);
        dest.writeSerializable(receivedAt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof ContactRequest)) {
            return false;
        }

        ContactRequest contactRequest = (ContactRequest) obj;
        return ((contactRequest.senderId != null) && (senderId != null)) && contactRequest.senderId.equals(senderId);
    }

    @Override
    public int hashCode() {
        int result = senderId != null ? senderId.hashCode() : 0;
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (receivedAt != null ? receivedAt.hashCode() : 0);
        return result;
    }

    /**
     * Return sender id.
     *
     * @return sender id.
     */
    public String getSenderId() {
        return senderId;
    }

    /**
     * Set sender id.
     *
     * @param id sender id.
     */
    public void setSenderId(String id) {
        senderId = id;
    }

    /**
     * Return contact request date.
     *
     * @return Date of contact request.
     */
    public XingCalendar getReceivedAt() {
        return receivedAt;
    }

    /**
     * Set date of contact request.
     *
     * @param receivedAt Date of contact request.
     */
    public void setReceivedAt(String receivedAt) {
        this.receivedAt = CalendarUtils.parseCalendarFromString(receivedAt);
    }

    /**
     * Return sender message.
     *
     * @return sender message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set sender message.
     *
     * @param message sender message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Return sender user object.
     *
     * @return sender user object.
     */
    public XingUser getSender() {
        return sender;
    }

    /**
     * Set sender user object.
     *
     * @param sender sender user object.
     */
    public void setSender(XingUser sender) {
        this.sender = sender;
    }
}
