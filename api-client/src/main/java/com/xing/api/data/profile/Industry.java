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

import android.support.annotation.Nullable;

import com.squareup.moshi.Json;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Represent the industry contained in a {@link Company}.
 *
 * @author angelo.marchesin
 * @see <a href="https://dev.xing.com/docs/get/misc/industries">List of Industries Resource</a>
 */
@SuppressWarnings("unused") // Public api.
public class Industry implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final float FIRST_CATEGORY_CONVERSION = 10000f;

    public static Industry newInstanceFromCompoundId(int compoundId) {
        Industry segment = new Industry(compoundId, "");
        return new Industry(extractFirstCategoryIndex(compoundId), "", Collections.singletonList(segment));
    }

    static int extractFirstCategoryIndex(int encodedId) {
        return (int) (Math.floor(encodedId / FIRST_CATEGORY_CONVERSION) * FIRST_CATEGORY_CONVERSION);
    }

    @Json(name = "id")
    private final int id;
    @Json(name = "localized_name")
    private final String name;
    @Json(name = "segments")
    private final List<Industry> segments;

    /** Construct an industry with with no segments. */
    public Industry(int id, String name) {
        this(id, name, null);
    }

    /** Construct an industry with it's list of segments. */
    public Industry(int id, String name, @Nullable List<Industry> segments) {
        this.id = id;
        this.name = name;
        this.segments = segments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Industry industry = (Industry) o;

        return id == industry.id
              && (name != null ? name.equals(industry.name) : industry.name == null)
              && (segments != null ? segments.equals(industry.segments) : industry.segments == null);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (segments != null ? segments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Industry{"
              + "id=" + id
              + ", name='" + name + '\''
              + ", segments=" + segments
              + '}';
    }

    /** Returns the industry id. */
    public int getId() {
        return id;
    }

    /** Returns the industry name. */
    public String getName() {
        return name;
    }

    /** Returns the industry segments (a list of sub-industries). */
    @Nullable
    public List<Industry> getSegments() {
        return segments;
    }
}
