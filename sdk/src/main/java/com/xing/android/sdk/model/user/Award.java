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
package com.xing.android.sdk.model.user;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.moshi.Json;
import com.xing.android.sdk.model.XingCalendar;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Represents an award of a user.
 * <p/>
 *
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/get/users/:id">User Profile</a>
 */
@SuppressWarnings("unused")
public class Award implements Serializable, Parcelable {
    private static final long serialVersionUID = 1792717237671423281L;

    /** Creator object for Parcelable contract. */
    public static final Creator<Award> CREATOR = new Creator<Award>() {
        @Override
        public Award createFromParcel(Parcel parcel) {
            return new Award(parcel);
        }

        @Override
        public Award[] newArray(int size) {
            return new Award[size];
        }
    };

    /** Name of the award. */
    @Json(name = "name")
    private String name;
    /** XWS returns only the year, but for compatibility with possible future changes we use Calendar. */
    @Json(name = "date_awarded")
    private XingCalendar dateAwarded;
    /** URL of the award. */
    @Json(name = "url")
    private String url;

    /** Create a simple Award object with empty fields. */
    public Award() {
    }

    /**
     * Create {@link Award} from {@link Parcel}.
     *
     * @param in Input {@link Parcel}
     */
    private Award(@NonNull Parcel in) {
        name = in.readString();
        dateAwarded = (XingCalendar) in.readSerializable();
        url = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Award)) {
            return false;
        }

        Award award = (Award) obj;
        return hashCode() == award.hashCode();
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (dateAwarded != null ? dateAwarded.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeSerializable(dateAwarded);
        parcel.writeString(url);
    }

    /**
     * Return name of award.
     *
     * @return Name of award.
     */
    public String getName() {
        return name;
    }

    /**
     * Set name of award.
     *
     * @param name award name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the year of award.
     *
     * @return The year int value, if the award year is not set -1.
     */
    public int getYearOfDateAwarded() {
        return dateAwarded != null ? dateAwarded.get(Calendar.YEAR) : -1;
    }

    /**
     * Return the year of award.
     *
     * @return The year as a string, if the year is not set will return null.
     */
    @Nullable
    public String getYearOfDateAwardedAsString() {
        return dateAwarded != null ? Integer.toString(dateAwarded.get(Calendar.YEAR)) : null;
    }

    /**
     * Set date of award.
     *
     * @param dateAwarded Date of award as calendar object.
     */
    public void setDateAwarded(@NonNull XingCalendar dateAwarded) {
        this.dateAwarded = dateAwarded;
    }

    /**
     * Set date of award.
     *
     * @param year The year as an int.
     */
    public void setDateAwarded(int year) {
        dateAwarded = new XingCalendar(year, 0, 1);
    }

    /**
     * Return url of award.
     *
     * @return award url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set url of award.
     *
     * @param url URL of award as string.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Return url of award.
     *
     * @return award url as a string.
     */
    @Nullable
    public String getUrlAsString() {
        return url != null ? url : null;
    }
}
