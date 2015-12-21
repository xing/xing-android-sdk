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
package com.xing.api.internal.json;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.xing.api.model.user.WebProfile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import okio.Buffer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author daniel.hartwich
 */
public class WebProfileJsonAdapterTest {
    private Moshi moshi;

    @Before
    public void setUp() throws Exception {
        moshi = new Moshi.Builder()
              .add(WebProfileJsonAdapter.FACTORY)
              .build();
    }

    @After
    public void tearDown() throws Exception {
        moshi = null;
    }

    @Test
    public void testFromJson() throws Exception {
        JsonAdapter<WebProfileHelper> adapter = moshi.adapter(WebProfileHelper.class);
        assertThat(adapter.fromJson("{\"web_profile\" : \"amazon\"}").webProfile).isEqualTo(WebProfile.AMAZON);
        assertThat(adapter.fromJson("{\"web_profile\" : \"delicious\"}").webProfile).isEqualTo(WebProfile.DELICIOUS);
        assertThat(adapter.fromJson("{\"web_profile\" : \"digg\"}").webProfile).isEqualTo(WebProfile.DIGG);
        assertThat(adapter.fromJson("{\"web_profile\" : \"doodle\"}").webProfile).isEqualTo(WebProfile.DOODLE);
        assertThat(adapter.fromJson("{\"web_profile\" : \"dopplr\"}").webProfile).isEqualTo(WebProfile.DOPPLR);
        assertThat(adapter.fromJson("{\"web_profile\" : \"ebay\"}").webProfile).isEqualTo(WebProfile.EBAY);
        assertThat(adapter.fromJson("{\"web_profile\" : \"facebook\"}").webProfile).isEqualTo(WebProfile.FACEBOOK);
        assertThat(adapter.fromJson("{\"web_profile\" : \"flickr\"}").webProfile).isEqualTo(WebProfile.FLICKR);
        assertThat(adapter.fromJson("{\"web_profile\" : \"foursquare\"}").webProfile).isEqualTo(WebProfile.FOURSQUARE);
        assertThat(adapter.fromJson("{\"web_profile\" : \"github\"}").webProfile).isEqualTo(WebProfile.GITHUB);
        assertThat(adapter.fromJson("{\"web_profile\" : \"google+\"}").webProfile).isEqualTo(WebProfile.GOOGLE_PLUS);
        assertThat(adapter.fromJson("{\"web_profile\" : \"homepage\"}").webProfile).isEqualTo(WebProfile.HOMEPAGE);
        assertThat(adapter.fromJson("{\"web_profile\" : \"last.fm\"}").webProfile).isEqualTo(WebProfile.LAST_FM);
        assertThat(adapter.fromJson("{\"web_profile\" : \"lifestream.fm\"}").webProfile)
              .isEqualTo(WebProfile.LIFESTREAM_FM);
        assertThat(adapter.fromJson("{\"web_profile\" : \"mindmeister\"}").webProfile)
              .isEqualTo(WebProfile.MINDMEISTER);
        assertThat(adapter.fromJson("{\"web_profile\" : \"mister wong\"}").webProfile).isEqualTo(WebProfile.MR_WONG);
        assertThat(adapter.fromJson("{\"web_profile\" : \"other\"}").webProfile).isEqualTo(WebProfile.OTHER);
        assertThat(adapter.fromJson("{\"web_profile\" : \"photobucket\"}").webProfile)
              .isEqualTo(WebProfile.PHOTOBUCKET);
        assertThat(adapter.fromJson("{\"web_profile\" : \"plazes\"}").webProfile).isEqualTo(WebProfile.PLAZES);
        assertThat(adapter.fromJson("{\"web_profile\" : \"qype\"}").webProfile).isEqualTo(WebProfile.QYPE);
        assertThat(adapter.fromJson("{\"web_profile\" : \"reddit\"}").webProfile).isEqualTo(WebProfile.REDDIT);
        assertThat(adapter.fromJson("{\"web_profile\" : \"second life\"}").webProfile)
              .isEqualTo(WebProfile.SECOND_LIFE);
        assertThat(adapter.fromJson("{\"web_profile\" : \"sevenload\"}").webProfile).isEqualTo(WebProfile.SEVENLOAD);
        assertThat(adapter.fromJson("{\"web_profile\" : \"slideshare\"}").webProfile).isEqualTo(WebProfile.SLIDESHARE);
        assertThat(adapter.fromJson("{\"web_profile\" : \"sourceforge\"}").webProfile)
              .isEqualTo(WebProfile.SOURCEFORGE);
        assertThat(adapter.fromJson("{\"web_profile\" : \"spreed\"}").webProfile).isEqualTo(WebProfile.SPREED);
        assertThat(adapter.fromJson("{\"web_profile\" : \"stumble upon\"}").webProfile)
              .isEqualTo(WebProfile.STUMPLE_UPON);
        assertThat(adapter.fromJson("{\"web_profile\" : \"twitter\"}").webProfile).isEqualTo(WebProfile.TWITTER);
        assertThat(adapter.fromJson("{\"web_profile\" : \"vimeo\"}").webProfile).isEqualTo(WebProfile.VIMEO);
        assertThat(adapter.fromJson("{\"web_profile\" : \"wikipedia\"}").webProfile).isEqualTo(WebProfile.WIKIPEDIA);
        assertThat(adapter.fromJson("{\"web_profile\" : \"yelp\"}").webProfile).isEqualTo(WebProfile.YELP);
        assertThat(adapter.fromJson("{\"web_profile\" : \"youtube\"}").webProfile).isEqualTo(WebProfile.YOUTUBE);
        assertThat(adapter.fromJson("{\"web_profile\" : \"zoominfo\"}").webProfile).isEqualTo(WebProfile.ZOOMINFO);
        assertThat(adapter.fromJson("{\"web_profile\" : \"xing\"}").webProfile).isNull();
    }

    @Test
    public void testToJson() throws Exception {
        Buffer buffer = new Buffer();
        JsonWriter writer = JsonWriter.of(buffer);
        writer.setLenient(true);
        moshi.adapter(WebProfile.class).toJson(writer, WebProfile.GITHUB);
        writer.flush();
        String bufferedString = buffer.readUtf8();
        assertThat(bufferedString).isEqualTo('"' + WebProfile.GITHUB.toString() + '"');
    }

    static class WebProfileHelper {
        @Json(name = "web_profile")
        public WebProfile webProfile;
    }
}
