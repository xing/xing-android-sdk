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


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Represent the industry contained in a {@link ExperienceCompany}.
 *
 * @author angelo.marchesin
 */
@SuppressWarnings("unused")
public class Industry implements Serializable, Parcelable {
    private static final long serialVersionUID = 6164637739744149347L;
    private static final float FIRST_CATEGORY_CONVERSION = 10000f;
    /** Creator object for the Parcelable contract. */
    public static final Creator<Industry> CREATOR = new Creator<Industry>() {
        @Override
        public Industry createFromParcel(Parcel source) {
            return new Industry(source);
        }

        @Override
        public Industry[] newArray(int size) {
            return new Industry[size];
        }
    };

    private final int mId;
    private List<Segment> mSegments;
    private String mTypeName;

    public static Industry newInstanceFromCompoundId(int compoundId) {
        Segment segment = new Segment(compoundId, "");
        return new Industry(extractFirstCategoryIndex(compoundId), "", Collections.singletonList(segment));
    }

    private static int extractFirstCategoryIndex(int encodedId) {
        return (int) (Math.floor(encodedId / FIRST_CATEGORY_CONVERSION) * FIRST_CATEGORY_CONVERSION);
    }

    public Industry(Parcel source) {
        mId = source.readInt();
        mTypeName = source.readString();
        source.readList(mSegments, List.class.getClassLoader());
    }

    public Industry(int id, String type) {
        this(id, type, null);
    }

    public Industry(int id, @NonNull String typeName, @Nullable List<Segment> segments) {
        mId = id;
        mTypeName = typeName;
        mSegments = segments;
    }

    public int getId() {
        return mId;
    }

    public String getTypeName() {
        return mTypeName;
    }

    public void setTypeName(String typeName) {
        mTypeName = typeName;
    }

    @Override
    public String toString() {
        return mTypeName;
    }

    @Nullable
    public List<Segment> getSegments() {
        return mSegments;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Industry && mId == ((Industry) object).mId;
    }

    @Override
    public int hashCode() {
        return mTypeName.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mTypeName);
        dest.writeList(mSegments);
    }

    /**
     * Represent a segment contained in the {@link Industry}'s list.
     *
     * @author angelo.marchesin
     */
    public static class Segment implements Serializable {
        private static final long serialVersionUID = -3150915425802346415L;

        private final int mId;
        private String mTypeName;

        public Segment(int id, String typeName) {
            mId = id;
            mTypeName = typeName;
        }

        public int getId() {
            return mId;
        }

        public String getTypeName() {
            return mTypeName;
        }

        public void setTypeName(String typeName) {
            mTypeName = typeName;
        }

        @Override
        public boolean equals(Object object) {
            return object instanceof Segment && mId == ((Segment) object).mId;
        }

        @Override
        public int hashCode() {
            return mTypeName.hashCode();
        }

        @Override
        public String toString() {
            return mTypeName;
        }
    }
}
