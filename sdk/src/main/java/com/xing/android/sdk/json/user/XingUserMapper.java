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
 * This parser is responsible for parsing the XingUser object
 *
 * @author david.gonzalez
 * @author daniel.hartwich
 */
public final class XingUserMapper {

    /**
     * Parse a list of {@link XingUser} objects that was returned by the request
     * For more information see {@link com.xing.android.sdk.network.request.UserProfilesRequests#details(List, List)}
     *
     * @param response The JSON String containing the information about Users
     * @return A list of XingUser objects
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
     * Parse a {@link XingUser} object that was returned by the request
     * For more information see {@link com.xing.android.sdk.network.request.UserProfilesRequests#details(List, List)}
     *
     * @param response The JSON String containing the single user information
     * @return A XingUser object
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
     * Parse the details for the {@link XingUser} object
     *
     * @param reader The json reader
     * @return A XingUser object filled with all the information
     * @throws IOException
     */
    public static XingUser parseXingUser(JsonReader reader) throws IOException {
        XingUser xinguser = new XingUser();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "id": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        xinguser.setId(reader.nextString());
                    }
                    break;
                }
                case "first_name": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        xinguser.setFirstName(reader.nextString());
                    }
                    break;
                }
                case "last_name": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        xinguser.setLastName(reader.nextString());
                    }
                    break;
                }
                case "display_name": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        xinguser.setDisplayName(reader.nextString());
                    }
                    break;
                }
                case "gender": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        xinguser.setGender(reader.nextString());
                    }
                    break;
                }
                case "birth_date": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {

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

                        xinguser.setBirthday(calendar);

                    }
                    break;
                }
                case "page_name": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        xinguser.setPageName(reader.nextString());
                    }
                    break;
                }
                case "permalink": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        xinguser.setPermalink(reader.nextString());
                    }
                    break;
                }
                case "employment_status": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        xinguser.setEmploymentStatus(reader.nextString());
                    }
                    break;
                }
                case "active_email": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        xinguser.setActiveEmail(reader.nextString());
                    }
                    break;
                }
                case "premium_services": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        xinguser.setPremiumServicesFromStringList(StringMapper.parseStringList(reader));
                    }
                    break;
                }
                case "badges": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        xinguser.setBadgesFromStringList(StringMapper.parseStringList(reader));
                    }
                    break;
                }
                case "wants": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        xinguser.setWants(reader.nextString());
                    }
                    break;
                }
                case "haves": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        xinguser.setHaves(reader.nextString());
                    }
                    break;
                }
                case "interests": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        xinguser.setInterests(reader.nextString());
                    }
                    break;
                }
                case "organisation_member": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        xinguser.setOrganisationMember(reader.nextString());
                    }
                    break;
                }
                case "private_address": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        xinguser.setPrivateAddress(XingAddressMapper.parseXingAddress(reader));
                    }
                    break;
                }
                case "business_address": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        xinguser.setBusinessAddress(XingAddressMapper.parseXingAddress(reader));
                    }
                    break;
                }
                case "educational_background": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        xinguser.setEducationBackground(EducationalBackgroundMapper.parseEducationalBackground(reader));
                    }
                    break;
                }
                case "photo_urls": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        xinguser.setPhotoUrls(XingPhotoUrlsMapper.parseXingPhotoUrls(reader));
                    }
                    break;
                }
                case "professional_experience": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        xinguser.setProfessionalExperience(ProfessionalExperienceMapper.parseProfessionalExperience(reader));
                    }
                    break;
                }
                case "time_zone": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        xinguser.setTimeZone(TimeZoneMapper.parseTimeZone(reader));
                    }
                    break;
                }
                case "languages": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {

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
                    break;
                }
                case "web_profiles": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {

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
                    break;
                }
                case "instant_messaging_accounts": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {

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
                    break;
                }
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return xinguser;
    }

    /**
     * Does literally the same as {@link XingUserMapper#parseXingUser(JsonReader)} but for a list of users
     *
     * @param reader The json reader
     * @return A list of XingUser objects
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
}