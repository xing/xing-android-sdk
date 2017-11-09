/*
 * Copyright (С) 2016 XING SE (http://xing.com/)
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
import com.xing.api.data.profile.XingUser;

import java.util.List;

/**
 * Represents the <a href="https://dev.xing.com/docs/resources#recommendations">'Recommendations'</a> resource.
 * <p>
 * Allows access to the specified user's contacts recommendations.
 *
 * @author daniel.hartwich
 */
public class RecommendationsResource extends Resource {
    public static final Resource.Factory FACTORY = new Resource.Factory(RecommendationsResource.class) {
        @Override public Resource create(XingApi api) {
            return new RecommendationsResource(api);
        }
    };

    /** Creates a resource instance. This should be the only constructor declared by child classes. */
    RecommendationsResource(XingApi api) {
        super(api);
    }

    /**
     * Get own recommendations.
     * Returns a list of users the specified user might know.
     *
     * <table>
     * <h4>Possible OPTIONAL parameters</h4>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>List of user attributes to return. If this parameter is not used, only the ID will be returned. For a list
     * of
     * available profile user attributes, please refer to the get user details call. {@link XingUser}</td>
     * </tr>
     * * <tr>
     * <td><b>limit</b></td>
     * <td>Limit the number of recommendations to be returned. This must be a positive number. Default: 10, Maximum:
     * 100</td>
     * </tr>
     * * <tr>
     * <td><b>offset</b></td>
     * <td>List of user attributes to return. If this parameter is not used, only the ID will be returned. For a list of
     * available profile user attributes, please refer to the get user details call.</td>
     * </tr>
     * </table>
     */
    public CallSpec<List<XingUser>, HttpError> getOwnRecommendations() {
        return Resource.<List<XingUser>, HttpError>newGetSpec(api, "/v1/users/me/network/recommendations")
              .responseAs(list(single(XingUser.class, "user"), "user_recommendations", "recommendations"))
              .build();
    }

    /**
     * Get recommendations.
     * Returns a list of users the specified user might know.
     *
     * <table>
     * <h4>Possible OPTIONAL parameters</h4>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>List of user attributes to return. If this parameter is not used, only the ID will be returned. For a list
     * of
     * available profile user attributes, please refer to the get user details call. {@link XingUser}</td>
     * </tr>
     * * <tr>
     * <td><b>limit</b></td>
     * <td>Limit the number of recommendations to be returned. This must be a positive number. Default: 10, Maximum:
     * 100</td>
     * </tr>
     * * <tr>
     * <td><b>offset</b></td>
     * <td>List of user attributes to return. If this parameter is not used, only the ID will be returned. For a list of
     * available profile user attributes, please refer to the get user details call.</td>
     * </tr>
     * </table>
     *
     * @param userId The id for which you want to get the recommendations
     */
    public CallSpec<List<XingUser>, HttpError> getRecommendations(String userId) {
        return Resource.<List<XingUser>, HttpError>newGetSpec(api, "/v1/users/{user_id}/network/recommendations")
              .pathParam("user_id", userId)
              .responseAs(list(single(XingUser.class, "user"), "user_recommendations", "recommendations"))
              .build();
    }

    /**
     * Block recommendation.
     * Block recommendation for user with given id.
     *
     * @param userId id of the user which you want to block from the recommendations.
     */
    public CallSpec<Void, HttpError> blockRecommendation(String userId) {
        return Resource.<Void, HttpError>newDeleteSpec(api, "/v1/users/me/network/recommendations/user/{id}", false)
              .pathParam("id", userId)
              .responseAs(Void.class)
              .build();
    }
}
