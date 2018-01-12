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
package com.xing.api.resources;

import com.xing.api.CallSpec;
import com.xing.api.HttpError;
import com.xing.api.Resource;
import com.xing.api.XingApi;
import com.xing.api.data.profile.ProfileVisit;

import java.util.List;

/**
 * Represents the <a href="https://dev.xing.com/docs/resources#profile-visits">'Profile Visits'</a> resource.
 *
 * Provides methods which allow access to a {@linkplain com.xing.api.data.profile.XingUser user's} profile visits.
 */
public class ProfileVisitsResource extends Resource {
    public static final Resource.Factory FACTORY = new Resource.Factory(ProfileVisitsResource.class) {
        @Override public Resource create(XingApi api) {
            return new ProfileVisitsResource(api);
        }
    };

    /** Creates a resource instance. This should be the only constructor declared by child classes. */
    ProfileVisitsResource(XingApi api) {
        super(api);
    }

    /**
     * Returns a list of users who recently visited the specified {@linkplain com.xing.api.data.profile.XingUser
     * user's} profile. Entries with {@code null} value for the <strong>user_id</strong> attribute indicate
     * anonymous (non-XING) users (e.g. resulting from Google searches). Any blacklisted
     * users returned will contain {@code null} values in the same way as the
     * <a href="https://dev.xing.com/docs/user_profile#empty-user-profile">empty user profile</a>.
     * <p>
     * The {@linkplain ProfileVisit#distance()} value returned will show only first (value = 1) and
     * second (value = 2) degree contacts.
     * For all other visitors who are XING users the value will be 0. For non-XING users it will be -1.
     * <p>
     * If the user is a basic member, only {@linkplain ProfileVisit#photoUrls()},
     * {@linkplain ProfileVisit#visitedAt()}, {@linkplain ProfileVisit#reason()},
     * {@linkplain ProfileVisit#visitedAtEncrypted()} and {@linkplain ProfileVisit#type()} will be available.
     * The remaining fields will be {@code null} or {@code -1}.
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><strong>limit</strong></td>
     * <td>Restricts the number of profile visits to be returned. Must be a positive number. Default: 10</td>
     * </tr>
     * <tr>
     * <td><strong>offset</strong></td>
     * <td>Offset. This must be a positive number. Default: 0.</td>
     * </tr>
     * <tr>
     * <td><strong>shared_contacts</strong></td>
     * <td>Number of shared contacts that should be included in the result. Must be a positive number. Default: 0,
     * Maximum: 10.</td>
     * </tr>
     * <tr>
     * <td><b>since</b></td>
     * <td>Only returns visits more recent than the specified time stamp (ISO 8601).
     * {@linkplain ProfileVisit#visitedAtEncrypted()} is also accepted and will allow to retrieve all all visits
     * that were more recent than the specified one one.</td>
     * </tr>
     * <tr>
     * <td><b>strip_html</b></td>
     * <td>Specifies whether the profile visit reason should be stripped of HTML ({@code true}) or not ({@code false}).
     * Default value: {@code false}. Values other then {@code true/false} will be treated as {@code false}.
     * </td>
     * </tr>
     * </table>
     *
     * @param userId Id of the user who's profile visits should be returned.
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/visits">'Get profile visit' resource page.</a>
     */
    public CallSpec<List<ProfileVisit>, HttpError> getUsersProfileVisits(String userId) {
        return Resource.<List<ProfileVisit>, HttpError>newGetSpec(api, "/v1/users/{user_id}/visits")
              .responseAs(list(ProfileVisit.class, "visits"))
              .pathParam("user_id", userId)
              .build();
    }

    /**
     * Returns a list of users who recently visited the authorizing {@linkplain com.xing.api.data.profile.XingUser
     * user's} profile. Same as {@linkplain #getUsersProfileVisits(String)}.
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><strong>limit</strong></td>
     * <td>Restricts the number of profile visits to be returned. Must be a positive number. Default: 10</td>
     * </tr>
     * <tr>
     * <td><strong>offset</strong></td>
     * <td>Offset. This must be a positive number. Default: 0.</td>
     * </tr>
     * <tr>
     * <td><strong>shared_contacts</strong></td>
     * <td>Number of shared contacts that should be included in the result. Must be a positive number. Default: 0,
     * Maximum: 10.</td>
     * </tr>
     * <tr>
     * <td><b>since</b></td>
     * <td>Only returns visits more recent than the specified time stamp (ISO 8601).
     * {@linkplain ProfileVisit#visitedAtEncrypted()} is also accepted and will allow to retrieve all all visits
     * that were more recent than the specified one one.</td>
     * </tr>
     * <tr>
     * <td><b>strip_html</b></td>
     * <td>Specifies whether the profile visit reason should be stripped of HTML ({@code true}) or not ({@code false}).
     * Default value: {@code false}. Values other then {@code true/false} will be treated as {@code false}.
     * </td>
     * </tr>
     * </table>
     *
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/visits">'Get profile visit' resource page.</a>
     */
    public CallSpec<List<ProfileVisit>, HttpError> getOwnProfileVisits() {
        return getUsersProfileVisits(ME);
    }

    /**
     * Generate a profile visit.
     * <p>
     * The visiting user will be derived from the user executing the call auth headers, and the visit reason derived
     * from the consumer.
     *
     * @param userId The user which is visited by the authorizing {@linkplain com.xing.api.data.profile.XingUser
     * user}.
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/post/users/:user_id/visits">'Create profile visit' resource page.</a>
     */
    public CallSpec<Void, HttpError> createProfileVisits(String userId) {
        return Resource.<Void, HttpError>newPostSpec(api, "v1/users/{user_id}/visits", false)
              .responseAs(Void.class)
              .pathParam("user_id", userId)
              .build();
    }
}
