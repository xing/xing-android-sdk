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
     * Get the list of groups the given user belongs to.
     * A note on user state: The user state field will have one of the following values: MEMBER, PENDING_MEMBER, MODERATOR,
     * OWNER.
     * A note on latest posts: The with_latest_posts parameter may be used to return up to 10 posts. Please see the get
     * posts of a group call for full details of the response format.
     *
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><strong>limit</strong></td>
     * <td>The default limit is 10, the maximum limit is 100</td>
     * </tr>
     * <tr>
     * <td><strong>offset</strong></td>
     * <td>The default offset is 0.</td>
     * </tr>
     * <tr>
     * <td><strong>order_by</strong></td>
     * <td>Parameter that determines the ascending order of the returned list. Currently only supports "latest_post".
     * Defaults to "id"</td>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>Author attributes to return within latest_posts, if requested. For a list of available profile user attributes,
     * please refer to the get user details call.</td>
     * </tr>
     * <tr>
     * <td><b>with_latest_posts</b></td>
     * <td>Number of latest posts to include for each group. Must be non-negative. Default: 1, Maximum: 10.
     * </td>
     * </tr>
     * </table>
     *
     * @param userId Id of the users who's groups should be returned.
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     */
    public CallSpec<List<Group>, HttpError> getUsersGroups(String userId) {
        return Resource.<List<Group>, HttpError>newGetSpec(api, "/v1/users/{user_id}/groups")
              .responseAs(list(Group.class, "groups", "items"))
              .pathParam("user_id", userId)
              .build();
    }

    /**
     * Get the list of groups for the current logged in user.
     * A note on user state: The user state field will have one of the following values: MEMBER, PENDING_MEMBER, MODERATOR,
     * OWNER.
     * A note on latest posts: The with_latest_posts parameter may be used to return up to 10 posts. Please see the get
     * posts of a group call for full details of the response format.
     *
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><strong>limit</strong></td>
     * <td>The default limit is 10, the maximum limit is 100</td>
     * </tr>
     * <tr>
     * <td><strong>offset</strong></td>
     * <td>The default offset is 0.</td>
     * </tr>
     * <tr>
     * <td><strong>order_by</strong></td>
     * <td>Parameter that determines the ascending order of the returned list. Currently only supports "latest_post".
     * Defaults to "id"</td>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>Author attributes to return within latest_posts, if requested. For a list of available profile user attributes,
     * please refer to the get user details call.</td>
     * </tr>
     * <tr>
     * <td><b>with_latest_posts</b></td>
     * <td>Number of latest posts to include for each group. Must be non-negative. Default: 1, Maximum: 10.
     * </td>
     * </tr>
     * </table>
     *
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     */
    public CallSpec<List<Group>, HttpError> getOwnGroups() {
        return getUsersGroups(ME);
    }

    /**
     * THIS CALL CURRENTLY DOES NOT WORK DUE TO A PROBLEM WITH THE RESPONSE FORMAT.
     *
     * Returns a list of groups found in accordance with the given list of keywords.
     *
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><strong>limit</strong></td>
     * <td>Restricts the number of groups to be returned. This must be a positive number. Default: 10, Maximum: 100</td>
     * </tr>
     * <tr>
     * <td><strong>offset</strong></td>
     * <td>Offset. This must be a positive number. Default: 0</td>
     * </tr>
     * </table>
     *
     * @param keywords List of keywords. An empty result will be returned if no keywords provided.
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     */
    @Experimental
    public CallSpec<List<Group>, HttpError> findGroupByKeyword(String keywords) {
        return Resource.<List<Group>, HttpError>newGetSpec(api, "/v1/groups/find")
              .responseAs(list(single(Group.class, "group"), "groups", "items"))
              .queryParam("keywords", keywords)
              .build();
    }

    /**
     * Get the list of forums contained in the given group.
     * A note on possible actions: Possible values for the permissions field are: READ, POST. These values indicate if the
     * current logged in user can view or post to each forum.
     *
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><strong>limit</strong></td>
     * <td>Restricts the number of groups to be returned. This must be a positive number. Default: 10, Maximum: 100</td>
     * </tr>
     * <tr>
     * <td><strong>offset</strong></td>
     * <td>Offset. This must be a positive number. Default: 0</td>
     * </tr>
     * </table>
     *
     * @param groupId ID of the group
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     */
    public CallSpec<List<Forum>, HttpError> getForumsOfGroup(String groupId) {
        return Resource.<List<Forum>, HttpError>newGetSpec(api, "/v1/groups/{group_id}/forums")
              .responseAs(list(Forum.class, "forums", "items"))
              .pathParam("group_id", groupId)
              .build();
    }

    /**
     * Get the list of posts contained in the given forum.
     * Posts are sorted by created_at in descending order.
     * A note on possible actions: The permissions field may contain several of the following values: READ, UPDATE, DELETE,
     * LIKE, UNLIKE, RECOMMEND, COMMENT
     *
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><strong>exclude_content</strong></td>
     * <td>Whether the content is excluded from the response or not (default is false).</td>
     * </tr>
     * <tr>
     * <td><strong>limit</strong></td>
     * <td>Restricts the number of groups to be returned. This must be a positive number. Default: 10, Maximum: 100</td>
     * </tr>
     * <tr>
     * <td><strong>offset</strong></td>
     * <td>Offset. This must be a positive number. Default: 0</td>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>List of user attributes to be returned. If this parameter is not used, only the ID will be returned. For a list
     * of available profile user attributes, please refer to the get user details call.</td>
     * </tr>
     * </table>
     *
     * @param forumId ID of the forum
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     */
    public CallSpec<List<Post>, HttpError> getPostsOfForum(String forumId) {
        return Resource.<List<Post>, HttpError>newGetSpec(api, "/v1/groups/forums/{forum_id}/posts")
              .responseAs(list(Post.class, "posts", "items"))
              .pathParam("forum_id", forumId)
              .build();
    }

    /**
     * Show a post in a group.
     * A note on possible actions: The permissions field may contain several of the following values: READ, UPDATE, DELETE,
     * LIKE, UNLIKE, RECOMMEND, COMMENT
     * A note on image_url field: The URLs are valid for 6 hours only.
     * Showing a post will not decrement the unread field of the Group posts call. It is not possible to mark individual
     * posts as read; one can mark all posts in a group as read using the Mark a group as read call.
     *
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>List of user attributes to be returned. If this parameter is not used, only the ID will be returned. For a list
     * of available profile user attributes, please refer to the get user details call.</td>
     * </tr>
     * </table>
     *
     * @param postId ID of the post
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     */
    public CallSpec<Post, HttpError> getPostInGroup(String postId) {
        return Resource.<Post, HttpError>newGetSpec(api, "/v1/groups/forums/posts/{post_id}")
              .responseAs(single(Post.class, "post"))
              .pathParam("post_id", postId)
              .build();
    }

    /**
     * Get the list of posts contained in the given group.
     * Posts are sorted by created_at in descending order.
     * A note on possible actions: The permissions field may contain several of the following values: READ, UPDATE, DELETE,
     * LIKE, UNLIKE, RECOMMEND, COMMENT
     * Showing a post will not decrement the unread field. It is not possible to mark individual posts as read; one can mark
     * all posts in a group as read using the Mark a group as read call.
     *
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><strong>exclude_content</strong></td>
     * <td>Whether the content is excluded from the response or not (default is false).</td>
     * </tr>
     * <tr>
     * <td><strong>limit</strong></td>
     * <td>The default limit is 10, the maximum limit is 100</td>
     * </tr>
     * <tr>
     * <td><strong>offset</strong></td>
     * <td>The default offset is 0.</td>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>Author attributes to return within latest_posts, if requested. For a list of available profile user attributes,
     * please refer to the get user details call.</td>
     * </tr>
     * </table>
     *
     * @param groupId ID of the group
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     */
    public CallSpec<List<Post>, HttpError> getAllPostsOfGroup(String groupId) {
        return Resource.<List<Post>, HttpError>newGetSpec(api, "/v1/groups/{group_id}/posts")
              .responseAs(list(Post.class, "posts", "items"))
              .pathParam("group_id", groupId)
              .build();
    }

    /**
     * Get the list of likes contained in the given post.
     *
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><strong>limit</strong></td>
     * <td>The default limit is 10, the maximum limit is 100</td>
     * </tr>
     * <tr>
     * <td><strong>offset</strong></td>
     * <td>The default offset is 0.</td>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>Author attributes to return within latest_posts, if requested. For a list of available profile user attributes,
     * please refer to the get user details call.</td>
     * </tr>
     * </table>
     *
     * @param postId Id of the post
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     */
    public CallSpec<List<XingUser>, HttpError> getLikersOfPost(String postId) {
        return Resource.<List<XingUser>, HttpError>newGetSpec(api, "/v1/groups/forums/posts/{post_id}")
              .responseAs(list(XingUser.class, "likes", "users"))
              .pathParam("post_id", postId)
              .build();
    }

    /**
     * Add a like to a forum post.
     *
     * @param postId The ID of the post to be liked.
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     */
    public CallSpec<Void, HttpError> likePost(String postId) {
        return Resource.<Void, HttpError>newPutSpec(api, "/v1/groups/forums/posts/{post_id}/like", false)
              .responseAs(Void.class)
              .pathParam("post_id", postId)
              .build();
    }

    /**
     * Remove the current users like from a post.
     *
     * @param postId The ID of the post to be unliked.
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     */
    public CallSpec<Void, HttpError> unlikePost(String postId) {
        return Resource.<Void, HttpError>newDeleteSpec(api, "/v1/groups/forums/posts/{post_id}/like", false)
              .responseAs(Void.class)
              .pathParam("post_id", postId)
              .build();
    }

    /**
     * Get the list of comments contained in the given post.
     * Comments are sorted by created_at in ascending order.
     * A note on possible actions: The permissions field may contain several of the following values: READ, UPDATE, DELETE,
     * LIKE, UNLIKE
     *
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><strong>limit</strong></td>
     * <td>The default limit is 10, the maximum limit is 100</td>
     * </tr>
     * <tr>
     * <td><strong>offset</strong></td>
     * <td>The default offset is 0.</td>
     * </tr>
     * <tr>
     * <td><strong>sort_direction</strong></td>
     * <td>Sort the comment by oldest first ("asc") or by latest first ("desc"). Oldest first is the default behavior.</td>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>Author attributes to return within latest_posts, if requested. For a list of available profile user attributes,
     * please refer to the get user details call.</td>
     * </tr>
     * </table>
     *
     * @param postId ID if the post
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     */
    public CallSpec<List<Comment>, HttpError> getCommentsOfPost(String postId) {
        return Resource.<List<Comment>, HttpError>newGetSpec(api, "/v1/groups/forums/posts/{post_id}/comments")
              .responseAs(list(Comment.class, "comments", "items"))
              .pathParam("post_id", postId)
              .build();
    }

    /**
     * Add a new comment to a post.
     * The image has to be less than 9 MB in size. The supported image formats are image/jpeg, image/png, image/gif,
     * image/bmp. The image should be sent as application/json with the image data Base64 encoded in the body: { "image": {
     * "file_name": "test.jpg", "mime_type": "image/jpeg", "content": "Base64 encoded image data" } }
     * Note that until the image has been fully processed a placeholder image will be returned in the comments index call.
     *
     * Call rate limits
     * Per consumer | Per user
     * 50 per minute | 5 per minute
     * 100 per hour | 10 per hour
     * 150 per day  | 15 per day
     *
     * In order to get higher rate limit for this call, please send an email to api-support@xing.com. We will then get back
     * to you with further information.
     *
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><strong>image</strong></td>
     * <td>Image attached to the comment</td>
     * </tr>
     * <tr>
     * <td><strong>media_preview_id</strong></td>
     * <td>Id of the media preview that should be shown in the comment.</td>
     * </tr>
     * </table>
     *
     * @param content UTF-8 encoded content of the comment
     * @param postId ID of the post
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     */
    public CallSpec<Void, HttpError> addCommentToPost(String content, String postId) {
        return Resource.<Void, HttpError>newPostSpec(api, "/v1/groups/forums/posts/{post_id}/comments", false)
              .responseAs(Void.class)
              .pathParam("post_id", postId)
              .queryParam("content", content)
              .build();
    }

    /**
     * Delete a comment of a post.
     *
     * @param commentId ID of the comment
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     */
    public CallSpec<Void, HttpError> deleteCommentOfPost(String commentId) {
        return Resource.<Void, HttpError>newDeleteSpec(api, "/v1/groups/forums/posts/comments/{comment_id}", false)
              .responseAs(Void.class)
              .pathParam("comment_id", commentId)
              .build();
    }

    /**
     * Get the list of likes contained in the given comment.
     *
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><strong>limit</strong></td>
     * <td>The default limit is 10, the maximum limit is 100</td>
     * </tr>
     * <tr>
     * <td><strong>offset</strong></td>
     * <td>The default offset is 0.</td>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>Author attributes to return within latest_posts, if requested. For a list of available profile user attributes,
     * please refer to the get user details call.</td>
     * </tr>
     * </table>
     *
     * @param commentId ID of the comment
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     */
    public CallSpec<List<XingUser>, HttpError> getLikersOfComment(String commentId) {
        return Resource.<List<XingUser>, HttpError>newGetSpec(api, "/v1/groups/forums/posts/comments/{comment_id}/likes")
              .responseAs(list(XingUser.class, "likes", "users"))
              .pathParam("comment_id", commentId)
              .build();
    }

    /**
     * Add a like to a comment of a forum post.
     *
     * @param commentId ID of the comment
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     */
    public CallSpec<Void, HttpError> likeComment(String commentId) {
        return Resource.<Void, HttpError>newPutSpec(api, "/v1/groups/forums/posts/comments/{comment_id}/like", false)
              .responseAs(Void.class)
              .pathParam("comment_id", commentId)
              .build();
    }

    /**
     * Remove the current users like from a comment of a post.
     *
     * @param commentId ID of the comment
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     */
    public CallSpec<Void, HttpError> unlikeComment(String commentId) {
        return Resource.<Void, HttpError>newDeleteSpec(api, "/v1/groups/forums/posts/comments/{comment_id}/like", false)
              .responseAs(Void.class)
              .pathParam("comment_id", commentId)
              .build();
    }

    /**
     * Marks all posts in the group as read.
     *
     * @param groupId ID of the group
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     */
    public CallSpec<Void, HttpError> markAllPostsAsRead(String groupId) {
        return Resource.<Void, HttpError>newPutSpec(api, "/v1/groups/{group_id}/read", false)
              .responseAs(Void.class)
              .pathParam("group_id", groupId)
              .build();
    }

    /**
     * Join an open group or request membership to a closed group.
     * A note on possible member states: The member_state field may contain several of the following values: MEMBER,
     * PENDING_MEMBER, MODERATOR, OWNER
     *
     * @param groupId ID of the group
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     */
    public CallSpec<Membership, HttpError> joinGroup(String groupId) {
        return Resource.<Membership, HttpError>newPostSpec(api, "/v1/groups/{group_id}/memberships", false)
              .responseAs(single(Membership.class, "membership", "member_state"))
              .pathParam("group_id", groupId)
              .build();
    }

    /**
     * Create a new post in a group.
     * The post must be linked to a forum. Image has to be less than 9 MB large. The supported image formats are
     * image/jpeg,
     * image/png, image/gif, image/bmp. Image should be sent as application/json with the image data Base64 encoded in the
     * body: { "image": { "file_name": "test.jpg", "mime_type": "image/jpeg", "content": "Base64 encoded image data" } }
     * Note that until the image has been fully processed a placeholder image will be returned in the show post call
     *
     * Call rate limits
     * Per consumer | Per user
     * 50 per minute | 5 per minute
     * 100 per hour | 10 per hour
     * 150 per day | 15 per day
     *
     * In order to get higher rate limit for this call, please send an email to api-support@xing.com. We will then get back
     * to you with further information.
     *
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><strong>image</strong></td>
     * <td>Image attached to the post</td>
     * </tr>
     * <tr>
     * <td><strong>media_preview_id</strong></td>
     * <td>Id of the media preview that should be shown in the post.</td>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>List of user attributes to be returned. If this parameter is not used, only the ID will be returned. For a list
     * of available profile user attributes, please refer to the get user details call.</td>
     * </tr>
     * </table>
     *
     * @param title UTF-8 encoded title of the post
     * @param content UTF-8 encoded content of the post
     * @param forumId ID of the forum where this post should be created
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     */
    public CallSpec<Post, HttpError> createPost(String title, String content, String forumId) {
        return Resource.<Post, HttpError>newPostSpec(api, "/v1/groups/forums/{forum_id}/posts", false)
              .responseAs(single(Post.class, "post"))
              .pathParam("forum_id", forumId)
              .queryParam("title", title)
              .queryParam("content", content)
              .build();
    }

    /**
     * Delete a post from a group.
     *
     * @param postId ID of the post to be deleted.
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     */
    public CallSpec<Void, HttpError> deletePost(String postId) {
        return Resource.<Void, HttpError>newDeleteSpec(api, "/v1/groups/forums/posts/{post_id}", false)
              .responseAs(Void.class)
              .pathParam("post_id", postId)
              .build();
    }

    /**
     * Leave a group.
     *
     * @param groupId ID of the group that should be leaved
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     */
    public CallSpec<Void, HttpError> leaveGroup(String groupId) {
        return Resource.<Void, HttpError>newDeleteSpec(api, "/v1/groups/{group_id}/memberships", false)
              .responseAs(Void.class)
              .pathParam("group_id", groupId)
              .build();
    }

    /**
     * Creates a media preview.
     * This preview can be used when creating a group post or comment
     *
     * Call rate limits
     * Per consumer | Per user
     * 50 per minute | 5 per minute
     * 100 per hour | 10 per hour
     * 150 per day | 15 per day
     *
     * In order to get higher rate limit for this call, please send an email to api-support@xing.com. We will then get back
     * to you with further information.
     *
     * @param url URL of the web site
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     */
    public CallSpec<MediaPreview, HttpError> createMediaPreview(String url) {
        return Resource.<MediaPreview, HttpError>newPostSpec(api, "/v1/groups/media_previews", false)
              .responseAs(single(MediaPreview.class, "media_preview"))
              .queryParam("url", url)
              .build();
    }
}
