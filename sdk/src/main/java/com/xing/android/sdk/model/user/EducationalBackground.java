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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a users educational background.
 *
 * @author serj.lotutovici
 * @see <a href="https://dev.xing.com/docs/get/users/:id">User Profile</a>
 */
public class EducationalBackground implements Serializable, Parcelable {
    private static final long serialVersionUID = 2927900492592865436L;
    /** Creator object for the Parcelable contract. */
    public static final Creator<EducationalBackground> CREATOR = new Creator<EducationalBackground>() {
        @Override
        public EducationalBackground createFromParcel(Parcel source) {
            return new EducationalBackground(source);
        }

        @Override
        public EducationalBackground[] newArray(int size) {
            return new EducationalBackground[size];
        }
    };

    /** Educational degree. */
    private String mDegree;
    /** Primary school attended. */
    private School mPrimarySchool;
    /** List of schools attended. */
    private List<School> mSchools;
    /** List of qualifications. */
    private List<String> mQualifications;

    /** Create a simple Educational Background object with empty fields. */
    public EducationalBackground() {
    }

    /**
     * Create {@link EducationalBackground} from {@link Parcel}.
     *
     * @param in Input {@link Parcel}
     */
    private EducationalBackground(Parcel in) {
        mDegree = in.readString();
        mPrimarySchool = in.readParcelable(School.class.getClassLoader());
        mSchools = new ArrayList<>();
        in.readTypedList(mSchools, School.CREATOR);
        mQualifications = new ArrayList<>();
        in.readStringList(mQualifications);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EducationalBackground)) {
            return false;
        }

        EducationalBackground that = (EducationalBackground) o;
        return hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        int result = mDegree != null ? mDegree.hashCode() : 0;
        result = 31 * result + (mPrimarySchool != null ? mPrimarySchool.hashCode() : 0);
        result = 31 * result + (mSchools != null ? mSchools.hashCode() : 0);
        result = 31 * result + (mQualifications != null ? mQualifications.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDegree);
        dest.writeParcelable(mPrimarySchool, 0);
        dest.writeTypedList(mSchools);
        dest.writeStringList(mQualifications);
    }

    /**
     * Return degree.
     *
     * @return degree.
     */
    public String getDegree() {
        return mDegree;
    }

    /**
     * Set degree.
     *
     * @param mDegree Set degree.
     */
    public void setDegree(String mDegree) {
        this.mDegree = mDegree;
    }

    /**
     * Return primary school.
     *
     * @return primary school.
     */
    public School getPrimarySchool() {
        return mPrimarySchool;
    }

    /**
     * Set primary school.
     *
     * @param mPrimarySchool primary school.
     */
    public void setPrimarySchool(School mPrimarySchool) {
        this.mPrimarySchool = mPrimarySchool;
    }

    /**
     * Return list of schools.
     *
     * @return list of schools.
     */
    public List<School> getSchools() {
        return mSchools;
    }

    /**
     * Set list of schools.
     *
     * @param mSchools list of schools.
     */
    public void setSchools(List<School> mSchools) {
        this.mSchools = mSchools;
    }

    /**
     * Return list of qualifications.
     *
     * @return list of qualifications.
     */
    public List<String> getQualifications() {
        return mQualifications;
    }

    /**
     * Set list of qualifications.
     *
     * @param mQualifications list of qualifications.
     */
    public void setQualifications(List<String> mQualifications) {
        this.mQualifications = mQualifications;
    }
}
