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
import com.xing.android.sdk.model.user.MessagingAccount;

/**
 * Represents of a XING user.
 *
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/get/users/:id">User Profile</a>
 */
@SuppressWarnings("unused")
public class XingUserField extends Field {
    public static final XingUserField ID = new XingUserField("id");
    public static final XingUserField FIRST_NAME = new XingUserField("first_name");
    public static final XingUserField LAST_NAME = new XingUserField("last_name");
    public static final XingUserField DISPLAY_NAME = new XingUserField("display_name");
    public static final XingUserField PAGE_NAME = new XingUserField("page_name");
    public static final XingUserField PERMALINK = new XingUserField("permalink");
    public static final XingUserField EMPLOYMENT_STATUS = new XingUserField("employment_status");
    public static final XingUserField GENDER = new XingUserField("gender");
    public static final XingUserField BIRTH_DATE = new XingUserField("birth_date");
    public static final XingUserField ACTIVE_EMAIL = new XingUserField("active_email");
    public static final XingUserField TIME_ZONE = new XingUserField("time_zone");
    public static final XingUserField PREMIUM_SERVICES = new XingUserField("premium_services");
    public static final XingUserField BADGES = new XingUserField("badges");
    public static final XingUserField WANTS = new XingUserField("wants");
    public static final XingUserField HAVES = new XingUserField("haves");
    public static final XingUserField INTERESTS = new XingUserField("interests");
    public static final XingUserField ORGANISATION_MEMBER = new XingUserField("organisation_member");
    public static final XingUserField LANGUAGES = new XingUserField("languages");
    public static final XingUserField PRIVATE_ADDRESS = new XingUserField("private_address");
    public static final XingUserField PUBLIC_ADDRESS = new XingUserField("public_address");
    public static final XingUserField WEB_PROFILES = new XingUserField("web_profiles");
    public static final XingUserField INSTANT_MESSAGING_ACCOUNTS = new XingUserField("instant_messaging_accounts");
    public static final XingUserField PROFESSIONAL_EXPERIENCE = new XingUserField("professional_experience");
    public static final XingUserField EDUCATIONAL_BACKGROUND = new XingUserField("educational_background");
    public static final XingUserField PHOTO_URLS = new XingUserField("photo_urls");

    private XingUserField(String name) {
        super(name);
    }

    public static XingUserField privateAddress(XingAddressField xingAddressField) {
        return new XingUserField(PRIVATE_ADDRESS + "." + xingAddressField);
    }

    public static XingUserField publicAddress(XingAddressField xingAddressField) {
        return new XingUserField(PUBLIC_ADDRESS + "." + xingAddressField);
    }

    public static XingUserField webProfiles(WebProfileField webProfileField) {
        return new XingUserField(WEB_PROFILES + "." + webProfileField);
    }

    public static XingUserField instantMessagingAccount(MessagingAccount messagingAccount) {
        return new XingUserField(INSTANT_MESSAGING_ACCOUNTS + "." + messagingAccount);
    }

    public static XingUserField professionalExperience(ProfessionalExperienceField professionalExperienceField) {
        return new XingUserField(PROFESSIONAL_EXPERIENCE + "." + professionalExperienceField);
    }

    public static XingUserField educationalBackground(EducationalBackgroundField educationalBackgroundField) {
        return new XingUserField(EDUCATIONAL_BACKGROUND + "." + educationalBackgroundField);
    }

    public static XingUserField photoUrls(PhotoUrlsField photoUrlsField) {
        return new XingUserField(PHOTO_URLS + "." + photoUrlsField);
    }
}
