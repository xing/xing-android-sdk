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
package com.xing.api.data.jobs;

import com.squareup.moshi.Json;

import java.io.Serializable;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanyLinks links = (CompanyLinks) o;
        return xing != null ? xing.equals(links.xing) : links.xing == null
              && (thumbnail != null ? thumbnail.equals(links.thumbnail) : links.thumbnail == null
              && (logo != null ? logo.equals(links.logo) : links.logo == null));
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
        return "CompanyLinks{"
              + "xing='" + xing + '\''
              + ", thumbnail='" + thumbnail + '\''
              + ", logo='" + logo + '\''
              + '}';
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
}
