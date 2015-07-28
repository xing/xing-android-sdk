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
import android.text.TextUtils;

import com.xing.android.sdk.model.XingCalendar;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a school.
 * <p/>
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/post/users/me/educational_background/schools">School</a>
 */
public class School implements Serializable, Parcelable {

    private static final long serialVersionUID = -700850712160641923L;

    /**
     * Creator object for Parcelable contract
     */
    public static Creator<School> CREATOR = new Creator<School>() {
        @Override
        public School createFromParcel(Parcel parcel) {
            return new School(parcel);
        }

        @Override
        public School[] newArray(int size) {
            return new School[size];
        }
    };

    /**
     * School ID.
     */
    private String mId;
    /**
     * Name of school.
     */
    private String mName;
    /**
     * Degree.
     */
    private String mDegree;
    /**
     * Additional notes such as specialized subjects.
     */
    private List<String> mNotes;
    /**
     * Describes the field of study.
     */
    private String mSubject;
    /**
     * Start date.
     */
    private XingCalendar mBeginDate;
    /**
     * End date.
     */
    private XingCalendar mEndDate;

    /**
     * Create a school object given id and name.
     *
     * @param id id of shcool.
     * @param name name of school.
     */
    public School(@NonNull final String id, @NonNull final String name) {
        mId = id;
        mName = name;
    }

    /**
     * Create a simple School object with empty fields.
     */
    public School() {
    }

    /**
     * Create {@link Award} from {@link Parcel}
     *
     * @param parcel Input {@link Parcel}
     */
    private School(Parcel parcel) {
        mId = parcel.readString();
        mName = parcel.readString();
        mDegree = parcel.readString();
        parcel.readList(mNotes, List.class.getClassLoader());
        mSubject = parcel.readString();
        mBeginDate = (XingCalendar) parcel.readSerializable();
        mEndDate = (XingCalendar) parcel.readSerializable();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof School)) {
            return false;
        }

        School school = (School) obj;
        return hashCode() == school.hashCode();
    }

    @Override
    public int hashCode() {
        int result = mId != null ? mId.hashCode() : 0;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        result = 31 * result + (mDegree != null ? mDegree.hashCode() : 0);
        result = 31 * result + (mNotes != null ? mNotes.hashCode() : 0);
        result = 31 * result + (mSubject != null ? mSubject.hashCode() : 0);
        result = 31 * result + (mBeginDate != null ? mBeginDate.hashCode() : 0);
        result = 31 * result + (mEndDate != null ? mEndDate.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mId);
        parcel.writeString(mName);
        parcel.writeString(mDegree);
        parcel.writeList(mNotes);
        parcel.writeString(mSubject);
        parcel.writeSerializable(mBeginDate);
        parcel.writeSerializable(mEndDate);
    }

    /**
     * Return school ID.
     *
     * @return school id.
     */
    public String getId() {
        return mId;
    }

    /**
     * Set school id.
     *
     * @param id id of shcool.
     */
    public void setId(String id) {
        if (!TextUtils.isEmpty(id)) {
            mId = id;
        }
    }

    /**
     * Return name of school.
     *
     * @return name of school.
     */
    public String getName() {
        return mName;
    }

    /**
     * Set name of school.
     *
     * @param name name of school.
     */
    public void setName(String name) {
        if (!TextUtils.isEmpty(name)) {
            mName = name;
        }
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
     * @param degree degree.
     */
    public void setDegree(String degree) {
        mDegree = degree;
    }

    /**
     * Return additional notes such as specialized subjects.
     *
     * @return list of notes.
     */
    public List<String> getNotes() {
        return mNotes;
    }

    /**
     * Set additional notes such as specialized subjects.
     *
     * @param notes notes.
     */
    public void setNotes(String notes) {
        String[] splitNotes = notes.split(", ");
        mNotes = Arrays.asList(splitNotes);
    }

    /**
     * Return subject of field of study.
     *
     * @return subject.
     */
    public String getSubject() {
        return mSubject;
    }

    /**
     * Set subject of field of study.
     *
     * @param subject subject.
     */
    public void setSubject(String subject) {
        mSubject = subject;
    }

    /**
     * Return start date.
     *
     * @return start date.
     */
    public XingCalendar getBeginDate() {
        return mBeginDate;
    }

    /**
     * Set return date.
     *
     * @param beginDate start date.
     */
    public void setBeginDate(XingCalendar beginDate) {
        mBeginDate = beginDate;
    }

    /**
     * Return end date.
     *
     * @return end date.
     */
    public XingCalendar getEndDate() {
        return mEndDate;
    }

    /**
     * Set end date.
     *
     * @param endDate end date.
     */
    public void setEndDate(XingCalendar endDate) {
        mEndDate = endDate;
    }
}
