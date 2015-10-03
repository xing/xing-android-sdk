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

package com.xing.android.sdk.model.user.field;

import com.xing.android.sdk.json.Field;

/**
 * List of possible external web profiles.
 *
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/put/users/me/web_profiles/:profile">Web Profile</a>
 */
@SuppressWarnings("unused")
public class WebProfileField extends Field {
    public static final WebProfileField AMAZON = new WebProfileField("amazon");
    public static final WebProfileField DELICIOUS = new WebProfileField("delicious");
    public static final WebProfileField DIGG = new WebProfileField("digg");
    public static final WebProfileField DOODLE = new WebProfileField("doodle");
    public static final WebProfileField DOPPLR = new WebProfileField("dopplr");
    public static final WebProfileField EBAY = new WebProfileField("ebay");
    public static final WebProfileField FACEBOOK = new WebProfileField("facebook");
    public static final WebProfileField FLICKR = new WebProfileField("flickr");
    public static final WebProfileField FOURSQUARE = new WebProfileField("foursquare");
    public static final WebProfileField GITHUB = new WebProfileField("github");
    public static final WebProfileField GOOGLE_PLUS = new WebProfileField("google+");
    public static final WebProfileField HOMEPAGE = new WebProfileField("homepage");
    public static final WebProfileField LAST_FM = new WebProfileField("last.fm");
    public static final WebProfileField LIFESTREAM_FM = new WebProfileField("lifestream.fm");
    public static final WebProfileField MINDMEISTER = new WebProfileField("mindmeister");
    public static final WebProfileField MR_WONG = new WebProfileField("mister wong");
    public static final WebProfileField OTHER = new WebProfileField("other");
    public static final WebProfileField PHOTOBUCKET = new WebProfileField("photobucket");
    public static final WebProfileField PLAZES = new WebProfileField("plazes");
    public static final WebProfileField QYPE = new WebProfileField("qype");
    public static final WebProfileField REDDIT = new WebProfileField("reddit");
    public static final WebProfileField SECOND_LIFE = new WebProfileField("second life");
    public static final WebProfileField SEVENLOAD = new WebProfileField("sevenload");
    public static final WebProfileField SLIDESHARE = new WebProfileField("slideshare");
    public static final WebProfileField SOURCEFORGE = new WebProfileField("sourceforge");
    public static final WebProfileField SPREED = new WebProfileField("spreed");
    public static final WebProfileField STUMPLE_UPON = new WebProfileField("stumble upon");
    public static final WebProfileField TWITTER = new WebProfileField("twitter");
    public static final WebProfileField VIMEO = new WebProfileField("vimeo");
    public static final WebProfileField WIKIPEDIA = new WebProfileField("wikipedia");
    public static final WebProfileField YELP = new WebProfileField("yelp");
    public static final WebProfileField YOUTUBE = new WebProfileField("youtube");
    public static final WebProfileField ZOOMINFO = new WebProfileField("zoominfo");

    private WebProfileField(String name) {
        super(name);
    }
}
