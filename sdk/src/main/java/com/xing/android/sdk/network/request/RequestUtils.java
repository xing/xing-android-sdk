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

package com.xing.android.sdk.network.request;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.List;

/**
 * Provides basic utility helper methods that allow to manipulate {@link Uri}'s,
 * {@link InputStream}'s and {@link String}'s when preparing, executing and handling requests
 */
public final class RequestUtils {

    /**
     * Key for the parameter limit. Used all across the api.
     */
    public static final String LIMIT_PARAM = "limit";

    /**
     * Key for the parameter offset. Used all across the api.
     */
    public static final String OFFSET_PARAM = "offset";

    /**
     * Key for the parameter user_fields. Used all across the api.
     */
    public static final String USER_FIELDS_PARAM = "user_fields";

    /**
     * Key for the parameter user_fields. Used all across the api.
     */
    public static final String SINCE_PARAM = "since";

    /**
     * Key for the parameter strip_html. Used all across the api.
     */
    public static final String STRIP_HTML_PARAM = "strip_html";

    /**
     * Block usage as object class
     */
    private RequestUtils() {
    }

    /**
     * Appends the <code>params</code> to the <code>builder</code>, creating one if it doesn't exist.
     *
     * @param builder Contains the url where the params will be added.
     * @param params  Params to be added to the url.
     */
    public static void appendParamsToBuilder(@NonNull Uri.Builder builder, @Nullable List<Pair<String, String>> params) {
        if (params != null) {
            for (Pair<String, String> param : params) {
                if (!TextUtils.isEmpty(param.first) && !TextUtils.isEmpty(param.second)) {
                    builder.appendQueryParameter(param.first, param.second);
                }
            }
        }
    }

    /**
     * Reads an InputStream and converts it to a String
     *
     * @param in The stream to read
     * @return The string value read from the stream
     * @throws IOException
     */
    @SuppressWarnings({"TryFinallyCanBeTryWithResources", "resource"})
    public static String inputStreamToString(InputStream in) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } finally {
            reader.close();
            in.close();
        }

        return result.toString();
    }

    public static Uri appendSegmentToUri(Uri uri, String segment) {
        if (uri == null || segment == null) {
            return uri;
        } else {
            return uri.buildUpon().appendPath(segment).build();
        }
    }

    /**
     * Formats the path with arguments, and returns a resulting uri
     *
     * @param path The path with arguments
     * @param args The arguments to add to the path
     * @return A new uri
     */
    @SuppressWarnings("unused")
    public static Uri formatUri(@NonNull String path, @Nullable Object... args) {
        MessageFormat format = new MessageFormat(path);
        return Uri.parse(format.format(args));
    }

    /**
     * Converts a list of strings on a unique one, separating them by commas.
     *
     * @param strings List of strings to concatenate.
     * @return Empty String if the list is empty or null, filled string otherwise.
     */
    public static String createCommaSeparatedStringFromStringList(@Nullable List<String> strings) {
        StringBuilder builder = new StringBuilder();

        if (strings != null && !strings.isEmpty()) {
            int numStrings = strings.size();
            String actualString;
            int startWithCommas = numStrings;

            //  Append the first valid string
            for (int stringsIterator = 0; stringsIterator < numStrings; stringsIterator++) {
                actualString = strings.get(stringsIterator);
                if (!TextUtils.isEmpty(actualString)) {
                    builder.append(actualString);
                    //  the string to continue, appending commas before
                    startWithCommas = stringsIterator + 1;
                    //  stops the for
                    stringsIterator = numStrings;
                }
            }

            for (int stringsIterator = startWithCommas; stringsIterator < numStrings; stringsIterator++) {
                actualString = strings.get(stringsIterator);
                if (!TextUtils.isEmpty(actualString)) {
                    builder.append(',').append(actualString);
                }
            }
        }

        return builder.toString();
    }
}
