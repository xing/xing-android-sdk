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
 * <p/>
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/put/users/me/web_profiles/:profile">Web Profile</a>
 */
@SuppressWarnings("unused")
public class WebProfileField extends Field {

    public static WebProfileField AMAZON = new WebProfileField("amazon");
    public static WebProfileField DELICIOUS = new WebProfileField("delicious");
    public static WebProfileField DIGG = new WebProfileField("digg");
    public static WebProfileField DOODLE = new WebProfileField("doodle");
    public static WebProfileField DOPPLR = new WebProfileField("dopplr");
    public static WebProfileField EBAY = new WebProfileField("ebay");
    public static WebProfileField FACEBOOK = new WebProfileField("facebook");
    public static WebProfileField FLICKR = new WebProfileField("flickr");
    public static WebProfileField FOURSQUARE = new WebProfileField("foursquare");
    public static WebProfileField GITHUB = new WebProfileField("github");
    public static WebProfileField GOOGLE_PLUS = new WebProfileField("google+");
    public static WebProfileField HOMEPAGE = new WebProfileField("homepage");
    public static WebProfileField LAST_FM = new WebProfileField("last.fm");
    public static WebProfileField LIFESTREAM_FM = new WebProfileField("lifestream.fm");
    public static WebProfileField MINDMEISTER = new WebProfileField("mindmeister");
    public static WebProfileField MR_WONG = new WebProfileField("mister wong");
    public static WebProfileField OTHER = new WebProfileField("other");
    public static WebProfileField PHOTOBUCKET = new WebProfileField("photobucket");
    public static WebProfileField PLAZES = new WebProfileField("plazes");
    public static WebProfileField QYPE = new WebProfileField("qype");
    public static WebProfileField REDDIT = new WebProfileField("reddit");
    public static WebProfileField SECOND_LIFE = new WebProfileField("second life");
    public static WebProfileField SEVENLOAD = new WebProfileField("sevenload");
    public static WebProfileField SLIDESHARE = new WebProfileField("slideshare");
    public static WebProfileField SOURCEFORGE = new WebProfileField("sourceforge");
    public static WebProfileField SPREED = new WebProfileField("spreed");
    public static WebProfileField STUMPLE_UPON = new WebProfileField("stumble upon");
    public static WebProfileField TWITTER = new WebProfileField("twitter");
    public static WebProfileField VIMEO = new WebProfileField("vimeo");
    public static WebProfileField WIKIPEDIA = new WebProfileField("wikipedia");
    public static WebProfileField YELP = new WebProfileField("yelp");
    public static WebProfileField YOUTUBE = new WebProfileField("youtube");
    public static WebProfileField ZOOMINFO = new WebProfileField("zoominfo");

    private WebProfileField(String name) {
        super(name);
    }
}
