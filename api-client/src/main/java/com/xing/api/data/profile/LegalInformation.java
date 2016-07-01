package com.xing.api.data.profile;

import com.squareup.moshi.Json;
import com.xing.api.resources.UserProfilesResource;

/**
 * Part of the {@link XingUser} that contains the user's set legal information as a preview.
 * <p>
 * To get the full version use {@link UserProfilesResource#getOwnLegalInformation()} or
 * {@link UserProfilesResource#getUserLegalInformation(String)}
 */
@SuppressWarnings("unused")
public class LegalInformation {

    @Json(name = "preview_content")
    private String previewContent;

    public String previewContent() {
        return previewContent;
    }

    public LegalInformation previewContent(String previewContent) {
        this.previewContent = previewContent;
        return this;
    }

    @Override
    public int hashCode() {
        return previewContent != null ? previewContent.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "LegalInformation{"
              + "previewContent='" + previewContent + '\''
              + '}';
    }
}
