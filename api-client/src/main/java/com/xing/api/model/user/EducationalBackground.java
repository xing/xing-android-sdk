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

import com.squareup.moshi.Json;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a users educational background.
 *
 * @author serj.lotutovici
 * @see <a href="https://dev.xing.com/docs/get/users/:id">User Profile</a>
 */
@SuppressWarnings("unused")
public class EducationalBackground implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Educational degree. */
    @Json(name = "degree")
    private String degree;
    /** Primary school attended. */
    @Json(name = "primary_school")
    private School primarySchool;
    /** List of schools attended. */
    @Json(name = "schools")
    private List<School> schools;
    /** List of qualifications. */
    @Json(name = "qualifications")
    private List<String> qualifications;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        //noinspection QuestionableName
        EducationalBackground that = (EducationalBackground) o;

        return (degree != null ? degree.equals(that.degree)
              : that.degree == null) && (primarySchool != null ? primarySchool.equals(that.primarySchool)
              : that.primarySchool == null) && (schools != null ? schools.equals(that.schools)
              : that.schools == null) && (qualifications != null ? qualifications.equals(that.qualifications)
              : that.qualifications == null);
    }

    @Override
    public int hashCode() {
        int result = degree != null ? degree.hashCode() : 0;
        result = 31 * result + (primarySchool != null ? primarySchool.hashCode() : 0);
        result = 31 * result + (schools != null ? schools.hashCode() : 0);
        result = 31 * result + (qualifications != null ? qualifications.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EducationalBackground{"
              + "degree='" + degree + '\''
              + ", primarySchool=" + primarySchool
              + ", schools=" + schools
              + ", qualifications=" + qualifications
              + '}';
    }

    /**
     * Return degree.
     *
     * @return degree.
     */
    public String getDegree() {
        return degree;
    }

    /**
     * Set degree.
     *
     * @param degree Set degree.
     */
    public void setDegree(String degree) {
        this.degree = degree;
    }

    /**
     * Return primary school.
     *
     * @return primary school.
     */
    public School getPrimarySchool() {
        return primarySchool;
    }

    /**
     * Set primary school.
     *
     * @param primarySchool primary school.
     */
    public void setPrimarySchool(School primarySchool) {
        this.primarySchool = primarySchool;
    }

    /**
     * Return list of schools.
     *
     * @return list of schools.
     */
    public List<School> getSchools() {
        return schools;
    }

    /**
     * Set list of schools.
     *
     * @param mSchools list of schools.
     */
    public void setSchools(List<School> mSchools) {
        schools = mSchools;
    }

    /**
     * Return list of qualifications.
     *
     * @return list of qualifications.
     */
    public List<String> getQualifications() {
        return qualifications;
    }

    /**
     * Set list of qualifications.
     *
     * @param qualifications list of qualifications.
     */
    public void setQualifications(List<String> qualifications) {
        this.qualifications = qualifications;
    }
}
