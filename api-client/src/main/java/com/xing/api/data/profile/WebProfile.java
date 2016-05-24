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
package com.xing.api.data.profile;

import com.squareup.moshi.Json;

/**
 * List of possible/accepted external web profiles.
 *
 * @see <a href="https://dev.xing.com/docs/put/users/me/web_profiles/:profile">Web Profile</a>
 */
public enum WebProfile {
    @Json(name = "amazon")
    AMAZON("amazon"),
    @Json(name = "delicious")
    DELICIOUS("delicious"),
    @Json(name = "digg")
    DIGG("digg"),
    @Json(name = "doodle")
    DOODLE("doodle"),
    @Json(name = "dopplr")
    DOPPLR("dopplr"),
    @Json(name = "ebay")
    EBAY("ebay"),
    @Json(name = "facebook")
    FACEBOOK("facebook"),
    @Json(name = "flickr")
    FLICKR("flickr"),
    @Json(name = "foursquare")
    FOURSQUARE("foursquare"),
    @Json(name = "github")
    GITHUB("github"),
    @Json(name = "google+")
    GOOGLE_PLUS("google+"),
    @Json(name = "homepage")
    HOMEPAGE("homepage"),
    @Json(name = "last.fm")
    LAST_FM("last.fm"),
    @Json(name = "lifestream.fm")
    LIFESTREAM_FM("lifestream.fm"),
    @Json(name = "mindmeister")
    MINDMEISTER("mindmeister"),
    @Json(name = "mister wong")
    MR_WONG("mister wong"),
    @Json(name = "other")
    OTHER("other"),
    @Json(name = "photobucket")
    PHOTOBUCKET("photobucket"),
    @Json(name = "plazes")
    PLAZES("plazes"),
    @Json(name = "qype")
    QYPE("qype"),
    @Json(name = "reddit")
    REDDIT("reddit"),
    @Json(name = "second life")
    SECOND_LIFE("second life"),
    @Json(name = "sevenload")
    SEVENLOAD("sevenload"),
    @Json(name = "slideshare")
    SLIDESHARE("slideshare"),
    @Json(name = "sourceforge")
    SOURCEFORGE("sourceforge"),
    @Json(name = "spreed")
    SPREED("spreed"),
    @Json(name = "stumble upon")
    STUMPLE_UPON("stumble upon"),
    @Json(name = "twitter")
    TWITTER("twitter"),
    @Json(name = "vimeo")
    VIMEO("vimeo"),
    @Json(name = "wikipedia")
    WIKIPEDIA("wikipedia"),
    @Json(name = "yelp")
    YELP("yelp"),
    @Json(name = "youtube")
    YOUTUBE("youtube"),
    @Json(name = "zoominfo")
    ZOOMINFO("zoominfo"),
    @Json(name = "blog")
    BLOG("blog"),
    @Json(name = "jabber")
    JABBER("jabber"),
    @Json(name = "xing coaches")
    XING_COACHES("xing coaches");

    /** Name value that is received form the json response. */
    private final String value;

    WebProfile(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
