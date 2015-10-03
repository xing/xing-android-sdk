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
 * Represents a users professional experience.
 *
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/get/users/me">Professional Experience</a>
 */
public class ProfessionalExperience implements Serializable, Parcelable {
    private static final long serialVersionUID = 5863766233252368533L;
    /** Creator object for Parcelable contract. */
    public static final Creator<ProfessionalExperience> CREATOR = new Creator<ProfessionalExperience>() {
        @Override
        public ProfessionalExperience createFromParcel(Parcel source) {
            return new ProfessionalExperience(source);
        }

        @Override
        public ProfessionalExperience[] newArray(int size) {
            return new ProfessionalExperience[size];
        }
    };

    /** Primary company. */
    private ExperienceCompany mPrimaryCompany;
    /** List of companies. */
    private List<ExperienceCompany> mCompanies;
    /** List of awards. */
    private List<Award> mAwards;

    /** Create a simple Professional experience object with empty fields. */
    public ProfessionalExperience() {
    }

    /**
     * Create {@link ProfessionalExperience} from {@link Parcel}.
     *
     * @param in Input {@link Parcel}
     */
    private ProfessionalExperience(Parcel in) {
        mPrimaryCompany = in.readParcelable(ExperienceCompany.class.getClassLoader());
        mCompanies = new ArrayList<>();
        in.readTypedList(mCompanies, ExperienceCompany.CREATOR);
        mAwards = new ArrayList<>();
        in.readTypedList(mAwards, Award.CREATOR);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ProfessionalExperience)) {
            return false;
        }

        ProfessionalExperience that = (ProfessionalExperience) obj;
        return hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        int result = mPrimaryCompany != null ? mPrimaryCompany.hashCode() : 0;
        result = 31 * result + (mCompanies != null ? mCompanies.hashCode() : 0);
        result = 31 * result + (mAwards != null ? mAwards.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mPrimaryCompany, 0);
        dest.writeTypedList(mCompanies);
        dest.writeTypedList(mAwards);
    }

    /**
     * Return primary company.
     *
     * @return primary company.
     */
    public ExperienceCompany getPrimaryCompany() {
        return mPrimaryCompany;
    }

    /**
     * Set primary company.
     *
     * @param primaryCompany primary company.
     */
    public void setPrimaryCompany(ExperienceCompany primaryCompany) {
        mPrimaryCompany = primaryCompany;
    }

    /**
     * Return list of companies.
     *
     * @return list of companies.
     */
    public List<ExperienceCompany> getCompanies() {
        return mCompanies;
    }

    /**
     * Set companies.
     *
     * @param companies list of companies.
     */
    public void setCompanies(List<ExperienceCompany> companies) {
        mCompanies = companies;
    }

    /**
     * Return list of awards.
     *
     * @return list of awards.
     */
    public List<Award> getAwards() {
        return mAwards;
    }

    /**
     * Set list of awards.
     *
     * @param awards list of awards.
     */
    public void setAwards(List<Award> awards) {
        mAwards = awards;
    }
}
