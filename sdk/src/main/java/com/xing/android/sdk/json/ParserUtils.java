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

import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class ParserUtils {
    /**
     * Convert string to Uri.
     *
     * @param url The url to convert
     * @return A new uri if the string is not null or empty, otherwise null
     */
    @Nullable
    public static Uri stringToUri(String url) {
        return TextUtils.isEmpty(url) ? null : Uri.parse(url);
    }

    /**
     * Parses an array of strings.
     *
     * @param reader The json reader
     * @return A list of string parsed form the array
     *
     * @throws IOException
     */
    public static List<String> parseArrayOfStrings(JsonReader reader) throws IOException {
        List<String> array = new ArrayList<>(0);

        reader.beginArray();
        while (reader.hasNext()) {
            if (isNextTokenNull(reader)) {
                reader.nextNull();
            } else {
                array.add(reader.nextString());
            }
        }
        reader.endArray();

        return array;
    }

    /**
     * Parses an array of strings to a set.
     *
     * @param reader The json reader
     * @return A set of strings that where represented as a json array
     *
     * @throws IOException
     */
    public static Set<String> parseArrayOfStringsToSet(JsonReader reader) throws IOException {
        Set<String> set = new LinkedHashSet<>(0);

        reader.beginArray();
        while (reader.hasNext()) {
            if (isNextTokenNull(reader)) {
                reader.nextNull();
            } else {
                set.add(reader.nextString());
            }
        }
        reader.endArray();

        return set;
    }

    /**
     * Check if the next reader token is null.
     *
     * @param reader The reader
     * @return {@code true} if the token is null, otherwise {@code false}
     *
     * @throws IOException
     */
    public static boolean isNextTokenNull(JsonReader reader) throws IOException {
        return reader.peek() == JsonToken.NULL;
    }

    private ParserUtils() {
        throw new AssertionError("No instances.");
    }
}
