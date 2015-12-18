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
import com.xing.api.model.XingCalendar;

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
