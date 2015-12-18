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

import android.support.annotation.Nullable;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.xing.api.model.user.WebProfile;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * @author daniel.hartwich
 */
public class WebProfileJsonAdapter extends JsonAdapter<WebProfile> {
    public static final Factory FACTORY = new Factory() {
        @Nullable
        @Override
        public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
            if (!annotations.isEmpty()) return null;
            Class<?> rawType = Types.getRawType(type);
            if (rawType != WebProfile.class) return null;
            return new WebProfileJsonAdapter().nullSafe();
        }
    };
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
