/*
 * Copyright (c) 2016 XING AG (http://xing.com/)
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

package com.xing.api.data.messages;

import com.squareup.moshi.Json;
import com.xing.api.data.SafeCalendar;

import java.io.Serializable;

/**
 * Java representation of an Attachment on the Message.
 */
public class MessageAttachment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "id")
    private String id;
    @Json(name = "created_at")
    private SafeCalendar createdAt;
    @Json(name = "filename")
    private String fileName;
    @Json(name = "mime_type")
    private String mimeType;
    @Json(name = "size")
    private int fileSize;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MessageAttachment) {
            if (((MessageAttachment) obj).id.equals(id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MessageAttachment{"
              + "id='" + id + '\''
              + ", createdAt='" + createdAt + '\''
              + ", fileName='" + fileName + '\''
              + ", mimeType='" + mimeType + '\''
              + ", fileSize='" + fileSize + '\''
              + '}';
    }

    public String id() {
        return id;
    }

    public MessageAttachment id(String id) {
        this.id = id;
        return this;
    }

    public SafeCalendar createdAt() {
        return createdAt;
    }

    public MessageAttachment createdAt(SafeCalendar createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String fileName() {
        return fileName;
    }

    public MessageAttachment fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String mimeType() {
        return mimeType;
    }

    public MessageAttachment mimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public int fileSize() {
        return fileSize;
    }

    public MessageAttachment fileSize(int fileSize) {
        this.fileSize = fileSize;
        return this;
    }
}
