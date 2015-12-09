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

package com.xing.android.sdk.model;

import com.squareup.moshi.Json;
import com.xing.android.sdk.model.user.XingPhotoUrls;

/**
 * Representation of a users id card.
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
