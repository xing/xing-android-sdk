/*
 * Copyright (c) 2015 XING AG (http://xing.com/)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.xing.android.sdk.model.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.xing.android.sdk.model.CalendarUtils;
import com.xing.android.sdk.model.XingCalendar;

import java.io.Serializable;

/**
 * Represents a contact request.
 *
 * @author ciprian.ursu
 * @see <a href="https://dev.xing.com/docs/get/users/:user_id/contact_requests">Contact Request</a>
 */
@SuppressWarnings("unused")
public class ContactPetition implements Serializable, Parcelable {
    private static final long serialVersionUID = 1792283724323281L;
    /** Creator object for the Parcelable contract. */
    public static final Creator<ContactPetition> CREATOR = new Creator<ContactPetition>() {
        @Override
        public ContactPetition createFromParcel(Parcel source) {
            return new ContactPetition(source);
        }

        @Override
        public ContactPetition[] newArray(int size) {
            return new ContactPetition[size];
        }
    };

    /** ID of sender. */
    private String senderId;
    /** Sender user object. */
    private XingUser sender;
    /** Message from sender. */
    private String message;
    /** Date of contact request. */
    private XingCalendar receivedAt;

    /** Create a simple Contact object with empty fields. */
    public ContactPetition() {
    }

    /**
     * Create {@link ContactPetition} from {@link Parcel}.
     *
     * @param in Input {@link Parcel}
     */
    private ContactPetition(Parcel in) {
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

        if (!(obj instanceof ContactPetition)) {
            return false;
        }

        ContactPetition contactPetition = (ContactPetition) obj;
        return ((contactPetition.senderId != null) && (senderId != null)) && contactPetition.senderId.equals(senderId);
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
