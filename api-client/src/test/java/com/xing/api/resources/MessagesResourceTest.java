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

import com.xing.api.HttpError;
import com.xing.api.Response;
import com.xing.api.data.messages.Conversation;
import com.xing.api.data.messages.ConversationMessage;
import com.xing.api.data.messages.MessageAttachment;

import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;

import static com.xing.api.TestUtils.file;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test each resource method against a valid json object. This test is a minimal safety major to ensure that each
 * method works as expected. In case if the json response changes on the server side, this test WILL NOT reflect that.
 */
public final class MessagesResourceTest extends ResourceTestCase<MessagesResource> {
    public MessagesResourceTest() {
        super(MessagesResource.class);
    }

    @Test
    public void validateRecipient() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK));

        Response<Void, HttpError> response = resource.validateRecipient("123").execute();
        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_OK);
    }

    @Test
    public void getSingleConversation() throws Exception {
        server.enqueue(new MockResponse().setBody(file("messages/single_conversation.json")));

        Response<Conversation, HttpError> response = resource.getSingleConversation("conversationid", "myUserId").execute();
        assertThat(response.body().id()).isEqualTo("51626_4be761");
        assertThat(response.body().subject()).isEqualTo("Business opportunities");
    }

    @Test
    public void getAttachmentsOfConversation() throws Exception {
        server.enqueue(new MockResponse().setBody(file("messages/attachments.json")));

        Response<List<MessageAttachment>, HttpError> response = resource.getAttachmentsOfConversation("123").execute();
        assertThat(response.body().size()).isEqualTo(1);
        assertThat(response.body().get(0).id()).isEqualTo("4321_xyza");
    }

    @Test
    public void getAttachmentDownloadLink() throws Exception {
        server.enqueue(new MockResponse().setBody(file("messages/attachment_download.json")));

        Response<String, HttpError> response = resource.getAttachmentDownloadLink("123", "4321_xyza").execute();
        assertThat(response.body()).isEqualTo("https://swift.xingassets"
              + ".com/v1/AUTH_messages/xing-message-production/-a4eq2LSuUUPGbmbUSZCIaSDRrVx8Vc49yYfvXR4mZw?temp_url_sig"
              + "=873add36a595df38097a2a9de56da1a4ff67cdac&temp_url_expires=1418399892");
    }

    @Test
    public void markConversationAsRead() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK));

        Response<Void, HttpError> response = resource.markConversationAsRead("123", "123").execute();
        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_OK);
    }

    @Test
    public void markConverastionAsUnread() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK));

        Response<Void, HttpError> response = resource.markConverastionAsUnread("123", "123").execute();
        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_OK);
    }

    @Test
    public void getConverationMessages() throws Exception {
        server.enqueue(new MockResponse().setBody(file("messages/conversation_messages.json")));

        Response<List<ConversationMessage>, HttpError> response = resource.getConversationMessages("123", "123").execute();
        assertThat(response.body().size()).isEqualTo(2);
        assertThat(response.body().get(0).content()).isEqualTo("Yes of course!");
    }

    @Test
    public void getSingleConversationMessage() throws Exception {
        server.enqueue(new MockResponse().setBody(file("messages/conversation_single_message.json")));

        Response<ConversationMessage, HttpError> response = resource.getSingleConversationMessage("123", "123", "123")
              .execute();
        assertThat(response.body().content()).isEqualTo("Wait a minute");
    }

    @Test
    public void markMessageAsRead() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK));

        Response<Void, HttpError> response = resource.markMessageAsRead("23", "123", "123").execute();
        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_OK);
    }

    @Test
    public void markMessageAsUnread() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK));

        Response<Void, HttpError> response = resource.markMessageAsUnread("23", "123", "123").execute();
        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_OK);
    }

    @Test
    public void deleteConversation() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK));

        Response<Void, HttpError> response = resource.deleteConversation("123", "123").execute();
        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_OK);
    }

    @Test
    public void getConversationsByUserId() throws Exception {
        server.enqueue(new MockResponse().setBody(file("messages/list_of_conversations.json")));

        Response<List<Conversation>, HttpError> response = resource.getConversationsByUserId("").execute();
        assertThat(response.body().size()).isEqualTo(2);
    }

    @Test
    public void createConversation() throws Exception {
        server.enqueue(new MockResponse().setBody(file("messages/conversation.json")));

        Response<Conversation, HttpError> response = resource.createConversation("", "", "", "").execute();
        assertThat(response.body().subject()).isEqualTo("The subject!");
    }

    @Test
    public void sendMessageToConversation() throws Exception {
        server.enqueue(new MockResponse().setBody(file("messages/conversation_message.json")));

        Response<ConversationMessage, HttpError> response = resource.sendMessageToConversation("", "", "").execute();
        assertThat(response.body().content()).isEqualTo("New message");
    }
}
