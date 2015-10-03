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

import android.util.JsonReader;
import android.util.JsonToken;

import com.xing.android.sdk.model.user.Award;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This parser takes a JsonReader object and extracts the awards from it.
 *
 * @author david.gonzalez
 */
public final class AwardMapper {
    /**
     * Parser method to extract the single awards from a list of awards.
     *
     * @param reader A JsonReaderObject containing only one object
     * @return An Award object
     * #
     */
    public static Award parseAward(JsonReader reader) throws IOException {
        Award award = new Award();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "name": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        award.setName(reader.nextString());
                    }
                    break;
                }
                case "url": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        award.setUrl(reader.nextString());
                    }
                    break;
                }
                case "date_awarded": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        award.setDateAwarded(reader.nextInt());
                    }
                    break;
                }
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return award;
    }

    /**
     * Parse a list of awards.
     *
     * @param reader A JsonReader object containing a list of awards
     * @return A list of Award objects
     */
    public static List<Award> parseAwardList(JsonReader reader) throws IOException {
        List<Award> awardList = new ArrayList<>(0);
        reader.beginArray();
        while (reader.hasNext()) {
            awardList.add(parseAward(reader));
        }
        reader.endArray();
        return awardList;
    }

    private AwardMapper() {
        throw new AssertionError("No instances.");
    }
}
