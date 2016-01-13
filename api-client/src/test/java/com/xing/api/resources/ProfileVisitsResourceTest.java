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

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.xing.api.CallSpec;
import com.xing.api.HttpError;
import com.xing.api.Response;
import com.xing.api.XingApi;
import com.xing.api.data.profile.ProfileVisit;
import com.xing.api.data.profile.ProfileVisit.Type;

import org.junit.Test;

import java.util.List;

import static com.xing.api.TestUtils.file;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test each resource method against a success server response. This test is a minimal safety major to ensure that each
 * method works as expected. In case if the json response changes on the server side, this test WILL NOT reflect that.
 */
@SuppressWarnings({"ConstantConditions", "CollectionWithoutInitialCapacity"})
public class ProfileVisitsResourceTest extends ResourceTestCase<ProfileVisitsResource> {
    public ProfileVisitsResourceTest() {
        super(ProfileVisitsResource.class);
    }

    @Test
    public void getUsersProfileVisits() throws Exception {
        testGetProfileVisits(resource.getUsersProfileVisits("some_id"));
    }

    @Test
    public void getOwnProfileVisits() throws Exception {
        testGetProfileVisits(resource.getOwnProfileVisits());
    }

    private final XingApi realApi = new XingApi.Builder()
          .apiEndpoint("https://preview.xing.com")
          .consumerKey("ae57a83582900aa7ea2f")
          .consumerSecret("96156ed002c47be0dcc6af8fd957e66ef827f178")
          .accessToken("cd921a5e286d8b7dcbde")
          .accessSecret("381a5558328ca9a033b9")
          .build();

    @Test
    public void createProfileVisits() throws Exception {
        testVoidSpec(resource.createProfileVisits("some_id"));
    }

    private void testGetProfileVisits(CallSpec<List<ProfileVisit>, HttpError> spec) throws Exception {
        server.enqueue(new MockResponse().setBody(file("visits.json")));

        Response<List<ProfileVisit>, HttpError> response = spec.execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body().size()).isEqualTo(4);
        // Check fist entry (contains nulls)
        ProfileVisit visit1 = response.body().get(0);
        assertThat(visit1.visitCount()).isEqualTo(-1);
        assertThat(visit1.distance()).isEqualTo(-1);
        assertThat(visit1.type()).isEqualTo(Type.LOGGED_OUT);
        // Check one more entry for reason.
        assertThat(response.body().get(2).reason().text()).isEqualTo("Click on contact path to <a "
              + "href=\"https://www.xing.com/profile/Maria_Mueller/N7.04c521\">Maria Müller</a>");
    }
}
