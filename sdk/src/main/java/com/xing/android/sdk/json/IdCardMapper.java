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

import android.util.JsonReader;

import com.xing.android.sdk.json.user.XingPhotoUrlsMapper;
import com.xing.android.sdk.model.IdCard;

import java.io.IOException;
import java.io.StringReader;

import static com.xing.android.sdk.json.ParserUtils.isNextTokenNull;

/**
 * Parses the response of the id card request into {@link IdCard}
 *
 * @author daniel.hartwich
 * @see <a href="https://dev.xing.com/docs/get/users/me/id_card">https://dev.xing.com/docs/get/users/me/id_card</a>
 */
public final class IdCardMapper {
    /**
     * Parses the IdCard returned by the {@link com.xing.android.sdk.task.user.UserIdCardTask}
     *
     * @param response The JSON String that will be parsed
     * @return An IdCard object for a user
     * @throws IOException
     * */
    public static IdCard parseIdCard(String response) throws IOException {
        IdCard idCard = new IdCard();
        JsonReader reader = new JsonReader(new StringReader(response));
        reader.beginObject();
        while (reader.hasNext()) {
            idCard = parseIdCardJson(reader);
        }
        reader.endObject();
        return idCard;
    }

    /**
     * Parses the details from the IdCard JsonReader object
     *
     * @param reader The JsonReader object to be parsed
     * @return An IdCard object for a user
     * @throws IOException
     * */
    public static IdCard parseIdCardJson(JsonReader reader) throws IOException {
        IdCard idCard = new IdCard();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "id": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        idCard.setId(reader.nextString());
                    }
                    break;
                }
                case "display_name": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        idCard.setDisplayName(reader.nextString());
                    }
                    break;
                }
                case "permalink": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        idCard.setPermalink(reader.nextString());
                    }
                    break;
                }
                case "photo_urls": {
                    if (isNextTokenNull(reader)) {
                        reader.nextNull();
                    } else {
                        idCard.setPhotoUrls(XingPhotoUrlsMapper.parseXingPhotoUrls(reader));
                    }
                    break;
                }
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return idCard;
    }
}
