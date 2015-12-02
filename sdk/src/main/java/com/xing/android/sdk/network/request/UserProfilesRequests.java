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
package com.xing.android.sdk.network.request;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Pair;

import com.xing.android.sdk.json.FieldUtils;
import com.xing.android.sdk.model.user.field.XingUserField;
import com.xing.android.sdk.network.XingController;
import com.xing.android.sdk.internal.Experimental;
import com.xing.android.sdk.network.info.Optional;
import com.xing.android.sdk.network.oauth.OauthSigner;
import com.xing.android.sdk.network.request.exception.NetworkException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Requests for user profiles.
 *
 * @author serj.lotutovici
 * @see <a href="https://dev.xing.com/docs/resources#user-profiles">https://dev.xing
 * .com/docs/resources#user-profiles</a>
 */
public final class UserProfilesRequests {
    /** Key for keywords param. Used in find request. */
    public static final String KEYWORDS_PARAM = "keywords";
    /** Resource for GET user details request. */
    private static final Uri GET_USER_DETAILS_RESOURCE = Uri.parse("v1/users/");
    /** Resource for GET user ID_CARD request. */
    private static final MessageFormat GET_USER_ID_CARD_RESOURCE = new MessageFormat("v1/users/{0}/id_card");
    /** Resource for GET user profile message request. */
    private static final MessageFormat GET_USER_PROFILE_MSG_RESOURCE =
            new MessageFormat("v1/users/{0}/profile_message");
    /** Resource for GET user legal information request. */
    private static final MessageFormat GET_USER_LEGAL_INFO_RESOURCE =
            new MessageFormat("v1/users/{0}/legal_information");
    /** Resource for find users by email request. */
    private static final Uri FIND_USERS_BY_EMAIL_RESOURCE = Uri.parse("v1/users/find_by_emails");
    /** Resource for find user request. */
    private static final Uri FIND_RESOURCE = Uri.parse("v1/users/find");
    /** Key for field param. Used in details requests. */
    private static final String FIELDS_PARAM = "fields";
    /** Key for emails param. Used in find by email request. */
    private static final String EMAILS_PARAM = "emails";
    /** Me value. */
    private static final String ME_VALUE = "me";

    /**
     * Request the user details for the provided id. If no id is provided the "/v1/users/me"
     * will be called see {@link UserProfilesRequests#detailsMe(List)}.
     *
     * @param id ID of requested user
     * @param fields List of fields that should be returned by the response (optional)
     * @return The json response as a string value
     *
     * @throws NetworkException If a network or server error occurs.
     * @throws OauthSigner.XingOauthException If the user is not authenticated
     * @see <a href="https://dev.xing.com/docs/get/users/:id">https://dev.xing.com/docs/get/users/:id</a>
     */
    public static String details(@Nullable String id, @Nullable List<XingUserField> fields)
            throws NetworkException, OauthSigner.XingOauthException {

        return XingController.getInstance().execute(buildDetailsRequest(id, fields));
    }

    /**
     * Request the user details for provided ids. If no id is provided the "/v1/users/me"
     * will be called, see {@link UserProfilesRequests#detailsMe(List)}.
     *
     * @param ids ID(s) of requested user(s) (must contain no more that 100 ids)
     * @param fields List of user attributes to return (optional)
     * @return The json response as a string value
     *
     * @throws NetworkException If a network or server error occurs.
     * @throws OauthSigner.XingOauthException If the user is not authenticated
     * @see <a href="https://dev.xing.com/docs/get/users/:id">https://dev.xing.com/docs/get/users/:id</a>
     */
    public static String details(@Nullable List<String> ids, @Nullable List<XingUserField> fields)
            throws NetworkException, OauthSigner.XingOauthException {

        return XingController.getInstance().execute(buildDetailsRequest(ids, fields));
    }

    /**
     * Request details for the currently logged in user.
     *
     * @param fields List of fields that should be returned by the response (optional)
     * @return The json response as a string value
     *
     * @throws NetworkException If a network or server error occurs.
     * @throws OauthSigner.XingOauthException If the user is not authenticated
     * @see <a href="https://dev.xing.com/docs/get/users/:id">https://dev.xing.com/docs/get/users/:id</a>
     */
    public static String detailsMe(@Nullable List<XingUserField> fields)
            throws NetworkException, OauthSigner.XingOauthException {
        return details(ME_VALUE, fields);
    }

    /**
     * Build request object for GET user(s) details request.
     *
     * @param id ID of requested user
     * @param fields List of user attributes to return (optional)
     * @return A new request object
     *
     * @see <a href="https://dev.xing.com/docs/get/users/:id">https://dev.xing.com/docs/get/users/:id</a>
     */
    public static Request buildDetailsRequest(@Nullable String id, @Nullable List<XingUserField> fields) {

        // Add request fields if necessary
        List<Pair<String, String>> params = new ArrayList<>(0);
        if (fields != null && !fields.isEmpty()) {
            params.add(new Pair<>(FIELDS_PARAM, FieldUtils.formatFieldsToString(fields)));
        }

        // Build request
        return new Request.Builder(Request.Method.GET).setUri(
                RequestUtils.appendSegmentToUri(GET_USER_DETAILS_RESOURCE, !TextUtils.isEmpty(id) ? id : ME_VALUE))
                .addParams(params)
                .build();
    }

    /**
     * Build request object for GET user(s) details request.
     *
     * @param ids ID(s) of requested user(s) (must contain no more that 100 ids)
     * @param fields List of user attributes to return (optional)
     * @return A new request object
     *
     * @see <a href="https://dev.xing.com/docs/get/users/:id">https://dev.xing.com/docs/get/users/:id</a>
     */
    public static Request buildDetailsRequest(@Nullable List<String> ids, @Nullable List<XingUserField> fields) {
        return buildDetailsRequest(RequestUtils.createCommaSeparatedStringFromStringList(ids), fields);
    }

    /**
     * Get user's id card.
     *
     * @param userId The users id
     * @return A string value representing the response for the request
     *
     * @throws NetworkException If a network or server error occurs.
     * @throws OauthSigner.XingOauthException If the user is not authenticated
     * @see <a href="https://dev.xing.com/docs/get/users/me/id_card">https://dev.xing.com/docs/get/users/me/id_card</a>
     */
    public static String userIdCard(@Nullable String userId) throws NetworkException, OauthSigner.XingOauthException {

        return XingController.getInstance().execute(buildUserIdCardRequest(userId));
    }

    /**
     * Build a request object for the GET user's id card request.
     *
     * @param userId The users id
     * @return A new request object
     *
     * @see <a href="https://dev.xing.com/docs/get/users/me/id_card">https://dev.xing.com/docs/get/users/me/id_card</a>
     */
    public static Request buildUserIdCardRequest(@Nullable String userId) {
        return buildRequestWithId(GET_USER_ID_CARD_RESOURCE, userId);
    }

    /**
     * Request the user profile message by id. If the user id is not specified then '/v1/users/me/profile_message'
     * will be called.
     *
     * @param userId The user id for who the profile is requested
     * @return The request response as a string value
     *
     * @throws NetworkException If a network or server error occurs.
     * @throws OauthSigner.XingOauthException If the user is not authenticated
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/profile_message">https://dev.xing
     * .com/docs/get/users/:user_id/profile_message</a>
     */
    public static String profileMessage(@Nullable @Optional String userId)
            throws NetworkException, OauthSigner.XingOauthException {

        return XingController.getInstance().execute(buildProfileMessageRequest(userId));
    }

    /**
     * Build request object for GET user's profile message.
     *
     * @param userId The users id
     * @return A new request object
     *
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/profile_message">https://dev.xing
     * .com/docs/get/users/:user_id/profile_message</a>
     */
    public static Request buildProfileMessageRequest(@Nullable String userId) {
        return buildRequestWithId(GET_USER_PROFILE_MSG_RESOURCE, userId);
    }

    /**
     * Request the users legal information by id. If the user id is not specified then
     * 'v1/users/me/legal_information' will me called.
     *
     * @param userId The user id who's legal info is requested
     * @return The request response as a string value
     *
     * @throws NetworkException If a network or server error occurs.
     * @throws OauthSigner.XingOauthException If the user is not authenticated
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/legal_information">https://dev.xing
     * .com/docs/get/users/:user_id/legal_information</a>
     */
    public static String legalInformation(@NonNull String userId)
            throws NetworkException, OauthSigner.XingOauthException {

        return XingController.getInstance().execute(buildLegalInfoRequest(userId));
    }

    /**
     * Build request object for GET user's legal info.
     *
     * @param userId The users id
     * @return A new request object
     *
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/legal_information">https://dev.xing
     * .com/docs/get/users/:user_id/legal_information</a>
     */
    public static Request buildLegalInfoRequest(@Nullable String userId) {
        return buildRequestWithId(GET_USER_LEGAL_INFO_RESOURCE, userId);
    }

    /**
     * Request for a search query by user emails.
     *
     * @param emails The list of emails to search with
     * @param fields List of fields that should be returned by the response (optional)
     * @return The request response as a string value
     *
     * @throws NetworkException If a network or server error occurs.
     * @throws OauthSigner.XingOauthException If the user is not authenticated
     * @see <a href="https://dev.xing.com/docs/get/users/find_by_emails">https://dev.xing
     * .com/docs/get/users/find_by_emails</a>
     */
    public static String findByEmail(@NonNull List<String> emails, @Nullable List<XingUserField> fields)
            throws NetworkException, OauthSigner.XingOauthException {

        return XingController.getInstance().execute(buildFindByEmailRequest(emails, fields));
    }

    /**
     * Build a request object for the find by email request.
     *
     * @param emails The list of emails to search with
     * @param fields List of fields that should be returned by the response (optional)
     * @return A new request object
     *
     * @see <a href="https://dev.xing.com/docs/get/users/find_by_emails">https://dev.xing
     * .com/docs/get/users/find_by_emails</a>
     */
    private static Request buildFindByEmailRequest(@NonNull List<String> emails, @Nullable List<XingUserField> fields) {
        return new Request.Builder(Request.Method.GET).setUri(FIND_USERS_BY_EMAIL_RESOURCE)
                .addParams(buildFindByEmailParams(emails, fields))
                .build();
    }

    /**
     * Build params for the find by email request.
     *
     * @param emails The list of emails to search with
     * @param fields List of fields that should be returned by the response (optional)
     * @return A list containing validated params
     */
    private static Collection<Pair<String, String>> buildFindByEmailParams(@NonNull List<String> emails,
            @Nullable List<XingUserField> fields) {
        List<Pair<String, String>> params = new ArrayList<>(0);
        params.add(new Pair<>(EMAILS_PARAM, RequestUtils.createCommaSeparatedStringFromStringList(emails)));
        params.add(new Pair<>(RequestUtils.USER_FIELDS_PARAM, FieldUtils.formatFieldsToString(fields)));
        return params;
    }

    /**
     * Executes a request that returns the list of users found in accordance with the given list of keywords.
     * <p/>
     * <b>Note: </b> In order to get access to the User Search API, please
     * send an email to api-support@xing.com with the subject "Request for User Search API".
     * We will then get back to you with further information.
     * <p/>
     *
     * @param keywords List of keywords. An empty result will be returned if no keywords provided.
     * @param limit Restricts the number of profile visits to be returned. This must be a positive number.
     * Default: 10, Maximum: 100
     * @param offset Offset. This must be a positive number. Default: 0
     * @param fields List of user attributes to return in nested user objects. If this parameter is not used,
     * only the ID's will be returned.
     * @return The request response as a string value
     *
     * @throws NetworkException If a network or server error occurs.
     * @throws OauthSigner.XingOauthException If the user is not authenticated
     * @see <a href="https://dev.xing.com/docs/get/users/find_by_emails">https://dev.xing
     * .com/docs/get/users/find_by_emails</a>
     */
    @Experimental
    public static String find(@Nullable List<String> keywords, @Nullable Integer limit, @Nullable Integer offset,
            @Nullable List<XingUserField> fields) throws NetworkException, OauthSigner.XingOauthException {

        return XingController.getInstance().execute(buildFindRequest(keywords, limit, offset, fields));
    }

    /**
     * Build request object for find by keyword request.
     *
     * @param keywords List of keywords. An empty result will be returned if no keywords provided.
     * @param limit Restricts the number of profile visits to be returned. This must be a positive number.
     * Default: 10, Maximum: 100
     * @param offset Offset. This must be a positive number. Default: 0
     * @param fields List of user attributes to return in nested user objects. If this parameter is not used,
     * only the ID's will be returned.
     * @return A new request object
     *
     * @see <a href="https://dev.xing.com/docs/get/users/find_by_emails">https://dev.xing
     * .com/docs/get/users/find_by_emails</a>
     */
    @Experimental
    private static Request buildFindRequest(@Nullable List<String> keywords, @Nullable Integer limit,
            @Nullable Integer offset, @Nullable List<XingUserField> fields) {
        return new Request.Builder(Request.Method.GET).setUri(FIND_RESOURCE)
                .addParams(buildFindRequestParams(keywords, limit, offset, fields))
                .build();
    }

    /**
     * Build params for the find request.
     *
     * @param keywords List of keywords. An empty result will be returned if no keywords provided.
     * @param limit Restricts the number of profile visits to be returned. This must be a positive number.
     * Default: 10, Maximum: 100
     * @param offset Offset. This must be a positive number. Default: 0
     * @param fields List of user attributes to return in nested user objects. If this parameter is not used,
     * only the ID's will be returned.
     * @return A list of validated params
     */
    private static Collection<Pair<String, String>> buildFindRequestParams(@Nullable List<String> keywords,
            @Nullable Integer limit, @Nullable Integer offset, @Nullable List<XingUserField> fields) {

        List<Pair<String, String>> params = new LinkedList<>();

        if (keywords != null && !keywords.isEmpty()) {
            params.add(new Pair<>(KEYWORDS_PARAM, RequestUtils.createCommaSeparatedStringFromStringList(keywords)));
        }

        if (limit != null && limit > 0) {
            params.add(new Pair<>(RequestUtils.LIMIT_PARAM, Integer.toString(limit)));
        }

        if (offset != null && offset > 0) {
            params.add(new Pair<>(RequestUtils.OFFSET_PARAM, Integer.toString(offset)));
        }

        if (fields != null && !fields.isEmpty()) {
            params.add(new Pair<>(FIELDS_PARAM, FieldUtils.formatFieldsToString(fields)));
        }

        return params;
    }

    /**
     * Build request object form string resource and provided id. In case the passed id will be null the method will
     * substitute it with {@link UserProfilesRequests#ME_VALUE}.
     *
     * @param resource The resource to use
     * @param userId The user id to append to the resource
     * @return A new request object
     */
    private static Request buildRequestWithId(@NonNull MessageFormat resource, @Nullable String userId) {
        String uri;
        if (TextUtils.isEmpty(userId)) {
            uri = resource.format(new String[]{ME_VALUE});
        } else {
            uri = resource.format(new String[]{userId});
        }

        return new Request.Builder(Request.Method.GET).setUri(Uri.parse(uri)).build();
    }

    private UserProfilesRequests() {
        throw new AssertionError("No instances.");
    }
}
