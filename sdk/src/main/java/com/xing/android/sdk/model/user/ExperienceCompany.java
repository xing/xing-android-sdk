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
package com.xing.android.sdk.model.user;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.squareup.moshi.Json;

import java.io.Serializable;
import java.security.InvalidParameterException;

/**
 * Represents company experience.
 *
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/get/users/:id">User Profile</a>
 */
@SuppressWarnings("unused") // Public api
public class ExperienceCompany implements Serializable, Parcelable {
    private static final long serialVersionUID = -3976504576025888701L;
    private static final int DESCRIPTION_LIMIT = 512;
    private static final int NAME_LIMIT = 80;
    private static final int TITLE_LIMIT = 80;
    private static final int URL_LIMIT = 128;
    /** Creator object for the Parcelable contract. */
    public static final Creator<ExperienceCompany> CREATOR = new Creator<ExperienceCompany>() {
        @Override
        public ExperienceCompany createFromParcel(Parcel source) {
            return new ExperienceCompany(source);
        }

        @Override
        public ExperienceCompany[] newArray(int size) {
            return new ExperienceCompany[size];
        }
    };

    /** Company id. */
    @Json(name = "id")
    private String id;
    /** Career level. */
    @Json(name = "career_level")
    private CareerLevel careerLevel;
    /** Company size. */
    @Json(name = "company_size")
    private CompanySize companySize;
    /** Company description. */
    @Json(name = "description")
    private String description;
    /** Employee status. */
    @Json(name = "form_of_employment")
    private FormOfEmployment formOfEmployment;
    /** Industry of company. */
//    @Json(name = "industry")
//    private Industry industry;
    /** Name of company. */
    @Json(name = "name")
    private String name;
    /** Job title at company. */
    @Json(name = "title")
    private String title;
    /** Tags. */
    @Json(name = "tag")
    private String tag;
    /** Currently working at company. */
    @Json(name = "until_now")
    private boolean untilNow;
    /** Company URL. */
    @Json(name = "url")
    private String url;
    /** Begin date of employment. */
    @Nullable
    @Json(name = "begin_date")
    private String beginDate;
    /** End date of employment. */
    @Nullable
    @Json(name = "end_date")
    private String endDate;

    public ExperienceCompany() {
    }

    /**
     * Create {@link ExperienceCompany} from {@link Parcel}.
     *
     * @param in Input {@link Parcel}
     */
    private ExperienceCompany(Parcel in) {
        id = in.readString();
        beginDate = in.readString();
        int tmpMCareerLevel = in.readInt();
        careerLevel = tmpMCareerLevel == -1 ? null : CareerLevel.values()[tmpMCareerLevel];
        int tmpMCompanySize = in.readInt();
        companySize = tmpMCompanySize == -1 ? null : CompanySize.values()[tmpMCompanySize];
        description = in.readString();
        endDate = in.readString();
        int tmpMFormOfEmployment = in.readInt();
        formOfEmployment = tmpMFormOfEmployment == -1 ? null : FormOfEmployment.values()[tmpMFormOfEmployment];
        int industryId = in.readInt();
        String industryType = in.readString();
        if (industryId > -1) {
//            industry = new Industry(industryId, industryType);
        }
        name = in.readString();
        title = in.readString();
        untilNow = (Boolean) in.readValue(Boolean.class.getClassLoader());
        url = in.readParcelable(Uri.class.getClassLoader());
        tag = in.readString();
    }

    /** Throw an exception with explicit message. */
    private static void throwArgumentToLong(Object arg, int limit) {
        throw new IllegalArgumentException(String.format("%s too long. %d characters is the maximum.", arg, limit));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ExperienceCompany)) {
            return false;
        }

        ExperienceCompany experienceCompany = (ExperienceCompany) obj;
        return hashCode() == experienceCompany.hashCode();
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (beginDate != null ? beginDate.hashCode() : 0);
        result = 31 * result + (careerLevel != null ? careerLevel.hashCode() : 0);
        result = 31 * result + (companySize != null ? companySize.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (formOfEmployment != null ? formOfEmployment.hashCode() : 0);
//        result = 31 * result + (industry != null ? industry.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (untilNow ? 1 : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeSerializable(beginDate);
        dest.writeInt(careerLevel == null ? -1 : careerLevel.ordinal());
        dest.writeInt(companySize == null ? -1 : companySize.ordinal());
        dest.writeString(description);
        dest.writeSerializable(endDate);
        dest.writeInt(formOfEmployment == null ? -1 : formOfEmployment.ordinal());
//        dest.writeInt(industry == null ? -1 : industry.getId());
//        dest.writeString(industry == null ? "" : industry.getTypeName());
        dest.writeString(name);
        dest.writeString(title);
        dest.writeValue(untilNow);
        dest.writeString(url);
        dest.writeString(tag);
    }

    /**
     * Return id.
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Set id.
     *
     * @param id id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Set begin date.
     *
     * @return begin date of employment.
     */
    @Nullable
    public String getBeginDate() {
        return beginDate;
    }

    /**
     * Set begin date.
     *
     * @param beginDate begin date of employment.
     */
    public void setBeginDate(@Nullable String beginDate) {
        this.beginDate = beginDate;
    }

    /**
     * Return career level.
     *
     * @return career level.
     */
    public CareerLevel getCareerLevel() {
        return careerLevel;
    }

    /**
     * Set career level.
     *
     * @param careerLevel career level.
     */
    public void setCareerLevel(CareerLevel careerLevel) {
        this.careerLevel = careerLevel;
    }

    /**
     * Set career level.
     *
     * @param careerLevel career level.
     */
    public void setCareerLevel(String careerLevel) {
//        this.careerLevel = EnumMapper.parseEnumFromString(CareerLevel.values(), careerLevel);
    }

    /**
     * Get size of company.
     *
     * @return company size.
     */
    public CompanySize getCompanySize() {
        return companySize;
    }

    /**
     * Set size of company.
     *
     * @param companySize company size.
     */
    public void setCompanySize(CompanySize companySize) {
        this.companySize = companySize;
    }

    /**
     * Set size of company.
     *
     * @param companySize company size.
     */
    public void setCompanySize(String companySize) {
//        this.companySize = EnumMapper.parseEnumFromString(CompanySize.values(), companySize);
    }

    public String getDescription() {
        return description;
    }

    /**
     * Set job description.
     *
     * @param description job description.
     * @throws InvalidParameterException description too long.
     */
    public void setDescription(String description) throws InvalidParameterException {
        if (description != null && description.length() > DESCRIPTION_LIMIT) {
            throwArgumentToLong("Description", DESCRIPTION_LIMIT);
        } else {
            this.description = description;
        }
    }

    /**
     * Return end date of employment.
     *
     * @return end date.
     */
    @Nullable
    public String getEndDate() {
        return endDate;
    }

    /**
     * Set end date of employment.
     *
     * @param endDate end date.
     */
    public void setEndDate(@Nullable String endDate) {
        this.endDate = endDate;
    }

    /**
     * Return form of employment.
     *
     * @return form of employment.
     */
    public FormOfEmployment getFormOfEmployment() {
        return formOfEmployment;
    }

    /**
     * Set form of employment.
     *
     * @param formOfEmployment form of employment.
     */
    public void setFormOfEmployment(FormOfEmployment formOfEmployment) {
        this.formOfEmployment = formOfEmployment;
    }

    /**
     * Set form of employment.
     *
     * @param formOfEmployment form of employment.
     */
    public void setFormOfEmployment(String formOfEmployment) {
//        this.formOfEmployment = EnumMapper.parseEnumFromString(FormOfEmployment.values(), formOfEmployment);
    }

    /**
     * Return company industry.
     *
     * @return industry of company.
     */
//    public Industry getIndustry() {
//        return industry;
//    }

    /**
     * Set company industry.
     *
     * @param industry industry of company.
     */
//    public void setIndustry(Industry industry) {
//        this.industry = industry;
//    }

    /**
     * Return name of company.
     *
     * @return name of company.
     */
    public String getName() {
        return name;
    }

    /**
     * Set name of company.
     *
     * @param name name of company.
     */
    public void setName(String name) {
        if (name != null && name.length() > NAME_LIMIT) {
            throwArgumentToLong("Name", NAME_LIMIT);
        } else {
            this.name = name;
        }
    }

    /**
     * Return job title.
     *
     * @return job title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set job title.
     *
     * @param title job title.
     */
    public void setTitle(String title) {
        if (title != null && title.length() > TITLE_LIMIT) {
            throwArgumentToLong("Title", TITLE_LIMIT);
        } else {
            this.title = title;
        }
    }

    /**
     * Return if job is current position.
     *
     * @return true, if current position, otherwise false.
     */
    public boolean getUntilNow() {
        return untilNow;
    }

    /**
     * Set job as current position.
     *
     * @param untilNow true, if job is current position.
     */
    public void setUntilNow(boolean untilNow) {
        this.untilNow = untilNow;
    }

    /**
     * Return company URL.
     *
     * @return company URL.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set company URL.
     *
     * @param url company URL.
     */
    public void setUrl(@Nullable String url) {
        if (url != null && url.length() > URL_LIMIT) {
            throwArgumentToLong("Url", URL_LIMIT);
        }

        this.url = url;
    }

    /**
     * Check if mandatory fields are filled.
     *
     * @return false, if fields are not filled.
     */
    public boolean isFilledForAddCompany() {
        boolean filled = true;
//        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(title) || industry == null || formOfEmployment == null) {
            filled = false;
//        }
        return filled;
    }

    /**
     * Return tag.
     *
     * @return tag.
     */
    public String getTag() {
        return tag;
    }

    /**
     * Set tag.
     *
     * @param tag tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }
}
