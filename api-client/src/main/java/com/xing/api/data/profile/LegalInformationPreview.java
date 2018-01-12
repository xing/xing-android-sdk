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
import com.xing.api.resources.UserProfilesResource;

import java.io.Serializable;

/**
 * Part of the {@link XingUser} that contains the user's set legal information as a preview.
 *
 * To get the full version use {@link UserProfilesResource#getOwnLegalInformation()} or
 * {@link UserProfilesResource#getUserLegalInformation(String)}
 */
@SuppressWarnings("unused")
public class LegalInformationPreview implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "preview_content")
    private String previewContent;

    public String previewContent() {
        return previewContent;
    }

    public LegalInformationPreview previewContent(String previewContent) {
        this.previewContent = previewContent;
        return this;
    }

    @Override
    public int hashCode() {
        return previewContent != null ? previewContent.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof LegalInformationPreview)) return false;

        LegalInformationPreview legalInformationPreview = (LegalInformationPreview) obj;
        return previewContent != null ? previewContent.equals(legalInformationPreview.previewContent)
              : legalInformationPreview.previewContent == null;
    }

    @Override
    public String toString() {
        return "LegalInformationPreview{"
              + "previewContent='" + previewContent + '\''
              + '}';
    }
}
