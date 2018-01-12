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
 * Model of comment that is returned by the
 * <a href=https://dev.xing.com/docs/get/groups/forums/posts/:post_id/comments>Get the comments of a post</a> call.
 */
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "id")
    private String id;
    @Json(name = "content")
    private String content;
    @Json(name = "permissions")
    private List<CommentPermission> permissions;
    @Json(name = "created_at")
    private SafeCalendar createdAt;
    @Json(name = "updated_at")
    private SafeCalendar updatedAt;
    @Json(name = "image")
    private String image;
    @Json(name = "author")
    private XingUser author;
    @Json(name = "user_liked")
    private boolean userLiked;
    @Json(name = "like_count")
    private int likeCount;
    @Json(name = "media_preview")
    private MediaPreview mediaPreview;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Comment comment = (Comment) object;

        if (userLiked != comment.userLiked) return false;
        if (likeCount != comment.likeCount) return false;
        if (id != null ? !id.equals(comment.id) : comment.id != null) return false;
        if (content != null ? !content.equals(comment.content) : comment.content != null) return false;
        if (permissions != null ? !permissions.equals(comment.permissions) : comment.permissions != null) return false;
        if (createdAt != null ? !createdAt.equals(comment.createdAt) : comment.createdAt != null) return false;
        if (updatedAt != null ? !updatedAt.equals(comment.updatedAt) : comment.updatedAt != null) return false;
        if (image != null ? !image.equals(comment.image) : comment.image != null) return false;
        if (author != null ? !author.equals(comment.author) : comment.author != null) return false;
        return mediaPreview != null ? mediaPreview.equals(comment.mediaPreview) : comment.mediaPreview == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (permissions != null ? permissions.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (userLiked ? 1 : 0);
        result = 31 * result + likeCount;
        result = 31 * result + (mediaPreview != null ? mediaPreview.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Comment{"
              + "id='" + id + '\''
              + ", content='" + content + '\''
              + ", permissions=" + permissions
              + ", createdAt=" + createdAt
              + ", updatedAt=" + updatedAt
              + ", image='" + image + '\''
              + ", author=" + author
              + ", userLiked=" + userLiked
              + ", likeCount=" + likeCount
              + ", mediaPreview=" + mediaPreview
              + '}';
    }

    public String id() {
        return id;
    }

    public Comment id(String id) {
        this.id = id;
        return this;
    }

    public String content() {
        return content;
    }

    public Comment content(String content) {
        this.content = content;
        return this;
    }

    public List<CommentPermission> permissions() {
        return permissions;
    }

    public Comment permissions(List<CommentPermission> permissions) {
        this.permissions = permissions;
        return this;
    }

    public SafeCalendar createdAt() {
        return createdAt;
    }

    public Comment createdAt(SafeCalendar createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public SafeCalendar updatedAt() {
        return updatedAt;
    }

    public Comment updatedAt(SafeCalendar updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String image() {
        return image;
    }

    public Comment image(String image) {
        this.image = image;
        return this;
    }

    public XingUser author() {
        return author;
    }

    public Comment author(XingUser author) {
        this.author = author;
        return this;
    }

    public boolean userLiked() {
        return userLiked;
    }

    public Comment userLiked(boolean userLiked) {
        this.userLiked = userLiked;
        return this;
    }

    public int likeCount() {
        return likeCount;
    }

    public Comment likeCount(int likeCount) {
        this.likeCount = likeCount;
        return this;
    }

    public MediaPreview mediaPreview() {
        return mediaPreview;
    }

    public Comment mediaPreview(MediaPreview mediaPreview) {
        this.mediaPreview = mediaPreview;
        return this;
    }
}
