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
import com.xing.api.HttpError;
import com.xing.api.Response;
import com.xing.api.data.profile.Gender;
import com.xing.api.data.profile.XingUser;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test each resource method against a valid response. This test is a minimal safety major to ensure that each
 * method works as expected. In case if the json response changes on the server side, this test WILL NOT reflect that.
 */
public final class RecommendationsResourceTest extends ResourceTestCase<RecommendationsResource> {
    public RecommendationsResourceTest() {
        super(RecommendationsResource.class);
    }

    @Test
    public void getOwnRecommendations() throws Exception {
        server.enqueue(new MockResponse().setBody("{\n"
              + "  \"user_recommendations\": {\n"
              + "    \"total\": 2,\n"
              + "    \"recommendations\": [\n"
              + "      {\n"
              + "        \"user\": {\n"
              + "          \"id\": \"123123_abcdef\",\n"
              + "          \"display_name\": \"Marianne Musterfrau\",\n"
              + "          \"gender\": \"f\"\n"
              + "        }\n"
              + "      },\n"
              + "      {\n"
              + "        \"user\": {\n"
              + "          \"id\": \"111111_cccccc\",\n"
              + "          \"display_name\": \"Max Musterdude\",\n"
              + "          \"gender\": \"m\"\n"
              + "        }\n"
              + "      }\n"
              + "    ]\n"
              + "  }\n"
              + '}'));

        Response<List<XingUser>, HttpError> response = resource.getOwnRecommendations().execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.body()).containsExactly(
              new XingUser()
                    .id("123123_abcdef")
                    .displayName("Marianne Musterfrau")
                    .gender(Gender.FEMALE),
              new XingUser()
                    .id("111111_cccccc")
                    .displayName("Max Musterdude")
                    .gender(Gender.MALE)
        );
    }

    @Test
    public void blockRecommendation() throws Exception {
        testVoidSpec(resource.blockRecommendation("some_id"));
    }
}
