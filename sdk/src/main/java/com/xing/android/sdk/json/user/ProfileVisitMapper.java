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

import com.xing.android.sdk.model.user.ProfileVisit;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * The ProfileVisitParser's function is to parse the JSON request response.
 *
 * @author daniel.hartwich
 */
@SuppressWarnings("unused")
public final class ProfileVisitMapper {
    /**
     * Parse the Detail response and get the visits list so that the visits can be unwrapped by further parsing.
     *
     * @param response The json that should be parsed
     * @return A list of ProfileVisits of a user
     *
     * @throws IOException
     */
    @Nullable
    public static List<ProfileVisit> parseDetailsResponse(String response) throws IOException {
        List<ProfileVisit> visits = null;
        JsonReader reader = new JsonReader(new StringReader(response));
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "visits": {
                    visits = parseProfileVisitList(reader);
                }
            }
        }
        reader.endObject();
        return visits;
    }

    /**
     * Parse the details from the ProfileVisit JsonReader.
     *
     * @param reader The JsonReader containing all the information about a visit
     * @return A single ProfileVisit object filled with information about the visitor
     *
     * @throws IOException
     */
    public static ProfileVisit parseProfileVisit(JsonReader reader) throws IOException {
        ProfileVisit profilevisit = new ProfileVisit();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "company_name": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        profilevisit.setCompanyName(reader.nextString());
                    }
                    break;
                }
                case "reason": {
                    reader.beginObject();
                    switch (reader.nextName()) {
                        case "text":
                            if (reader.peek() == JsonToken.NULL) {
                                reader.nextNull();
                            } else {
//                                profilevisit.setReason(reader.nextString());
                            }
                            break;
                    }
                    reader.endObject();
                    break;
                }
                case "visited_at_encrypted": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        profilevisit.setVisitedAtEncrypted(reader.nextString());
                    }
                    break;
                }
                case "photo_urls": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        profilevisit.setPhotoUrls(XingPhotoUrlsMapper.parseXingPhotoUrls(reader));
                    }
                    break;
                }
                case "user_id": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        profilevisit.setUserId(reader.nextString());
                    }
                    break;
                }
                case "job_title": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        profilevisit.setJobTitle(reader.nextString());
                    }
                    break;
                }
                case "display_name": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        profilevisit.setDisplayName(reader.nextString());
                    }
                    break;
                }
                case "distance": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        profilevisit.setDistance(reader.nextInt());
                    }
                    break;
                }
                case "visited_at": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        profilevisit.setVisitedAt(reader.nextString());
                    }
                    break;
                }
                case "visit_count": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        profilevisit.setVisitCount(reader.nextInt());
                    }
                    break;
                }
                case "gender": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        profilevisit.setGender(reader.nextString());
                    }
                    break;
                }
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return profilevisit;
    }

    /**
     * Parses a Json String to get all the ProfileVisits of a User.
     *
     * @param json The json string that should be parsed
     * @return A list of ProfileVisit objects
     *
     * @throws IOException
     */
    public static List<ProfileVisit> parseProfileVisitList(String json) throws IOException {
        return parseProfileVisitList(new JsonReader(new StringReader(json)));
    }

    /**
     * Initiates the parsing of the profile visits.
     *
     * @param reader The JsonReader
     * @return A list of ProfileVisits of a user
     *
     * @throws IOException
     */
    public static List<ProfileVisit> parseProfileVisitList(JsonReader reader) throws IOException {
        List<ProfileVisit> profileVisitList = new ArrayList<>(0);
        reader.beginArray();
        while (reader.hasNext()) {
            profileVisitList.add(parseProfileVisit(reader));
        }
        reader.endArray();
        return profileVisitList;
    }

    private ProfileVisitMapper() {
        throw new AssertionError("No instances.");
    }
}
