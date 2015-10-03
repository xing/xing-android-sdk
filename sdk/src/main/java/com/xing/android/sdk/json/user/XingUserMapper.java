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

package com.xing.android.sdk.json.user;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.JsonReader;

import com.xing.android.sdk.json.EnumMapper;
import com.xing.android.sdk.json.StringMapper;
import com.xing.android.sdk.model.XingCalendar;
import com.xing.android.sdk.model.user.Language;
import com.xing.android.sdk.model.user.LanguageSkill;
import com.xing.android.sdk.model.user.MessagingAccount;
import com.xing.android.sdk.model.user.WebProfile;
import com.xing.android.sdk.model.user.XingUser;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.xing.android.sdk.json.ParserUtils.isNextTokenNull;
import static com.xing.android.sdk.json.ParserUtils.parseArrayOfStringsToSet;

/**
 * This parser is responsible for parsing the {@link XingUser} object.
 *
 * @author david.gonzalez
 * @author daniel.hartwich
 */
public final class XingUserMapper {
    /**
     * Parse a list of {@link XingUser} objects that was returned by the request.
     * For more information see {@link com.xing.android.sdk.network.request.UserProfilesRequests#details(List, List)}.
     *
     * @param response The JSON String containing the information about Users
     * @return A list of XingUser objects
     *
     * @throws IOException
     */
    @Nullable
    public static List<XingUser> parseDetailsResponse(String response) throws IOException {
        List<XingUser> users = null;
        JsonReader reader = new JsonReader(new StringReader(response));
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "users": {
                    users = parseXingUserList(reader);
                }
            }
        }
        reader.endObject();
        return users;
    }

    /**
     * Parse a {@link XingUser} object that was returned by the request.
     * For more information see {@link com.xing.android.sdk.network.request.UserProfilesRequests#details(List, List)}.
     *
     * @param response The JSON String containing the single user information
     * @return A XingUser object
     *
     * @throws IOException
     */
    @Nullable
    public static XingUser parseDetailsResponseSingleUser(String response) throws IOException {
        XingUser user = null;
        JsonReader reader = new JsonReader(new StringReader(response));
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "users": {
                    user = parseXingUserList(reader).get(0);
                }
            }
        }
        reader.endObject();
        return user;
    }

    /**
     * Parse the details for the {@link XingUser} object.
     *
     * @param reader The json reader
     * @return A XingUser object filled with all the information
     *
     * @throws IOException
     */
    public static XingUser parseXingUser(JsonReader reader) throws IOException {
        XingUser xinguser = new XingUser();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (isNextTokenNull(reader)) {
                reader.nextNull();
            } else {
                switch (name) {
                    case "id": {
                        xinguser.setId(reader.nextString());
                        break;
                    }
                    case "first_name": {
                        xinguser.setFirstName(reader.nextString());
                        break;
                    }
                    case "last_name": {
                        xinguser.setLastName(reader.nextString());
                        break;
                    }
                    case "display_name": {
                        xinguser.setDisplayName(reader.nextString());
                        break;
                    }
                    case "gender": {
                        xinguser.setGender(reader.nextString());
                        break;
                    }
                    case "birth_date": {
                        xinguser.setBirthday(parseBirthDate(reader));
                        break;
                    }
                    case "page_name": {
                        xinguser.setPageName(reader.nextString());
                        break;
                    }
                    case "permalink": {
                        xinguser.setPermalink(reader.nextString());
                        break;
                    }
                    case "employment_status": {
                        xinguser.setEmploymentStatus(reader.nextString());
                        break;
                    }
                    case "active_email": {
                        xinguser.setActiveEmail(reader.nextString());
                        break;
                    }
                    case "premium_services": {
                        xinguser.setPremiumServicesFromStringList(StringMapper.parseStringList(reader));
                        break;
                    }
                    case "badges": {
                        xinguser.setBadgesFromStringList(StringMapper.parseStringList(reader));
                        break;
                    }
                    case "wants": {
                        xinguser.setWants(reader.nextString());
                        break;
                    }
                    case "haves": {
                        xinguser.setHaves(reader.nextString());
                        break;
                    }
                    case "interests": {
                        xinguser.setInterests(reader.nextString());
                        break;
                    }
                    case "organisation_member": {
                        xinguser.setOrganisationMember(reader.nextString());
                        break;
                    }
                    case "private_address": {
                        xinguser.setPrivateAddress(XingAddressMapper.parseXingAddress(reader));
                        break;
                    }
                    case "business_address": {
                        xinguser.setBusinessAddress(XingAddressMapper.parseXingAddress(reader));
                        break;
                    }
                    case "educational_background": {
                        xinguser.setEducationBackground(EducationalBackgroundMapper.parseEducationalBackground(reader));
                        break;
                    }
                    case "photo_urls": {
                        xinguser.setPhotoUrls(XingPhotoUrlsMapper.parseXingPhotoUrls(reader));
                        break;
                    }
                    case "professional_experience": {
                        xinguser.setProfessionalExperience(
                                ProfessionalExperienceMapper.parseProfessionalExperience(reader));
                        break;
                    }
                    case "time_zone": {
                        xinguser.setTimeZone(TimeZoneMapper.parseTimeZone(reader));
                        break;
                    }
                    case "languages": {
                        parseLanguages(reader, xinguser);
                        break;
                    }
                    case "web_profiles": {
                        parseWebProfiles(reader, xinguser);
                        break;
                    }
                    case "instant_messaging_accounts": {
                        parseMessagingAccounts(reader, xinguser);
                        break;
                    }
                    default:
                        reader.skipValue();
                }
            }
        }
        reader.endObject();
        return xinguser;
    }

    @NonNull
    private static XingCalendar parseBirthDate(JsonReader reader) throws IOException {
        XingCalendar calendar = new XingCalendar();
        calendar.clear();

        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "day": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        calendar.set(Calendar.DAY_OF_MONTH, reader.nextInt());
                    }
                }
                break;
                case "month": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        calendar.set(Calendar.MONTH, reader.nextInt() - 1);
                    }
                }
                break;
                case "year": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        calendar.set(Calendar.YEAR, reader.nextInt());
                    }
                }
                break;
                default: {
                    reader.skipValue();
                }
            }
        }
        reader.endObject();
        return calendar;
    }

    private static void parseLanguages(JsonReader reader, XingUser xinguser) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            // Read language
            Language language = EnumMapper.parseEnumFromString(Language.values(), reader.nextName());
            // If the language is null, no need to add it to the map
            if (language == null) {
                reader.skipValue();
                continue;
            }
            // Check if we can read the skill
            if (isNextTokenNull(reader)) {
                xinguser.addLanguage(language, null);
                reader.nextNull();
                continue;
            }
            // Read the skill
            LanguageSkill skill = EnumMapper.parseEnumFromString(LanguageSkill.values(), reader.nextString());
            // Put the language and the skill in map
            xinguser.addLanguage(language, skill);
        }
        reader.endObject();
    }

    private static void parseWebProfiles(JsonReader reader, XingUser xinguser) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            // Read web profile
            WebProfile profile = EnumMapper.parseEnumFromString(WebProfile.values(), reader.nextName());
            // Check if the profile is not null
            if (profile == null) {
                reader.skipValue();
                continue;
            }
            // Check if the value is not null
            if (isNextTokenNull(reader)) {
                xinguser.addWebProfile(profile, null);
                reader.nextNull();
                continue;
            }
            // Try to add the web profiles to the user
            xinguser.setWebProfiles(profile, parseArrayOfStringsToSet(reader));
        }
        reader.endObject();
    }

    private static void parseMessagingAccounts(JsonReader reader, XingUser xinguser) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            // Read account enum key
            MessagingAccount account = EnumMapper.parseEnumFromString(MessagingAccount.values(), reader.nextName());
            // If the account is null
            if (account == null) {
                reader.skipValue();
                continue;
            }
            // Check if we can read the account value
            if (isNextTokenNull(reader)) {
                xinguser.addInstantMessagingAccount(account, null);
                reader.nextNull();
                continue;
            }
            // Try to add the value
            xinguser.addInstantMessagingAccount(account, reader.nextString());
        }
        reader.endObject();
    }

    /**
     * Does literally the same as {@link XingUserMapper#parseXingUser(JsonReader)} but for a list of users.
     *
     * @param reader The json reader
     * @return A list of XingUser objects
     *
     * @throws IOException
     */
    public static List<XingUser> parseXingUserList(JsonReader reader) throws IOException {
        List<XingUser> xingUserList = new ArrayList<>(0);
        reader.beginArray();
        while (reader.hasNext()) {
            XingUser user = parseXingUser(reader);
            if (!TextUtils.isEmpty(user.getId())) {
                xingUserList.add(user);
            }
        }
        reader.endArray();
        return xingUserList;
    }

    private XingUserMapper() {
        throw new AssertionError("No instances.");
    }
}
