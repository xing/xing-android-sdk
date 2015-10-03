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

import java.io.Serializable;

/**
 * Timezone.
 *
 * @author serj.lotutovici
 */
public class TimeZone implements Serializable, Parcelable {
    private static final long serialVersionUID = -9131199441954044707L;
    /** Creator object for Parcelable contract. */
    public static final Creator<TimeZone> CREATOR = new Creator<TimeZone>() {
        @Override
        public TimeZone createFromParcel(Parcel source) {
            return new TimeZone(source);
        }

        @Override
        public TimeZone[] newArray(int size) {
            return new TimeZone[size];
        }
    };

    /** Name of timezone. */
    private String mName;
    /** Offset. */
    private float mUtcOffset;

    /** Create a simple TimeZone object with empty fields. */
    public TimeZone() {
    }

    /**
     * Create {@link TimeZone} from {@link Parcel}.
     *
     * @param in Input {@link Parcel}
     */
    private TimeZone(Parcel in) {
        this.mName = in.readString();
        this.mUtcOffset = in.readFloat();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeZone)) {
            return false;
        }

        TimeZone timeZone = (TimeZone) o;

        return Float.compare(timeZone.mUtcOffset, mUtcOffset) == 0 && !(mName != null ? !mName.equals(timeZone.mName)
                : timeZone.mName != null);
    }

    @Override
    public int hashCode() {
        int result = mName != null ? mName.hashCode() : 0;
        result = 31 * result + (mUtcOffset != +0.0f ? Float.floatToIntBits(mUtcOffset) : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeFloat(this.mUtcOffset);
    }

    /**
     * Return name of timezone.
     *
     * @return timezone name.
     */
    public String getName() {
        return mName;
    }

    /**
     * Set name of timezone.
     *
     * @param name timezone.
     */
    public void setName(String name) {
        mName = name;
    }

    /**
     * Return UTC offset.
     *
     * @return UTC offset.
     */
    public float getUtcOffset() {
        return mUtcOffset;
    }

    /**
     * Set UTC offset.
     *
     * @param utcOffset UTC offset.
     */
    public void setUtcOffset(float utcOffset) {
        mUtcOffset = utcOffset;
    }
}
