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

import com.xing.android.sdk.json.ParserUtils;
import com.xing.android.sdk.model.user.XingPhotoUrls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser that gets the Time Zone from a JsonReader.
 *
 * @author david.gonzalez
 */
@SuppressWarnings("unused")
public final class XingPhotoUrlsMapper {
    /**
     * Extracts the TimeZone out of the JsonReader.
     *
     * @param reader The JsonReader containing the XingPhotoUrls
     * @return The XingPhotoUrls object
     *
     * @throws IOException
     */
    public static XingPhotoUrls parseXingPhotoUrls(JsonReader reader) throws IOException {
        XingPhotoUrls xingphotourls = new XingPhotoUrls();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "large": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        xingphotourls.setPhotoLargeUrl(ParserUtils.stringToUri(reader.nextString()));
                    }
                    break;
                }
                case "maxi_thumb": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        xingphotourls.setPhotoMaxiThumbUrl(ParserUtils.stringToUri(reader.nextString()));
                    }
                    break;
                }
                case "medium_thumb": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        xingphotourls.setPhotoMediumThumbUrl(ParserUtils.stringToUri(reader.nextString()));
                    }
                    break;
                }
                case "mini_thumb": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        xingphotourls.setPhotoMiniThumbUrl(ParserUtils.stringToUri(reader.nextString()));
                    }
                    break;
                }
                case "thumb": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        xingphotourls.setPhotoThumbUrl(ParserUtils.stringToUri(reader.nextString()));
                    }
                    break;
                }
                case "size_32x32": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        xingphotourls.setPhotoSize32Url(ParserUtils.stringToUri(reader.nextString()));
                    }
                    break;
                }
                case "size_48x48": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        xingphotourls.setPhotoSize48Url(ParserUtils.stringToUri(reader.nextString()));
                    }
                    break;
                }
                case "size_64x64": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        xingphotourls.setPhotoSize64Url(ParserUtils.stringToUri(reader.nextString()));
                    }
                    break;
                }
                case "size_96x96": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        xingphotourls.setPhotoSize96Url(ParserUtils.stringToUri(reader.nextString()));
                    }
                    break;
                }
                case "size_128x128": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        xingphotourls.setPhotoSize128Url(ParserUtils.stringToUri(reader.nextString()));
                    }
                    break;
                }
                case "size_192x192": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        xingphotourls.setPhotoSize192Url(ParserUtils.stringToUri(reader.nextString()));
                    }
                    break;
                }
                case "size_256x256": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        xingphotourls.setPhotoSize256Url(ParserUtils.stringToUri(reader.nextString()));
                    }
                    break;
                }
                case "size_1024x1024": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        xingphotourls.setPhotoSize1024Url(ParserUtils.stringToUri(reader.nextString()));
                    }
                    break;
                }
                case "size_original": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        xingphotourls.setPhotoSizeOriginalUrl(ParserUtils.stringToUri(reader.nextString()));
                    }
                    break;
                }
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return xingphotourls;
    }

    /**
     * Same as {@link XingPhotoUrlsMapper#parseXingPhotoUrls(JsonReader)} but for multiple XingPhotoUrls objects.
     *
     * @param reader The JsonReader
     * @return A list with XingPhotoUrls objects
     *
     * @throws IOException
     */
    public static List<XingPhotoUrls> parseXingPhotoUrlsList(JsonReader reader) throws IOException {
        List<XingPhotoUrls> xingPhotoUrlsList = new ArrayList<>(0);
        reader.beginArray();
        while (reader.hasNext()) {
            xingPhotoUrlsList.add(parseXingPhotoUrls(reader));
        }
        reader.endArray();
        return xingPhotoUrlsList;
    }

    private XingPhotoUrlsMapper() {
        throw new AssertionError("No instances.");
    }
}
