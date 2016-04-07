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
package com.xing.api.resources;

import com.xing.api.CallSpec;
import com.xing.api.HttpError;
import com.xing.api.Resource;
import com.xing.api.XingApi;
import com.xing.api.data.profile.Bookmark;

import java.util.List;

/**
 * Represents the <a href="https://dev.xing.com/docs/resources#bookmarks">'Bookmarks'</a> resource.
 * <p>
 * Provides methods which allow access to a {@linkplain com.xing.api.data.profile.XingUser user's} bookmarks.
 *
 * @author daniel.hartwich
 * @author serj.lotutovici
 */
public class BookmarksResource extends Resource {
    /** Creates a resource instance. This should be the only constructor declared by child classes. */
    BookmarksResource(XingApi api) {
        super(api);
    }

    /**
     * Returns a list of bookmarked users by the authorizing {@linkplain com.xing.api.data.profile.XingUser user}.
     * This list is sorted by the creation date (see: {@linkplain Bookmark#createdAt()}) of the bookmarks.
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><strong>limit</strong></td>
     * <td>Restrict the number of bookmarks to be returned. Must be a positive number. Default: 10</td>
     * </tr>
     * <tr>
     * <td><strong>offset</strong></td>
     * <td>Offset. This must be a positive number. Default: 0.</td>
     * </tr>
     * <tr>
     * <td><strong>user_fields</strong></td>
     * <td>List of user attributes to return in nested user objects. If not used, only the ID will be returned.</td>
     * </tr>
     * </table>
     *
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/bookmarks">'Get list of bookmarks' resource page.</a>
     */
    public CallSpec<List<Bookmark>, HttpError> getListOfOwnBookmarks() {
        return Resource.<List<Bookmark>, HttpError>newGetSpec(api, "/v1/users/me/bookmarks")
              .responseAs(list(Bookmark.class, "bookmarks", "items"))
              .build();
    }

    /**
     * Bookmarks a user for the authorizing {@linkplain com.xing.api.data.profile.XingUser user}.
     *
     * @param userId ID of the user to be bookmarked.
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/put/users/:user_id/bookmarks/:id">'Create a bookmark' resource page.</a>
     */
    public CallSpec<Void, HttpError> createOwnBookmark(String userId) {
        return Resource.<Void, HttpError>newPutSpec(api, "/v1/users/me/bookmarks/{id}", false)
              .responseAs(Void.class)
              .pathParam("id", userId)
              .build();
    }

    /**
     * Deletes a user form the list of bookmarks of the authorizing {@linkplain com.xing.api.data.profile.XingUser
     * user}.
     *
     * @param userId ID of the user to be deleted from bookmarks.
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/delete/users/:user_id/bookmarks/:id">'Dekete a bookmark' resource
     * page.</a>
     */
    public CallSpec<Void, HttpError> deleteOwnBookmark(String userId) {
        return Resource.<Void, HttpError>newDeleteSpec(api, "/v1/users/me/bookmarks/{id}", false)
              .responseAs(Void.class)
              .pathParam("id", userId)
              .build();
    }
}
