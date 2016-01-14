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
package com.xing.api.data.contact;

import com.squareup.moshi.Json;
import com.xing.api.data.profile.XingUser;
import com.xing.api.internal.json.ContactPath;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a data model for contact paths between two XING {@linkplain XingUser users}.
 *
 * @author daniel.hartwich
 * @see <a href="https://dev.xing.com/docs/get/users/:user_id/network/:other_user_id/paths">'Contact Paths' resource
 * page.</a>
 */
public class ContactPaths implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "paths")
    @ContactPath // TODO may be it makes sense to put this burden on CompositeType and add a complimentary annotation.
    private final List<List<XingUser>> paths;
    @Json(name = "distance")
    private final int distance;
    @Json(name = "total")
    private final int total;

    public ContactPaths(List<List<XingUser>> paths, int distance, int total) {
        this.paths = paths;
        this.distance = distance;
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactPaths other = (ContactPaths) o;

        return distance == other.distance
              && total == other.total
              && (paths != null ? paths.equals(other.paths) : other.paths == null);
    }

    @Override
    public int hashCode() {
        int result = paths != null ? paths.hashCode() : 0;
        result = 31 * result + distance;
        result = 31 * result + total;
        return result;
    }

    @Override
    public String toString() {
        return "ContactPaths{"
              + "paths=" + paths
              + ", distance=" + distance
              + ", total=" + total
              + '}';
    }

    /** Returns a list of available paths, which are represented as a list of {@linkplain XingUser users}. */
    public List<List<XingUser>> paths() {
        return paths;
    }

    /** Returns the smallest distance between the users. */
    public int distance() {
        return distance;
    }

    /** Returns the total number of available paths. */
    public int total() {
        return total;
    }
}
