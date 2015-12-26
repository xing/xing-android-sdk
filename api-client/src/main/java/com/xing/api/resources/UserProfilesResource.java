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
import com.xing.api.internal.Experimental;
import com.xing.api.model.IdCard;
import com.xing.api.model.SearchResult;
import com.xing.api.model.user.ProfileMessage;
import com.xing.api.model.user.XingUser;

import java.util.List;

/**
 * This is a representation of the User Profile API.
 * See <a href="https://dev.xing.com/docs/resources#user-profiles"></a>
 *
 * @author serj.lotutovici
 * @author daniel.hartwich
 */
public class UserProfilesResource extends Resource {
    private static final String ME = "me";

    /**
     * Creates a resource instance. This should be the only constructor declared by child classes.
     *
     * @param api An instance of XingApi
     */
    protected UserProfilesResource(XingApi api) {
        super(api);
    }

    /**
     * Shows a particular user’s profile. The data returned by this call will be checked against and filtered on the
     * basis of the privacy settings of the requested user.
     *
     * Possible optional query parameters are:
     * fields - List of user attributes to return.
     *
     * @param id The ID of the user you want to have the profile from
     * @return A CallSpec object which can be executed, enqueued or run with RX Java
     */
    public CallSpec<XingUser, HttpError> getUsersById(String id) {
        return Resource.<XingUser, HttpError>newGetSpec(api, "/v1/users/{id}")
              .pathParam("id", id)
              .responseAsFirst(XingUser.class, "users")
              .build();
    }

    /**
     * Shows the profile of the user who has granted access to an API consumer. The response format equals the one
     * depicted in the get user details call, but you will only get access to the XING profile of the authorizing user.
     *
     * Possible optional query parameters are:
     * fields - List of user attributes to return.
     *
     * @return A CallSpec object which can be executed, enqueued or run with RX Java
     */
    public CallSpec<XingUser, HttpError> getYourProfile() {
        return getUsersById(ME);
    }

    /**
     * Shows minimal profile information of the user that authorized the consumer. If you need more user details please
     * also have a look at the get user details and the get app user’s details call.
     *
     * @return A CallSpec object which can be executed, enqueued or run with RX Java
     */
    public CallSpec<IdCard, HttpError> getYourIdCard() {
        return Resource.<IdCard, HttpError>newGetSpec(api, "/v1/users/me/id_card")
              .responseAs(IdCard.class, "id_card")
              .build();
    }

    /**
     * Returns the list of users that belong directly to the given list of email addresses. The users will be returned
     * in the same order as the corresponding email addresses. If addresses are invalid or no user was found, the user
     * will be returned with the value null.
     *
     * Possible optional query parameters are:
     * hash_function - Consider values of the emails field to be hashed using the specified function. Currently
     * supported is only
     * user_fields - List of user attributes to return in nested user objects. If this parameter is not used, only the
     * ID will be returned.
     * “MD5”.
     *
     * @param emails Comma-seperated list of email addresses to search for
     * @return A CallSpec object which can be executed, enqueued or run with RX Java
     */
    public CallSpec<List<XingUser>, HttpError> findUsersByEmail(String emails) {
        return Resource.<List<XingUser>, HttpError>newGetSpec(api, "/v1/users/find_by_emails")
              .pathParam("emails", emails)
              .responseAsListOf(XingUser.class, "results", "items")
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
     * @return A CallSpec object which can be executed, enqueued or run with RX Java
     */
    @Experimental
    public CallSpec<List<SearchResult>, HttpError> findUsersByKeyword(String keywords) {
        return Resource.<List<SearchResult>, HttpError>newGetSpec(api, "/v1/users/find")
              .responseAsListOf(SearchResult.class, "users", "items")
              .queryParam("keywords", keywords)
              .build();
    }

    /**
     * Get the recent profile message for the user with the given ID.
     *
     * @return A CallSpec object which can be executed, enqueued or run with RX Java
     */
    public CallSpec<ProfileMessage, HttpError> getUserProfileMessage(String userId) {
        return Resource.<ProfileMessage, HttpError>newGetSpec(api, "/v1/{user_id}/profile_message")
              .pathParam("user_id", userId)
              .responseAs(ProfileMessage.class, "profile_message")
              .build();
    }

    /**
     * Get your recent profile message.
     *
     * @return A CallSpec object which can be executed, enqueued or run with RX Java
     */
    public CallSpec<ProfileMessage, HttpError> getYourProfileMessage() {
        return getUserProfileMessage(ME);
    }

    /**
     * Fetch legal information of a user.
     *
     * @param userId The ID of the user from whom you want to receive the legal information
     * @return A CallSpec object which can be executed, enqueued or run with RX Java
     */
    public CallSpec<String, HttpError> getLegalInformation(String userId) {
        return Resource.<String, HttpError>newGetSpec(api, "/v1/users/{user_id}/legal_information")
              .pathParam("user_id", userId)
              .responseAs(String.class, "legal_information", "content")
              .build();
    }

    /**
     * Fetch legal information of the authorized user.
     *
     * @return A CallSpec object which can be executed, enqueued or run with RX Java
     */
    public CallSpec<String, HttpError> getYourLegalInformation() {
        return getLegalInformation(ME);
    }
}
