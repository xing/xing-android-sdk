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
package com.xing.api.model.user;

/**
 * List of possible external web profiles.
 *
 * @see <a href="https://dev.xing.com/docs/put/users/me/web_profiles/:profile">Web Profile</a>
 */
public enum WebProfile {
    AMAZON("amazon"),
    DELICIOUS("delicious"),
    DIGG("digg"),
    DOODLE("doodle"),
    DOPPLR("dopplr"),
    EBAY("ebay"),
    FACEBOOK("facebook"),
    FLICKR("flickr"),
    FOURSQUARE("foursquare"),
    GITHUB("github"),
    GOOGLE_PLUS("google+"),
    HOMEPAGE("homepage"),
    LAST_FM("last.fm"),
    LIFESTREAM_FM("lifestream.fm"),
    MINDMEISTER("mindmeister"),
    MR_WONG("mister wong"),
    OTHER("other"),
    PHOTOBUCKET("photobucket"),
    PLAZES("plazes"),
    QYPE("qype"),
    REDDIT("reddit"),
    SECOND_LIFE("second life"),
    SEVENLOAD("sevenload"),
    SLIDESHARE("slideshare"),
    SOURCEFORGE("sourceforge"),
    SPREED("spreed"),
    STUMPLE_UPON("stumble upon"),
    TWITTER("twitter"),
    VIMEO("vimeo"),
    WIKIPEDIA("wikipedia"),
    YELP("yelp"),
    YOUTUBE("youtube"),
    ZOOMINFO("zoominfo");

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
