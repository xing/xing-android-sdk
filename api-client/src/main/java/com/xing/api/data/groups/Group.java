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
package com.xing.api.data.groups;

import com.squareup.moshi.Json;

import java.io.Serializable;
import java.util.List;

/**
 * Model of a group that is returned by the <a href=https://dev.xing.com/docs/get/users/:user_id/groups></a> call.
 */
public class Group implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "id")
    private String id;
    @Json(name = "name")
    private String name;
    @Json(name = "description")
    private String description;
    @Json(name = "closed")
    private boolean closed;
    @Json(name = "logo_urls")
    private LogoUrls logoUrls;
    @Json(name = "member_count")
    private int memberCount;
    @Json(name = "post_count")
    private int postCount;
    @Json(name = "unread_posts")
    private int unreadPosts;
    @Json(name = "comment_count")
    private int commentCount;
    @Json(name = "user_state")
    private Membership userState;
    @Json(name = "latest_posts")
    private List<Post> latestPosts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (closed != group.closed) return false;
        if (memberCount != group.memberCount) return false;
        if (postCount != group.postCount) return false;
        if (unreadPosts != group.unreadPosts) return false;
        if (commentCount != group.commentCount) return false;
        if (id != null ? !id.equals(group.id) : group.id != null) return false;
        if (name != null ? !name.equals(group.name) : group.name != null) return false;
        if (description != null ? !description.equals(group.description) : group.description != null) return false;
        if (logoUrls != null ? !logoUrls.equals(group.logoUrls) : group.logoUrls != null) return false;
        if (userState != group.userState) return false;
        return latestPosts != null ? latestPosts.equals(group.latestPosts) : group.latestPosts == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (closed ? 1 : 0);
        result = 31 * result + (logoUrls != null ? logoUrls.hashCode() : 0);
        result = 31 * result + memberCount;
        result = 31 * result + postCount;
        result = 31 * result + unreadPosts;
        result = 31 * result + commentCount;
        result = 31 * result + (userState != null ? userState.hashCode() : 0);
        result = 31 * result + (latestPosts != null ? latestPosts.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Group{"
              + "id='" + id + '\''
              + ", name='" + name + '\''
              + ", description='" + description + '\''
              + ", closed=" + closed
              + ", logoUrls=" + logoUrls
              + ", memberCount=" + memberCount
              + ", postCount=" + postCount
              + ", unreadPosts=" + unreadPosts
              + ", commentCount=" + commentCount
              + ", userState=" + userState
              + ", latestPosts=" + latestPosts
              + '}';
    }

    public String id() {
        return id;
    }

    public Group id(String id) {
        this.id = id;
        return this;
    }

    public String name() {
        return name;
    }

    public Group name(String name) {
        this.name = name;
        return this;
    }

    public String description() {
        return description;
    }

    public Group description(String description) {
        this.description = description;
        return this;
    }

    public boolean closed() {
        return closed;
    }

    public Group closed(boolean closed) {
        this.closed = closed;
        return this;
    }

    public LogoUrls logoUrls() {
        return logoUrls;
    }

    public Group logoUrls(LogoUrls logoUrls) {
        this.logoUrls = logoUrls;
        return this;
    }

    public int memberCount() {
        return memberCount;
    }

    public Group memberCount(int memberCount) {
        this.memberCount = memberCount;
        return this;
    }

    public int postCount() {
        return postCount;
    }

    public Group postCount(int postCount) {
        this.postCount = postCount;
        return this;
    }

    public int unreadPosts() {
        return unreadPosts;
    }

    public Group unreadPosts(int unreadPosts) {
        this.unreadPosts = unreadPosts;
        return this;
    }

    public int commentCount() {
        return commentCount;
    }

    public Group commentCount(int commentCount) {
        this.commentCount = commentCount;
        return this;
    }

    public Membership userState() {
        return userState;
    }

    public Group userState(Membership userState) {
        this.userState = userState;
        return this;
    }

    public List<Post> latestPosts() {
        return latestPosts;
    }

    public Group latestPosts(List<Post> latestPosts) {
        this.latestPosts = latestPosts;
        return this;
    }
}
