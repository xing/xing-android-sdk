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
package com.xing.api.data.profile;

import com.squareup.moshi.Json;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a {@linkplain XingUser user's} professional experience container.
 *
 * @see <a href="https://dev.xing.com/docs/get/users/:id">User Profile Resource</a>
 */
public class ProfessionalExperience implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Primary company. */
    @Json(name = "primary_company")
    private Company primaryCompany;
    /** List of companies. */
    @Json(name = "companies")
    private List<Company> companies;
    /** List of awards. */
    @Json(name = "awards")
    private List<Award> awards;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        //noinspection QuestionableName
        ProfessionalExperience that = (ProfessionalExperience) o;
        return (primaryCompany != null ? primaryCompany.equals(that.primaryCompany) : that.primaryCompany == null)
              && (companies != null ? companies.equals(that.companies) : that.companies == null)
              && (awards != null ? awards.equals(that.awards) : that.awards == null);
    }

    @Override
    public int hashCode() {
        int result = primaryCompany != null ? primaryCompany.hashCode() : 0;
        result = 31 * result + (companies != null ? companies.hashCode() : 0);
        result = 31 * result + (awards != null ? awards.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProfessionalExperience{"
              + "primaryCompany=" + primaryCompany
              + ", companies=" + companies
              + ", awards=" + awards
              + '}';
    }

    /** Returns the {@linkplain XingUser user's} primary company. */
    public Company primaryCompany() {
        return primaryCompany;
    }

    /** Sets the {@linkplain XingUser user's} primary company. */
    public ProfessionalExperience primaryCompany(Company primaryCompany) {
        this.primaryCompany = primaryCompany;
        return this;
    }

    /** Returns a list of companies the user has made part of his professional experience. */
    public List<Company> companies() {
        return companies;
    }

    /** Sets the list of companies. */
    public ProfessionalExperience companies(List<Company> companies) {
        this.companies = companies;
        return this;
    }

    /** Returns a list of awards of the user. */
    public List<Award> awards() {
        return awards;
    }

    /** Sets the list of user awards. */
    public ProfessionalExperience awards(List<Award> awards) {
        this.awards = awards;
        return this;
    }
}
