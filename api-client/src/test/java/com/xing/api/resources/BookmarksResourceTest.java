/*
 * Copyright (C) 2016 XING SE (http://xing.com/)
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

import okhttp3.mockwebserver.MockResponse;
import com.xing.api.HttpError;
import com.xing.api.Response;
import com.xing.api.data.profile.Bookmark;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test each resource method against a success server response. This test is a minimal safety major to ensure that each
 * method works as expected. In case if the json response changes on the server side, this test WILL NOT reflect that.
 */
@SuppressWarnings({"CollectionWithoutInitialCapacity"})
public class BookmarksResourceTest extends ResourceTestCase<BookmarksResource> {
    public BookmarksResourceTest() {
        super(BookmarksResource.class);
    }

    @Test
    public void getListOfOwnBookmarks() throws Exception {
        server.enqueue(new MockResponse().setBody("{\n"
              + "  \"bookmarks\": {\n"
              + "    \"total\": 2,\n"
              + "    \"items\": [\n"
              + "      {\n"
              + "        \"created_at\": \"2011-05-25T10:26:10Z\",\n"
              + "        \"user\": {\n"
              + "          \"id\": \"2432425_bf123c\",\n"
              + "          \"display_name\": \"John Doe\"\n"
              + "        }\n"
              + "      },\n"
              + "      {\n"
              + "        \"created_at\": \"2011-05-25T10:25:34Z\",\n"
              + "        \"user\": {\n"
              + "          \"id\": \"24324123_bf123d\",\n"
              + "          \"display_name\": \"Harry Potter\"\n"
              + "        }\n"
              + "      }\n"
              + "    ]\n"
              + "  }\n"
              + '}'));

        Response<List<Bookmark>, HttpError> response = resource.getListOfOwnBookmarks().execute();
        // If no exception was thrown then the spec is build correctly.
        assertThat(response.body().size()).isEqualTo(2);
        assertThat(response.body().get(1).user().id()).isEqualTo("24324123_bf123d");
    }

    @Test
    public void createOwnBookmark() throws Exception {
        testVoidSpec(resource.createOwnBookmark("some_id"));
    }

    @Test
    public void deleteOwnBookmark() throws Exception {
        testVoidSpec(resource.deleteOwnBookmark("some_di"));
    }
}
