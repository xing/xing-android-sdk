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
package com.xing.api.model;

import com.squareup.moshi.Json;
import com.xing.api.model.user.XingUser;

import java.util.List;

/**
 * @author daniel.hartwich
 */
public class ContactPaths {
    @Json(name = "paths")
    private List<ContactPath> contactPaths;
    @Json(name = "distance")
    private int distance;
    @Json(name = "total")
    private int total;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    private static final class ContactPath {
        @Json(name = "users")
        private List<XingUser> userList;

        public List<XingUser> getUserList() {
            return userList;
        }

        public void setUserList(List<XingUser> userList) {
            this.userList = userList;
        }
    }
}

