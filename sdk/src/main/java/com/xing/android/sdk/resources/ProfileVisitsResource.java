/*
 * Copyright (c) 2015 XING AG (http://xing.com/)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.xing.android.sdk.resources;

import com.xing.android.sdk.CallSpec;
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
    public CallSpec<List<ProfileVisit>, Object> getProfileVisits(String userId) {
        return Resource.<List<ProfileVisit>, Object>newGetSpec(api, "/v1/users/{user_id}/visits")
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
    public CallSpec<String, String> createProfileVisits(String userId) {
        return Resource.<String, String>newPostSpec(api, "v1/users/{user_id}/visits", true)
              .pathParam("user_id", userId)
              .responseAs(String.class)
              .build();
    }
}
