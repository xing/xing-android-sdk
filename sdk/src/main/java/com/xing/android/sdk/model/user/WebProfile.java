/*
 * Copyright (c) 2015 XING AG (http://xing.com/)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.xing.android.sdk.model.user;

import android.support.annotation.NonNull;

import com.xing.android.sdk.model.JsonEnum;

/**
 * List of possible external web profiles.
 * <p/>
 * @author serj.lotutovici
 * @see <a href="https://dev.xing.com/docs/put/users/me/web_profiles/:profile">Web Profile</a>
 */
public enum WebProfile implements JsonEnum {
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

    /**
     * Name value that is received form the json response
     */
    private final String mName;

    WebProfile(@NonNull final String name) {
        mName = name;
    }

    @Override
    public String getJsonValue() {
        return mName;
    }
}
