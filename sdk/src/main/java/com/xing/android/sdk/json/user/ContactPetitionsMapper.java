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
import android.util.JsonToken;

import com.xing.android.sdk.model.user.ContactRequest;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses the contact petitions to gather information about a contact request.
 *
 * @author ciprian.ursu
 */
@SuppressWarnings("unused")
public final class ContactPetitionsMapper {
    /**
     * Parse the json string containing contact petition information.
     *
     * @param json The json that was returned by the request
     * @return A ContactRequest object
     *
     * @throws IOException
     */
    public static ContactRequest parseContactPetition(String json) throws IOException {
        return parseContactPetition(new JsonReader(new StringReader(json)));
    }

    /**
     * Parse the JsonReader object containing contact petition information.
     *
     * @param reader The json reader that contains the json information
     * @return A ContactRequest object
     *
     * @throws IOException
     */
    public static ContactRequest parseContactPetition(JsonReader reader) throws IOException {
        ContactRequest contactRequest = new ContactRequest();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "sender_id": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        contactRequest.setSenderId(reader.nextString());
                    }
                    break;
                }
                case "received_at": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        contactRequest.setReceivedAt(reader.nextString());
                    }
                    break;
                }
                case "message": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        contactRequest.setMessage(reader.nextString());
                    }
                    break;
                }
                case "sender": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        contactRequest.setSender(XingUserMapper.parseXingUser(reader));
                    }
                    break;
                }
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return contactRequest;
    }

    /**
     * Gets the recipient id of the sent contact request.
     *
     * @param reader The json reader that contains the json information
     * @return A string containing the recipient id
     *
     * @throws IOException
     */
    @Nullable
    public static String parseSentContactPetitionId(JsonReader reader) throws IOException {
        String id = null;
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "recipient_id": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        id = reader.nextString();
                    }
                    break;
                }
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return id;
    }

    /**
     * Gets a list of contact requests.
     *
     * @param json A String with JSON that contains a list of contact requests
     * @return A list of contact petitions
     *
     * @throws IOException
     */
    public static List<ContactRequest> parseContactPetitionList(String json) throws IOException {
        return parseContactPetitionList(new JsonReader(new StringReader(json)));
    }

    /**
     * Gets a list of contact requests.
     *
     * @param reader The json reader that contains the json information
     * @return A list of contact petitions
     *
     * @throws IOException
     */
    public static List<ContactRequest> parseContactPetitionList(JsonReader reader) throws IOException {
        List<ContactRequest> contactrequestList = new ArrayList<>(0);
        reader.beginArray();
        while (reader.hasNext()) {
            contactrequestList.add(parseContactPetition(reader));
        }
        reader.endArray();
        return contactrequestList;
    }

    /**
     * Gets a list of sent contact requests.
     *
     * @param json A String with JSON that contains a list of sent contact requests
     * @return A list of sent sent contact petitions ids
     *
     * @throws IOException
     */
    public static List<String> parseSentContactRequestList(String json) throws IOException {
        return parseSentContactRequestList(new JsonReader(new StringReader(json)));
    }

    /**
     * Gets a list of sent contact requests.
     *
     * @param reader The json reader that contains the json information
     * @return A list of sent sent contact petitions ids
     *
     * @throws IOException
     */
    public static List<String> parseSentContactRequestList(JsonReader reader) throws IOException {
        List<String> sentContactRequestList = new ArrayList<>(0);
        reader.beginArray();
        while (reader.hasNext()) {
            String id = parseSentContactPetitionId(reader);
            if (id != null) {
                sentContactRequestList.add(id);
            }
        }
        reader.endArray();
        return sentContactRequestList;
    }

    private ContactPetitionsMapper() {
        throw new AssertionError("No instances.");
    }
}
