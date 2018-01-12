/*
 * Copyright (С) 2018 XING SE (http://xing.com/)
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

/**
 * Represents a company's discipline.
 *
 * @see <a href="https://dev.xing.com/docs/get/users/:id">User Profile Resource</a>
 */
public class Discipline implements Serializable {
    @Json(name = "id")
    private final String id;
    @Json(name = "key")
    private final String key;

    public Discipline(String id, String key) {
        this.id = id;
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Discipline discipline = (Discipline) o;

        return (id != null ? id.equals(discipline.id) : discipline.id == null)
              && (key != null ? key.equals(discipline.key) : discipline.key == null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Discipline{"
              + "id='" + id + '\''
              + ", key='" + key + '\''
              + '}';
    }

    /** Returns discipline id. */
    public String id() {
        return id;
    }

    /** Returns discipline key. */
    public String key() {
        return key;
    }
}
