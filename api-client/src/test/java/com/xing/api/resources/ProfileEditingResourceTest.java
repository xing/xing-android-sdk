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

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import com.xing.api.CallSpec;
import com.xing.api.HttpError;
import com.xing.api.Response;
import com.xing.api.XingApi;
import com.xing.api.model.SafeCalendar;
import com.xing.api.model.edit.PictureUpload;
import com.xing.api.model.edit.UploadProgress;
import com.xing.api.model.edit.UploadProgress.Status;
import com.xing.api.model.user.Address;
import com.xing.api.model.user.Award;
import com.xing.api.model.user.Company;
import com.xing.api.model.user.FormOfEmployment;
import com.xing.api.model.user.Language;
import com.xing.api.model.user.LanguageSkill;
import com.xing.api.model.user.MessagingAccount;
import com.xing.api.model.user.School;
import com.xing.api.model.user.WebProfile;
import com.xing.api.model.user.XingUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.xing.api.TestUtils.file;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test each resource method against a valid response. This test is a minimal safety major to ensure that each
 * method works as expected. In case if the json response changes on the server side, this test WILL NOT reflect that.
 */
@SuppressWarnings({"ConstantConditions", "CollectionWithoutInitialCapacity", "ZeroLengthArrayAllocation"})
public class ProfileEditingResourceTest {
    private static final String BODY_COMPANY = "{\n"
          + "  \"company\": {\n"
          + "    \"id\": \"23_abcdef\",\n"
          + "    \"name\": \"XING AG\",\n"
          + "    \"title\": \"Software Engineer\",\n"
          + "    \"form_of_employment\": \"FULL_TIME_EMPLOYEE\",\n"
          + "    \"industry\": \"INTERNET\",\n"
          + "    \"industries\": [\n"
          + "      {\n"
          + "        \"id\": 90700,\n"
          + "        \"localized_name\": \"Internet and online media\"\n"
          + "      }\n"
          + "    ],\n"
          + "    \"begin_date\": null,\n"
          + "    \"end_date\": null,\n"
          + "    \"description\": null,\n"
          + "    \"company_size\": null,\n"
          + "    \"career_level\": null,\n"
          + "    \"discipline\": null,\n"
          + "    \"url\": null,\n"
          + "    \"tag\": \"XINAG\"\n"
          + "  }\n"
          + '}';

    @Rule
    public final MockWebServer server = new MockWebServer();

    XingApi mockApi;
    ProfileEditingResource resource;

    @Before
    public void setUp() throws Exception {
        mockApi = new XingApi.Builder()
              .apiEndpoint(server.url("/"))
              .loggedOut()
              .build();
        resource = mockApi.resource(ProfileEditingResource.class);
    }

    @Test
    public void updateOwnGeneralInformation() throws Exception {
        server.enqueue(new MockResponse().setBody("{\n"
              + "  \"users\": [\n"
              + "    \n"
              + file("user.json")
              + "  ]\n"
              + '}'));

        Response<XingUser, HttpError> response = resource.updateGeneralInformation().execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body().getId()).isEqualTo("123456_abcdef");
    }

    @Test
    public void updateOwnProfilePicture() throws Exception {
        // This is not a valid body, but the server prevents that in the real world.
        PictureUpload upload = PictureUpload.pictureUploadJPEG("picture.jpeg", new byte[0]);
        testVoidSpec(resource.updateProfilePicture(upload));

        testVoidSpec(resource.updateProfilePicture(RequestBody.create(MediaType.parse("multipart/form-data"), "")));
    }

    @Test
    public void deleteOwnProfilePicture() throws Exception {
        testVoidSpec(resource.deleteProfilePicture());
    }

    @Test
    public void getProfilePictureUploadProgress() throws Exception {
        server.enqueue(new MockResponse().setBody("{\n"
              + "  \"progress\": {\n"
              + "    \"status\": \"IN_PROGRESS\",\n"
              + "    \"percentage\": 80\n"
              + "  }\n"
              + '}'));

        Response<UploadProgress, HttpError> response = resource.getPictureUploadProgress().execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body().getStatus()).isEqualTo(Status.IN_PROGRESS);
        assertThat(response.body().getPercentage()).isEqualTo(80);
    }

    @Test
    public void updateOwnPrivateAddress() throws Exception {
        testVoidSpec(resource.updatePrivateAddress());
        testVoidSpec(resource.updatePrivateAddress(new Address()));
    }

    @Test
    public void updateOwnBusinessAddress() throws Exception {
        testVoidSpec(resource.updateBusinessAddress());
        testVoidSpec(resource.updateBusinessAddress(new Address()));
    }

    @Test
    public void addSchoolToOwnProfile() throws Exception {
        MockResponse mockResponse = new MockResponse().setBody("{\n"
              + "  \"school\": {\n"
              + "    \"id\": \"42\",\n"
              + "    \"name\": \"University of Hamburg\",\n"
              + "    \"subject\": null,\n"
              + "    \"degree\": null,\n"
              + "    \"begin_date\": null,\n"
              + "    \"end_date\": null,\n"
              + "    \"notes\": null\n"
              + "  }\n"
              + '}');

        server.enqueue(mockResponse);
        server.enqueue(mockResponse);

        Response<School, HttpError> response1 = resource.addSchool("University of Hamburg").execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response1.body().getId()).isEqualTo("42");

        // This would require a name, but the server prevents that in the real world.
        Response<School, HttpError> response2 = resource.addSchool(new School()).execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response2.body().getId()).isEqualTo("42");
    }

    @Test
    public void updateSchool() throws Exception {
        server.enqueue(new MockResponse().setBody("{\n"
              + "  \"school\": {\n"
              + "    \"id\": \"42\",\n"
              + "    \"name\": \"University of Hamburg\",\n"
              + "    \"subject\": null,\n"
              + "    \"degree\": null,\n"
              + "    \"begin_date\": null,\n"
              + "    \"end_date\": null,\n"
              + "    \"notes\": null\n"
              + "  }\n"
              + '}'));

        Response<School, HttpError> response = resource.updateSchool("42").execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body().getId()).isEqualTo("42");
    }

    @Test
    public void deleteSchool() throws Exception {
        testVoidSpec(resource.deleteSchool("id"));
    }

    @Test
    public void setSchoolAsPrimary() throws Exception {
        testVoidSpec(resource.setSchoolAsPrimary("id"));
    }

    @Test
    public void addQualification() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(201).setBody("{\n"
              + "  \"qualifications\": [\n"
              + "    \"API programming champion 2016\",\n"
              + "    \"Linux Daemons\"\n"
              + "  ]\n"
              + '}'));

        Response<List<String>, HttpError> response = resource.addQualification("Some desc").execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body().size()).isEqualTo(2);
        assertThat(response.body().get(1)).isEqualTo("Linux Daemons");
    }

    @Test
    public void addCompany() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(201).setBody(BODY_COMPANY));

        Response<Company, HttpError> response = resource.addCompany("Name", "Title", FormOfEmployment
              .FULL_TIME_EMPLOYEE).execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body()).isNotNull();
        assertThat(response.body().getId()).isEqualTo("23_abcdef");
    }

    @Test
    public void updateCompany() throws Exception {
        server.enqueue(new MockResponse().setBody(BODY_COMPANY));

        Response<Company, HttpError> response = resource.updateCompany("sdfadf").execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body()).isNotNull();
        assertThat(response.body().getId()).isEqualTo("23_abcdef");
    }

    @Test
    public void deleteCompany() throws Exception {
        testVoidSpec(resource.deleteCompany("some_id"));
    }

    @Test
    public void setCompanyAsPrimary() throws Exception {
        testVoidSpec(resource.setCompanyAsPrimary("some_id"));
    }

    @Test
    public void updateAwards() throws Exception {
        Award award = new Award("Test Award", new SafeCalendar(2016), "http://test.com");
        testVoidSpec(resource.updateAwards(Collections.singletonList(award)));

        RecordedRequest request = server.takeRequest(500, TimeUnit.MILLISECONDS);
        assertThat(request).isNotNull();

        assertThat(request.getBody().readUtf8())
              .startsWith("{\"awards\":[")
              .containsOnlyOnce("\"name\":\"Test Award\"")
              .containsOnlyOnce("\"date_awarded\":\"2016\"")
              .containsOnlyOnce("\"url\":\"http://test.com\"")
              .endsWith("]}");
    }

    @Test
    public void updateLanguageSkill() throws Exception {
        testVoidSpec(resource.updateLanguageSkill(Language.EN, LanguageSkill.BASIC));
    }

    @Test
    public void deleteLanguage() throws Exception {
        testVoidSpec(resource.deleteLanguage(Language.FR));
    }

    @Test
    public void updateBirthDate() throws Exception {
        testVoidSpec(resource.updateBirthDate(new SafeCalendar(1970, 3, 23)));
    }

    @Test
    public void updateWebProfile() throws Exception {
        List<String> urls = new ArrayList<>();
        urls.add("http://www.stumbleupon.com/lists/667513592373069968/");
        urls.add("http://www.stumbleupon.com/interest/cats");

        testVoidSpec(resource.updateWebProfile(WebProfile.STUMPLE_UPON, urls));

        RecordedRequest request = server.takeRequest(500, TimeUnit.MILLISECONDS);
        assertThat(request).isNotNull();

        assertThat(request.getPath()).isEqualTo("/v1/users/me/web_profiles/stumble%20upon?"
              + "url=http%3A%2F%2Fwww.stumbleupon.com%2Flists%2F667513592373069968%2F"
              + "%2C%20" // comma & space
              + "http%3A%2F%2Fwww.stumbleupon.com%2Finterest%2Fcats");
    }

    @Test
    public void deleteWebProfile() throws Exception {
        testVoidSpec(resource.deleteWebProfile(WebProfile.AMAZON));
    }

    @Test
    public void updateMessagingAccount() throws Exception {
        testVoidSpec(resource.updateMessagingAccount(MessagingAccount.SKYPE, "test"));
    }

    @Test
    public void deleteMessagingAccount() throws Exception {
        testVoidSpec(resource.deleteMessagingAccount(MessagingAccount.SKYPE));
    }

    @Test
    public void updateLegalInfo() throws Exception {
        testVoidSpec(resource.updateLegalInfo("Legal Text"));
    }

    @Test
    public void updateUsersProfileMessage() throws Exception {
        testVoidSpec(resource.updateUsersProfileMessage("id", "message"));
    }

    /** Asserts a spec that expects 204 as success response. */
    private void testVoidSpec(CallSpec<Void, HttpError> spec) throws Exception {
        server.enqueue(new MockResponse().setResponseCode(204));

        Response<Void, HttpError> response = spec.execute();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.body()).isNull();
    }
}
