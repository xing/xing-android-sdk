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
import com.xing.android.sdk.ErrorBody;
import com.xing.android.sdk.Resource;
import com.xing.android.sdk.XingApi;
import com.xing.android.sdk.internal.Experimental;
import com.xing.android.sdk.model.ContactPaths;
import com.xing.android.sdk.model.InvitationStats;
import com.xing.android.sdk.model.PendingContactRequest;
import com.xing.android.sdk.model.user.ContactRequest;
import com.xing.android.sdk.model.user.XingUser;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author daniel.hartwich
 */
public class ContactsResource extends Resource {
    private static final String ME = "me";

    /**
     * Creates a resource instance. This should be the only constructor declared by child classes.
     */
    protected ContactsResource(XingApi api) {
        super(api);
    }

    /**
     * Get contacts of a user
     *
     * Returns the requested user’s contacts. The nested user data this call returns are the same as the get user
     * details call. You can’t request more than 100 contacts at once (see limit parameter), but you can perform
     * several requests in parallel. If you execute this call with limit=0, it will tell you how many contacts the
     * user has without returning any user data.
     * <p></p>
     * If the current user doesn’t have access to the requested user’s contacts, the value for the user’s key will be
     * null (not an empty list!).
     * <p></p>
     * By default this call can <b>only</b> access the contacts of your direct contacts, i.e. second-level contacts. If
     * you need further access, please get in touch with us.
     *
     * <table>
     * <h4>Possible OPTIONAL parameters</h4>
     * <tr>
     * <th>Paramter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>changed_since</b></td>
     * <td>Filter contacts that changed since the specified date (ISO 8601). The date can be up to 24 hours in the
     * past.
     * If an earlier date is specified, 24 hours in the past will be used instead. This parameter can only be used when
     * the user_id parameter is set the to current user.</td>
     * </tr>
     * <tr>
     * <td><b>limit</b></td>
     * <td>Restrict the number of attachments to be returned. This must be a positive number. Default: 10</td>
     * </tr>
     * <tr>
     * <td><b>offset</b></td>
     * <td>Offset. This must be a positive number. Default: 0</td>
     * </tr>
     * <tr>
     * <td><b>order_by</b></td>
     * <td>Field that determines the ascending order of the returned list. Currently only supports “last_name”.
     * Defaults
     * to “id”</td>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>List of user attributes to return. If this parameter is not used, only the ID will be returned. For a list
     * of
     * available profile user attributes, please refer to the get user details call. {@link XingUser}</td>
     * </tr>
     * </table>
     */
    public CallSpec<List<XingUser>, ErrorBody> getContacts(String userId) {
        return Resource.<List<XingUser>, ErrorBody>newGetSpec(api, "/v1/users/{user_id}/contacts")
              .pathParam("user_id", userId)
              .responseAsListOf(XingUser.class, "contacts, users")
              .build();
    }

    /**
     * Get the current users's contacts.
     */
    public CallSpec<List<XingUser>, ErrorBody> getYourContacts() {
        return getContacts(ME);
    }

    /**
     * Get the current user's contact IDs.
     * Returns all contact IDs of the current user.
     * If you only need the number of contact IDs, please use the contacts call with limit=0.
     */
    public CallSpec<List<String>, ErrorBody> getYourContactIds() {
        return Resource.<List<String>, ErrorBody>newGetSpec(api, "/v1/users/me/contact_ids")
              .responseAsListOf(String.class, "contact_ids", "items")
              .build();
    }

    // TODO SerjLtt Implement New or extended CompositeType to be able to handle weird looking json like
    // TODO  https://dev.xing.com/docs/get/users/:user_id/contacts/:contact_id/tags
    public CallSpec<Type, String> retrieveAssignedTags() {
        return null;
    }

    /**
     * Get shared contacts.
     *
     * Returns the list of contacts who are direct contacts of both the given and the current user. The nested user
     * data this call returns are the same as the get user details call. You can’t request more than 100 shared
     * contacts at once (see limit parameter), but you can perform several requests in parallel. If you execute this
     * call with limit=0, it will tell you how many contacts the user has without returning any user data.
     * <p></p>
     * If the current user doesn’t have access to the requested user’s contacts, he isn’t allowed to see how many
     * contacts he has in common with the requested user. In contrast to the get contacts call, this call will fail
     * and return a 403 message instead.
     *
     * <table>
     * <h4>Possible OPTIONAL parameters</h4>
     * <tr>
     * <th>Paramter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>limit</b></td>
     * <td>Restrict the number of attachments to be returned. This must be a positive number. Default: 10</td>
     * </tr>
     * <tr>
     * <td><b>offset</b></td>
     * <td>Offset. This must be a positive number. Default: 0</td>
     * </tr>
     * <tr>
     * <td><b>order_by</b></td>
     * <td>Field that determines the ascending order of the returned list. Currently only supports “last_name”.
     * Defaults
     * to “id”</td>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>List of user attributes to return. If this parameter is not used, only the ID will be returned. For a list
     * of
     * available profile user attributes, please refer to the get user details call. {@link XingUser}</td>
     * </tr>
     * </table>
     */
    public CallSpec<List<XingUser>, ErrorBody> getSharedContacts(String userId) {
        return Resource.<List<XingUser>, ErrorBody>newGetSpec(api, "/v1/users/{user_id}/contacts/shared")
              .pathParam("user_id", userId)
              .responseAsListOf(XingUser.class, "shared_contacts", "users")
              .build();
    }

    /**
     * Retrieve upcoming birthdays
     *
     * <table>
     * <h4>Possible OPTIONAL parameters</h4>
     * <tr>
     * <th>Paramter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>List of user attributes to return. If this parameter is not used, only the ID will be returned. For a list
     * of
     * available profile user attributes, please refer to the get user details call. {@link XingUser}</td>
     * </tr>
     * </table>
     */
    @Experimental
    public CallSpec<List<XingUser>, ErrorBody> getUpcomingBirthdays(String userId) {
        return Resource.<List<XingUser>, ErrorBody>newGetSpec(api, "/v1/users/{user_id}/contacts/shared")
              .pathParam("user_id", userId)
              .responseAsListOf(XingUser.class, "users")
              .build();
    }

    /**
     * Get incoming contact requests.
     *
     * Lists all pending incoming contact requests the specified user has received from other users.
     *
     * <table>
     * <h4>Possible OPTIONAL parameters</h4>
     * <tr>
     * <th>Paramter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>limit</b></td>
     * <td>Restricts the number of contact requests to be returned. This must be a positive number. Default: 10</td>
     * </tr>
     * <tr>
     * <td><b>offset</b></td>
     * <td>Offset. This must be a positive number. Default: 0</td>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>List of user attributes to return. If this parameter is not used, only the ID will be returned. For a
     * list of available profile fields, please refer to the example response below. All top-level attributes (e.g.
     * id, page_name, photo_urls,…) are valid fields. It is also possible to get an even more detailed response, e.g
     * photo_urls.medium or professional_experience.primary_company.name {@link XingUser}</td>
     * </tr>
     * </table>
     */
    public CallSpec<List<ContactRequest>, ErrorBody> getIncomingContactRequests() {
        return Resource.<List<ContactRequest>, ErrorBody>newGetSpec(api, "/v1/users/me/contact_requests")
              .responseAsListOf(ContactRequest.class, "contact_requests")
              .build();
    }

    /**
     * Get sent contact requests.
     *
     * Lists all pending contact requests the specified user has sent.
     *
     * <table>
     * <h4>Possible OPTIONAL parameters</h4>
     * <tr>
     * <th>Paramter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>limit</b></td>
     * <td>Restricts the number of contact requests to be returned. This must be a positive number. Default: 10</td>
     * </tr>
     * <tr>
     * <td><b>offset</b></td>
     * <td>Offset. This must be a positive number. Default: 0</td>
     * </tr>
     * <tr>
     * <td><b>recipient_id</b></td>
     * <td>Filter the contact requests for a given user ID.</td>
     * </tr>
     * </table>
     */
    public CallSpec<List<PendingContactRequest>, ErrorBody> getPendingContactRequests() {
        return Resource.<List<PendingContactRequest>, ErrorBody>newGetSpec(api, " /v1/users/me/contact_requests/sent")
              .responseAsListOf(PendingContactRequest.class, "contact_requests")
              .build();
    }

    /**
     * Initiate a contact request.
     *
     * Initiates a contact request between the current user (sender) and the specified user (recipient).
     *
     * <table>
     * <h4>Possible OPTIONAL parameters</h4>
     * <tr>
     * <th>Paramter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>message</b></td>
     * <td>Message attached to the contact request</td>
     * </tr>
     * </table>
     *
     * @param userId ID of the user receiving the contact request
     */
    public CallSpec<String, ErrorBody> sendContactRequest(String userId) {
        return Resource.<String, ErrorBody>newPostSpec(api, "/v1/users/{user_id}/contact_requests", false)
              .pathParam("user_id", userId)
              .responseAs(String.class)
              .build();
    }

    /**
     * Accept contact request.
     *
     * Accepts an incoming contact request.
     *
     * @param recipientId Recipient ID
     * @param senderId Sender ID
     */
    public CallSpec<String, ErrorBody> acceptContactRequest(String recipientId, String senderId) {
        return Resource.<String, ErrorBody>newPutSpec(api, "/v1/users/{user_id}/contact_requests/{id}/accept", false)
              .pathParam("user_id", recipientId)
              .pathParam("id", senderId)
              .responseAs(String.class)
              .build();
    }

    /**
     * Revoke or deny contact request.
     *
     * Denies an incoming contact request or revokes an initiated contact request.
     * <p></p>
     * There is a limit on the number of contact requests you can revoke. Currently this limit is 20 contact
     * requests within 5 days.
     * <p></p>
     * If you try to revoke more contact requests this call will reply with ACCESS_DENIED and you have to wait for 7
     * days until you can start revoking requests again.
     * <p></p>
     *
     * @param recipientId Recipient ID
     * @param senderId Sender ID
     */
    public CallSpec<String, ErrorBody> denyOrRevokeContactRequest(String recipientId, String senderId) {
        return Resource.<String, ErrorBody>newDeleteSpec(api, "/v1/users/{user_id}/contact_requests/{id}")
              .pathParam("user_id", recipientId)
              .pathParam("id", senderId)
              .responseAs(String.class)
              .build();
    }

    /**
     * Get contact path(s).
     *
     * Get the shortest contact path(s) between a user and any other XING user.
     *
     * <table>
     * <h4>Possible OPTIONAL parameters</h4>
     * <tr>
     * <th>Paramter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>all_paths</b></td>
     * <td>Specifies whether this call returns just one contact path (default) or all contact paths. Possible values
     * are true or false. Default: false</td>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>Comma-separated list of user attributes to return in nested user objects (along with the ID). If empty,
     * only the ID will be returned.</td>
     * </tr>
     * </table>
     *
     * @param userId ID of the user whose contact path(s) are to be returned
     * @param otherUserId ID of any other XING user
     */
    public CallSpec<ContactPaths, ErrorBody> getContactPaths(String userId, String otherUserId) {
        return Resource.<ContactPaths, ErrorBody>newGetSpec(api, "/v1/users/{user_id}/network/{other_user_id}/paths")
              .pathParam("user_id", userId)
              .pathParam("other_user_id", otherUserId)
              .responseAs(ContactPaths.class, "contact_paths")
              .build();
    }

    /**
     * Send an Invite to XING to a user via E-Mail
     *
     * Send invitations via email to contacts who do not have a XING profile. The user is allowed to invite 2000
     * people per week.
     *
     * <table>
     * <h4>Possible OPTIONAL parameters</h4>
     * <tr>
     * <th>Paramter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>message</b></td>
     * <td>Message that is sent together with the invitation. The maximum length of this message is 150 characters
     * for BASIC users and 600 characters for PREMIUM users. Defaults to the XING standard text for invitations.</td>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>List of user attributes to be returned. If this parameter is not used, only the ID will be returned. For a
     * list of available profile user attributes, please refer to the get user details call. {@link XingUser}</td>
     * </tr>
     * </table>
     *
     * @param toEmails List of one or more comma-separated email addresses.
     * NOTE: The current user’s email address will be filtered out.
     */
    public CallSpec<InvitationStats, ErrorBody> sendInvitation(String... toEmails) {
        return Resource.<InvitationStats, ErrorBody>newPostSpec(api, "/v1/users/invite", true)
              //TODO SerjLtt|DanielH Make this array a string which is comma seperated
              .pathParam("to_emails", toEmails.toString())
              .responseAs(InvitationStats.class, "invitation_stats")
              .build();
    }
}
