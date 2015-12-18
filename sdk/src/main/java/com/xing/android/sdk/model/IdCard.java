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
package com.xing.android.sdk.model;

import com.squareup.moshi.Json;
import com.xing.android.sdk.model.user.XingPhotoUrls;

/**
 * Representation of a users id card.
 * FIXME IdCard is just a ripped of XingUser...
 * <p/>
 *
 * @author daniel.hartwich
 * @see <a href="https://dev.xing.com/docs/get/users/me/id_card">Get your id card</a>
 */
@SuppressWarnings("unused") // Public api
public class IdCard {
    /**
     * user id.
     */
    @Json(name = "id")
    private String id;

    /**
     * user name.
     */
    @Json(name = "display_name")
    private String displayName;

    /**
     * User profile url.
     */
    @Json(name = "permalink")
    private String permalink;

    /**
     * User photo urls.
     */
    @Json(name = "photo_urls")
    private XingPhotoUrls photoUrls;

    /**
     * Return id.
     *
     * @return id.
     */
    public String getId() {
        return id;
    }

    /**
     * Set user id.
     *
     * @param id id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Return user name.
     *
     * @return user name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Set user name.
     *
     * @param displayName user name.
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Return URL for user profile.
     *
     * @return user profile URL.
     */
    public String getPermalink() {
        return permalink;
    }

    /**
     * Set user profile URL.
     *
     * @param permalink user profile URL.
     */
    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    /**
     * Return user photo URLs.
     *
     * @return user photo URLs.
     */
    public XingPhotoUrls getPhotoUrls() {
        return photoUrls;
    }

    /**
     * Set user photo URLs.
     *
     * @param photoUrls user photo URLs.
     */
    public void setPhotoUrls(XingPhotoUrls photoUrls) {
        this.photoUrls = photoUrls;
    }
}
