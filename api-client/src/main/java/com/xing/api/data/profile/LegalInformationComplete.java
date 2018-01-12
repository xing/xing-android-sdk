/*
 * Copyright (С) 2018 XING SE (http://xing.com/)
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
package com.xing.api.data.profile;

import com.squareup.moshi.Json;

import java.io.Serializable;

/**
 * Represents full legal information.
 */
public class LegalInformationComplete implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "content")
    private String content;

    public LegalInformationComplete(String content) {
        this.content = content;
    }

    public String content() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LegalInformationComplete)) return false;

        LegalInformationComplete that = (LegalInformationComplete) o;
        return content != null ? content.equals(that.content) : that.content == null;
    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "LegalInformationComplete{"
                + "content='" + content + '\''
                + '}';
    }
}
