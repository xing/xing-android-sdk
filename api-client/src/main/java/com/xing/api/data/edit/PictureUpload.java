/*
 * Copyright (С) 2016 XING AG (http://xing.com/)
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
package com.xing.api.data.edit;

import com.squareup.moshi.Json;

import java.util.Arrays;

/**
 * Represents a profile picture upload model containing required information for the upload.
 * <p>
 * <strong>Use</strong> only with
 * {@linkplain com.xing.api.resources.ProfileEditingResource#updateProfilePicture(PictureUpload)
 * ProfileEditingResource#updateProfilePicture(PictureUpload)} method. The JSON which will
 * be send will look similar to:
 * <pre>{@code
 * {
 *   "photo": {
 *      "file_name": "test.jpg",
 *      "mime_type": "image/jpeg",
 *      "content": "Base64 encoded image data"
 *   }
 * }
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

    /** Creates a {@link PictureUpload} object with the mimetype set to JPEG. */
    public static PictureUpload pictureUploadJPEG(String fileName, byte[] content) {
        return new PictureUpload(fileName, MIME_TYPE_JPEG, content);
    }

    /** Creates a {@link PictureUpload} object with the mimetype set to PNG. */
    public static PictureUpload pictureUploadPNG(String fileName, byte[] content) {
        return new PictureUpload(fileName, MIME_TYPE_PNG, content);
    }

    @Json(name = "file_name")
    private final String fileName;
    @Json(name = "mime_type")
    private final String mimeType;
    @Json(name = "content")
    private final byte[] content;

    private PictureUpload(String fileName, String mimeType, byte[] content) {
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.content = content;
    }

    @Override
    public String toString() {
        return "PictureUpload{"
              + "fileName='" + fileName + '\''
              + ", mimeType='" + mimeType + '\''
              + ", content=" + Arrays.toString(content)
              + '}';
    }
}
