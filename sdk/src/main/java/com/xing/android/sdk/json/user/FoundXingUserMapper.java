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

import com.xing.android.sdk.model.user.FoundXingUser;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static com.xing.android.sdk.json.ParserUtils.isNextTokenNull;

/**
 * The parser for the FoundXingUser model.
 *
 * @author daniel.hartwich
 */
public final class FoundXingUserMapper {
    /**
     * Parses the initial JSON String searching for the "items" field before more parsing can be done.
     *
     * @param response A JSON String containing the found users that need to be parsed
     * @return A list of FoundXingUser objects
     *
     * @throws IOException
     */
    @Nullable
    public static List<FoundXingUser> parseFoundXingUser(String response) throws IOException {
        List<FoundXingUser> foundXingUser = null;
        JsonReader reader = new JsonReader(new StringReader(response));
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "results": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        reader.beginArray();
                        while (reader.hasNext()) {
                            switch (reader.nextName()) {
                                case "items": {
                                    if (isNextTokenNull(reader)) {
                                        reader.nextName();
                                    } else {
                                        foundXingUser = parseFoundXingUserList(reader);
                                    }
                                }
                                break;
                            }
                        }
                        reader.endArray();
                    }
                }
            }
        }
        reader.endObject();
        return foundXingUser;
    }

    /**
     * Parses the details of a FoundXingUser response.
     *
     * @param reader A JsonReader containing the detail data of the FoundXingUser
     * @return The FoundXingUser object
     *
     * @throws IOException
     */
    private static FoundXingUser parseFoundXingUserJson(JsonReader reader) throws IOException {
        FoundXingUser foundxinguser = new FoundXingUser();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "hash": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        foundxinguser.setHash(reader.nextString());
                    }
                    break;
                }
                case "email": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        foundxinguser.setEmail(reader.nextString());
                    }
                    break;
                }
                case "user": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        foundxinguser.setUser(XingUserMapper.parseXingUser(reader));
                    }
                    break;
                }
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return foundxinguser;
    }

    /**
     * Parse a List of FoundXingUser objects.
     *
     * @param reader A JsonReader containing the FoundXingUser objects
     * @return A list of FoundXingUser objects
     *
     * @throws IOException
     */
    private static List<FoundXingUser> parseFoundXingUserList(JsonReader reader) throws IOException {
        List<FoundXingUser> foundXingUserList = new ArrayList<>(0);
        reader.beginArray();
        while (reader.hasNext()) {
            foundXingUserList.add(parseFoundXingUserJson(reader));
        }
        reader.endArray();
        return foundXingUserList;
    }

    private FoundXingUserMapper() {
        throw new AssertionError("No instances.");
    }
}
