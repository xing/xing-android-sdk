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
package com.xing.api.data.groups;

import com.squareup.moshi.Json;

import java.io.Serializable;

/**
 * A media preview that can be used to display a preview of a link.
 */
public class MediaPreview implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "id")
    private String id;
    @Json(name = "url")
    private String url;
    @Json(name = "title")
    private String title;
    @Json(name = "description")
    private String description;
    @Json(name = "image_url")
    private String imageUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaPreview that = (MediaPreview) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        return imageUrl != null ? imageUrl.equals(that.imageUrl) : that.imageUrl == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MediaPreview{"
              + "id='" + id + '\''
              + ", url='" + url + '\''
              + ", title='" + title + '\''
              + ", description='" + description + '\''
              + ", imageUrl='" + imageUrl + '\''
              + '}';
    }

    public String id() {
        return id;
    }

    public MediaPreview id(String id) {
        this.id = id;
        return this;
    }

    public String url() {
        return url;
    }

    public MediaPreview url(String url) {
        this.url = url;
        return this;
    }

    public String title() {
        return title;
    }

    public MediaPreview title(String title) {
        this.title = title;
        return this;
    }

    public String description() {
        return description;
    }

    public MediaPreview description(String description) {
        this.description = description;
        return this;
    }

    public String imageUrl() {
        return imageUrl;
    }

    public MediaPreview imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
