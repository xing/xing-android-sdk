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

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.xing.api.HttpError;
import com.xing.api.Response;
import com.xing.api.XingApi;
import com.xing.api.model.SafeCalendar;
import com.xing.api.model.user.ProfileMessage;
import com.xing.api.model.user.XingUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.xing.api.TestUtils.file;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test each resource method against a valid json object. This test is a minimal safety major to ensure that each
 * method works as expected. In case if the json response changes on the server side, this test WILL NOT reflect that.
 */
@SuppressWarnings({"ConstantConditions", "CollectionWithoutInitialCapacity"})
public final class UserProfilesResourceTest {
    @Rule
    public final MockWebServer server = new MockWebServer();

    XingApi mockApi;
    UserProfilesResource resource;
    String userJson;

    @Before
    public void setUp() throws Exception {
        mockApi = new XingApi.Builder()
              .apiEndpoint(server.url("/"))
              .loggedOut()
              .build();
        resource = mockApi.resource(UserProfilesResource.class);
        userJson = file("user.json");
    }

    @Test
    public void getUsersById() throws Exception {
        server.enqueue(new MockResponse().setBody("{\n"
              + "  \"users\": [\n"
              + "    \n"
              + userJson + ", "
              + userJson
              + "  ]\n"
              + '}'));

        List<String> ids = new ArrayList<>();
        ids.add("some_id");
        ids.add("another_id");
        Response<List<XingUser>, HttpError> response = resource.getUsersById(ids).execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body().size()).isEqualTo(2);
    }

    @Test
    public void getUserById() throws Exception {
        server.enqueue(new MockResponse().setBody("{\n"
              + "  \"users\": [\n"
              + "    \n"
              + userJson + ", "
              + userJson
              + "  ]\n"
              + '}'));

        Response<XingUser, HttpError> response = resource.getUserById("some_id").execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body().getId()).isEqualTo("123456_abcdef");
    }

    @Test
    public void getOwnProfile() throws Exception {
        server.enqueue(new MockResponse().setBody("{\n"
              + "  \"users\": [\n"
              + "    \n"
              + userJson
              + "  ]\n"
              + '}'));

        Response<XingUser, HttpError> response = resource.getOwnProfile().execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body().getId()).isEqualTo("123456_abcdef");
    }

    @Test
    public void getOwnIdCard() throws Exception {
        server.enqueue(new MockResponse().setBody("{\n"
              + "  \"id_card\": {\n"
              + "    \"id\": \"test_id\"\n"
              + "  }\n"
              + '}'));

        Response<XingUser, HttpError> response = resource.getOwnIdCard().execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body().getId()).isEqualTo("test_id");
    }

    @Test
    public void findUsersByEmail() throws Exception {
        server.enqueue(new MockResponse().setBody("{\n"
              + "  \"results\": {\n"
              + "    \"items\": [\n"
              + "      {\n"
              + "        \"email\": \"existing_user@xing.com\",\n"
              + "        \"hash\": null,\n"
              + "        \"user\": {\n"
              + "          \"id\": \"10368_ddec16\"\n"
              + "        }\n"
              + "      },\n"
              + "      {\n"
              + "        \"email\": \"unknown_user@xing.com\",\n"
              + "        \"hash\": null,\n"
              + "        \"user\": null\n"
              + "      }\n"
              + "    ],\n"
              + "    \"total\": 2\n"
              + "  }\n"
              + '}'));

        Response<List<XingUser>, HttpError> response = resource
              .findUsersByEmail(Collections.singletonList("mail")).execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body().size()).isEqualTo(2);
        assertThat(response.body().get(0).getId()).isEqualTo("10368_ddec16");
        assertThat(response.body().get(1)).isNull();
    }

    @Test
    public void findUsersByKeyword() throws Exception {
        server.enqueue(new MockResponse().setBody("{\n"
              + "  \"users\": {\n"
              + "    \"items\": [\n"
              + "      {\n"
              + "        \"user\": {\n"
              + "          \"id\": \"2_abcdef\"\n"
              + "        }\n"
              + "      },\n"
              + "      {\n"
              + "        \"user\": {\n"
              + "          \"id\": \"3_ad7e12\"\n"
              + "        }\n"
              + "      }\n"
              + "    ],\n"
              + "    \"total\": 2\n"
              + "  }\n"
              + '}'));

        Response<List<XingUser>, HttpError> response = resource.findUsersByKeyword("test").execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body().size()).isEqualTo(2);
        assertThat(response.body().get(0).getId()).isEqualTo("2_abcdef");
        assertThat(response.body().get(1).getId()).isEqualTo("3_ad7e12");
    }

    @Test
    public void getUserProfileMessage() throws Exception {
        MockResponse mockResponse = new MockResponse().setBody("{\n"
              + "  \"profile_message\": {\n"
              + "    \"updated_at\": \"2011-07-18T11:40:19Z\",\n"
              + "    \"message\": \"My new profile message.\"\n"
              + "  }\n"
              + '}');
        // Enqueue twice
        server.enqueue(mockResponse);
        server.enqueue(mockResponse);

        Response<ProfileMessage, HttpError> response1 = resource.getUserProfileMessage("test").execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response1.body().getUpdatedAt()).isEqualTo(new SafeCalendar(2011, 6, 18, 11, 40, 19));
        assertThat(response1.body().getMessage()).isEqualTo("My new profile message.");

        Response<ProfileMessage, HttpError> response2 = resource.getOwnProfileMessage().execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response2.body().getUpdatedAt()).isEqualTo(new SafeCalendar(2011, 6, 18, 11, 40, 19));
        assertThat(response2.body().getMessage()).isEqualTo("My new profile message.");
    }

    @Test
    public void getUserLegalInformation() throws Exception {
        MockResponse mockResponse = new MockResponse().setBody("{\n"
              + "  \"legal_information\": {\n"
              + "    \"content\": \"Hello there.\"\n"
              + "  }\n"
              + '}');
        // Enqueue twice
        server.enqueue(mockResponse);
        server.enqueue(mockResponse);

        Response<String, HttpError> response1 = resource.getUserLegalInformation("test").execute();
        assertThat(response1.body()).isEqualTo("Hello there.");
        Response<String, HttpError> response2 = resource.getOwnLegalInformation().execute();
        assertThat(response2.body()).isEqualTo("Hello there.");
    }
}
