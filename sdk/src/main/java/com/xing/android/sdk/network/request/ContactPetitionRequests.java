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
import android.util.JsonWriter;
import android.util.Pair;

import com.xing.android.sdk.json.FieldUtils;
import com.xing.android.sdk.json.XingJsonException;
import com.xing.android.sdk.model.user.field.XingUserField;
import com.xing.android.sdk.network.XingController;
import com.xing.android.sdk.network.oauth.OauthSigner;
import com.xing.android.sdk.network.request.exception.NetworkException;

import java.io.IOException;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Request for the contact petitions section.
 *
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/resources#contact-requests">https://dev.xing.com/docs/resources#contact-requests</a>
 */
public final class ContactPetitionRequests {

    /**
     * Resource for the incoming contact petition requests.
     */
    private static final MessageFormat INCOMING_CONTACT_PETITIONS_RESOURCE = new MessageFormat("/v1/users/{0}/contact_requests");

    /**
     * Resource for the sent contact petition requests.
     */
    private static final MessageFormat SENT_CONTACT_PETITIONS_RESOURCE = new MessageFormat("/v1/users/{0}/contact_requests/sent");

    /**
     * Resource for the accept contact petition and decline contact petition requests.
     */
    private static final MessageFormat ACCEPT_OR_DECLINE_CONTACT_PETITIONS_RESOURCE = new MessageFormat("/v1/users/{0}/contact_requests/{1}/accept");

    /**
     * Key for the recipient_id parameter. Used on sent contact petitions.
     */
    private static final String RECIPIENT_ID_PARAM = "recipient_id";

    /**
     * Key for the message parameter. Used on create contact petition.
     */
    private static final String MESSAGE_PARAM = "message";

    private ContactPetitionRequests() {
    }

    /**
     * Creates and executes the request to get the incoming contact petitions.
     *
     * @param userId     ID of the user whose incoming contact petitions are to be returned.
     * @param limit      Restricts the number of contact petitions to be returned. This must be a positive number. Default: 10.
     * @param offset     Offset. This must be a positive number. Default: 0.
     * @param userFields List of user attributes to return.
     * @return Result of the execution of the request, in Json format.
     * @throws NetworkException               Error produced during the network connection.
     * @throws OauthSigner.XingOauthException Error because of an Oauth problem.
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/contact_requests">https://dev.xing.com/docs/get/users/:user_id/contact_requests</a>
     */
    public static String incomingContactPetitions(@NonNull String userId,
                                                  @Nullable Integer limit,
                                                  @Nullable Integer offset,
                                                  @Nullable List<XingUserField> userFields)
            throws NetworkException, OauthSigner.XingOauthException {

        return XingController.getInstance().execute(buildIncomingContactPetitionsRequest(userId,
                limit,
                offset,
                userFields));

    }

    /**
     * Creates the request to get the incoming contact petitions.
     *
     * @param userId     ID of the user whose incoming contact petitions are to be returned.
     * @param limit      Restricts the number of contact petitions to be returned. This must be a positive number. Default: 10.
     * @param offset     Offset. This must be a positive number. Default: 0.
     * @param userFields List of user attributes to return.
     * @return Request object ready to be executed.
     */
    public static Request buildIncomingContactPetitionsRequest(@NonNull String userId,
                                                               @Nullable Integer limit,
                                                               @Nullable Integer offset,
                                                               @Nullable List<XingUserField> userFields) {
        return new Request.Builder(Request.Method.GET)
                .setUri(Uri.parse(INCOMING_CONTACT_PETITIONS_RESOURCE.format(userId)))
                .addParams(buildIncomingContactPetitionsParameters(limit, offset, userFields))
                .build();
    }

    /**
     * Creates the list of params for the incoming contact petitions request.
     *
     * @param limit      Restricts the number of contact petitions to be returned. This must be a positive number. Default: 10.
     * @param offset     Offset. This must be a positive number. Default: 0.
     * @param userFields List of user attributes to return.
     * @return List with the params for the incoming contact petitions request, as name-value pair.
     */
    private static List<Pair<String, String>> buildIncomingContactPetitionsParameters(
            @Nullable Integer limit,
            @Nullable Integer offset,
            @Nullable List<XingUserField> userFields) {

        List<Pair<String, String>> params = new ArrayList<>(3);

        if (limit != null && limit > 0) {
            params.add(new Pair<>(RequestUtils.LIMIT_PARAM, Integer.toString(limit)));
        }
        if (offset != null && offset > 0) {
            params.add(new Pair<>(RequestUtils.OFFSET_PARAM, Integer.toString(offset)));
        }
        if (userFields != null) {
            params.add(new Pair<>(RequestUtils.USER_FIELDS_PARAM,
                    FieldUtils.formatFieldsToString(userFields)));
        }

        return params;
    }

    /**
     * Creates and executes the request to get the sent contact petitions.
     *
     * @param userId      ID of the user who sent the contact petitions.
     * @param limit       Restrict the number of contact requests to be returned. This must be a positive number. Default: 10.
     * @param offset      Offset. This must be a positive number. Default: 0.
     * @param recipientId Filter the contact petitions for a given user ID.
     * @return Result of the execution of the request, in Json format.
     * @throws NetworkException               Error produced during the network connection.
     * @throws OauthSigner.XingOauthException Error because of an Oauth problem.
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/contact_requests/sent">https://dev.xing.com/docs/get/users/:user_id/contact_requests/sent</a>
     */
    public static String sentContactPetitions(@NonNull String userId,
                                              @Nullable Integer limit,
                                              @Nullable Integer offset,
                                              @Nullable String recipientId)
            throws NetworkException, OauthSigner.XingOauthException {

        return XingController.getInstance().execute(
                buildSentContactPetitionsRequest(
                        userId,
                        limit,
                        offset,
                        recipientId
                ));
    }

    /**
     * Creates the request to get the sent contact petitions.
     *
     * @param userId      ID of the user who sent the contact petitions.
     * @param limit       Restrict the number of contact requests to be returned. This must be a positive number. Default: 10.
     * @param offset      Offset. This must be a positive number. Default: 0.
     * @param recipientId Filter the contact petitions for a given user ID.
     * @return Request object ready to be executed.
     */
    public static Request buildSentContactPetitionsRequest(@NonNull String userId,
                                                           @Nullable Integer limit,
                                                           @Nullable Integer offset,
                                                           @Nullable String recipientId) {
        return new Request.Builder(Request.Method.GET)
                .setUri(Uri.parse(SENT_CONTACT_PETITIONS_RESOURCE.format(userId)))
                .addParams(buildSentContactPetitionsParameters(limit, offset, recipientId))
                .build();
    }

    /**
     * Creates the list of params for the sent contact petitions request.
     *
     * @param limit       Restrict the number of contact requests to be returned. This must be a positive number. Default: 10.
     * @param offset      Offset. This must be a positive number. Default: 0.
     * @param recipientId Filter the contact petitions for a given user ID.
     * @return List with the params for the sent contact petitions request, as name-value pair.
     */
    private static List<Pair<String, String>> buildSentContactPetitionsParameters(
            @Nullable Integer limit,
            @Nullable Integer offset,
            @Nullable String recipientId) {

        List<Pair<String, String>> params = new ArrayList<>(3);

        if (limit != null && limit > 0) {
            params.add(new Pair<>(RequestUtils.LIMIT_PARAM, Integer.toString(limit)));
        }
        if (offset != null && offset > 0) {
            params.add(new Pair<>(RequestUtils.OFFSET_PARAM, Integer.toString(offset)));
        }
        if (!TextUtils.isEmpty(recipientId)) {
            params.add(new Pair<>(RECIPIENT_ID_PARAM, recipientId));
        }

        return params;
    }

    /**
     * Creates and executes the request to get the incoming contact petitions.
     *
     * @param userId  ID of the user receiving the contact petition.
     * @param message Message attached to the contact petition.
     * @return Result of the execution of the request, in Json format.
     * @throws NetworkException               Error produced during the network connection.
     * @throws OauthSigner.XingOauthException Error because of an Oauth problem.
     * @see <a href="https://dev.xing.com/docs/post/users/:user_id/contact_requests">https://dev.xing.com/docs/post/users/:user_id/contact_requests</a>
     */
    public static String createContactPetition(@NonNull String userId,
                                               @Nullable String message)
            throws NetworkException, OauthSigner.XingOauthException, XingJsonException {

        return XingController.getInstance().execute(buildCreateContactPetitionRequest(userId, message));

    }

    /**
     * Creates the request to create a contact petition.
     *
     * @param userId  ID of the user receiving the contact petition.
     * @param message Message attached to the contact petition.
     * @return Request object ready to be executed.
     */
    public static Request buildCreateContactPetitionRequest(@NonNull String userId,
                                                            @Nullable String message) throws XingJsonException {
        return new Request.Builder(Request.Method.POST)
                .setUri(Uri.parse(INCOMING_CONTACT_PETITIONS_RESOURCE.format(userId)))
                .setBody(buildCreateContactPetitionBody(message))
                .build();
    }

    /**
     * Creates the list of params for the incoming contact petitions request.
     *
     * @param message Restricts the number of contact petitions to be returned. This must be a positive number. Default: 10.
     * @return List with the params for the incoming contact petitions request, as name-value pair.
     */
    private static String buildCreateContactPetitionBody(
            @Nullable String message) throws XingJsonException {

        String body = "";

        if (!TextUtils.isEmpty(message)) {
            try {
                StringWriter stringWriter = new StringWriter();
                JsonWriter jsonWriter = new JsonWriter(stringWriter);
                jsonWriter.beginObject();
                jsonWriter.name(MESSAGE_PARAM).value(message);
                jsonWriter.endObject();
                jsonWriter.close();
            } catch (IOException exception) {
                throw new XingJsonException(ContactPetitionRequests.class.getSimpleName(),
                        "buildCreateContactPetitionBody", exception.getMessage());
            }
        }

        return body;
    }

    /**
     * Creates and executes the request to accept an incoming contact petition.
     *
     * @param senderId    id of the sender of the petition.
     * @param recipientId id of the recipient of the petition.
     * @throws NetworkException               Error produced during the network connection.
     * @throws OauthSigner.XingOauthException Error because of an Oauth problem.
     * @see <a href="https://dev.xing.com/docs/put/users/:user_id/contact_requests/:id/accept">https://dev.xing.com/docs/put/users/:user_id/contact_requests/:id/accept</a>
     */
    public static void acceptContactPetition(@NonNull String senderId,
                                             @NonNull String recipientId)
            throws NetworkException, OauthSigner.XingOauthException {
        XingController.getInstance().execute(buildAcceptContactPetitionRequest(senderId, recipientId));
    }

    /**
     * Creates the request to accept an incoming contact petition.
     *
     * @param senderId    id of the sender of the petition.
     * @param recipientId id of the recipient of the petition.
     * @return Request object ready to be executed.
     */
    public static Request buildAcceptContactPetitionRequest(@NonNull String senderId,
                                                            @NonNull String recipientId) {
        return new Request.Builder(Request.Method.PUT)
                .setUri(Uri.parse(
                        ACCEPT_OR_DECLINE_CONTACT_PETITIONS_RESOURCE.format(
                                new Object[]{senderId, recipientId}
                        ))).build();
    }

    /**
     * Creates and executes the request to decline an incoming contact petition, or to revoke an
     * initiated one.
     *
     * @param senderId    id of the sender of the petition.
     * @param recipientId id of the recipient of the petition.
     * @throws NetworkException               Error produced during the network connection.
     * @throws OauthSigner.XingOauthException Error because of an Oauth problem.
     * @see <a href="https://dev.xing.com/docs/delete/users/:user_id/contact_requests/:id">https://dev.xing.com/docs/delete/users/:user_id/contact_requests/:id</a>
     */
    public static void revokeOrDenyContactPetition(@NonNull String senderId,
                                                   @NonNull String recipientId)
            throws NetworkException, OauthSigner.XingOauthException {
        XingController.getInstance().execute(buildRevokeOrDenyContactPetitionRequest(senderId, recipientId));
    }

    /**
     * Creates the request to decline an incoming contact petition, or to revoke an initiated one.
     *
     * @param senderId    id of the sender of the petition.
     * @param recipientId id of the recipient of the petition.
     * @return Request object ready to be executed.
     */
    public static Request buildRevokeOrDenyContactPetitionRequest(@NonNull String senderId,
                                                                  @NonNull String recipientId) {
        return new Request.Builder(Request.Method.DELETE)
                .setUri(Uri.parse(
                        ACCEPT_OR_DECLINE_CONTACT_PETITIONS_RESOURCE.format(
                                new Object[]{senderId, recipientId}
                        ))).build();
    }
}
