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

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

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
    @Json(name = "name")
    private String name;
    /** Offset. */
    @Json(name = "utc_offset")
    private float utcOffset;

    /** Create a simple TimeZone object with empty fields. */
    public TimeZone() {
    }

    /**
     * Create {@link TimeZone} from {@link Parcel}.
     *
     * @param in Input {@link Parcel}
     */
    private TimeZone(Parcel in) {
        name = in.readString();
        utcOffset = in.readFloat();
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

        return Float.compare(timeZone.utcOffset, utcOffset) == 0 && (name != null ? name.equals(timeZone.name)
              : timeZone.name == null);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (utcOffset != +0.0f ? Float.floatToIntBits(utcOffset) : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeFloat(utcOffset);
    }

    /**
     * Return name of timezone.
     *
     * @return timezone name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set name of timezone.
     *
     * @param name timezone.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return UTC offset.
     *
     * @return UTC offset.
     */
    public float getUtcOffset() {
        return utcOffset;
    }

    /**
     * Set UTC offset.
     *
     * @param utcOffset UTC offset.
     */
    public void setUtcOffset(float utcOffset) {
        this.utcOffset = utcOffset;
    }
}
