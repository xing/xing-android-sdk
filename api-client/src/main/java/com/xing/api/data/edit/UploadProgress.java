/*
 * Copyright (C) 2018 XING SE (http://xing.com/)
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

/**
 * Represents the upload progress response for the
 * {@link com.xing.api.resources.ProfileEditingResource#getPictureUploadProgress()} call.
 *
 * @author serj.lotutovici
 */
public class UploadProgress {
    /** Possible values for upload progress status. */
    public enum Status {
        IN_PROGRESS,
        DONE,
        FAILED
    }

    @Json(name = "status")
    private final Status status;
    @Json(name = "percentage")
    private final int percentage;

    public UploadProgress(Status status, int percentage) {
        this.status = status;
        this.percentage = percentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UploadProgress other = (UploadProgress) o;
        return percentage == other.percentage && status == other.status;
    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + percentage;
        return result;
    }

    @Override
    public String toString() {
        return "UploadProgress{"
              + "status=" + status
              + ", percentage=" + percentage
              + '}';
    }

    public Status status() {
        return status;
    }

    public int percentage() {
        return percentage;
    }
}
