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
    private String mSenderId;
    /** Sender user object. */
    private XingUser mSender;
    /** Message from sender. */
    private String mMessage;
    /** Date of contact request. */
    private XingCalendar mReceivedAt;

    /** Create a simple Contact object with empty fields. */
    public ContactPetition() {
    }

    /**
     * Create {@link ContactPetition} from {@link Parcel}.
     *
     * @param in Input {@link Parcel}
     */
    private ContactPetition(Parcel in) {
        mSenderId = in.readString();
        mSender = (XingUser) in.readSerializable();
        mMessage = in.readString();
        mReceivedAt = (XingCalendar) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(mSenderId);
        dest.writeSerializable(mSender);
        dest.writeString(mMessage);
        dest.writeSerializable(mReceivedAt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof ContactPetition)) {
            return false;
        }

        final ContactPetition that = (ContactPetition) obj;
        return ((that.mSenderId != null) && (mSenderId != null)) && that.mSenderId.equals(mSenderId);
    }

    @Override
    public int hashCode() {
        int result = mSenderId != null ? mSenderId.hashCode() : 0;
        result = 31 * result + (mSender != null ? mSender.hashCode() : 0);
        result = 31 * result + (mMessage != null ? mMessage.hashCode() : 0);
        result = 31 * result + (mReceivedAt != null ? mReceivedAt.hashCode() : 0);
        return result;
    }

    /**
     * Return sender id.
     *
     * @return sender id.
     */
    public String getSenderId() {
        return mSenderId;
    }

    /**
     * Set sender id.
     *
     * @param id sender id.
     */
    public void setSenderId(final String id) {
        mSenderId = id;
    }

    /**
     * Return contact request date.
     *
     * @return Date of contact request.
     */
    public XingCalendar getReceivedAt() {
        return mReceivedAt;
    }

    /**
     * Set date of contact request.
     *
     * @param receivedAt Date of contact request.
     */
    public void setReceivedAt(final String receivedAt) {
        mReceivedAt = CalendarUtils.parseCalendarFromString(receivedAt);
    }

    /**
     * Return sender message.
     *
     * @return sender message.
     */
    public String getMessage() {
        return mMessage;
    }

    /**
     * Set sender message.
     *
     * @param message sender message.
     */
    public void setMessage(final String message) {
        mMessage = message;
    }

    /**
     * Return sender user object.
     *
     * @return sender user object.
     */
    public XingUser getSender() {
        return mSender;
    }

    /**
     * Set sender user object.
     *
     * @param sender sender user object.
     */
    public void setSender(final XingUser sender) {
        mSender = sender;
    }
}
