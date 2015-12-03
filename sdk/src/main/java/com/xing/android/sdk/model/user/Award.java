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

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
    private String name;
    /** XWS returns only the year, but for compatibility with possible future changes we use Calendar. */
    private Calendar dateAwarded;
    /** URL of the award. */
    private Uri url;

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
        dateAwarded = (Calendar) in.readSerializable();
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
        parcel.writeParcelable(url, 0);
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
    public void setDateAwarded(@NonNull Calendar dateAwarded) {
        this.dateAwarded = dateAwarded;
    }

    /**
     * Set date of award.
     *
     * @param year The year as an int.
     */
    public void setDateAwarded(int year) {
        dateAwarded = new GregorianCalendar(year, 0, 1);
    }

    /**
     * Return url of award.
     *
     * @return award url.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * Set url of award.
     *
     * @param url URL of award as string.
     */
    public void setUrl(String url) {
        this.url = Uri.parse(url);
    }

    /**
     * Return url of award.
     *
     * @return award url as a string.
     */
    @Nullable
    public String getUrlAsString() {
        return url != null ? url.toString() : null;
    }
}
