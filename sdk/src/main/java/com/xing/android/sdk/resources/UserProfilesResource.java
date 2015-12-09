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
import com.xing.android.sdk.model.IdCard;
import com.xing.android.sdk.model.SearchResult;
import com.xing.android.sdk.model.user.LegalInformation;
import com.xing.android.sdk.model.user.XingUser;

import java.util.List;

/**
 * @author serj.lotutovici
 */
public class UserProfilesResource extends Resource {
    /** Creates a resource instance. This should be the only constructor declared by child classes. */
    protected UserProfilesResource(XingApi api) {
        super(api);
    }

    //     TODO Use this as an example.

    /**
     * Shows a particular user’s profile. The data returned by this call will be checked against and filtered on the
     * basis of the privacy settings of the requested user.
     */
    public CallSpec<XingUser, String> getUsersById(String id) {
        return Resource.<XingUser, String>newGetSpec(api, "/v1/users/{id}")
              .pathParam("id", id)
              .responseAsFirst(XingUser.class, "users")
              .build();
    }

    /**
     * Shows the profile of the user who has granted access to an API consumer. The response format equals the one
     * depicted in the get user details call, but you will only get access to the XING profile of the authorizing user.
     */
    public CallSpec<XingUser, Object> getYourProfile() {
        return Resource.<XingUser, Object>newGetSpec(api, "/v1/users/me")
              .responseAsFirst(XingUser.class, "users")
              .build();
    }

    /**
     * Shows minimal profile information of the user that authorized the consumer. If you need more user details please
     * also have a look at the get user details and the get app user’s details call.
     */
    public CallSpec<IdCard, Object> getYourIdCard() {
        return Resource.<IdCard, Object>newGetSpec(api, "/v1/users/me/id_card")
              .responseAs(IdCard.class, "id_card")
              .build();
    }

    /**
     * Fetch legal information of the authorized user.
     */
    public CallSpec<LegalInformation, Object> getYourLegalInformation() {
        return Resource.<LegalInformation, Object>newGetSpec(api, "/v1/users/me/legal_information")
              .responseAs(LegalInformation.class, "legal_information")
              .build();
    }

    /**
     * Fetch legal information of a user.
     */
    public CallSpec<LegalInformation, Object> getLegalInformation(String id) {
        return Resource.<LegalInformation, Object>newGetSpec(api, "/v1/users/{id}/legal_information")
              .pathParam("id", id)
              .responseAs(LegalInformation.class, "legal_information")
              .build();
    }

    /**
     * Returns the list of users found in accordance with the given list of keywords.
     *
     * This call currently has EXPERIMENTAL status. You shouldn’t use it in production environments as it
     * may be missing some functionality, and both input and output interfaces are subject to change. Only certain
     * consumers are granted access to experimental calls. When in doubt, please contact the API team.
     *
     * @param keywords A String representing the keywords you want to search for, if this is empty the search will
     * return and empty set
     */
    public CallSpec<List<SearchResult>, Object> findUsersByKeyword(String keywords) {
        return Resource.<List<SearchResult>, Object>newGetSpec(api, "/v1/users/find")
              .responseAsListOf(SearchResult.class, "users", "items")
              .queryParam("keywords", keywords)
              .build();
    }
}
