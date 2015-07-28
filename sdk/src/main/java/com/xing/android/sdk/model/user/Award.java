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

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Represents an award of a user.
 * <p/>
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/get/users/:id">User Profile</a>
 */
@SuppressWarnings("unused")
public class Award implements Serializable, Parcelable {

    private static final long serialVersionUID = 1792717237671423281L;

    /**
     * Creator object for Parcelable contract
     */
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

    /**
     * Name of the award.
     */
    private String mName;
    /**
     * XWS returns only the year, but for compatibility with possible future changes we use Calendar.
     */
    private Calendar mDateAwarded;
    /**
     * URL of the award.
     */
    private Uri mUrl;

    /**
     * Create a simple Award object with empty fields.
     */
    public Award() {
    }

    /**
     * Create {@link Award} from {@link Parcel}
     *
     * @param in Input {@link Parcel}
     */
    private Award(@NonNull final Parcel in) {
        mName = in.readString();
        mDateAwarded = (Calendar) in.readSerializable();
        mUrl = in.readParcelable(Uri.class.getClassLoader());
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
        int result = mName != null ? mName.hashCode() : 0;
        result = 31 * result + (mDateAwarded != null ? mDateAwarded.hashCode() : 0);
        result = 31 * result + (mUrl != null ? mUrl.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mName);
        parcel.writeSerializable(mDateAwarded);
        parcel.writeParcelable(mUrl, 0);
    }

    /**
     * Return name of award.
     *
     * @return Name of award.
     */
    public String getName() {
        return mName;
    }

    /**
     * Set name of award.
     *
     * @param name award name.
     */
    public void setName(String name) {
        mName = name;
    }

    /**
     * Return the year of award.
     *
     * @return The year int value, if the award year is not set -1.
     */
    public int getYearOfDateAwarded() {
        return mDateAwarded != null ? mDateAwarded.get(Calendar.YEAR) : -1;
    }

    /**
     * Return the year of award.
     *
     * @return The year as a string, if the year is not set will return null.
     */
    public String getYearOfDateAwardedAsString() {
        return mDateAwarded != null ? Integer.toString(mDateAwarded.get(Calendar.YEAR)) : null;
    }

    /**
     * Set date of award.
     *
     * @param dateAwarded Date of award as calendar object.
     */
    public void setDateAwarded(@NonNull final Calendar dateAwarded) {
        mDateAwarded = dateAwarded;
    }

    /**
     * Set date of award.
     *
     * @param year The year as an int.
     */
    public void setDateAwarded(final int year) {
        mDateAwarded = new GregorianCalendar(year, 0, 1);
    }

    /**
     * Return url of award.
     *
     * @return award url.
     */
    public Uri getUrl() {
        return mUrl;
    }

    /**
     * Set url of award.
     *
     * @param url URL of award as string.
     */
    public void setUrl(String url) {
        mUrl = Uri.parse(url);
    }

    /**
     * Return url of award.
     *
     * @return award url as a string.
     */
    public String getUrlAsString() {
        return mUrl != null ? mUrl.toString() : null;
    }
}
