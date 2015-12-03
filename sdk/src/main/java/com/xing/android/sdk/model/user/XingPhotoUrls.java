/*
 * Copyright (c) 2015 XING AG (http://xing.com/)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.xing.android.sdk.model.user;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import java.io.Serializable;

/**
 * Represents the photos of a user.
 *
 * @author serj.lotutovici
 */
@SuppressWarnings("unused")
public class XingPhotoUrls implements Serializable, Parcelable {
    private static final long serialVersionUID = 1864837657156226558L;
    /** Parcelable creator required by contract. */
    public static final Creator<XingPhotoUrls> CREATOR = new Creator<XingPhotoUrls>() {

        @Override
        public XingPhotoUrls createFromParcel(Parcel source) {
            return new XingPhotoUrls(source);
        }

        @Override
        public XingPhotoUrls[] newArray(int size) {
            return new XingPhotoUrls[size];
        }
    };

    @Json(name = "large")
    private Uri photoLargeUrl;
    @Json(name = "maxi_thumb")
    private Uri photoMaxiThumbUrl;
    @Json(name = "medium_thumb")
    private Uri photoMediumThumbUrl;
    @Json(name = "mini_thumb")
    private Uri photoMiniThumbUrl;
    @Json(name = "thumb")
    private Uri photoThumbUrl;
    @Json(name = "size_32x32")
    private Uri photoSize32Url;
    @Json(name = "size_48x48")
    private Uri photoSize48Url;
    @Json(name = "size_64x64")
    private Uri photoSize64Url;
    @Json(name = "size_96x96")
    private Uri photoSize96Url;
    @Json(name = "size_128x128")
    private Uri photoSize128Url;
    @Json(name = "size_192x192")
    private Uri photoSize192Url;
    @Json(name = "size_256x256")
    private Uri photoSize256Url;
    @Json(name = "size_1024x1024")
    private Uri photoSize1024Url;
    @Json(name = "size_original")
    private Uri photoSizeOriginalUrl;

    /** Create a simple XingPhotoUrls object with empty fields. */
    public XingPhotoUrls() {
    }

    /**
     * Create {@link XingPhotoUrls} from {@link Parcel}.
     *
     * @param in Input {@link Parcel}
     */
    private XingPhotoUrls(Parcel in) {
        photoLargeUrl = in.readParcelable(Uri.class.getClassLoader());
        photoMaxiThumbUrl = in.readParcelable(Uri.class.getClassLoader());
        photoMediumThumbUrl = in.readParcelable(Uri.class.getClassLoader());
        photoMiniThumbUrl = in.readParcelable(Uri.class.getClassLoader());
        photoThumbUrl = in.readParcelable(Uri.class.getClassLoader());
        photoSize32Url = in.readParcelable(Uri.class.getClassLoader());
        photoSize48Url = in.readParcelable(Uri.class.getClassLoader());
        photoSize64Url = in.readParcelable(Uri.class.getClassLoader());
        photoSize96Url = in.readParcelable(Uri.class.getClassLoader());
        photoSize128Url = in.readParcelable(Uri.class.getClassLoader());
        photoSize192Url = in.readParcelable(Uri.class.getClassLoader());
        photoSize256Url = in.readParcelable(Uri.class.getClassLoader());
        photoSize1024Url = in.readParcelable(Uri.class.getClassLoader());
        photoSizeOriginalUrl = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof XingPhotoUrls)) {
            return false;
        }

        XingPhotoUrls xingPhotoUrls = (XingPhotoUrls) obj;
        return hashCode() == xingPhotoUrls.hashCode();
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(photoLargeUrl, 0);
        dest.writeParcelable(photoMaxiThumbUrl, 0);
        dest.writeParcelable(photoMediumThumbUrl, 0);
        dest.writeParcelable(photoMiniThumbUrl, 0);
        dest.writeParcelable(photoThumbUrl, 0);
        dest.writeParcelable(photoSize32Url, 0);
        dest.writeParcelable(photoSize48Url, 0);
        dest.writeParcelable(photoSize64Url, 0);
        dest.writeParcelable(photoSize96Url, 0);
        dest.writeParcelable(photoSize128Url, 0);
        dest.writeParcelable(photoSize192Url, 0);
        dest.writeParcelable(photoSize256Url, 0);
        dest.writeParcelable(photoSize1024Url, 0);
        dest.writeParcelable(photoSizeOriginalUrl, 0);
    }

    public Uri getPhotoLargeUrl() {
        return photoLargeUrl;
    }

    public void setPhotoLargeUrl(Uri photoLargeUrl) {
        this.photoLargeUrl = photoLargeUrl;
    }

    public Uri getPhotoMaxiThumbUrl() {
        return photoMaxiThumbUrl;
    }

    public void setPhotoMaxiThumbUrl(Uri photoMaxiThumbUrl) {
        this.photoMaxiThumbUrl = photoMaxiThumbUrl;
    }

    public Uri getPhotoMediumThumbUrl() {
        return photoMediumThumbUrl;
    }

    public void setPhotoMediumThumbUrl(Uri photoMediumThumbUrl) {
        this.photoMediumThumbUrl = photoMediumThumbUrl;
    }

    public Uri getPhotoMiniThumbUrl() {
        return photoMiniThumbUrl;
    }

    public void setPhotoMiniThumbUrl(Uri photoMiniUrl) {
        photoMiniThumbUrl = photoMiniUrl;
    }

    public Uri getPhotoThumbUrl() {
        return photoThumbUrl;
    }

    public void setPhotoThumbUrl(Uri photoThumbUrl) {
        this.photoThumbUrl = photoThumbUrl;
    }

    public Uri getPhotoSize32Url() {
        return photoSize32Url;
    }

    public void setPhotoSize32Url(Uri photoSize32Url) {
        this.photoSize32Url = photoSize32Url;
    }

    public Uri getPhotoSize48Url() {
        return photoSize48Url;
    }

    public void setPhotoSize48Url(Uri photoSize48Url) {
        this.photoSize48Url = photoSize48Url;
    }

    public Uri getPhotoSize64Url() {
        return photoSize64Url;
    }

    public void setPhotoSize64Url(Uri photoSize64Url) {
        this.photoSize64Url = photoSize64Url;
    }

    public Uri getPhotoSize96Url() {
        return photoSize96Url;
    }

    public void setPhotoSize96Url(Uri photoSize96Url) {
        this.photoSize96Url = photoSize96Url;
    }

    public Uri getPhotoSize128Url() {
        return photoSize128Url;
    }

    public void setPhotoSize128Url(Uri photoSize128Url) {
        this.photoSize128Url = photoSize128Url;
    }

    public Uri getPhotoSize192Url() {
        return photoSize192Url;
    }

    public void setPhotoSize192Url(Uri photoSize192Url) {
        this.photoSize192Url = photoSize192Url;
    }

    public Uri getPhotoSize256Url() {
        return photoSize256Url;
    }

    public void setPhotoSize256Url(Uri photoSize256Url) {
        this.photoSize256Url = photoSize256Url;
    }

    public Uri getPhotoSize1024Url() {
        return photoSize1024Url;
    }

    public void setPhotoSize1024Url(Uri photoSize1024Url) {
        this.photoSize1024Url = photoSize1024Url;
    }

    public Uri getPhotoSizeOriginalUrl() {
        return photoSizeOriginalUrl;
    }

    public void setPhotoSizeOriginalUrl(Uri photoSizeOriginalUrl) {
        this.photoSizeOriginalUrl = photoSizeOriginalUrl;
    }
}
