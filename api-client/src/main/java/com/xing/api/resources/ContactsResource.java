/*
 * Copyright (С) 2016 XING AG (http://xing.com/)
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
import com.xing.api.data.contact.ContactPaths;
import com.xing.api.data.contact.ContactRequest;
import com.xing.api.data.contact.InvitationStats;
import com.xing.api.data.contact.PendingContactRequest;
import com.xing.api.data.profile.XingUser;
import com.xing.api.internal.Experimental;

import java.util.List;

/**
 * Represent the <a href="https://dev.xing.com/docs/resources#contacts">'Contacts'</a> resource.
 * <p>
 * Provides methods which allow access a {@linkplain XingUser user's} contact list, as well as read, send and
 * respond to contact requests.
 *
 * @author daniel.hartwich
 * @author serj.lotutovici
 */
public class ContactsResource extends Resource {
    public static final Resource.Factory FACTORY = new Resource.Factory(ContactsResource.class) {
        @Override public Resource create(XingApi api) {
            return new ContactsResource(api);
        }
    };

    /** Creates a resource instance. This should be the only constructor declared by child classes. */
    ContactsResource(XingApi api) {
        super(api);
    }

    /**
     * Returns the requested {@linkplain XingUser user’s} contacts.
     * <p>
     * No more than 100 contacts at once (see limit parameter) can be requested, but several requests in parallel can
     * be performed. If executed with {@code limit=0} only the number of contacts will be returned.
     * <p>
     * If the authorizing {@linkplain XingUser user} does not have access to the requested user’s contacts, the value
     * for the user’s key will be {@code null} (not an empty list!).
     * <p>
     * By default this call can <b>only</b> access the contacts of the authorizing user direct contacts, i.e.
     * second-level contacts. For further level access, please get <a href="api@xing.com">in touch with the support
     * team.</a>
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>limit</b></td>
     * <td>Restrict the number of attachments to be returned. Must be a positive number. Default: 10</td>
     * </tr>
     * <tr>
     * <td><b>offset</b></td>
     * <td>Offset. Must be a positive number. Default: 0</td>
     * </tr>
     * <tr>
     * <td><b>order_by</b></td>
     * <td>Field that determines the ascending order of the returned list. Currently only <b>"last_name"</b> is
     * supported. Defaults to "id"</td>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>List of user attributes to be returned. If not used, only the ID will be returned. For a list
     * of available profile user attributes, refer to the {@link XingUser user profile} representation.</td>
     * </tr>
     * </table>
     *
     * @param userId Id of the {@linkplain XingUser user} who's contact list should be returned.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/contacts">'Get contacts' resource page</a>
     */
    public CallSpec<List<XingUser>, HttpError> getUserContacts(String userId) {
        return Resource.<List<XingUser>, HttpError>newGetSpec(api, "/v1/users/{user_id}/contacts")
              .pathParam("user_id", userId)
              .responseAs(list(XingUser.class, "contacts", "users"))
              .build();
    }

    /**
     * Returns the authorizing {@linkplain XingUser user’s} contacts.
     * <p>
     * No more than 100 contacts at once (see limit parameter) can be requested, but several requests in parallel can
     * be performed. If executed with {@code limit=0} only the number of contacts will be returned.
     * <p>
     * If the authorizing {@linkplain XingUser user} does not have access to the requested user’s contacts, the value
     * for the user’s key will be {@code null} (not an empty list!).
     * <p>
     * By default this call can <b>only</b> access the contacts of the authorizing user direct contacts, i.e.
     * second-level contacts. For further level access, please get <a href="api@xing.com">in touch with the support
     * team.</a>
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
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
     * <td>Restrict the number of attachments to be returned. Must be a positive number. Default: 10</td>
     * </tr>
     * <tr>
     * <td><b>offset</b></td>
     * <td>Offset. Must be a positive number. Default: 0</td>
     * </tr>
     * <tr>
     * <td><b>order_by</b></td>
     * <td>Field that determines the ascending order of the returned list. Currently only <b>"last_name"</b> is
     * supported. Defaults to "id"</td>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>List of user attributes to be returned. If not used, only the ID will be returned. For a list
     * of available profile user attributes, refer to the {@link XingUser user profile} representation.</td>
     * </tr>
     * </table>
     *
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/contacts">'Get contacts' resource page</a>
     */
    public CallSpec<List<XingUser>, HttpError> getOwnContacts() {
        return getUserContacts(ME);
    }

    /**
     * Returns all contact IDs of the authorizing {@linkplain XingUser user}.
     * <p>
     * If only the number on contacts is required, use {@link #getOwnContacts()} with {@code limit=0}.
     *
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/me/contact_ids">'Get the current user's contact IDs' resource
     * page</a>
     */
    public CallSpec<List<String>, HttpError> getOwnContactsIds() {
        return Resource.<List<String>, HttpError>newGetSpec(api, "/v1/users/me/contact_ids")
              .responseAs(list(String.class, "contact_ids", "items"))
              .build();
    }

    /**
     * Retrieve assigned tags.
     * Retrieve all tags the user has assigned to a contact.
     *
     * @param contactId ID of the users contact.
     * @param userId ID of the user who assigned the tags.
     */
    public CallSpec<List<String>, HttpError> getAssignedTags(String userId, String contactId) {
        return Resource.<List<String>, HttpError>newGetSpec(api, "/v1/users/{user_id}/contacts/{contact_id}/tags")
              .pathParam("user_id", userId)
              .pathParam("contact_id", contactId)
              .responseAs(list(single(String.class, "tag"), "tags", "items"))
              .build();
    }

    /**
     * Retrieve assigned tags by current user.
     * Retrieve all tags the current user has assigned to a contact.
     * Basically works the same as {@link ContactsResource#getAssignedTags(String, String)}, but already putting the user
     * id as ME.
     *
     * @param contactId ID of the users contact.
     */
    public CallSpec<List<String>, HttpError> getOwnAssignedTags(String contactId) {
        return getAssignedTags(ME, contactId);
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
     * <th>Parameter Name</th>
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
     * <td>Field that determines the ascending order of the returned list. Currently only supports "last_name".
     * Defaults
     * to "id"</td>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>List of user attributes to return. If this parameter is not used, only the ID will be returned. For a list
     * of
     * available profile user attributes, please refer to the get user details call. {@link XingUser}</td>
     * </tr>
     * </table>
     */
    public CallSpec<List<XingUser>, HttpError> getSharedContacts(String userId) {
        return Resource.<List<XingUser>, HttpError>newGetSpec(api, "/v1/users/{user_id}/contacts/shared")
              .pathParam("user_id", userId)
              .responseAs(list(XingUser.class, "shared_contacts", "users"))
              .build();
    }

    /**
     * Retrieve upcoming birthdays
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
     * </table>
     */
    @Experimental
    public CallSpec<List<XingUser>, HttpError> getUpcomingBirthdays() {
        return Resource.<List<XingUser>, HttpError>newGetSpec(api, "/v1/users/me/contacts/upcoming_birthdays")
              .responseAs(list(XingUser.class, "users"))
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
    public CallSpec<List<ContactRequest>, HttpError> getIncomingContactRequests() {
        return Resource.<List<ContactRequest>, HttpError>newGetSpec(api, "/v1/users/me/contact_requests")
              .responseAs(list(ContactRequest.class, "contact_requests"))
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
    public CallSpec<List<PendingContactRequest>, HttpError> getPendingContactRequests() {
        return Resource.<List<PendingContactRequest>, HttpError>newGetSpec(api, " /v1/users/me/contact_requests/sent")
              .responseAs(list(PendingContactRequest.class, "contact_requests"))
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
    public CallSpec<Void, HttpError> sendContactRequest(String userId) {
        return Resource.<Void, HttpError>newPostSpec(api, "/v1/users/{user_id}/contact_requests", false)
              .pathParam("user_id", userId)
              .responseAs(Void.class)
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
    public CallSpec<Void, HttpError> acceptContactRequest(String recipientId, String senderId) {
        return Resource
              .<Void, HttpError>newPutSpec(api, "/v1/users/{user_id}/contact_requests/{sender_id}/accept", false)
              .pathParam("user_id", recipientId)
              .pathParam("sender_id", senderId)
              .responseAs(Void.class)
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
    public CallSpec<Void, HttpError> revokeContactRequest(String recipientId, String senderId) {
        return Resource.<Void, HttpError>newDeleteSpec(api, "/v1/users/{user_id}/contact_requests/{sender_id}", false)
              .pathParam("user_id", recipientId)
              .pathParam("sender_id", senderId)
              .responseAs(Void.class)
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
    public CallSpec<ContactPaths, HttpError> getContactPaths(String userId, String otherUserId) {
        return Resource.<ContactPaths, HttpError>newGetSpec(api, "/v1/users/{user_id}/network/{other_user_id}/paths")
              .pathParam("user_id", userId)
              .pathParam("other_user_id", otherUserId)
              .responseAs(single(ContactPaths.class, "contact_paths"))
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
     * @param emails List of one or more comma-separated email addresses.
     * NOTE: The current user’s email address will be filtered out.
     */
    public CallSpec<InvitationStats, HttpError> inviteByMail(String... emails) {
        return Resource.<InvitationStats, HttpError>newPostSpec(api, "/v1/users/invite", true)
              .queryParam("to_emails", emails)
              .responseAs(single(InvitationStats.class, "invitation_stats"))
              .build();
    }
}
