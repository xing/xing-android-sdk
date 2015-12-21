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

import com.squareup.moshi.Json;

import java.io.Serializable;

/**
 * Timezone.
 *
 * @author serj.lotutovici
 */
public class TimeZone implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Name of timezone. */
    @Json(name = "name")
    private String name;
    /** Offset. */
    @Json(name = "utc_offset")
    private float utcOffset;

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
