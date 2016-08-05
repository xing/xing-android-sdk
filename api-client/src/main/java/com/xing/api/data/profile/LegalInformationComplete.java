package com.xing.api.data.profile;

import com.squareup.moshi.Json;

import java.io.Serializable;

/** Represents full legal information */
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
        if (o == null || getClass() != o.getClass()) return false;

        LegalInformationComplete that = (LegalInformationComplete) o;

        return content != null ? content.equals(that.content) : that.content == null;
    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "LegalInformationComplete{" +
              "content='" + content + '\'' +
              '}';
    }
}