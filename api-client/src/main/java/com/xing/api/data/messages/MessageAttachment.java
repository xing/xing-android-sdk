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

import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.xing.api.data.SafeCalendar;

import java.io.Serializable;

/**
 * Java representation of an Attachment on the Message.
 */
@AutoValue
public abstract class MessageAttachment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "id")
    public abstract String id();

    @Json(name = "created_at")
    public abstract SafeCalendar createdAt();

    @Json(name = "filename")
    public abstract String fileName();

    @Json(name = "mime_type")
    public abstract String mimeType();

    @Json(name = "size")
    public abstract int fileSize();

    static Builder builder() {
        return new AutoValue_MessageAttachment.Builder();
    }

    @AutoValue.Builder
    abstract static class Builder {
        abstract Builder id(String id);

        abstract Builder createdAt(SafeCalendar createdAt);

        abstract Builder fileName(String fileName);

        abstract Builder mimeType(String mimeType);

        abstract Builder fileSize(int fileSize);

        abstract MessageAttachment build();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MessageAttachment) {
            if (((MessageAttachment) obj).id().equals(id())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id().hashCode();
    }

    public static JsonAdapter<MessageAttachment> jsonAdapter(Moshi moshi) {
        return new AutoValue_MessageAttachment.MoshiJsonAdapter(moshi);
    }
}
