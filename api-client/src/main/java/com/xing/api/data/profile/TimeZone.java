/*
 * Copyright (С) 2016 XING SE (http://xing.com/)
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
package com.xing.api.data.profile;

import com.squareup.moshi.Json;

import java.io.Serializable;

/** Java representation of the time zone in which the {@linkplain XingUser user} is located. */
public class TimeZone implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Name of timezone. */
    @Json(name = "name")
    private final String name;
    /** Offset. */
    @Json(name = "utc_offset")
    private final double utcOffset;

    public TimeZone(String name, double utcOffset) {
        this.name = name;
        this.utcOffset = utcOffset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeZone)) return false;

        TimeZone timeZone = (TimeZone) o;

        return Double.compare(timeZone.utcOffset, utcOffset) == 0
              && (name != null ? name.equals(timeZone.name) : timeZone.name == null);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        temp = Double.doubleToLongBits(utcOffset);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "TimeZone{"
              + "name='" + name + '\''
              + ", utcOffset=" + utcOffset
              + '}';
    }

    /** Returns the  name of timezone. */
    public String name() {
        return name;
    }

    /** Returns the UTC offset. */
    public double utcOffset() {
        return utcOffset;
    }
}
