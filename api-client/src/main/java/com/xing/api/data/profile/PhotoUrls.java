/*
 * Copyright (С) 2015 XING AG (http://xing.com/)
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
 * Java representation of a {@linkplain XingUser user's} picture/photo container. XWS scales the user's profile
 * picture in a different variety of sizes. This object allows the <strong>user</strong> to use any of the
 * {@linkplain XingUser user's} profile picture without scaling on the client side.
 *
 * @see <a href="https://dev.xing.com/docs/get/users/:id">User Profile Resource</a>
 */
@SuppressWarnings("unused")
public class PhotoUrls implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "large")
    private String photoLargeUrl;
    @Json(name = "maxi_thumb")
    private String photoMaxiThumbUrl;
    @Json(name = "medium_thumb")
    private String photoMediumThumbUrl;
    @Json(name = "mini_thumb")
    private String photoMiniThumbUrl;
    @Json(name = "thumb")
    private String photoThumbUrl;
    @Json(name = "size_32x32")
    private String photoSize32Url;
    @Json(name = "size_48x48")
    private String photoSize48Url;
    @Json(name = "size_64x64")
    private String photoSize64Url;
    @Json(name = "size_96x96")
    private String photoSize96Url;
    @Json(name = "size_128x128")
    private String photoSize128Url;
    @Json(name = "size_192x192")
    private String photoSize192Url;
    @Json(name = "size_256x256")
    private String photoSize256Url;
    @Json(name = "size_1024x1024")
    private String photoSize1024Url;
    @Json(name = "size_original")
    private String photoSizeOriginalUrl;

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        //noinspection QuestionableName
        PhotoUrls that = (PhotoUrls) o;

        if (photoLargeUrl != null ? !photoLargeUrl.equals(that.photoLargeUrl) : that.photoLargeUrl != null) {
            return false;
        }
        if (photoMaxiThumbUrl != null ? !photoMaxiThumbUrl.equals(that.photoMaxiThumbUrl)
              : that.photoMaxiThumbUrl != null) {
            return false;
        }
        if (photoMediumThumbUrl != null ? !photoMediumThumbUrl.equals(that.photoMediumThumbUrl)
              : that.photoMediumThumbUrl != null) {
            return false;
        }
        if (photoMiniThumbUrl != null ? !photoMiniThumbUrl.equals(that.photoMiniThumbUrl)
              : that.photoMiniThumbUrl != null) {
            return false;
        }
        if (photoThumbUrl != null ? !photoThumbUrl.equals(that.photoThumbUrl) : that.photoThumbUrl != null) {
            return false;
        }
        if (photoSize32Url != null ? !photoSize32Url.equals(that.photoSize32Url) : that.photoSize32Url != null) {
            return false;
        }
        if (photoSize48Url != null ? !photoSize48Url.equals(that.photoSize48Url) : that.photoSize48Url != null) {
            return false;
        }
        if (photoSize64Url != null ? !photoSize64Url.equals(that.photoSize64Url) : that.photoSize64Url != null) {
            return false;
        }
        if (photoSize96Url != null ? !photoSize96Url.equals(that.photoSize96Url) : that.photoSize96Url != null) {
            return false;
        }
        if (photoSize128Url != null ? !photoSize128Url.equals(that.photoSize128Url) : that.photoSize128Url != null) {
            return false;
        }
        if (photoSize192Url != null ? !photoSize192Url.equals(that.photoSize192Url) : that.photoSize192Url != null) {
            return false;
        }
        if (photoSize256Url != null ? !photoSize256Url.equals(that.photoSize256Url) : that.photoSize256Url != null) {
            return false;
        }
        if (photoSize1024Url != null ? !photoSize1024Url.equals(that.photoSize1024Url)
              : that.photoSize1024Url != null) {
            return false;
        }
        return photoSizeOriginalUrl != null ? photoSizeOriginalUrl.equals(that.photoSizeOriginalUrl)
              : that.photoSizeOriginalUrl == null;
    }

    @Override
    public int hashCode() {
        int result = photoLargeUrl != null ? photoLargeUrl.hashCode() : 0;
        result = 31 * result + (photoMaxiThumbUrl != null ? photoMaxiThumbUrl.hashCode() : 0);
        result = 31 * result + (photoMediumThumbUrl != null ? photoMediumThumbUrl.hashCode() : 0);
        result = 31 * result + (photoMiniThumbUrl != null ? photoMiniThumbUrl.hashCode() : 0);
        result = 31 * result + (photoThumbUrl != null ? photoThumbUrl.hashCode() : 0);
        result = 31 * result + (photoSize32Url != null ? photoSize32Url.hashCode() : 0);
        result = 31 * result + (photoSize48Url != null ? photoSize48Url.hashCode() : 0);
        result = 31 * result + (photoSize64Url != null ? photoSize64Url.hashCode() : 0);
        result = 31 * result + (photoSize96Url != null ? photoSize96Url.hashCode() : 0);
        result = 31 * result + (photoSize128Url != null ? photoSize128Url.hashCode() : 0);
        result = 31 * result + (photoSize192Url != null ? photoSize192Url.hashCode() : 0);
        result = 31 * result + (photoSize256Url != null ? photoSize256Url.hashCode() : 0);
        result = 31 * result + (photoSize1024Url != null ? photoSize1024Url.hashCode() : 0);
        result = 31 * result + (photoSizeOriginalUrl != null ? photoSizeOriginalUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PhotoUrls{"
              + "photoLargeUrl='" + photoLargeUrl + '\''
              + ", photoMaxiThumbUrl='" + photoMaxiThumbUrl + '\''
              + ", photoMediumThumbUrl='" + photoMediumThumbUrl + '\''
              + ", photoMiniThumbUrl='" + photoMiniThumbUrl + '\''
              + ", photoThumbUrl='" + photoThumbUrl + '\''
              + ", photoSize32Url='" + photoSize32Url + '\''
              + ", photoSize48Url='" + photoSize48Url + '\''
              + ", photoSize64Url='" + photoSize64Url + '\''
              + ", photoSize96Url='" + photoSize96Url + '\''
              + ", photoSize128Url='" + photoSize128Url + '\''
              + ", photoSize192Url='" + photoSize192Url + '\''
              + ", photoSize256Url='" + photoSize256Url + '\''
              + ", photoSize1024Url='" + photoSize1024Url + '\''
              + ", photoSizeOriginalUrl='" + photoSizeOriginalUrl + '\''
              + '}';
    }

    public String getPhotoLargeUrl() {
        return photoLargeUrl;
    }

    public void setPhotoLargeUrl(String photoLargeUrl) {
        this.photoLargeUrl = photoLargeUrl;
    }

    public String getPhotoMaxiThumbUrl() {
        return photoMaxiThumbUrl;
    }

    public void setPhotoMaxiThumbUrl(String photoMaxiThumbUrl) {
        this.photoMaxiThumbUrl = photoMaxiThumbUrl;
    }

    public String getPhotoMediumThumbUrl() {
        return photoMediumThumbUrl;
    }

    public void setPhotoMediumThumbUrl(String photoMediumThumbUrl) {
        this.photoMediumThumbUrl = photoMediumThumbUrl;
    }

    public String getPhotoMiniThumbUrl() {
        return photoMiniThumbUrl;
    }

    public void setPhotoMiniThumbUrl(String photoMiniUrl) {
        photoMiniThumbUrl = photoMiniUrl;
    }

    public String getPhotoThumbUrl() {
        return photoThumbUrl;
    }

    public void setPhotoThumbUrl(String photoThumbUrl) {
        this.photoThumbUrl = photoThumbUrl;
    }

    public String getPhotoSize32Url() {
        return photoSize32Url;
    }

    public void setPhotoSize32Url(String photoSize32Url) {
        this.photoSize32Url = photoSize32Url;
    }

    public String getPhotoSize48Url() {
        return photoSize48Url;
    }

    public void setPhotoSize48Url(String photoSize48Url) {
        this.photoSize48Url = photoSize48Url;
    }

    public String getPhotoSize64Url() {
        return photoSize64Url;
    }

    public void setPhotoSize64Url(String photoSize64Url) {
        this.photoSize64Url = photoSize64Url;
    }

    public String getPhotoSize96Url() {
        return photoSize96Url;
    }

    public void setPhotoSize96Url(String photoSize96Url) {
        this.photoSize96Url = photoSize96Url;
    }

    public String getPhotoSize128Url() {
        return photoSize128Url;
    }

    public void setPhotoSize128Url(String photoSize128Url) {
        this.photoSize128Url = photoSize128Url;
    }

    public String getPhotoSize192Url() {
        return photoSize192Url;
    }

    public void setPhotoSize192Url(String photoSize192Url) {
        this.photoSize192Url = photoSize192Url;
    }

    public String getPhotoSize256Url() {
        return photoSize256Url;
    }

    public void setPhotoSize256Url(String photoSize256Url) {
        this.photoSize256Url = photoSize256Url;
    }

    public String getPhotoSize1024Url() {
        return photoSize1024Url;
    }

    public void setPhotoSize1024Url(String photoSize1024Url) {
        this.photoSize1024Url = photoSize1024Url;
    }

    public String getPhotoSizeOriginalUrl() {
        return photoSizeOriginalUrl;
    }

    public void setPhotoSizeOriginalUrl(String photoSizeOriginalUrl) {
        this.photoSizeOriginalUrl = photoSizeOriginalUrl;
    }
}
