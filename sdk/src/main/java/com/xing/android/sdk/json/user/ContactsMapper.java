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

import com.xing.android.sdk.json.ParserUtils;
import com.xing.android.sdk.model.XingCalendar;
import com.xing.android.sdk.model.user.XingUser;
import com.xing.android.sdk.network.request.ContactsRequests;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static com.xing.android.sdk.json.ParserUtils.isNextTokenNull;

/**
 * This parser takes the JSON response returned by the ContactsRequest
 * and returns a list of XingUser objects, namely the user's contacts
 *
 * @author declan.mccormack
 * @see ContactsRequests#contacts
 */
public final class ContactsMapper {
    private static final String KEY_CONTACTS = "contacts";
    private static final String KEY_SHARED_CONTACTS = "shared_contacts";
    private static final String KEY_CONTACT_IDS = "contact_ids";
    private static final String KEY_ITEMS = "items";
    private static final String KEY_TAGS = "tags";
    private static final String KEY_TAG = "tag";

    /**
     * Parse the json string from 'v1/contacts request'
     * For more information see {@link ContactsRequests#contacts(String, XingCalendar, Integer, Integer, String, List)}
     *
     * @param json The json that was returned by the request
     * @return A list of received users
     * @throws IOException
     */
    @Nullable
    public static List<XingUser> parseContactsFromRequest(String json) throws IOException {
        return parseContactsFromRequest(json, KEY_CONTACTS);
    }

    /**
     * Parse the json string from 'v1/contact_ids request'
     * For more information see {@link com.xing.android.sdk.task.contact.IdsTask}
     *
     * @param json The json that was returned by the request
     * @return A list with all contact ids
     * @throws IOException
     */
    @Nullable
    public static List<String> parseContactIds(String json) throws IOException {
        List<String> response = null;
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case KEY_CONTACT_IDS:
                    response = parseContactIdsItems(reader);
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return response;
    }

    /**
     * Parse the assigned tags json returned by the Assigned Tags request
     * For more information see {@link com.xing.android.sdk.task.contact.AssignedTagsTask}
     *
     * @param json The json that was returned by the request
     * @return A list of tags
     * @throws IOException
     */
    @Nullable
    public static List<String> parseAssignedTags(String json) throws IOException {
        List<String> response = null;
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.beginObject();
        while (reader.hasNext()) {
            if (reader.nextName().equals(KEY_TAGS)) {
                if (isNextTokenNull(reader)) {
                    reader.nextNull();
                } else {
                    response = parseAssignedTagsItems(reader);
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return response;
    }

    /**
     * Parse the shared contactsjson returned by the v1/shared_contacts request
     * For more information see {@link com.xing.android.sdk.task.contact.AssignedTagsTask}
     *
     * @param json The json with shared contacts that was returned by the request
     * @return A list of XingUser objects
     * @throws IOException
     */
    @Nullable
    public static List<XingUser> parseSharedContacts(String json) throws IOException {
        return parseContactsFromRequest(json, KEY_SHARED_CONTACTS);
    }

    /**
     * Helper method that does the actual parsing for the
     * parseSharedContacts and parseContactsFromRequest methods
     * For more information see {@link ContactsMapper#parseContactsFromRequest(String)}
     *
     * @param json The JSON request response that needs to be parsed
     * @param key  The key to identify what should be parsed (Contacts or SharedContacts)
     * @return A list of XingUser objects
     */
    @Nullable
    private static List<XingUser> parseContactsFromRequest(String json, String key) throws IOException {
        List<XingUser> response = null;
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.beginObject();
        while (reader.hasNext()) {
            if (reader.nextName().equals(key)) {
                if (isNextTokenNull(reader)) {
                    reader.nextNull();
                } else {
                    response = UserProfilesMapper.parseUsersFromRequest(reader);
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return response;
    }

    /**
     * Parse Contact Ids
     *
     * @param reader The JsonReader object containing the json with contact ids
     * @return A list of String objects containing the contact ids
     * @see ContactsMapper#parseContactIds(String)
     */
    @Nullable
    private static List<String> parseContactIdsItems(JsonReader reader) throws IOException {
        List<String> response = null;
        reader.beginObject();
        while (reader.hasNext()) {
            if (reader.nextName().equals(KEY_ITEMS)) {
                if (isNextTokenNull(reader)) {
                    reader.nextNull();
                } else {
                    response = ParserUtils.parseArrayOfStrings(reader);
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return response;
    }

    /**
     * Parse assigned tags
     *
     * @param reader The JsonReader object containing the json with assigned tags
     * @return A list of String objects containing the parsed assigned tags
     * @see ContactsMapper#parseAssignedTags(String)
     */
    @Nullable
    private static List<String> parseAssignedTagsItems(JsonReader reader) throws IOException {
        List<String> response = null;
        reader.beginObject();
        while (reader.hasNext()) {
            if (reader.nextName().equals(KEY_ITEMS)) {
                if (isNextTokenNull(reader)) {
                    reader.nextNull();
                } else {
                    response = parseAssignedTagsItemsTag(reader);
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return response;
    }

    /**
     * Parse assigned tags helper
     *
     * @param reader The JsonReader object containing the json with assigned tags
     * @return A list of String objects containing the parsed assigned tags
     * @see ContactsMapper#parseAssignedTagsItems(JsonReader)
     */
    @Nullable
    private static List<String> parseAssignedTagsItemsTag(JsonReader reader) throws IOException {
        List<String> response = new ArrayList<>(0);
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            while (reader.hasNext()) {
                if (reader.nextName().equals(KEY_TAG)) {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        response.add(reader.nextString());
                    }
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        }
        reader.endArray();
        return response;
    }
}
