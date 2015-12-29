/*
 * Copyright (С) 2015 XING AG (http://xing.com/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xing.api.model;

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
            return "Photo{ fileName='"
                  + fileName + '\''
                  + ", mimeType='" + mimeType + '\''
                  + ", content=" + Arrays.toString(content)
                  + '}';
        }
    }
}
