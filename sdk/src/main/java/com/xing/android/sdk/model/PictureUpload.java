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

package com.xing.android.sdk.model;

import com.squareup.moshi.Json;

import java.util.Arrays;

/**
 * The PictureUpload class which should hold all the required information about the new Profile Picture the user is
 * trying to upload.
 *
 * The inner static class Photo was created as a wrapper, because the API expects the actual information about the
 * photo wrapped in another object, called photo. With this approach there was no need to create a custom moshi
 * adapter.
 * The JSON which will be send should look similar to this:
 * <pre>
 * {@code
 * {
 *  "photo":
 *  {   "file_name": "test.jpg",
 *      "mime_type": "image/jpeg",
 *      "content": "Base64 encoded image data"
 *  }
 * }
 * </pre>
 *
 * @author daniel.hartwich
 * @author serj.lotutovici
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"}) // All fields  will be picked up by moshi.
public final class PictureUpload {
    private static final String MIME_TYPE_JPEG = "image/jpeg";
    private static final String MIME_TYPE_PNG = "image/png";

    /**
     * This is a static "Constructor" method that creates the PictureUpload object with the mimetype already set to
     * JPEG.
     */
    public static PictureUpload pictureUploadJPEG(String fileName, byte[] content) {
        return new PictureUpload(new Photo(fileName, MIME_TYPE_JPEG, content));
    }

    /**
     * This is a static "Constructor" method that creates the PictureUpload object with the mimetype already set to
     * PNG.
     */
    public static PictureUpload pictureUploadPNG(String fileName, byte[] content) {
        return new PictureUpload(new Photo(fileName, MIME_TYPE_PNG, content));
    }

    @Json(name = "photo")
    private final Photo photo;

    private PictureUpload(Photo photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "PictureUpload{photo=" + photo + '}';
    }

    /**
     * Internal class which is needed, because the server expects.
     * A JSON object which looks something like this:
     * <pre>
     * {@code
     * {
     *  "photo":
     *  {   "file_name": "test.jpg",
     *      "mime_type": "image/jpeg",
     *      "content": "Base64 encoded image data"
     *  }
     * }
     * </pre>
     */
    private static final class Photo {
        @Json(name = "file_name")
        private final String fileName;
        @Json(name = "mime_type")
        private final String mimeType;
        @Json(name = "content")
        private final byte[] content;

        private Photo(String fileName, String mimeType, byte[] content) {
            this.fileName = fileName;
            this.mimeType = mimeType;
            this.content = content;
        }

        @Override
        public String toString() {
            return "Photo{ fileName='" + fileName + '\'' + ", mimeType='" + mimeType + '\''
                  + ", content=" + Arrays.toString(content) + '}';
        }
    }
}
