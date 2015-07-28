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

import java.io.Serializable;

/**
 * Represents the photos of a user.
 *
 * @author serj.lotutovici
 */
@SuppressWarnings("unused")
public class XingPhotoUrls implements Serializable, Parcelable {

    private static final long serialVersionUID = 1864837657156226558L;

    /**
     * Parcelable creator required by contract
     */
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

    private Uri mPhotoLargeUrl;
    private Uri mPhotoMaxiThumbUrl;
    private Uri mPhotoMediumThumbUrl;
    private Uri mPhotoMiniThumbUrl;
    private Uri mPhotoThumbUrl;
    private Uri mPhotoSize32Url;
    private Uri mPhotoSize48Url;
    private Uri mPhotoSize64Url;
    private Uri mPhotoSize96Url;
    private Uri mPhotoSize128Url;
    private Uri mPhotoSize192Url;
    private Uri mPhotoSize256Url;
    private Uri mPhotoSize1024Url;
    private Uri mPhotoSizeOriginalUrl;

    /**
     * Create a simple XingPhotoUrls object with empty fields.
     */
    public XingPhotoUrls() {
    }

    /**
     * Create {@link XingPhotoUrls} from {@link Parcel}
     *
     * @param in Input {@link Parcel}
     */
    private XingPhotoUrls(Parcel in) {
        mPhotoLargeUrl = in.readParcelable(Uri.class.getClassLoader());
        mPhotoMaxiThumbUrl = in.readParcelable(Uri.class.getClassLoader());
        mPhotoMediumThumbUrl = in.readParcelable(Uri.class.getClassLoader());
        mPhotoMiniThumbUrl = in.readParcelable(Uri.class.getClassLoader());
        mPhotoThumbUrl = in.readParcelable(Uri.class.getClassLoader());
        mPhotoSize32Url = in.readParcelable(Uri.class.getClassLoader());
        mPhotoSize48Url = in.readParcelable(Uri.class.getClassLoader());
        mPhotoSize64Url = in.readParcelable(Uri.class.getClassLoader());
        mPhotoSize96Url = in.readParcelable(Uri.class.getClassLoader());
        mPhotoSize128Url = in.readParcelable(Uri.class.getClassLoader());
        mPhotoSize192Url = in.readParcelable(Uri.class.getClassLoader());
        mPhotoSize256Url = in.readParcelable(Uri.class.getClassLoader());
        mPhotoSize1024Url = in.readParcelable(Uri.class.getClassLoader());
        mPhotoSizeOriginalUrl = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof XingPhotoUrls)) {
            return false;
        }

        XingPhotoUrls that = (XingPhotoUrls) obj;
        return hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        int result = mPhotoLargeUrl != null ? mPhotoLargeUrl.hashCode() : 0;
        result = 31 * result + (mPhotoMaxiThumbUrl != null ? mPhotoMaxiThumbUrl.hashCode() : 0);
        result = 31 * result + (mPhotoMediumThumbUrl != null ? mPhotoMediumThumbUrl.hashCode() : 0);
        result = 31 * result + (mPhotoMiniThumbUrl != null ? mPhotoMiniThumbUrl.hashCode() : 0);
        result = 31 * result + (mPhotoThumbUrl != null ? mPhotoThumbUrl.hashCode() : 0);
        result = 31 * result + (mPhotoSize32Url != null ? mPhotoSize32Url.hashCode() : 0);
        result = 31 * result + (mPhotoSize48Url != null ? mPhotoSize48Url.hashCode() : 0);
        result = 31 * result + (mPhotoSize64Url != null ? mPhotoSize64Url.hashCode() : 0);
        result = 31 * result + (mPhotoSize96Url != null ? mPhotoSize96Url.hashCode() : 0);
        result = 31 * result + (mPhotoSize128Url != null ? mPhotoSize128Url.hashCode() : 0);
        result = 31 * result + (mPhotoSize192Url != null ? mPhotoSize192Url.hashCode() : 0);
        result = 31 * result + (mPhotoSize256Url != null ? mPhotoSize256Url.hashCode() : 0);
        result = 31 * result + (mPhotoSize1024Url != null ? mPhotoSize1024Url.hashCode() : 0);
        result = 31 * result + (mPhotoSizeOriginalUrl != null ? mPhotoSizeOriginalUrl.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mPhotoLargeUrl, 0);
        dest.writeParcelable(mPhotoMaxiThumbUrl, 0);
        dest.writeParcelable(mPhotoMediumThumbUrl, 0);
        dest.writeParcelable(mPhotoMiniThumbUrl, 0);
        dest.writeParcelable(mPhotoThumbUrl, 0);
        dest.writeParcelable(mPhotoSize32Url, 0);
        dest.writeParcelable(mPhotoSize48Url, 0);
        dest.writeParcelable(mPhotoSize64Url, 0);
        dest.writeParcelable(mPhotoSize96Url, 0);
        dest.writeParcelable(mPhotoSize128Url, 0);
        dest.writeParcelable(mPhotoSize192Url, 0);
        dest.writeParcelable(mPhotoSize256Url, 0);
        dest.writeParcelable(mPhotoSize1024Url, 0);
        dest.writeParcelable(mPhotoSizeOriginalUrl, 0);
    }

    public Uri getPhotoLargeUrl() {
        return mPhotoLargeUrl;
    }

    public void setPhotoLargeUrl(Uri photoLargeUrl) {
        mPhotoLargeUrl = photoLargeUrl;
    }

    public Uri getPhotoMaxiThumbUrl() {
        return mPhotoMaxiThumbUrl;
    }

    public void setPhotoMaxiThumbUrl(Uri photoMaxiThumbUrl) {
        mPhotoMaxiThumbUrl = photoMaxiThumbUrl;
    }

    public Uri getPhotoMediumThumbUrl() {
        return mPhotoMediumThumbUrl;
    }

    public void setPhotoMediumThumbUrl(Uri photoMediumThumbUrl) {
        mPhotoMediumThumbUrl = photoMediumThumbUrl;
    }

    public Uri getPhotoMiniThumbUrl() {
        return mPhotoMiniThumbUrl;
    }

    public void setPhotoMiniThumbUrl(Uri photoMiniUrl) {
        mPhotoMiniThumbUrl = photoMiniUrl;
    }

    public Uri getPhotoThumbUrl() {
        return mPhotoThumbUrl;
    }

    public void setPhotoThumbUrl(Uri photoThumbUrl) {
        mPhotoThumbUrl = photoThumbUrl;
    }

    public Uri getPhotoSize32Url() {
        return mPhotoSize32Url;
    }

    public void setPhotoSize32Url(Uri photoSize32Url) {
        mPhotoSize32Url = photoSize32Url;
    }

    public Uri getPhotoSize48Url() {
        return mPhotoSize48Url;
    }

    public void setPhotoSize48Url(Uri photoSize48Url) {
        mPhotoSize48Url = photoSize48Url;
    }

    public Uri getPhotoSize64Url() {
        return mPhotoSize64Url;
    }

    public void setPhotoSize64Url(Uri photoSize64Url) {
        mPhotoSize64Url = photoSize64Url;
    }

    public Uri getPhotoSize96Url() {
        return mPhotoSize96Url;
    }

    public void setPhotoSize96Url(Uri photoSize96Url) {
        mPhotoSize96Url = photoSize96Url;
    }

    public Uri getPhotoSize128Url() {
        return mPhotoSize128Url;
    }

    public void setPhotoSize128Url(Uri photoSize128Url) {
        mPhotoSize128Url = photoSize128Url;
    }

    public Uri getPhotoSize192Url() {
        return mPhotoSize192Url;
    }

    public void setPhotoSize192Url(Uri photoSize192Url) {
        mPhotoSize192Url = photoSize192Url;
    }

    public Uri getPhotoSize256Url() {
        return mPhotoSize256Url;
    }

    public void setPhotoSize256Url(Uri photoSize256Url) {
        mPhotoSize256Url = photoSize256Url;
    }

    public Uri getPhotoSize1024Url() {
        return mPhotoSize1024Url;
    }

    public void setPhotoSize1024Url(Uri photoSize1024Url) {
        mPhotoSize1024Url = photoSize1024Url;
    }

    public Uri getPhotoSizeOriginalUrl() {
        return mPhotoSizeOriginalUrl;
    }

    public void setPhotoSizeOriginalUrl(Uri photoSizeOriginalUrl) {
        mPhotoSizeOriginalUrl = photoSizeOriginalUrl;
    }
}
