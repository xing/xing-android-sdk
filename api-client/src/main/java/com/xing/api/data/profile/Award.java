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
package com.xing.api.data.profile;

import com.squareup.moshi.Json;
import com.xing.api.data.SafeCalendar;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Represents an award object that can be present in a user's <strong>professional experience</strong> field.
 *
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/get/users/:id">User Profile Resource</a>
 */
@SuppressWarnings("unused")
public class Award implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Name of the award. */
    @Json(name = "name")
    private final String name;
    /** The XING API returns only the year, but for compatibility with possible future changes we use Calendar. */
    @Json(name = "date_awarded")
    private final SafeCalendar dateAwarded;
    /** URL of the award. */
    @Json(name = "url")
    private final String url;

    public Award(String name, SafeCalendar dateAwarded, String url) {
        this.name = name;
        this.dateAwarded = dateAwarded;
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Award award = (Award) o;

        return (name != null ? name.equals(award.name) : award.name == null)
              && (dateAwarded != null ? dateAwarded.equals(award.dateAwarded) : award.dateAwarded == null)
              && (url != null ? url.equals(award.url) : award.url == null);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (dateAwarded != null ? dateAwarded.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Award{"
              + "name='" + name + '\''
              + ", dateAwarded=" + dateAwarded
              + ", url='" + url + '\''
              + '}';
    }

    /** Returns the name of the award. */
    public String name() {
        return name;
    }

    /** Returns the year of the award. If the award year is not set, <strong>-1</strong> will be returned. */
    public int yearOfAward() {
        return dateAwarded != null ? dateAwarded.get(Calendar.YEAR) : -1;
    }

    /** Returns the year of award as a {@link String} if set, otherwise {@code null}. */
    public String yearOfAwardAsString() {
        return dateAwarded != null ? Integer.toString(dateAwarded.get(Calendar.YEAR)) : null;
    }

    /** Returns the url of the award if present. */
    public String url() {
        return url;
    }
}
