/*
 * Copyright (C) 2018 XING SE (http://xing.com/)
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

/**
 * Represents a bookmark response object. See {@linkplain com.xing.api.resources.BookmarksResource Bookmarks Resource}.
 */
public class Bookmark implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "created_at")
    private final SafeCalendar createdAt;
    @Json(name = "user")
    private final XingUser user;

    public Bookmark(XingUser user, SafeCalendar createdAt) {
        this.createdAt = createdAt;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bookmark bookmark = (Bookmark) o;
        return (createdAt != null ? createdAt.equals(bookmark.createdAt) : bookmark.createdAt == null)
              && (user != null ? user.equals(bookmark.user) : bookmark.user == null);
    }

    @Override
    public int hashCode() {
        int result = createdAt != null ? createdAt.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Bookmark{"
              + "createdAt=" + createdAt
              + ", user=" + user
              + '}';
    }

    /** Returns the date when the bookmark was created. */
    public SafeCalendar createdAt() {
        return createdAt;
    }

    /** Returns the bookmarked user. */
    public XingUser user() {
        return user;
    }
}
