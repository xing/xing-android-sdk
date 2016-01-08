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
import com.xing.api.data.profile.Industry;
import com.xing.api.data.profile.Language;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.xing.api.TestUtils.file;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test each resource method against a success server response. This test is a minimal safety major to ensure that each
 * method works as expected. In case if the json response changes on the server side, this test WILL NOT reflect that.
 */
@SuppressWarnings({"ConstantConditions", "CollectionWithoutInitialCapacity"})
public class MiscellaneousResourceTest extends ResourceTestCase<MiscellaneousResource> {
    public MiscellaneousResourceTest() {
        super(MiscellaneousResource.class);
    }

    @Test
    public void getTranslatedIndustries() throws Exception {
        server.enqueue(new MockResponse().setBody(file("industries.json")));

        Response<Map<Language, List<Industry>>, HttpError> response = resource.getTranslatedIndustries(null).execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.isSuccess()).isTrue();
        // Randomly check the body.
        List<Industry> industries = response.body().get(Language.EN);
        assertThat(industries.size()).isEqualTo(23);
        assertThat(industries.get(5).getId()).isEqualTo(190000);
        assertThat(industries.get(5).getSegments().size()).isEqualTo(6);
    }
}
