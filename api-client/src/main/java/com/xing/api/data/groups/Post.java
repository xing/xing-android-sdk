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
import com.xing.api.data.SafeCalendar;
import com.xing.api.data.profile.XingUser;

import java.io.Serializable;
import java.util.List;

/**
 * Representation of a post inside a group / forum.
 */
public class Post implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "id")
    private String id;

    @Json(name = "title")
    private String title;

    @Json(name = "content")
    private String content;

    @Json(name = "closed")
    private boolean closed;

    @Json(name = "author")
    private XingUser author;

    @Json(name = "created_at")
    private SafeCalendar createdAt;

    @Json(name = "updated_at")
    private SafeCalendar updatedAt;

    @Json(name = "permissions")
    private List<PostPermission> permissions;

    @Json(name = "user_liked")
    private boolean userLiked;

    @Json(name = "like_count")
    private int likeCount;

    @Json(name = "comment_count")
    private int commentCount;

    @Json(name = "media_preview")
    private MediaPreview mediaPreview;

    @Json(name = "image_url")
    private String imageUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (closed != post.closed) return false;
        if (userLiked != post.userLiked) return false;
        if (likeCount != post.likeCount) return false;
        if (commentCount != post.commentCount) return false;
        if (id != null ? !id.equals(post.id) : post.id != null) return false;
        if (title != null ? !title.equals(post.title) : post.title != null) return false;
        if (content != null ? !content.equals(post.content) : post.content != null) return false;
        if (author != null ? !author.equals(post.author) : post.author != null) return false;
        if (createdAt != null ? !createdAt.equals(post.createdAt) : post.createdAt != null) return false;
        if (updatedAt != null ? !updatedAt.equals(post.updatedAt) : post.updatedAt != null) return false;
        if (permissions != null ? !permissions.equals(post.permissions) : post.permissions != null) return false;
        if (mediaPreview != null ? !mediaPreview.equals(post.mediaPreview) : post.mediaPreview != null) return false;
        return imageUrl != null ? imageUrl.equals(post.imageUrl) : post.imageUrl == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (closed ? 1 : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + (permissions != null ? permissions.hashCode() : 0);
        result = 31 * result + (userLiked ? 1 : 0);
        result = 31 * result + likeCount;
        result = 31 * result + commentCount;
        result = 31 * result + (mediaPreview != null ? mediaPreview.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Post{"
              + "id='" + id + '\''
              + ", title='" + title + '\''
              + ", content='" + content + '\''
              + ", closed=" + closed
              + ", author=" + author
              + ", createdAt=" + createdAt
              + ", updatedAt=" + updatedAt
              + ", permissions=" + permissions
              + ", userLiked=" + userLiked
              + ", likeCount=" + likeCount
              + ", commentCount=" + commentCount
              + ", mediaPreview=" + mediaPreview
              + ", imageUrl='" + imageUrl + '\''
              + '}';
    }

    public String id() {
        return id;
    }

    public Post id(String id) {
        this.id = id;
        return this;
    }

    public String title() {
        return title;
    }

    public Post title(String title) {
        this.title = title;
        return this;
    }

    public String content() {
        return content;
    }

    public Post content(String content) {
        this.content = content;
        return this;
    }

    public boolean closed() {
        return closed;
    }

    public Post closed(boolean closed) {
        this.closed = closed;
        return this;
    }

    public XingUser author() {
        return author;
    }

    public Post author(XingUser author) {
        this.author = author;
        return this;
    }

    public SafeCalendar createdAt() {
        return createdAt;
    }

    public Post createdAt(SafeCalendar createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public SafeCalendar updatedAt() {
        return updatedAt;
    }

    public Post updatedAt(SafeCalendar updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public List<PostPermission> permissions() {
        return permissions;
    }

    public Post permissions(List<PostPermission> permissions) {
        this.permissions = permissions;
        return this;
    }

    public boolean userLiked() {
        return userLiked;
    }

    public Post userLiked(boolean userLiked) {
        this.userLiked = userLiked;
        return this;
    }

    public int likeCount() {
        return likeCount;
    }

    public Post likeCount(int likeCount) {
        this.likeCount = likeCount;
        return this;
    }

    public int commentCount() {
        return commentCount;
    }

    public Post commentCount(int commentCount) {
        this.commentCount = commentCount;
        return this;
    }

    public MediaPreview mediaPreview() {
        return mediaPreview;
    }

    public Post mediaPreview(MediaPreview mediaPreview) {
        this.mediaPreview = mediaPreview;
        return this;
    }

    public String imageUrl() {
        return imageUrl;
    }

    public Post imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
