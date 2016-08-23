package com.xing.api.resources;

import com.xing.api.CallSpec;
import com.xing.api.HttpError;
import com.xing.api.Resource;
import com.xing.api.XingApi;
import com.xing.api.data.groups.Comment;
import com.xing.api.data.groups.Forum;
import com.xing.api.data.groups.Group;
import com.xing.api.data.groups.MediaPreview;
import com.xing.api.data.groups.Membership;
import com.xing.api.data.groups.Post;
import com.xing.api.data.profile.XingUser;
import com.xing.api.internal.Experimental;

import java.util.List;

/**
 * Resource for all the possible groups calls.
 *
 * @author dhartwich1991
 */
public class GroupsResource extends Resource {
    /**
     * Creates a resource instance. This should be the only constructor declared by child classes.
     */
    protected GroupsResource(XingApi api) {
        super(api);
    }

    /**
     * Get all Groups for a specific user.
     */
    public CallSpec<List<Group>, HttpError> getUsersGroups(String userId) {
        return Resource.<List<Group>, HttpError>newGetSpec(api, "/v1/users/{user_id}/groups")
              .responseAs(list(Group.class, "groups", "items"))
              .pathParam("user_id", userId)
              .build();
    }

    public CallSpec<List<Group>, HttpError> getOwnGroups() {
        return getUsersGroups(ME);
    }

    /** THIS CALL CURRENTLY DOES NOT WORK DUE TO A PROBLEM WITH THE RESPONSE FORMAT. */
    @Experimental
    public CallSpec<List<Group>, HttpError> findGroupByKeyword(String keyword) {
        return Resource.<List<Group>, HttpError>newGetSpec(api, "/v1/groups/find")
              .responseAs(list(Group.class, "groups", "items"))
              .queryParam("keyword", keyword)
              .build();
    }

    public CallSpec<List<Forum>, HttpError> getForumsOfGroup(String groupId) {
        return Resource.<List<Forum>, HttpError>newGetSpec(api, "/v1/groups/{group_id}/forums")
              .responseAs(list(Forum.class, "forums", "items"))
              .pathParam("group_id", groupId)
              .build();
    }

    public CallSpec<List<Post>, HttpError> getPostsOfForum(String forumId) {
        return Resource.<List<Post>, HttpError>newGetSpec(api, "/v1/groups/forums/{forum_id}/posts")
              .responseAs(list(Post.class, "posts", "items"))
              .pathParam("forum_id", forumId)
              .build();
    }

    public CallSpec<Post, HttpError> getPostInGroup(String postId) {
        return Resource.<Post, HttpError>newGetSpec(api, "/v1/groups/forums/posts/{post_id}")
              .responseAs(single(Post.class, "post"))
              .pathParam("post_id", postId)
              .build();
    }

    public CallSpec<List<Post>, HttpError> getAllPostsOfGroup(String groupId) {
        return Resource.<List<Post>, HttpError>newGetSpec(api, "/v1/groups/{group_id}/posts")
              .responseAs(list(Post.class, "posts", "items"))
              .pathParam("group_id", groupId)
              .build();
    }

    public CallSpec<List<XingUser>, HttpError> getLikersOfPost(String postId) {
        return Resource.<List<XingUser>, HttpError>newGetSpec(api, "/v1/groups/forums/posts/{post_id}")
              .responseAs(list(XingUser.class, "likes", "users"))
              .pathParam("post_id", postId)
              .build();
    }

    public CallSpec<Void, HttpError> likePost(String postId) {
        return Resource.<Void, HttpError>newPutSpec(api, "/v1/groups/forums/posts/{post_id}/like", false)
              .responseAs(Void.class)
              .pathParam("post_id", postId)
              .build();
    }

    public CallSpec<Void, HttpError> unlikePost(String postId) {
        return Resource.<Void, HttpError>newDeleteSpec(api, "/v1/groups/forums/posts/{post_id}/like", false)
              .responseAs(Void.class)
              .pathParam("post_id", postId)
              .build();
    }

    public CallSpec<List<Comment>, HttpError> getCommentsOfPost(String postId) {
        return Resource.<List<Comment>, HttpError>newGetSpec(api, "/v1/groups/forums/posts/{post_id}/comments")
              .responseAs(list(Comment.class, "comments", "items"))
              .pathParam("post_id", postId)
              .build();
    }

    public CallSpec<Void, HttpError> addCommentToPost(String content, String postId) {
        return Resource.<Void, HttpError>newPostSpec(api, "/v1/groups/forums/posts/{post_id}/comments", false)
              .responseAs(Void.class)
              .pathParam("post_id", postId)
              .queryParam("content", content)
              .build();
    }

    public CallSpec<Void, HttpError> deleteCommentOfPost(String commentId) {
        return Resource.<Void, HttpError>newDeleteSpec(api, "/v1/groups/forums/posts/comments/{comment_id}", false)
              .responseAs(Void.class)
              .pathParam("comment_id", commentId)
              .build();
    }

    public CallSpec<List<XingUser>, HttpError> getLikersOfComment(String commentId) {
        return Resource.<List<XingUser>, HttpError>newGetSpec(api, "/v1/groups/forums/posts/comments/{comment_id}/likes")
              .responseAs(list(XingUser.class, "likes", "users"))
              .pathParam("comment_id", commentId)
              .build();
    }

    public CallSpec<Void, HttpError> likeComment(String commentId) {
        return Resource.<Void, HttpError>newPutSpec(api, "/v1/groups/forums/posts/comments/{comment_id}/like", false)
              .responseAs(Void.class)
              .pathParam("comment_id", commentId)
              .build();
    }

    public CallSpec<Void, HttpError> unlikeComment(String commentId) {
        return Resource.<Void, HttpError>newDeleteSpec(api, "/v1/groups/forums/posts/comments/{comment_id}/like", false)
              .responseAs(Void.class)
              .pathParam("comment_id", commentId)
              .build();
    }

    public CallSpec<Void, HttpError> markAllPostsAsRead(String groupId) {
        return Resource.<Void, HttpError>newPutSpec(api, "/v1/groups/{group_id}/read", false)
              .responseAs(Void.class)
              .pathParam("group_id", groupId)
              .build();
    }

    public CallSpec<Membership, HttpError> joinGroup(String groupId) {
        return Resource.<Membership, HttpError>newPostSpec(api, "/v1/groups/{group_id}/memberships", false)
              .responseAs(single(Membership.class, "membership", "member_state"))
              .pathParam("group_id", groupId)
              .build();
    }

    public CallSpec<Post, HttpError> createPost(String title, String content, String forumId) {
        return Resource.<Post, HttpError>newPostSpec(api, "/v1/groups/forums/{forum_id}/posts", false)
              .responseAs(single(Post.class, "post"))
              .pathParam("forum_id", forumId)
              .queryParam("title", title)
              .queryParam("content", content)
              .build();
    }

    public CallSpec<Void, HttpError> deletePost(String postId) {
        return Resource.<Void, HttpError>newDeleteSpec(api, "/v1/groups/forums/posts/{post_id}", false)
              .responseAs(Void.class)
              .pathParam("post_id", postId)
              .build();
    }

    public CallSpec<Void, HttpError> leaveGroup(String groupId) {
        return Resource.<Void, HttpError>newDeleteSpec(api, "/v1/groups/{group_id}/memberships", false)
              .responseAs(Void.class)
              .pathParam("group_id", groupId)
              .build();
    }

    public CallSpec<MediaPreview, HttpError> createMediaPreview(String url) {
        return Resource.<MediaPreview, HttpError>newPostSpec(api, "/v1/groups/media_previews", false)
              .responseAs(single(MediaPreview.class, "media_preview"))
              .queryParam("url", url)
              .build();
    }
}
