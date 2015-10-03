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

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

/**
 * An abstract class with json test helper methods. Should contain generic and utility methods
 * that should be used for most parser tests.
 * <p/>
 *
 * @author serj.lotutovici
 */
public abstract class ParserUnitTestBase {

    /**
     * Get a json reader for the provided json string
     *
     * @param json The json string to read
     * @return A new instance of json reader
     *
     * @throws NullPointerException If the json string is null
     */
    protected JsonReader getReaderForJson(@NonNull final String json) {
        return new JsonReader(new StringReader(json));
    }

    /**
     * Passes the {@link Parcelable} object through a parcel
     * and returns a copy read from that parcel
     *
     * @param initial The initial object
     * @param creator The {@link Parcelable.Creator} object for that class
     * @param <T> The type parameter for the object class
     * @return A new object read from the parcel
     */
    @NonNull
    protected <T extends Parcelable> T createNewObjectViaParcelFlow(@NonNull final T initial,
            @NonNull final Parcelable.Creator<T> creator) {

        // Obtain a parcel and put the object in it
        Parcel parcel = Parcel.obtain();
        initial.writeToParcel(parcel, 0x01);

        // Reset parcel for reading
        parcel.setDataPosition(0);

        // Read the object form parcel
        return creator.createFromParcel(parcel);
    }

    /**
     * Read a file from assets
     *
     * @param fileName The file name to read
     * @return A string value of the file contents, may be empty
     *
     * @throws IOException If the the read is not possible or some other errors.
     * @deprecated Marked as deprecated until the a gradle related bug will be fixed in robolectric
     */
    @NonNull
    @Deprecated
    @SuppressWarnings("unused")
    protected String readFileFromAssets(@NonNull Context context, @NonNull final String fileName) throws IOException {
        StringBuilder json = new StringBuilder();

        BufferedReader reader = null;
        try {

            // Prepare the assets reader
            AssetManager assets = context.getAssets();
            InputStreamReader isr = new InputStreamReader(assets.open(fileName), "UTF-8");
            reader = new BufferedReader(isr);

            // Read the assets file line by line
            String line = reader.readLine();
            while (line != null) {
                json.append(line);
                line = reader.readLine();
            }
        } finally {
            // We need to close the reader even if an exception was thrown
            if (reader != null) {
                reader.close();
            }
        }

        return json.toString();
    }
}
