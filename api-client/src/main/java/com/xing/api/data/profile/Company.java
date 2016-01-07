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
import com.xing.api.data.SafeCalendar;

import java.io.Serializable;
import java.util.List;

/**
 * Java representation of a company object. This model is primiraly present in a user's <strong>professional
 * experience</strong> field as a <strong>primary_company</strong> or a list of pass companies. See {@link XingUser}
 * for more info.
 *
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/get/users/:id">User Profile Resource</a>
 */
@SuppressWarnings("unused") // Public api
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;
    static final int LIMIT_DESCRIPTION = 512;
    static final int LIMIT_NAME = 80;
    static final int LIMIT_TITLE = 80;
    static final int LIMIT_URL = 128;

    /** Throw an exception with explicit message. */
    static void throwIfArgumentToLong(String tag, String arg, int limit) {
        if (arg != null && arg.length() > limit) {
            throw new IllegalArgumentException(
                  String.format("Argument %s too long. %d characters is the maximum.", tag, limit));
        }
    }

    /** Company id. */
    @Json(name = "id")
    private String id;
    /** Name of company. */
    @Json(name = "name")
    private String name;
    /** Job title at company. */
    @Json(name = "title")
    private String title;
    /** Company size. */
    @Json(name = "company_size")
    private CompanySize companySize;
    /** Tag. */
    @Json(name = "tag")
    private String tag;
    /** Company URL. */
    @Json(name = "url")
    private String url;
    /** Career level. */
    @Json(name = "career_level")
    private CareerLevel careerLevel;
    /** Begin date of employment. */
    @Json(name = "begin_date")
    private SafeCalendar beginDate;
    /** End date of employment. */
    @Json(name = "end_date")
    private SafeCalendar endDate;
    /** Company description. */
    @Json(name = "description")
    private String description;
    /** Industry of company. */
    @Json(name = "industries")
    private List<Industry> industries;
    /** Employee status. */
    @Json(name = "form_of_employment")
    private FormOfEmployment formOfEmployment;
    /** Currently working at company. */
    @Json(name = "until_now")
    private boolean untilNow;
    @Json(name = "discipline")
    private Discipline discipline;

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;

        if (untilNow != company.untilNow) return false;
        if (id != null ? !id.equals(company.id) : company.id != null) return false;
        if (name != null ? !name.equals(company.name) : company.name != null) return false;
        if (title != null ? !title.equals(company.title) : company.title != null) return false;
        if (companySize != company.companySize) return false;
        if (tag != null ? !tag.equals(company.tag) : company.tag != null) return false;
        if (url != null ? !url.equals(company.url) : company.url != null) return false;
        if (careerLevel != company.careerLevel) return false;
        if (beginDate != null ? !beginDate.equals(company.beginDate) : company.beginDate != null) return false;
        if (endDate != null ? !endDate.equals(company.endDate) : company.endDate != null) return false;
        if (description != null ? !description.equals(company.description) : company.description != null) return false;
        if (industries != null ? !industries.equals(company.industries) : company.industries != null) return false;
        if (formOfEmployment != company.formOfEmployment) return false;
        return discipline != null ? discipline.equals(company.discipline) : company.discipline == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (companySize != null ? companySize.hashCode() : 0);
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (careerLevel != null ? careerLevel.hashCode() : 0);
        result = 31 * result + (beginDate != null ? beginDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (industries != null ? industries.hashCode() : 0);
        result = 31 * result + (formOfEmployment != null ? formOfEmployment.hashCode() : 0);
        result = 31 * result + (untilNow ? 1 : 0);
        result = 31 * result + (discipline != null ? discipline.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Company{"
              + "id='" + id + '\''
              + ", careerLevel=" + careerLevel
              + ", companySize=" + companySize
              + ", description='" + description + '\''
              + ", formOfEmployment=" + formOfEmployment
              + ", industries=" + industries
              + ", name='" + name + '\''
              + ", title='" + title + '\''
              + ", tag='" + tag + '\''
              + ", untilNow=" + untilNow
              + ", url='" + url + '\''
              + ", beginDate='" + beginDate + '\''
              + ", endDate='" + endDate + '\''
              + ", discipline='" + discipline + '\''
              + '}';
    }

    /** Returns the company id. */
    public String getId() {
        return id;
    }

    /** Set the company id. */
    public void setId(String id) {
        this.id = id;
    }

    /** Returns begin date (date when the user started to work for <strong>this</strong> company). */
    public SafeCalendar getBeginDate() {
        return beginDate;
    }

    /** Set begin date (date when the user started to work for <strong>this</strong> company). */
    public void setBeginDate(SafeCalendar beginDate) {
        this.beginDate = beginDate;
    }

    /** Returns the user's career level. <br><i>Non-mandatory field if creating new company.</i> */
    public CareerLevel getCareerLevel() {
        return careerLevel;
    }

    /** Set the user's career level. <br><i>Non-mandatory field if creating new company.</i> */
    public void setCareerLevel(CareerLevel careerLevel) {
        this.careerLevel = careerLevel;
    }

    /** Returns the size of company. Available values in {@link CompanySize}. */
    public CompanySize getCompanySize() {
        return companySize;
    }

    /** Set the size of company. Available values in {@link CompanySize}. */
    public void setCompanySize(CompanySize companySize) {
        this.companySize = companySize;
    }

    /** Returns the job description at <strong>this</strong> company. */
    public String getDescription() {
        return description;
    }

    /**
     * Set the job description at <strong>this</strong> company.
     * <i>Character limit: {@value Company#LIMIT_DESCRIPTION}</i>
     */
    public void setDescription(String description) {
        throwIfArgumentToLong("Description", description, LIMIT_DESCRIPTION);
        this.description = description;
    }

    /** Returns the end date of employment at <strong>this</strong> company. */
    public SafeCalendar getEndDate() {
        return endDate;
    }

    /** Set the end date of employment at <strong>this</strong> company. */
    public void setEndDate(SafeCalendar endDate) {
        this.endDate = endDate;
    }

    /** Returns form of employment at <strong>this</strong> company. Possible values in {@link FormOfEmployment}. */
    public FormOfEmployment getFormOfEmployment() {
        return formOfEmployment;
    }

    /** Set form of employment at <strong>this</strong> company. Possible values in {@link FormOfEmployment}. */
    public void setFormOfEmployment(FormOfEmployment formOfEmployment) {
        this.formOfEmployment = formOfEmployment;
    }

    /** Returns the company's industries. */
    public List<Industry> getIndustry() {
        return industries;
    }

    /** Set company industries. */
    public void setIndustries(List<Industry> industries) {
        this.industries = industries;
    }

    /** Returns the name of <strong>this</strong> company. */
    public String getName() {
        return name;
    }

    /**
     * Set the name of <strong>this</strong> company.
     * * <i>Character limit: {@value Company#LIMIT_NAME}</i>
     */
    public void setName(String name) {
        throwIfArgumentToLong("Name", name, LIMIT_NAME);
        this.name = name;
    }

    /** Returns the user's job title at <strong>this</strong> company. */
    public String getTitle() {
        return title;
    }

    /**
     * Set the user's job title at <strong>this</strong> company.
     * <i>Character limit: {@value Company#LIMIT_TITLE}.</i>
     */
    public void setTitle(String title) {
        throwIfArgumentToLong("Title", title, LIMIT_TITLE);
        this.title = title;
    }

    /** Returns {@code true} if the user's current position is at <strong>this</strong> company. */
    public boolean untilNow() {
        return untilNow;
    }

    /** Set flag to represent if the user's current position is at <strong>this</strong> company. */
    public void setUntilNow(boolean untilNow) {
        this.untilNow = untilNow;
    }

    /** Returns <strong>this</strong> company's url. */
    public String getUrl() {
        return url;
    }

    /**
     * Set <strong>this</strong> company's url.
     * <i>Character limit: {@value Company#LIMIT_URL}.</i>
     */
    public void setUrl(String url) {
        throwIfArgumentToLong("Url", url, LIMIT_URL);
        this.url = url;
    }

    /** Returns <strong>this</strong> company's tag. */
    public String getTag() {
        return tag;
    }

    /** Set <strong>this</strong> company's tag. */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /** Returns <strong>this</strong> company's discipline. */
    public Discipline getDiscipline() {
        return discipline;
    }

    /** Set <strong>this</strong> company's discipline. */
    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    /** Returns {@code true} if all fields mandatory for 'add company' action are set. */
    public boolean isFilledForAddCompany() {
        return (name != null && !name.isEmpty())
              && (title != null && !title.isEmpty())
              && (industries != null && !industries.isEmpty())
              && formOfEmployment != null;
    }
}
