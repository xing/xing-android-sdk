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
package com.xing.api.data.groups;

import com.squareup.moshi.Json;

import java.io.Serializable;
import java.util.List;

/**
 * Model of a forum that is returned by the <a href= https://dev.xing.com/docs/get/groups/:group_id/forums>Get Forums</a>
 * call.
 */
public class Forum implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "id")
    private String id;

    @Json(name = "name")
    private String name;

    @Json(name = "permissions")
    private List<ForumPermission> forumPermissions;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Forum forum = (Forum) object;

        if (id != null ? !id.equals(forum.id) : forum.id != null) return false;
        if (name != null ? !name.equals(forum.name) : forum.name != null) return false;
        return forumPermissions != null ? forumPermissions.equals(forum.forumPermissions) : forum.forumPermissions == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (forumPermissions != null ? forumPermissions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Forum{"
              + "id='" + id + '\''
              + ", name='" + name + '\''
              + ", forumPermissions=" + forumPermissions
              + '}';
    }

    public String id() {
        return id;
    }

    public Forum id(String id) {
        this.id = id;
        return this;
    }

    public String name() {
        return name;
    }

    public Forum name(String name) {
        this.name = name;
        return this;
    }

    public List<ForumPermission> permissions() {
        return forumPermissions;
    }

    public Forum permissions(List<ForumPermission> forumPermissions) {
        this.forumPermissions = forumPermissions;
        return this;
    }
}
