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

import com.xing.api.HttpError;
import com.xing.api.Response;
import com.xing.api.data.SafeCalendar;
import com.xing.api.data.contact.ContactPaths;
import com.xing.api.data.contact.ContactRequest;
import com.xing.api.data.contact.InvitationStats;
import com.xing.api.data.contact.PendingContactRequest;
import com.xing.api.data.profile.XingUser;

import org.junit.Test;

import java.util.Calendar;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;

import static com.xing.api.TestUtils.file;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test each resource method against a success server response. This test is a minimal safety major to ensure that each
 * method works as expected. In case if the json response changes on the server side, this test WILL NOT reflect that.
 */
@SuppressWarnings({"ConstantConditions", "CollectionWithoutInitialCapacity"})
public class ContactsResourceTest extends ResourceTestCase<ContactsResource> {
    private static final String LIST_OF_USERS = "{\n"
          + "        \"display_name\": \"Christina Ahnefeld\",\n"
          + "        \"id\": \"64060_b12067\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"display_name\": \"Hasso Sieber\",\n"
          + "        \"id\": \"3167095_eb35c2\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"display_name\": \"Elmar Hinsenkamp\",\n"
          + "        \"id\": \"3217478_24d6c7\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"display_name\": \"Prof. Tammo Freese\",\n"
          + "        \"id\": \"3228492_025d41\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"display_name\": \"Jonas Wunderlich\",\n"
          + "        \"id\": \"3312533_101654\"\n"
          + "      }\n";
    private static final String BODY_LIST_OF_CONTACTS = "{\n"
          + "  \"contacts\": {\n"
          + "    \"total\": 47,\n"
          + "    \"users\": [\n"
          + LIST_OF_USERS
          + "    ]\n"
          + "  }\n"
          + '}';

    public ContactsResourceTest() {
        super(ContactsResource.class);
    }

    @Test
    public void getUserContacts() throws Exception {
        server.enqueue(new MockResponse().setBody(BODY_LIST_OF_CONTACTS));
        Response<List<XingUser>, HttpError> response = resource.getUserContacts("some_id").execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body().size()).isEqualTo(5);
    }

    @Test
    public void getOwnContacts() throws Exception {
        server.enqueue(new MockResponse().setBody(BODY_LIST_OF_CONTACTS));
        Response<List<XingUser>, HttpError> response = resource.getOwnContacts().execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body().size()).isEqualTo(5);
    }

    @Test
    public void getOwnContactsIds() throws Exception {
        server.enqueue(new MockResponse().setBody("{\n"
              + "  \"contact_ids\": {\n"
              + "    \"items\": [\n"
              + "      \"3582_f213af\",\n"
              + "      \"385526_b94012\",\n"
              + "      \"391722_0ab373\"\n"
              + "    ]\n"
              + "  }\n"
              + '}'));

        Response<List<String>, HttpError> response = resource.getOwnContactsIds().execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body()).containsExactly("3582_f213af", "385526_b94012", "391722_0ab373");
    }

    @Test
    public void getAssignedTags() throws Exception {
        server.enqueue(new MockResponse().setBody("{\n"
              + "  \"tags\": {\n"
              + "    \"total\": 2,\n"
              + "    \"items\": [\n"
              + "      {\n"
              + "        \"tag\": \"Business opportunities in München/Munich\"\n"
              + "      },\n"
              + "      {\n"
              + "        \"tag\": \"friends\"\n"
              + "      }\n"
              + "    ]\n"
              + "  }\n"
              + '}'));

        Response<List<String>, HttpError> response = resource.getAssignedTags("user_id", "contact_id").execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body()).containsExactly("Business opportunities in München/Munich", "friends");
    }

    @Test
    public void getOwnAssignedTags() throws Exception {
        server.enqueue(new MockResponse().setBody("{\n"
              + "  \"tags\": {\n"
              + "    \"total\": 2,\n"
              + "    \"items\": [\n"
              + "      {\n"
              + "        \"tag\": \"android\"\n"
              + "      },\n"
              + "      {\n"
              + "        \"tag\": \"kotlin\"\n"
              + "      }\n"
              + "    ]\n"
              + "  }\n"
              + '}'));

        Response<List<String>, HttpError> response = resource.getOwnAssignedTags("contact_id").execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body()).containsExactly("android", "kotlin");
    }

    @Test
    public void getSharedContacts() throws Exception {
        server.enqueue(new MockResponse().setBody("{\n"
              + "  \"shared_contacts\": {\n"
              + "    \"total\": 47,\n"
              + "    \"users\": [\n"
              + LIST_OF_USERS
              + "    ]\n"
              + "  }\n"
              + '}'));

        Response<List<XingUser>, HttpError> response = resource.getSharedContacts("contact_id").execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body().size()).isEqualTo(5);
    }

    @Test
    public void getUpcomingBirthdays() throws Exception {
        server.enqueue(new MockResponse().setBody("{\n"
              + "  \"users\": [\n"
              + file("user.json") + ','
              + file("user.json") + ','
              + file("user.json")
              + "  ]\n"
              + '}'));

        Response<List<XingUser>, HttpError> response = resource.getUpcomingBirthdays().execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body().size()).isEqualTo(3);
    }

    @Test
    public void getIncomingContactRequests() throws Exception {
        server.enqueue(new MockResponse().setBody("{\n"
              + "  \"contact_requests\": [\n"
              + "    {\n"
              + "      \"sender_id\": \"6055623_5cf823\",\n"
              + "      \"sender\": {\n"
              + "        \"id\": \"6055623_5cf823\",\n"
              + "        \"display_name\": \"John Doe\"\n"
              + "      },\n"
              + "      \"message\": \"Sehr geehrter Herr Irgendwas, ich würde Sie gern zu meinen Kontakten hinzufügen"
              + "\",\n"
              + "      \"received_at\": \"2010-11-17T10:56:16Z\"\n"
              + "    }\n"
              + "  ]\n"
              + '}'));

        Response<List<ContactRequest>, HttpError> response = resource.getIncomingContactRequests().execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body()).containsExactly(
              new ContactRequest()
                    .message("Sehr geehrter Herr Irgendwas, ich würde Sie gern zu meinen Kontakten hinzufügen")
                    .senderId("6055623_5cf823")
                    .receivedAt(new SafeCalendar(2010, Calendar.NOVEMBER, 17, 10, 56, 16))
                    .sender(new XingUser().id("6055623_5cf823").displayName("John Doe")));
    }

    @Test
    public void getPendingContactRequests() throws Exception {
        server.enqueue(new MockResponse().setBody("{\n"
              + "  \"contact_requests\": [\n"
              + "    {\n"
              + "      \"sender_id\": \"19828488_2f3282\",\n"
              + "      \"recipient_id\": \"8957208_181cf4\"\n"
              + "    },\n"
              + "    {\n"
              + "      \"sender_id\": \"19828488_2f3282\",\n"
              + "      \"recipient_id\": \"20195666_868fe8\"\n"
              + "    },\n"
              + "    {\n"
              + "      \"sender_id\": \"19828488_2f3282\",\n"
              + "      \"recipient_id\": \"12022726_a0415b\"\n"
              + "    }\n"
              + "  ]\n"
              + '}'));

        Response<List<PendingContactRequest>, HttpError> response = resource.getPendingContactRequests().execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body()).containsExactly(
              new PendingContactRequest("19828488_2f3282", "8957208_181cf4"),
              new PendingContactRequest("19828488_2f3282", "20195666_868fe8"),
              new PendingContactRequest("19828488_2f3282", "12022726_a0415b"));
    }

    @Test
    public void sendContactRequest() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(201));
        Response<Void, HttpError> response = resource.sendContactRequest("11187562").execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.body()).isNull();
    }

    @Test
    public void acceptContactRequest() throws Exception {
        testVoidSpec(resource.acceptContactRequest("user_id", "sender_id"));
    }

    @Test
    public void revokeContactRequest() throws Exception {
        testVoidSpec(resource.revokeContactRequest("user_id", "sender_id"));
    }

    @Test
    public void getContactPaths() throws Exception {
        server.enqueue(new MockResponse().setBody("{\n"
              + "  \"contact_paths\": {\n"
              + "    \"paths\": [\n"
              + "      {\n"
              + "        \"users\": [\n"
              + "          {\n"
              + "            \"permalink\": \"https://www.xing.com/profile/John_Doe\",\n"
              + "            \"id\": \"3228492_861e68\",\n"
              + "            \"gender\": \"m\",\n"
              + "            \"display_name\": \"John Doe\"\n"
              + "          },\n"
              + "          {\n"
              + "            \"permalink\": \"https://www.xing.com/profile/Alex_Kaemper\",\n"
              + "            \"id\": \"1160_5fb1b2\",\n"
              + "            \"gender\": \"m\",\n"
              + "            \"display_name\": \"Alex Kämper\"\n"
              + "          },\n"
              + "          {\n"
              + "            \"permalink\": \"https://www.xing.com/profile/Mark_Fugazi\",\n"
              + "            \"id\": \"3647111_82d2c2\",\n"
              + "            \"gender\": \"m\",\n"
              + "            \"display_name\": \"Mark Fugazi\"\n"
              + "          },\n"
              + "          {\n"
              + "            \"permalink\": \"https://www.xing.com/profile/Max_Mustermann\",\n"
              + "            \"id\": \"3644173_68b1ee\",\n"
              + "            \"gender\": \"m\",\n"
              + "            \"display_name\": \"Max Mustermann\"\n"
              + "          }\n"
              + "        ]\n"
              + "      }\n"
              + "    ],\n"
              + "    \"distance\": 3,\n"
              + "    \"total\": 1\n"
              + "  }\n"
              + '}'));

        Response<ContactPaths, HttpError> response = resource.getContactPaths("firstId", "secondId").execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body().paths().size()).isEqualTo(1);
        assertThat(response.body().paths().get(0).size()).isEqualTo(4);
    }

    @Test
    public void inviteByMail() throws Exception {
        server.enqueue(new MockResponse().setBody("{\n"
              + "  \"invitation_stats\": {\n"
              + "    \"total_addresses\": 7,\n"
              + "    \"invitations_sent\": 3,\n"
              + "    \"already_invited\": [\n"
              + "      \"kven.sever@example.net\"\n"
              + "    ],\n"
              + "    \"already_member\": [\n"
              + "      {\n"
              + "        \"id\": \"666666_abcdef\",\n"
              + "        \"email\": \"sark.midt@xing.com\",\n"
              + "        \"display_name\": \"Sark Midt\"\n"
              + "      },\n"
              + "      {\n"
              + "        \"id\": \"12345_abcdef\",\n"
              + "        \"email\": \"kennart.loopmann@xing.com\",\n"
              + "        \"display_name\": \"Kennart Loopmann\"\n"
              + "      }\n"
              + "    ],\n"
              + "    \"invalid_addresses\": [\n"
              + "      \"@example.f\"\n"
              + "    ]\n"
              + "  }\n"
              + '}'));

        Response<InvitationStats, HttpError> response = resource.inviteByMail("test@test.test", "email").execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body().totalAddresses()).isEqualTo(7);
        assertThat(response.body().invitationsSent()).isEqualTo(3);
        assertThat(response.body().alreadyInvited()).containsExactly("kven.sever@example.net");
        assertThat(response.body().invalidAddresses()).containsExactly("@example.f");
        assertThat(response.body().alreadyMember().size()).isEqualTo(2);
    }
}
