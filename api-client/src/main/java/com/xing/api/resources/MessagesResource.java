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
}
