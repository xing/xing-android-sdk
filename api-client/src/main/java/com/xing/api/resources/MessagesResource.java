/*
 * Copyright (c) 2016 XING AG (http://xing.com/)
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
import com.xing.api.data.messages.Conversation;
import com.xing.api.data.messages.ConversationMessage;
import com.xing.api.data.messages.MessageAttachment;

import java.util.List;

/**
 * Represent the <a href="https://dev.xing.com/docs/resources#messages">'Messages'</a> resource.
 * <p>
 * Provides methods which allow access to user's {@linkplain Conversation conversations}.
 */
public class MessagesResource extends Resource {
    /** Creates a resource instance. This should be the only constructor declared by child classes. */
    MessagesResource(XingApi api) {
        super(api);
    }

    /**
     * Gets a list of {@linkplain com.xing.api.data.profile.XingUser user's} {@linkplain Conversation conversations}.
     * The data returned by this call will be checked and filtered on the basis of the privacy settings of each
     * requested user.
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
     * <td><b>strip_html</b></td>
     * <td>Specifies whether the message content should be stripped of HTML (true) or not (false). The default value is
     * false.</td>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>List of user attributes to return. If this parameter is not used, only the ID will be returned.</td>
     * </tr>
     * <tr>
     * <td><b>with_latest_messages</b></td>
     * <td>Number of latest messages to include. Must be non-negative. Default: 0, Maximum: 100</td>
     * </tr>
     * </table>
     *
     * @param userId Id of the {@linkplain com.xing.api.data.profile.XingUser user} to request its
     * {@linkplain Conversation conversations}.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/conversations">'List user conversations' resource
     * page</a>
     */
    public CallSpec<List<Conversation>, HttpError> getConversationsByUserId(String userId) {
        return Resource.<List<Conversation>, HttpError>newGetSpec(api, "/v1/users/{id}/conversations")
              .pathParam("id", userId)
              .responseAs(list(Conversation.class, "conversations", "items"))
              .build();
    }

    /**
     * Starts a {@linkplain Conversation conversation} by sending the passed message to the recipients.
     * The subject of a conversation cannot be changed afterwards.
     * Basic members are not allowed to send messages to non-contacts.
     * Premium members are limited to 20 messages to non-contacts per month.
     *
     * @param content Message text with max size of 16384 UTF-8 characters
     * @param recipientIds Comma-separated list of recipients. There must be between one and 10 recipients.
     * Sender cannot be included.
     * @param subject Subject for conversation. Max. size is 255 UTF-8 characters
     * @param userId Id of the {@linkplain com.xing.api.data.profile.XingUser user} starting the conversation
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/post/users/:user_id/conversations">'Create a conversation' resource page</a>
     */
    public CallSpec<Conversation, HttpError> createConversation(String userId, String subject, String content,
          String... recipientIds) {
        return Resource.<Conversation, HttpError>newPostSpec(api, "/v1/users/{user_id}/conversations", false)
              .pathParam("user_id", userId)
              .queryParam("content", content)
              .queryParam("recipient_ids", recipientIds)
              .queryParam("subject", subject)
              .responseAs(single(Conversation.class, "conversation"))
              .build();
    }

    /**
     * Check if it’s possible to send a message to the selected recipient.
     * The call will return a 404 if the recipient is not valid.
     *
     * @param userId ID of the user that should be validated
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/me/conversations/valid_recipients/:id">Validate recipient</a>
     */
    public CallSpec<Void, HttpError> validateRecipient(String userId) {
        return Resource.<Void, HttpError>newGetSpec(api, "/v1/users/me/conversations/valid_recipients/{id}")
              .pathParam("id", userId)
              .responseAs(Void.class)
              .build();
    }

    /**
     * Show a {@linkplain Conversation}
     * Returns a single conversation. This call returns the same conversations format as the
     * {@linkplain MessagesResource#getConversationsByUserId(String)} call.
     *
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>strip_html</b></td>
     * <td>Specifies whether the message content should be stripped of HTML (true) or not (false). The default value is
     * false.</td>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>List of user attributes to return. If this parameter is not used, only the ID will be returned.</td>
     * </tr>
     * <tr>
     * <td><b>with_latest_messages</b></td>
     * <td>Number of latest messages to include. Must be non-negative. Default: 0, Maximum: 100</td>
     * </tr>
     * </table>
     *
     * @param conversationId Conversation that you want to retrieve
     * @param userId ID of the user owwning the conversation
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/conversations/:id">Show a conversation</a>
     */
    public CallSpec<Conversation, HttpError> getSingleConversation(String conversationId, String userId) {
        return Resource.<Conversation, HttpError>newGetSpec(api, "/v1/users/{user_id}/conversations/{conversation_id}")
              .pathParam("user_id", userId)
              .pathParam("conversation_id", conversationId)
              .responseAs(single(Conversation.class, "conversation"))
              .build();
    }

    /**
     * Get all attachments of a conversation.
     * Aggregate the attachments of all messages in a conversations.
     *
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>limit</b></td>
     * <td>Restrict the number of attachments to be returned. This must be a positive number. Default: 10</td>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>List of user attributes to return. If this parameter is not used, only the ID will be returned.</td>
     * </tr>
     * <tr>
     * <td><b>offset</b></td>
     * <td>Offset. This must be a positive number. Default: 0</td>
     * </tr>
     * </table>
     *
     * @param conversationId Id of the {@link Conversation conversation} that you want to get the attachments from.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/me/conversations/:conversation_id/attachments">Get all attachments
     * of a conversation</a>
     */
    public CallSpec<List<MessageAttachment>, HttpError> getAttachmentsOfConversation(String conversationId) {
        return Resource.<List<MessageAttachment>, HttpError>newGetSpec(api,
              "/v1/users/me/conversations/{conversation_id}/attachments")
              .pathParam("conversation_id", conversationId)
              .responseAs(list(MessageAttachment.class, "attachments", "items"))
              .build();
    }

    /**
     * Create Download link for attachment.
     * The URL will be returned via response body and is valid for <b>300</b> seconds.
     *
     * @param conversationId Id of the {@link Conversation conversation} that the attachment belongs to
     * @param attachmentId id of the (@link {@link MessageAttachment} that you want to receive the download link for
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/post/users/me/conversations/:conversation_id/attachments/:id/download">Create
     * a temporary URL to download an attachment.</a>
     */
    public CallSpec<String, HttpError> getAttachmentDownloadLink(String conversationId, String attachmentId) {
        return Resource.<String, HttpError>newPostSpec(api,
              "/v1/users/me/conversations/{conversation_id}/attachments/{attachment_id}/download", false)
              .pathParam("conversation_id", conversationId)
              .pathParam("attachment_id", attachmentId)
              .responseAs(single(String.class, "download", "url"))
              .build();
    }

    /**
     * Mark a conversation as read.
     *
     * Marks all messages in the conversation as read.
     *
     * @param conversationId Conversation Id that should be marked as read
     * @param userId ID of the user owwning the message
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href=" https://dev.xing.com/docs/put/users/:user_id/conversations/:id/read">Mark a conversation as
     * read</a>
     */
    public CallSpec<Void, HttpError> markConversationAsRead(String conversationId, String userId) {
        return Resource.<Void, HttpError>newPutSpec(api, "/v1/users/{user_id}/conversations/{conversation_id}/read", false)
              .pathParam("user_id", userId)
              .pathParam("conversation_id", conversationId)
              .responseAs(Void.class)
              .build();
    }

    /**
     * Mark a conversation as unread.
     * Marks all messages in the conversation as unread.
     *
     * @param conversationId Conversation Id that should be marked as unread
     * @param userId ID of the user owwning the message
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/delete/users/:user_id/conversations/:id/read">Mark a conversation as
     * unread</a>
     */
    public CallSpec<Void, HttpError> markConverastionAsUnread(String conversationId, String userId) {
        return Resource
              .<Void, HttpError>newDeleteSpec(api, "/v1/users/{user_id}/conversations/{conversation_id}/read", false)
              .pathParam("user_id", userId)
              .pathParam("conversation_id", conversationId)
              .responseAs(Void.class)
              .build();
    }

    /**
     * Show conversation messages.
     * Return the messages for a conversation.
     *
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
     * <td><b>strip_html</b></td>
     * <td>Specifies whether the message content should be stripped of HTML (true) or not (false). The default value is
     * false.</td>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>List of user attributes to return. If this parameter is not used, only the ID will be returned.</td>
     * </tr>
     * </table>
     *
     * @param conversationId Id of conversation that you want to retrieve the messages from.
     * @param userId ID of the user owning the message
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/conversations/:conversation_id/messages">Show
     * conversation messages</a>
     */
    public CallSpec<List<ConversationMessage>, HttpError> getConversationMessages(String conversationId, String userId) {
        return Resource.<List<ConversationMessage>, HttpError>newGetSpec(api,
              "/v1/users/{user_id}/conversations/{conversation_id}/messages")
              .pathParam("user_id", userId)
              .pathParam("conversation_id", conversationId)
              .responseAs(list(ConversationMessage.class, "messages", "items"))
              .build();
    }

    /**
     * Show a message.
     * Returns a single message.
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>strip_html</b></td>
     * <td>Specifies whether the message content should be stripped of HTML (true) or not (false). The default value is
     * false.</td>
     * </tr>
     * <tr>
     * <td><b>user_fields</b></td>
     * <td>List of user attributes to return. If this parameter is not used, only the ID will be returned.</td>
     * </tr>
     * </table>
     *
     * @param conversationId The conversation ID from which you want to retrieve the message
     * @param messageId The Id of the message you want to retrieve
     * @param userId Id of the user that wants to retrieve the message
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/conversations/:conversation_id/messages/:id">Show a
     * message.</a>
     */
    public CallSpec<ConversationMessage, HttpError> getSingleConversationMessage(String conversationId, String messageId,
          String userId) {
        return Resource.<ConversationMessage, HttpError>newGetSpec(api,
              "/v1/users/{user_id}/conversations/{conversation_id}/messages/{message_id}")
              .pathParam("user_id", userId)
              .pathParam("conversation_id", conversationId)
              .pathParam("message_id", messageId)
              .responseAs(single(ConversationMessage.class, "message"))
              .build();
    }

    /**
     * Mark message as read.
     * Marks a message in a conversation as read.
     *
     * @param userId ID of the user marks the message as read
     * @param conversationId Id of the {@link Conversation conversation} that holds the message to be marked as read
     * @param messageId Id of the message that should be marked as read.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/delete/users/:user_id/conversations/:id/read">Mark message as unread</a>
     */
    public CallSpec<Void, HttpError> markMessageAsRead(String conversationId, String messageId, String userId) {
        return Resource.<Void, HttpError>newPutSpec(api,
              "/v1/users/{user_id}/conversations/{conversation_id}/messages/{message_id}/read", false)
              .pathParam("user_id", userId)
              .pathParam("conversation_id", conversationId)
              .pathParam("message_id", messageId)
              .responseAs(Void.class)
              .build();
    }

    /**
     * Mark message as unread.
     * Marks a message in a conversation as unread.
     *
     * @param userId ID of the user marks the message as unread
     * @param conversationId Id of the {@link Conversation conversation} that holds the message to be marked as unread
     * @param messageId Id of the message that should be marked as unread.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/delete/users/:user_id/conversations/:id/read">Mark message as unread</a>
     */
    public CallSpec<Void, HttpError> markMessageAsUnread(String conversationId, String messageId, String userId) {
        return Resource.<Void, HttpError>newDeleteSpec(api,
              "/v1/users/{user_id}/conversations/{conversation_id}/messages/{message_id}/read", false)
              .pathParam("user_id", userId)
              .pathParam("conversation_id", conversationId)
              .pathParam("message_id", messageId)
              .responseAs(Void.class)
              .build();
    }

    /**
     * Creates a new {@linkplain ConversationMessage message} in an existing {@linkplain Conversation conversation}.
     *
     * @param messageContent The {@linkplain ConversationMessage message} to add
     * @param conversationId Id of the {@link Conversation conversation} where the message is created in
     * @param userId Id of the {@linkplain com.xing.api.data.profile.XingUser user} who sends the message
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/post/users/:user_id/conversations/:conversation_id/messages">'Create
     * message in a conversation' resource page</a>
     */
    public CallSpec<ConversationMessage, HttpError> sendMessageToConversation(String userId, String conversationId,
          String messageContent) {
        return Resource.<ConversationMessage, HttpError>newPostSpec(api,
              "/v1/users/{user_id}/conversations/{conversation_id}/messages", false)
              .pathParam("user_id", userId)
              .pathParam("conversation_id", conversationId)
              .queryParam("content", messageContent)
              .responseAs(single(ConversationMessage.class, "message"))
              .build();
    }

    /**
     * Delete a {@linkplain Conversation}.
     *
     * @param userId ID of the user that wants to delete this {@linkplain Conversation}
     * @param conversationId Id of the {@link Conversation conversation} that should be deleted
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request
     *
     * @see <a href="https://dev.xing.com/docs/delete/users/:user_id/conversations/:id">Delete a Conversation</a>
     */
    public CallSpec<Void, HttpError> deleteConversation(String userId, String conversationId) {
        return Resource.<Void, HttpError>newDeleteSpec(api, "/v1/users/{user_id}/conversations/{conversation_id}", false)
              .pathParam("user_id", userId)
              .pathParam("conversation_id", conversationId)
              .responseAs(Void.class)
              .build();
    }
}
