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

import com.squareup.moshi.Json;
import com.xing.android.sdk.model.XingCalendar;

import java.io.Serializable;

/**
 * Represents the profile message of a user.
 *
 * @author serj.lotutovici
 * @see <a href="https://dev.xing.com/docs/get/users/:user_id/profile_message">User Profile Message</a>
 */
public class ProfileMessage implements Serializable, Parcelable {
    private static final long serialVersionUID = -132857097764920225L;
    /** Creator object for Parcelable contract. */
    public static final Creator<ProfileMessage> CREATOR = new Creator<ProfileMessage>() {
        @Override
        public ProfileMessage createFromParcel(Parcel source) {
            return new ProfileMessage(source);
        }

        @Override
        public ProfileMessage[] newArray(int size) {
            return new ProfileMessage[size];
        }
    };

    /** Date message was updated. */
    @Json(name = "updated_at")
    private XingCalendar updatedAt;
    /** Message. */
    @Json(name = "message")
    private String message;

    /** Create a simple Profile Message object with empty fields. */
    public ProfileMessage() {
    }

    /**
     * Create {@link ProfileMessage} from {@link Parcel}.
     *
     * @param in Input {@link Parcel}
     */
    private ProfileMessage(Parcel in) {
        updatedAt = (XingCalendar) in.readSerializable();
        message = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(updatedAt);
        dest.writeString(message);
    }

    /**
     * Return date message was updated.
     *
     * @return date message updated.
     */
    public XingCalendar getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Set date message is updated.
     *
     * @param updatedAt date message updated.
     */
    public void setUpdatedAt(XingCalendar updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Return message.
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set message.
     *
     * @param message message.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
