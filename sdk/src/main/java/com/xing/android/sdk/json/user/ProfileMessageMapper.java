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

import com.xing.android.sdk.model.CalendarUtils;
import com.xing.android.sdk.model.user.ProfileMessage;

import java.io.IOException;
import java.io.StringReader;

import static com.xing.android.sdk.json.ParserUtils.isNextTokenNull;

/**
 * This parser gets the Profile Message from a user
 *
 * @author serj.lotutovici
 */
@SuppressWarnings("unused")
public final class ProfileMessageMapper {

    /**
     * Parses the Professional Experience of a user
     *
     * @param response  The JSON String response returned by the request
     * @return The profile message of a user
     * @throws IOException
     */
    @Nullable
    public static ProfileMessage parseDetailsResponseJson(String response) throws IOException {
        ProfileMessage profileMessage= null;
        JsonReader reader = new JsonReader(new StringReader(response));
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "profile_message": {
                    profileMessage = parseProfileMessage(reader);
                }
            }
        }
        reader.endObject();
        return profileMessage;
    }

    /**
     * Parse the content of a profile message
     *
     * @param reader The JsonReader object containing the actual content of the profile message
     * @return A ProfileMessage object
     * @throws IOException
     */
    @Nullable
    public static ProfileMessage parseProfileMessage(JsonReader reader) throws IOException {
        ProfileMessage message = null;

        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "updated_at": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {

                        if (message == null) {
                            message = new ProfileMessage();
                        }
                        message.setUpdatedAt(CalendarUtils.parseCalendarFromString(reader.nextString()));
                    }

                }
                break;
                case "message": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {

                        if (message == null) {
                            message = new ProfileMessage();
                        }
                        message.setMessage(reader.nextString());
                    }
                }
                break;
                default: {
                    reader.skipValue();
                }
            }
        }
        reader.endObject();

        return message;
    }

}
