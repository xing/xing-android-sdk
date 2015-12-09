package com.xing.android.sdk;/*
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

import android.support.annotation.Nullable;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.xing.android.sdk.model.user.WebProfile;

import java.io.IOException;

/**
 * @author daniel.hartwich
 */
public class WebProfileJsonAdapter extends JsonAdapter<WebProfile> {
    @Nullable
    @Override
    public WebProfile fromJson(JsonReader reader) throws IOException {
        String webProfile = reader.nextString();
        switch (webProfile) {
            case "amazon":
                return WebProfile.AMAZON;
            case "delicious":
                return WebProfile.DELICIOUS;
            case "digg":
                return WebProfile.DIGG;
            case "doodle":
                return WebProfile.DOODLE;
            case "dopplr":
                return WebProfile.DOPPLR;
            case "ebay":
                return WebProfile.EBAY;
            case "facebook":
                return WebProfile.FACEBOOK;
            case "flickr":
                return WebProfile.FLICKR;
            case "foursquare":
                return WebProfile.FOURSQUARE;
            case "github":
                return WebProfile.GITHUB;
            case "google+":
                return WebProfile.GOOGLE_PLUS;
            case "homepage":
                return WebProfile.HOMEPAGE;
            case "last.fm":
                return WebProfile.LAST_FM;
            case "lifestream.fm":
                return WebProfile.LIFESTREAM_FM;
            case "mindmeister":
                return WebProfile.MINDMEISTER;
            case "mister wong":
                return WebProfile.MR_WONG;
            case "other":
                return WebProfile.OTHER;
            case "photobucket":
                return WebProfile.PHOTOBUCKET;
            case "plazes":
                return WebProfile.PLAZES;
            case "qype":
                return WebProfile.QYPE;
            case "reddit":
                return WebProfile.REDDIT;
            case "second life":
                return WebProfile.SECOND_LIFE;
            case "sevenload":
                return WebProfile.SEVENLOAD;
            case "slideshare":
                return WebProfile.SLIDESHARE;
            case "sourceforge":
                return WebProfile.SOURCEFORGE;
            case "spreed":
                return WebProfile.SPREED;
            case "stumble upon":
                return WebProfile.STUMPLE_UPON;
            case "twitter":
                return WebProfile.TWITTER;
            case "vimeo":
                return WebProfile.VIMEO;
            case "wikipedia":
                return WebProfile.WIKIPEDIA;
            case "yelp":
                return WebProfile.YELP;
            case "youtube":
                return WebProfile.YOUTUBE;
            case "zoominfo":
                return WebProfile.ZOOMINFO;
            default:
                return null;
        }
    }

    @Override
    public void toJson(JsonWriter writer, WebProfile value) throws IOException {
        writer.value(value.getJsonValue());
    }
}
