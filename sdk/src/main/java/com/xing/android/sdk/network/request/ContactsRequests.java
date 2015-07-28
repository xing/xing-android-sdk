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
import com.xing.android.sdk.model.CalendarUtils;
import com.xing.android.sdk.model.XingCalendar;
import com.xing.android.sdk.model.user.field.XingUserField;
import com.xing.android.sdk.network.XingController;
import com.xing.android.sdk.network.oauth.OauthSigner;
import com.xing.android.sdk.network.request.exception.NetworkException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Requests for the Contacts section
 *
 * @author serj.lotutovici
 * @see <a href="https://dev.xing.com/docs/resources#contacts">https://dev.xing.com/docs/resources#contacts</a>
 */
public final class ContactsRequests {

    /** Resource for GET contacts request */
    private static final MessageFormat GET_CONTACTS_RESOURCE = new MessageFormat("v1/users/{0}/contacts");

    /** Resource for GET contacts ids request */
    private static final Uri GET_CONTACT_IDS_RESOURCE = Uri.parse("v1/users/me/contact_ids");

    /** Resource for GET assigned tags request */
    private static final MessageFormat GET_ASSIGNED_TAGS_RESOURCE = new MessageFormat("v1/users/{0}/contacts/{1}/tags");

    /** Resource for GET shared contacts request */
    private static final MessageFormat GET_SHARED_CONTACTS_RESOURCE = new MessageFormat("v1/users/:user_id/contacts/shared");

    /** Key for changed since param. Used in GET contacts request */
    public static final String CHANGED_SINCE_PARAM = "changed_since";

    /** Key for order by param. User in GET contacts and shared contacts requests */
    public static final String ORDER_BY_PARAM = "order_by";

    /**
     * Request user's contacts.
     *
     * @param userId       ID of the user who assigned the tags.
     * @param changedSince Filter contacts that changed since the specified date
     * @param limit        Restrict the number of attachments to be returned. Default: 10.
     * @param offset       Offset. This must be a positive number. Default: 0
     * @param orderBy      Field that determines the ascending order of the returned list
     * @param fields       List of user attributes to return
     * @return The json as a string with the user's contacts
     * @throws NetworkException               If a network or server error occurs.
     * @throws OauthSigner.XingOauthException If the user is not authenticated
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/contacts">https://dev.xing.com/docs/get/users/:user_id/contacts</a>
     */
    public static String contacts(@NonNull String userId,
                                  @Nullable XingCalendar changedSince,
                                  @Nullable Integer limit,
                                  @Nullable Integer offset,
                                  @Nullable String orderBy,
                                  @Nullable List<XingUserField> fields) throws
            NetworkException,
            OauthSigner.XingOauthException {

        return XingController.getInstance().execute(buildContactsRequest(
                userId,
                changedSince,
                limit,
                offset,
                orderBy,
                fields
        ));
    }

    /**
     * Build request object for the GET contacts request
     *
     * @param userId       ID of the user who assigned the tags.
     * @param changedSince Filter contacts that changed since the specified date
     * @param limit        Restrict the number of attachments to be returned. Default: 10.
     * @param offset       Offset. This must be a positive number. Default: 0
     * @param orderBy      Field that determines the ascending order of the returned list
     * @param fields       List of user attributes to return
     * @return A new GET request object
     */
    public static Request buildContactsRequest(@NonNull String userId,
                                               @Nullable XingCalendar changedSince,
                                               @Nullable Integer limit,
                                               @Nullable Integer offset,
                                               @Nullable String orderBy,
                                               @Nullable List<XingUserField> fields) {

        return new Request.Builder(Request.Method.GET)
                .setUri(Uri.parse(GET_CONTACTS_RESOURCE.format(new Object[]{userId})))
                .addParams(buildContactsRequestParams(changedSince, limit, offset, orderBy, fields))
                .build();
    }

    /**
     * Build params for GET contracts request
     *
     * @param changedSince Filter contacts that changed since the specified date
     * @param limit        Restrict the number of attachments to be returned. Default: 10.
     * @param offset       Offset. This must be a positive number. Default: 0
     * @param orderBy      Field that determines the ascending order of the returned list
     * @param fields       List of user attributes to return
     * @return A list of all valid parameters
     */
    private static Collection<Pair<String, String>> buildContactsRequestParams(@Nullable XingCalendar changedSince,
                                                                               @Nullable Integer limit,
                                                                               @Nullable Integer offset,
                                                                               @Nullable String orderBy,
                                                                               @Nullable List<XingUserField> fields) {

        List<Pair<String, String>> params = new ArrayList<>(5);

        if (changedSince != null) {
            params.add(new Pair<>(CHANGED_SINCE_PARAM, CalendarUtils.calendarToTimestamp(changedSince)));
        }

        if (limit != null && limit >= 0) {
            params.add(new Pair<>(RequestUtils.LIMIT_PARAM, Integer.toString(limit)));
        }

        if (offset != null && offset > 0) {
            params.add(new Pair<>(RequestUtils.OFFSET_PARAM, Integer.toString(offset)));
        }

        if (!TextUtils.isEmpty(orderBy)) {
            params.add(new Pair<>(ORDER_BY_PARAM, orderBy));
        }

        if (fields != null && !fields.isEmpty()) {
            params.add(new Pair<>(
                            RequestUtils.USER_FIELDS_PARAM,
                            FieldUtils.formatFieldsToString(fields)
                    )
            );
        }

        return params;
    }

    /**
     * Get the current user's contact IDs
     *
     * @return The json as a string with a list of the user's contacts ids
     * @throws NetworkException               If a network or server error occurs.
     * @throws OauthSigner.XingOauthException If the user is not authenticated
     * @see <a href="https://dev.xing.com/docs/get/users/me/contact_ids">https://dev.xing.com/docs/get/users/me/contact_ids</a>
     */
    public static String contactIds() throws NetworkException, OauthSigner.XingOauthException {
        return XingController.getInstance().execute(
                buildContactIdsRequest()
        );
    }

    /**
     * Build request object for GET current user's contact IDs
     */
    public static Request buildContactIdsRequest() {
        return new Request.Builder(Request.Method.GET)
                .setUri(GET_CONTACT_IDS_RESOURCE)
                .build();
    }

    /**
     * Retrieve assigned tags
     *
     * @param userId    ID of the user who assigned the tags.
     * @param contactId ID of the users contact
     * @return The json as a string with the assigned tags
     * @throws NetworkException               If a network or server error occurs.
     * @throws OauthSigner.XingOauthException If the user is not authenticated
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/contacts/:contact_id/tags">https://dev.xing.com/docs/get/users/:user_id/contacts/:contact_id/tags</a>
     */
    public static String assignedTags(@NonNull String userId,
                                      @NonNull String contactId) throws
            NetworkException,
            OauthSigner.XingOauthException {

        return XingController.getInstance().execute(
                buildAssignedTagsRequest(userId, contactId)
        );
    }

    /**
     * Build request object for GET assigned tags
     *
     * @param userId    ID of the user who assigned the tags.
     * @param contactId ID of the users contact
     * @return A new request object
     */
    public static Request buildAssignedTagsRequest(@NonNull String userId,
                                                    @NonNull String contactId) {
        return new Request.Builder(Request.Method.GET)
                .setUri(Uri.parse(GET_ASSIGNED_TAGS_RESOURCE.format(new Object[]{userId, contactId})))
                .build();
    }

    /**
     * Get shared contacts
     *
     * @param userId  ID of the user who assigned the tags
     * @param limit   Restrict the number of attachments to be returned. This must be a positive number.
     *                Default: 10
     * @param offset  Offset. This must be a positive number. Default: 0
     * @param orderBy Field that determines the ascending order of the returned list
     * @param fields  List of user attributes to return
     * @return The json as a string with the list of shared contacts
     * @throws NetworkException               If a network or server error occurs.
     * @throws OauthSigner.XingOauthException If the user is not authenticated
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/contacts/shared">https://dev.xing.com/docs/get/users/:user_id/contacts/shared</a>
     */
    public static String sharedContacts(@NonNull String userId,
                                         @Nullable Integer limit,
                                         @Nullable Integer offset,
                                         @Nullable String orderBy,
                                         @Nullable List<XingUserField> fields) throws
            NetworkException,
            OauthSigner.XingOauthException {

        return XingController.getInstance().execute(
                buildSharedContactsRequest(userId, limit, offset, orderBy, fields)
        );
    }

    /**
     * Build request object for GET shared contacts
     *
     * @param userId  ID of the user who assigned the tags
     * @param limit   Restrict the number of attachments to be returned. This must be a positive number.
     *                Default: 10
     * @param offset  Offset. This must be a positive number. Default: 0
     * @param orderBy Field that determines the ascending order of the returned list
     * @param fields  List of user attributes to return
     * @return A new request object
     */
    public static Request buildSharedContactsRequest(@NonNull String userId,
                                                      @Nullable Integer limit,
                                                      @Nullable Integer offset,
                                                      @Nullable String orderBy,
                                                      @Nullable List<XingUserField> fields) {

        return new Request.Builder(Request.Method.GET)
                .setUri(Uri.parse(GET_SHARED_CONTACTS_RESOURCE.format(new Object[]{userId})))
                .addParams(buildSharedContactsRequestParams(limit, offset, orderBy, fields))
                .build();
    }

    /**
     * Build params for GET shared contacts request
     *
     * @param limit   Restrict the number of attachments to be returned. Default: 10.
     * @param offset  Offset. This must be a positive number. Default: 0
     * @param orderBy Field that determines the ascending order of the returned list
     * @param fields  List of user attributes to return
     * @return A list of all valid parameters
     */
    private static Collection<Pair<String, String>> buildSharedContactsRequestParams(@Nullable Integer limit,
                                                                                     @Nullable Integer offset,
                                                                                     @Nullable String orderBy,
                                                                                     @Nullable List<XingUserField> fields) {

        List<Pair<String, String>> params = new ArrayList<>(4);

        if (limit != null && limit > 0) {
            params.add(new Pair<>(RequestUtils.LIMIT_PARAM, Integer.toString(limit)));
        }

        if (offset != null && offset > 0) {
            params.add(new Pair<>(RequestUtils.OFFSET_PARAM, Integer.toString(offset)));
        }

        if (!TextUtils.isEmpty(orderBy)) {
            params.add(new Pair<>(ORDER_BY_PARAM, orderBy));
        }

        if (fields != null && !fields.isEmpty()) {
            params.add(new Pair<>(
                            RequestUtils.USER_FIELDS_PARAM,
                            FieldUtils.formatFieldsToString(fields)
                    )
            );
        }

        return params;
    }

    /** No instance should be made */
    private ContactsRequests() {
    }
}
