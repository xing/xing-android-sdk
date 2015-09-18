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
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.xing.android.sdk.json.EnumMapper;
import com.xing.android.sdk.model.XingCalendar;

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

    /**
     * Creator object for the Parcelable contract
     */
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

    private static final int DESCRIPTION_LIMIT = 512;
    private static final int NAME_LIMIT = 80;
    private static final int TITLE_LIMIT = 80;
    private static final int URL_LIMIT = 128;

    /**
     * Company id.
     */
    private String mId;
    /**
     * Career level.
     */
    private CareerLevel mCareerLevel;
    /**
     * Company size.
     */
    private CompanySize mCompanySize;
    /**
     * Company description.
     */
    private String mDescription;
    /**
     * Employee status.
     */
    private FormOfEmployment mFormOfEmployment;
    /**
     * Industry of company.
     */
    private Industry mIndustry;
    /**
     * Name of company.
     */
    private String mName;
    /**
     * Job title at company.
     */
    private String mTitle;
    /**
     * Tags.
     */
    private String mTag;
    /**
     * Currently working at company.
     */
    private boolean mUntilNow;
    /**
     * Company URL.
     */
    private Uri mUrl;
    /**
     * Begin date of employment.
     */
    @Nullable
    private XingCalendar mBeginDate;
    /**
     * End date of employment.
     */
    @Nullable
    private XingCalendar mEndDate;

    public ExperienceCompany() {
    }

    /**
     * Create {@link ExperienceCompany} from {@link Parcel}
     *
     * @param in Input {@link Parcel}
     */
    private ExperienceCompany(Parcel in) {
        mId = in.readString();
        mBeginDate = (XingCalendar) in.readSerializable();
        int tmpMCareerLevel = in.readInt();
        mCareerLevel = tmpMCareerLevel == -1 ? null : CareerLevel.values()[tmpMCareerLevel];
        int tmpMCompanySize = in.readInt();
        mCompanySize = tmpMCompanySize == -1 ? null : CompanySize.values()[tmpMCompanySize];
        mDescription = in.readString();
        mEndDate = (XingCalendar) in.readSerializable();
        int tmpMFormOfEmployment = in.readInt();
        mFormOfEmployment = tmpMFormOfEmployment == -1 ? null : FormOfEmployment.values()[tmpMFormOfEmployment];
        int industryId = in.readInt();
        String industryType = in.readString();
        if (industryId > -1) {
            mIndustry = new Industry(industryId, industryType);
        }
        mName = in.readString();
        mTitle = in.readString();
        mUntilNow = (Boolean) in.readValue(Boolean.class.getClassLoader());
        mUrl = in.readParcelable(Uri.class.getClassLoader());
        mTag = in.readString();
    }

    /** Throw an exception with explicit message */
    private static void throwArgumentToLong(Object arg, int limit) {
        throw new IllegalArgumentException(
                String.format("%s too long. %d characters is the maximum.", arg, limit)
        );
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
        int result = mId != null ? mId.hashCode() : 0;
        result = 31 * result + (mBeginDate != null ? mBeginDate.hashCode() : 0);
        result = 31 * result + (mCareerLevel != null ? mCareerLevel.hashCode() : 0);
        result = 31 * result + (mCompanySize != null ? mCompanySize.hashCode() : 0);
        result = 31 * result + (mDescription != null ? mDescription.hashCode() : 0);
        result = 31 * result + (mEndDate != null ? mEndDate.hashCode() : 0);
        result = 31 * result + (mFormOfEmployment != null ? mFormOfEmployment.hashCode() : 0);
        result = 31 * result + (mIndustry != null ? mIndustry.hashCode() : 0);
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        result = 31 * result + (mTitle != null ? mTitle.hashCode() : 0);
        result = 31 * result + (mUntilNow ? 1 : 0);
        result = 31 * result + (mUrl != null ? mUrl.hashCode() : 0);
        result = 31 * result + (mTag != null ? mTag.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeSerializable(mBeginDate);
        dest.writeInt(mCareerLevel == null ? -1 : mCareerLevel.ordinal());
        dest.writeInt(mCompanySize == null ? -1 : mCompanySize.ordinal());
        dest.writeString(mDescription);
        dest.writeSerializable(mEndDate);
        dest.writeInt(mFormOfEmployment == null ? -1 : mFormOfEmployment.ordinal());
        dest.writeInt(mIndustry == null ? -1 : mIndustry.getId());
        dest.writeString(mIndustry == null ? "" : mIndustry.getTypeName());
        dest.writeString(mName);
        dest.writeString(mTitle);
        dest.writeValue(mUntilNow);
        dest.writeParcelable(mUrl, flags);
        dest.writeString(mTag);
    }

    /**
     * Return id.
     *
     * @return id
     */
    public String getId() {
        return mId;
    }

    /**
     * Set id
     *
     * @param id id
     */
    public void setId(String id) {
        mId = id;
    }

    /**
     * Set begin date.
     *
     * @return begin date of employment.
     */
    @Nullable
    public XingCalendar getBeginDate() {
        return mBeginDate;
    }

    /**
     * Set begin date.
     *
     * @param beginDate begin date of employment.
     */
    public void setBeginDate(@Nullable XingCalendar beginDate) {
        mBeginDate = beginDate;
    }

    /**
     * Return career level.
     *
     * @return career level.
     */
    public CareerLevel getCareerLevel() {
        return mCareerLevel;
    }
    /**
     * Set career level.
     *
     * @param careerLevel career level.
     */
    public void setCareerLevel(CareerLevel careerLevel) {
        mCareerLevel = careerLevel;
    }

    /**
     * Set career level.
     *
     * @param careerLevel career level.
     */
    public void setCareerLevel(String careerLevel) {
        mCareerLevel = EnumMapper.parseEnumFromString(CareerLevel.values(), careerLevel);
    }

    /**
     * Get size of company.
     *
     * @return company size.
     */
    public CompanySize getCompanySize() {
        return mCompanySize;
    }

    /**
     * Set size of company.
     *
     * @param companySize company size.
     */
    public void setCompanySize(CompanySize companySize) {
        mCompanySize = companySize;
    }

    /**
     * Set size of company.
     *
     * @param companySize company size.
     */
    public void setCompanySize(String companySize) {
        mCompanySize = EnumMapper.parseEnumFromString(CompanySize.values(), companySize);
    }

    public String getDescription() {
        return mDescription;
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
            mDescription = description;
        }
    }

    /**
     * Return end date of employment.
     *
     * @return end date.
     */
    @Nullable
    public XingCalendar getEndDate() {
        return mEndDate;
    }

    /**
     * Set end date of employment.
     *
     * @param endDate end date.
     */
    public void setEndDate(@Nullable XingCalendar endDate) {
        mEndDate = endDate;
    }

    /**
     * Return form of employment.
     *
     * @return form of employment.
     */
    public FormOfEmployment getFormOfEmployment() {
        return mFormOfEmployment;
    }

    /**
     * Set form of employment.
     *
     * @param formOfEmployment form of employment.
     */
    public void setFormOfEmployment(FormOfEmployment formOfEmployment) {
        mFormOfEmployment = formOfEmployment;
    }

    /**
     * Set form of employment.
     *
     * @param formOfEmployment form of employment.
     */
    public void setFormOfEmployment(String formOfEmployment) {
        mFormOfEmployment = EnumMapper.parseEnumFromString(FormOfEmployment.values(), formOfEmployment);
    }

    /**
     * Return company industry.
     *
     * @return industry of company.
     */
    public Industry getIndustry() {
        return mIndustry;
    }

    /**
     * Set company industry.
     *
     * @param industry industry of company.
     */
    public void setIndustry(Industry industry) {
        mIndustry = industry;
    }

    /**
     * Return name of company.
     *
     * @return name of company.
     */
    public String getName() {
        return mName;
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
            mName = name;
        }
    }

    /**
     * Return job title.
     *
     * @return job title.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Set job title.
     * @param title job title.
     */
    public void setTitle(String title) {
        if (title != null && title.length() > TITLE_LIMIT) {
            throwArgumentToLong("Title", TITLE_LIMIT);
        } else {
            mTitle = title;
        }
    }

    /**
     * Return if job is current position.
     *
     * @return true, if current position, otherwise false.
     */
    public boolean getUntilNow() {
        return mUntilNow;
    }

    /**
     * Set job as current position.
     *
     * @param untilNow true, if job is current position.
     */
    public void setUntilNow(boolean untilNow) {
        mUntilNow = untilNow;
    }

    /**
     * Return company URL.
     *
     * @return company URL.
     */
    public Uri getUrl() {
        return mUrl;
    }

    /**
     * Set company URL
     *
     * @param url company URL.
     */
    public void setUrl(@Nullable Uri url) {
        if (url != null && url.toString().length() > URL_LIMIT) {
            throwArgumentToLong("Url", URL_LIMIT);
        } else {
            mUrl = url;
        }
    }

    /**
     * Check if mandatory fields are filled.
     *
     * @return false, if fields are not filled.
     */
    public boolean isFilledForAddCompany() {
        boolean filled = true;

        if (TextUtils.isEmpty(mName) || TextUtils.isEmpty(mTitle) || mIndustry == null ||
                mFormOfEmployment == null) {
            filled = false;
        }

        return filled;
    }

    /**
     * Return tag.
     *
     * @return tag.
     */
    public String getTag() {
        return mTag;
    }

    /**
     * Set tag.
     *
     * @param tag tag
     */
    public void setTag(String tag) {
        mTag = tag;
    }
}
