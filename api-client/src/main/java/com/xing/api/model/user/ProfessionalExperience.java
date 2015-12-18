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
package com.xing.api.model.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

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
    @Json(name = "primary_company")
    private ExperienceCompany primaryCompany;
    /** List of companies. */
    @Json(name = "companies")
    private List<ExperienceCompany> companies;
    /** List of awards. */
    @Json(name = "awards")
    private List<Award> awards;

    /** Create a simple Professional experience object with empty fields. */
    public ProfessionalExperience() {
    }

    /**
     * Create {@link ProfessionalExperience} from {@link Parcel}.
     *
     * @param in Input {@link Parcel}
     */
    private ProfessionalExperience(Parcel in) {
        primaryCompany = in.readParcelable(ExperienceCompany.class.getClassLoader());
        companies = new ArrayList<>(1);
        in.readTypedList(companies, ExperienceCompany.CREATOR);
        awards = new ArrayList<>(1);
        in.readTypedList(awards, Award.CREATOR);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ProfessionalExperience)) {
            return false;
        }

        ProfessionalExperience professionalExperience = (ProfessionalExperience) obj;
        return hashCode() == professionalExperience.hashCode();
    }

    @Override
    public int hashCode() {
        int result = primaryCompany != null ? primaryCompany.hashCode() : 0;
        result = 31 * result + (companies != null ? companies.hashCode() : 0);
        result = 31 * result + (awards != null ? awards.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(primaryCompany, 0);
        dest.writeTypedList(companies);
        dest.writeTypedList(awards);
    }

    /**
     * Return primary company.
     *
     * @return primary company.
     */
    public ExperienceCompany getPrimaryCompany() {
        return primaryCompany;
    }

    /**
     * Set primary company.
     *
     * @param primaryCompany primary company.
     */
    public void setPrimaryCompany(ExperienceCompany primaryCompany) {
        this.primaryCompany = primaryCompany;
    }

    /**
     * Return list of companies.
     *
     * @return list of companies.
     */
    public List<ExperienceCompany> getCompanies() {
        return companies;
    }

    /**
     * Set companies.
     *
     * @param companies list of companies.
     */
    public void setCompanies(List<ExperienceCompany> companies) {
        this.companies = companies;
    }

    /**
     * Return list of awards.
     *
     * @return list of awards.
     */
    public List<Award> getAwards() {
        return awards;
    }

    /**
     * Set list of awards.
     *
     * @param awards list of awards.
     */
    public void setAwards(List<Award> awards) {
        this.awards = awards;
    }
}
