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

package com.xing.android.sdk.json;

import android.support.annotation.Nullable;
import android.util.JsonReader;

import java.io.IOException;
import java.io.StringReader;

import static com.xing.android.sdk.json.ParserUtils.isNextTokenNull;

/**
 * Parser for the Legal Information JSON response.
 *
 * @author daniel.hartwich
 */
public final class LegalInformationMapper {
    /**
     * Parses the legal information response returned by {@link com.xing.android.sdk.task.user.LegalInformationTask}.
     *
     * @param response The legal information response in a JSON String
     * @return A String containing the legal information
     *
     * @throws IOException
     */
    @Nullable
    public static String parseLegalInformation(String response) throws IOException {
        String legalInformation = null;
        JsonReader reader = new JsonReader(new StringReader(response));
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "legal_information": {
                    legalInformation = parseLegalInformationJson(reader);
                }
            }
        }
        reader.endObject();
        return legalInformation;
    }

    /**
     * Parses the content field of the Legal Information JSON.
     *
     * @param reader The JsoNReader object containing the content of the legal information
     * @return The content of the legal information inside a String
     *
     * @throws IOException
     */
    @Nullable
    private static String parseLegalInformationJson(JsonReader reader) throws IOException {
        String legalInformation = null;
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "content": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        legalInformation = reader.nextString();
                    }
                    break;
                }
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return legalInformation;
    }

    private LegalInformationMapper() {
        throw new AssertionError("No instances.");
    }
}
