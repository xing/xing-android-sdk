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
import android.util.JsonReader;

import com.xing.android.sdk.model.user.ProfileMessage;
import com.xing.android.sdk.model.user.XingUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.xing.android.sdk.json.ParserUtils.isNextTokenNull;

/**
 * Parses the User Profiles.
 *
 * @author serj.lotutovici
 */
public final class UserProfilesMapper {
    private static final String KEY_USERS = "users";
    private static final String KEY_ITEMS = "items";
    private static final String KEY_USER = "user";
    private static final String KEY_ID_CARD = "id_card";

    /**
     * Parse a list of {@link XingUser} objects form 'v1/users request'.
     *
     * @param reader The json reader
     * @return A list of received users
     *
     * @throws IOException
     */
    @Nullable
    public static List<XingUser> parseUsersFromRequest(JsonReader reader) throws IOException {
        List<XingUser> response = null;

        reader.beginObject();
        while (reader.hasNext()) {
            if (reader.nextName().equals(KEY_USERS)) {
                if (isNextTokenNull(reader)) {
                    reader.nextNull();
                } else {
                    response = XingUserMapper.parseXingUserList(reader);
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return response;
    }

    /**
     * Parse the a list of {@link XingUser} objects from '/v1/users/find'.
     *
     * @param reader The json reader
     * @return A list of received users
     *
     * @throws IOException
     */
    @Nullable
    public static List<XingUser> parseUsersFromFindRequest(JsonReader reader) throws IOException {
        List<XingUser> response = null;
        reader.beginObject();
        while (reader.hasNext()) {
            if (reader.nextName().equals(KEY_USERS)) {
                if (isNextTokenNull(reader)) {
                    reader.nextNull();
                } else {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        if (reader.nextName().equals(KEY_ITEMS)) {
                            if (isNextTokenNull(reader)) {
                                reader.nextNull();
                            } else {
                                reader.beginArray();
                                while (reader.hasNext()) {
                                    reader.beginObject();
                                    if (reader.nextName().equals(KEY_USER)) {
                                        if (isNextTokenNull(reader)) {
                                            reader.nextNull();
                                        } else {
                                            if (response == null) {
                                                response = new ArrayList<>(0);
                                            }
                                            response.add(XingUserMapper.parseXingUser(reader));
                                        }
                                    } else {
                                        reader.skipValue();
                                    }
                                    reader.endObject();
                                }
                                reader.endArray();
                            }
                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return response;
    }

    /**
     * Parse a {@link XingUser} object from '/v1/users/me/id_card'.
     *
     * @param reader The json reader
     * @return A XingUser object
     *
     * @throws IOException
     */
    @Nullable
    public static XingUser parseUserFromIdCard(JsonReader reader) throws IOException {
        XingUser user = null;
        reader.beginObject();
        while (reader.hasNext()) {
            if (reader.nextName().equals(KEY_ID_CARD)) {

                if (isNextTokenNull(reader)) {
                    reader.nextNull();
                } else {
                    user = XingUserMapper.parseXingUser(reader);
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return user;
    }

    /**
     * Parse a list of {@link XingUser} from the '/v1/users/find_by_emails'.
     *
     * @param reader The json reader
     * @return A map where the key is the search query email/hash and the value is the parsed
     * {@link XingUser} object
     *
     * @throws IOException
     */
    @Nullable
    public static Map<String, XingUser> parseUsersFromSearchByEmailRequest(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            if ("results".equals(reader.nextName())) {

                if (isNextTokenNull(reader)) {
                    reader.nextNull();
                } else {
                    return parseResultsFromSearchByEmailRequest(reader);
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return null;
    }

    /**
     * Parse the 'results' in the search by email request response.
     *
     * @param reader The json reader
     * @return A map where the key is the search query email/hash and the value is the parsed
     *
     * @throws IOException
     */
    @Nullable
    private static Map<String, XingUser> parseResultsFromSearchByEmailRequest(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            if ("items".equals(reader.nextName())) {
                if (isNextTokenNull(reader)) {
                    reader.nextNull();
                } else {
                    return parseItemsFromSearchByEmailRequest(reader);
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return null;
    }

    /**
     * Parse the array of 'items' in the search by email request response.
     *
     * @param reader The json reader
     * @return A map where the key is the search query email/hash and the value is the parsed
     *
     * @throws IOException
     */
    @Nullable
    private static Map<String, XingUser> parseItemsFromSearchByEmailRequest(JsonReader reader) throws IOException {
        Map<String, XingUser> response = null;
        reader.beginArray();
        while (reader.hasNext()) {
            if (isNextTokenNull(reader)) {
                reader.skipValue();
            } else {
                String key = null;
                XingUser user = null;

                reader.beginObject();
                while (reader.hasNext()) {
                    switch (reader.nextName()) {
                        case "hash": {
                            if (isNextTokenNull(reader)) {
                                reader.nextNull();
                            } else {
                                key = reader.nextString();
                            }
                        }
                        break;
                        case "email": {
                            if (isNextTokenNull(reader)) {
                                reader.nextNull();
                            } else {
                                key = reader.nextString();
                            }
                        }
                        break;
                        case "user": {
                            if (isNextTokenNull(reader)) {
                                reader.nextNull();
                            } else {
                                user = XingUserMapper.parseXingUser(reader);
                            }
                        }
                        break;
                        default: {
                            reader.skipValue();
                        }
                    }
                }
                reader.endObject();
                if (response == null) {
                    response = new LinkedHashMap<>(0);
                }
                response.put(key, user);
            }
        }
        reader.endArray();
        return response;
    }

    /**
     * Parse the legal information string from '/v1/users/:user_id/legal_information'.
     *
     * @param reader The json reader
     * @return A string value of the legal info, or null
     *
     * @throws IOException
     */
    @Nullable
    public static String parseLegalInfo(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            if ("legal_information".equals(reader.nextName())) {
                if (isNextTokenNull(reader)) {
                    reader.nextNull();
                } else {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        if ("content".equals(reader.nextName())) {
                            return reader.nextString();
                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return null;
    }

    /**
     * Parse the profile message of a user from '/v1/users/:user_id/profile_message'.
     *
     * @param reader The json reader
     * @return A {@link ProfileMessage} object, may me empty
     *
     * @throws IOException
     */
    @Nullable
    public static ProfileMessage parseProfileMessage(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            if ("profile_message".equals(reader.nextName())) {
                if (isNextTokenNull(reader)) {
                    reader.nextNull();
                } else {
                    return ProfileMessageMapper.parseProfileMessage(reader);
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return null;
    }

    private UserProfilesMapper() {
        throw new AssertionError("No instances.");
    }
}
