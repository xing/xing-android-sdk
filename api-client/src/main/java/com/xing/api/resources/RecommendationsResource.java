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
    /** Creates a resource instance. This should be the only constructor declared by child classes. */
    RecommendationsResource(XingApi api) {
        super(api);
    }

    // TODO docs.
    public CallSpec<List<XingUser>, HttpError> getOwnRecommendations() {
        return Resource.<List<XingUser>, HttpError>newGetSpec(api, "/v1/users/me/network/recommendations")
              .responseAs(list(single(XingUser.class, "user"), "user_recommendations", "recommendations"))
              .build();
    }

    // TODO docs.
    public CallSpec<Void, HttpError> blockRecommendation(String userId) {
        return Resource.<Void, HttpError>newDeleteSpec(api, "/v1/users/me/network/recommendations/user/{id}", false)
              .pathParam("id", userId)
              .responseAs(Void.class)
              .build();
    }
}
