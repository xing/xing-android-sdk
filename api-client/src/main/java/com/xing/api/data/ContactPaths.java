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
package com.xing.api.data;

import com.squareup.moshi.Json;
import com.xing.api.data.profile.XingUser;

import java.io.Serializable;
import java.util.List;

/**
 * @author daniel.hartwich
 */
public class ContactPaths implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "paths")
    private List<ContactPath> contactPaths;
    @Json(name = "distance")
    private int distance;
    @Json(name = "total")
    private int total;

    public List<ContactPath> contactPaths() {
        return contactPaths;
    }

    public ContactPaths contactPaths(List<ContactPath> contactPaths) {
        this.contactPaths = contactPaths;
        return this;
    }

    public int distance() {
        return distance;
    }

    public ContactPaths distance(int distance) {
        this.distance = distance;
        return this;
    }

    public int total() {
        return total;
    }

    public ContactPaths total(int total) {
        this.total = total;
        return this;
    }

    public static final class ContactPath {
        @Json(name = "users")
        private final List<XingUser> contacts;

        public ContactPath(List<XingUser> contacts) {
            this.contacts = contacts;
        }

        public List<XingUser> contacts() {
            return contacts;
        }
    }
}

