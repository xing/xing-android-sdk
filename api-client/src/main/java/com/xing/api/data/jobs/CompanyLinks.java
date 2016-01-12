package com.xing.api.data.jobs;

import com.squareup.moshi.Json;

import java.io.Serializable;

/**
 * @author cristian.monforte
 */
public class CompanyLinks implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "xing")
    private final String xing;
    @Json(name = "thumbnail")
    private final String thumbnail;
    @Json(name = "logo")
    private final String logo;

    public CompanyLinks(String xing, String thumbnail, String logo) {
        this.xing = xing;
        this.thumbnail = thumbnail;
        this.logo = logo;
    }

    public String xing() {
        return xing;
    }

    public String thumbnail() {
        return thumbnail;
    }

    public String logo() {
        return logo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanyLinks that = (CompanyLinks) o;

        if (xing != null ? !xing.equals(that.xing) : that.xing != null) return false;
        if (thumbnail != null ? !thumbnail.equals(that.thumbnail) : that.thumbnail != null) return false;
        return !(logo != null ? !logo.equals(that.logo) : that.logo != null);
    }

    @Override
    public int hashCode() {
        int result = xing != null ? xing.hashCode() : 0;
        result = 31 * result + (thumbnail != null ? thumbnail.hashCode() : 0);
        result = 31 * result + (logo != null ? logo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CompanyLinks{" +
              "xing='" + xing + '\'' +
              ", thumbnail='" + thumbnail + '\'' +
              ", logo='" + logo + '\'' +
              '}';
    }
}
