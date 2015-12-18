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
package com.xing.android.sdk.resources;

import com.xing.android.sdk.CallSpec;
import com.xing.android.sdk.HttpError;
import com.xing.android.sdk.Resource;
import com.xing.android.sdk.XingApi;
import com.xing.android.sdk.model.user.ProfileVisit;

import java.util.List;

/**
 * This is a representation of the Profile Visits API.
 * See <a href="https://dev.xing.com/docs/resources#profile-visits"></a>
 *
 * @author daniel.hartwich
 */
public class ProfileVisitsResource extends Resource {
    /**
     * Creates a resource instance. This should be the only constructor declared by child classes.
     */
    protected ProfileVisitsResource(XingApi api) {
        super(api);
    }

    /**
     * Get profile visits.
     *
     * Returns a list of users who recently visited the specified user’s profile. Entries with a value of null in the
     * user_id attribute indicate anonymous (non-XING) users (e.g. resulting from Google searches). Any blacklisted
     * users returned will contain null values in the same way as the empty user profile.
     */
    public CallSpec<List<ProfileVisit>, HttpError> getProfileVisits(String userId) {
        return Resource.<List<ProfileVisit>, HttpError>newGetSpec(api, "/v1/users/{user_id}/visits")
              .pathParam("user_id", userId)
              .responseAsListOf(ProfileVisit.class)
              .build();
    }

    /**
     * Generate a profile visit.
     *
     * The visiting user will be derived from the user executing the call, and the visit reason derived from the
     * consumer.
     *
     * @param userId The user which is visited by the consumer of this call
     */
    public CallSpec<String, HttpError> createProfileVisits(String userId) {
        return Resource.<String, HttpError>newPostSpec(api, "v1/users/{user_id}/visits", true)
              .pathParam("user_id", userId)
              .responseAs(String.class)
              .build();
    }
}
