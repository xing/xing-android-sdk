package com.xing.api.data.groups;

import com.squareup.moshi.Json;
import com.xing.api.data.profile.PhotoUrls;

import java.io.Serializable;
import java.util.List;

/**
 * @author dhartwich1991
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
    private PhotoUrls logoUrls;
    @Json(name = "member_count")
    private int memberCount;
    @Json(name = "post_count")
    private int postCount;
    @Json(name = "unread_posts")
    private int unreadPosts;
    @Json(name = "comment_count")
    private int commentCount;
    @Json(name = "user_state")
    private UserState userState;
    @Json(name = "latest_posts")
    private List<Post> latestPosts;
}
