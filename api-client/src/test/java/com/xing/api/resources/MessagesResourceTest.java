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

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.xing.api.HttpError;
import com.xing.api.Response;
import com.xing.api.data.messages.Conversation;
import com.xing.api.data.messages.ConversationMessage;

import org.junit.Test;

import java.util.List;

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
    public void getConversationsByUserId() throws Exception {
        server.enqueue(new MockResponse().setBody(file("list_of_conversations.json")));

        Response<List<Conversation>, HttpError> response = resource.getConversationsByUserId("").execute();
        assertThat(response.body().size()).isEqualTo(2);
    }

    @Test
    public void createConversation() throws Exception {
        server.enqueue(new MockResponse().setBody(file("conversation.json")));

        Response<Conversation, HttpError> response = resource.createConversation("", "", "", "").execute();
        assertThat(response.body().subject()).isEqualTo("The subject!");
    }

    @Test
    public void sendMessageToConversation() throws Exception {
        server.enqueue(new MockResponse().setBody(file("conversation_message.json")));

        Response<ConversationMessage, HttpError> response = resource.sendMessageToConversation("", "", "").execute();
        assertThat(response.body().content()).isEqualTo("New message");
    }
}
