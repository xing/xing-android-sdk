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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Patterns;

import com.xing.android.sdk.json.EnumMapper;
import com.xing.android.sdk.model.XingCalendar;
import com.xing.android.sdk.network.request.RequestUtils;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Java representation of a XING user object
 *
 * @author david.gonzalez
 * @author serj.lotutovici
 * @see <a href="https://dev.xing.com/docs/get/users/:id">https://dev.xing.com/docs/get/users/:id</a>
 */
@SuppressWarnings("unused") // Public api
public class XingUser implements Serializable, Parcelable {

    private static final long serialVersionUID = 3037193617271688856L;

    public static final Creator<XingUser> CREATOR = new Creator<XingUser>() {
        @Override
        public XingUser createFromParcel(Parcel source) {
            return new XingUser(source);
        }

        @Override
        public XingUser[] newArray(int size) {
            return new XingUser[size];
        }
    };

    private static final Pattern COMMA_SEPARATOR = Pattern.compile(", ");
    private String mId;
    private String mFirstName;
    private String mLastName;
    private String mDisplayName;
    private String mPageName;
    private Uri mPermalink;
    private EmploymentStatus mEmploymentStatus;
    private Gender mGender;
    private XingCalendar mBirthday;
    private String mActiveEmail;
    private TimeZone mTimeZone;
    private List<PremiumService> mPremiumServices;
    private List<Badge> mBadges;
    private List<String> mWants;
    private List<String> mHaves;
    private List<String> mInterests;
    private List<String> mOrganisationMember;
    private EnumMap<Language, LanguageSkill> mLanguages;
    private XingAddress mPrivateAddress;
    private XingAddress mBusinessAddress;
    private EnumMap<WebProfile, Set<String>> mWebProfiles;
    private EnumMap<MessagingAccount, String> mInstantMessagingAccounts;
    private ProfessionalExperience mProfessionalExperience;
    private EducationalBackground mEducationBackground;
    private XingPhotoUrls mPhotoUrls;

    public XingUser(@NonNull final String id) {
        if (TextUtils.isEmpty(id)) {
            throw new IllegalArgumentException("id can not be null neither empty");
        } else {
            mId = id;
        }
    }

    public XingUser() {
    }

    /**
     * Create {@link XingUser} from {@link Parcel}
     *
     * @param in Input {@link Parcel}
     */
    @SuppressWarnings("unchecked")
    private XingUser(Parcel in) {
        mId = in.readString();
        mFirstName = in.readString();
        mLastName = in.readString();
        mDisplayName = in.readString();
        mPageName = in.readString();
        mPermalink = in.readParcelable(Uri.class.getClassLoader());
        int tmpMEmploymentStatus = in.readInt();
        mEmploymentStatus = tmpMEmploymentStatus == -1 ? null : EmploymentStatus.values()[tmpMEmploymentStatus];
        int tmpMGender = in.readInt();
        mGender = tmpMGender == -1 ? null : Gender.values()[tmpMGender];
        mBirthday = (XingCalendar) in.readSerializable();
        mActiveEmail = in.readString();
        mPremiumServices = new ArrayList<>();
        in.readList(mPremiumServices, ArrayList.class.getClassLoader());
        mBadges = new ArrayList<>();
        in.readList(mBadges, ArrayList.class.getClassLoader());
        mWants = new ArrayList<>();
        in.readStringList(mWants);
        mHaves = new ArrayList<>();
        in.readStringList(mHaves);
        mInterests = new ArrayList<>();
        in.readStringList(mInterests);
        mOrganisationMember = new ArrayList<>();
        in.readStringList(mOrganisationMember);
        mLanguages = (EnumMap<Language, LanguageSkill>) in.readSerializable();
        mPrivateAddress = in.readParcelable(XingAddress.class.getClassLoader());
        mTimeZone = in.readParcelable(TimeZone.class.getClassLoader());
        mBusinessAddress = in.readParcelable(XingAddress.class.getClassLoader());
        mWebProfiles = (EnumMap<WebProfile, Set<String>>) in.readSerializable();
        mInstantMessagingAccounts = (EnumMap<MessagingAccount, String>) in.readSerializable();
        mEducationBackground = in.readParcelable(EducationalBackground.class.getClassLoader());
        mProfessionalExperience = in.readParcelable(ProfessionalExperience.class.getClassLoader());
        mPhotoUrls = in.readParcelable(XingPhotoUrls.class.getClassLoader());
    }

    public static boolean isUserIdValid(String userId) {
        return !isUserIdInValid(userId);
    }

    public static boolean isUserIdInValid(String userId) {
        return TextUtils.isEmpty(userId);
    }

    /**
     * Creates a String with the ids of the users separated by commas.
     *
     * @param users List of users to read their ids. Can not be empty.
     * @return String with the ids, in a comma separated format.
     * @throws IllegalArgumentException if users is empty.
     */
    public static String getIdList(@NonNull final List<XingUser> users) {
        if (users.isEmpty()) {
            throw new IllegalArgumentException("users can not be empty");
        }

        int numUsers = users.size();
        List<String> ids = new ArrayList<>(users.size());
        for (int iterator = 0; iterator < numUsers; iterator++) {
            ids.add(users.get(iterator).mId);
        }

        return RequestUtils.createCommaSeparatedStringFromStringList(ids);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof XingUser)) {
            return false;
        }

        XingUser user = (XingUser) obj;
        return hashCode() == user.hashCode();
    }

    @Override
    public int hashCode() {
        int result = mId != null ? mId.hashCode() : 0;
        result = 31 * result + (mFirstName != null ? mFirstName.hashCode() : 0);
        result = 31 * result + (mLastName != null ? mLastName.hashCode() : 0);
        result = 31 * result + (mDisplayName != null ? mDisplayName.hashCode() : 0);
        result = 31 * result + (mPageName != null ? mPageName.hashCode() : 0);
        result = 31 * result + (mPermalink != null ? mPermalink.hashCode() : 0);
        result = 31 * result + (mEmploymentStatus != null ? mEmploymentStatus.hashCode() : 0);
        result = 31 * result + (mGender != null ? mGender.hashCode() : 0);
        result = 31 * result + (mBirthday != null ? mBirthday.hashCode() : 0);
        result = 31 * result + (mActiveEmail != null ? mActiveEmail.hashCode() : 0);
        result = 31 * result + (mPremiumServices != null ? mPremiumServices.hashCode() : 0);
        result = 31 * result + (mBadges != null ? mBadges.hashCode() : 0);
        result = 31 * result + (mWants != null ? mWants.hashCode() : 0);
        result = 31 * result + (mHaves != null ? mHaves.hashCode() : 0);
        result = 31 * result + (mInterests != null ? mInterests.hashCode() : 0);
        result = 31 * result + (mOrganisationMember != null ? mOrganisationMember.hashCode() : 0);
        result = 31 * result + (mLanguages != null ? mLanguages.hashCode() : 0);
        result = 31 * result + (mPrivateAddress != null ? mPrivateAddress.hashCode() : 0);
        result = 31 * result + (mTimeZone != null ? mTimeZone.hashCode() : 0);
        result = 31 * result + (mBusinessAddress != null ? mBusinessAddress.hashCode() : 0);
        result = 31 * result + (mWebProfiles != null ? mWebProfiles.hashCode() : 0);
        result = 31 * result + (mInstantMessagingAccounts != null ? mInstantMessagingAccounts.hashCode() : 0);
        result = 31 * result + (mEducationBackground != null ? mEducationBackground.hashCode() : 0);
        result = 31 * result + (mProfessionalExperience != null ? mProfessionalExperience.hashCode() : 0);
        result = 31 * result + (mPhotoUrls != null ? mPhotoUrls.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "XingUser{" +
                "mId='" + mId + '\'' +
                ", mFirstName='" + mFirstName + '\'' +
                ", mLastName='" + mLastName + '\'' +
                ", mDisplayName='" + mDisplayName + '\'' +
                ", mPageName='" + mPageName + '\'' +
                ", mPermalink=" + mPermalink +
                ", mEmploymentStatus=" + mEmploymentStatus +
                ", mGender=" + mGender +
                ", mBirthday=" + mBirthday +
                ", mActiveEmail='" + mActiveEmail + '\'' +
                ", mPremiumServices=" + mPremiumServices +
                ", mBadges=" + mBadges +
                ", mWants=" + mWants +
                ", mHaves=" + mHaves +
                ", mInterests=" + mInterests +
                ", mOrganisationMember=" + mOrganisationMember +
                ", mLanguages=" + mLanguages +
                ", mPrivateAddress=" + mPrivateAddress +
                ", mTimeZone=" + mTimeZone +
                ", mBusinessAddress=" + mBusinessAddress +
                ", mWebProfiles=" + mWebProfiles +
                ", mInstantMessagingAccounts=" + mInstantMessagingAccounts +
                ", mEducationBackground=" + mEducationBackground +
                ", mProfessionalExperience=" + mProfessionalExperience +
                ", mPhotoUrls=" + mPhotoUrls +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeString(mDisplayName);
        dest.writeString(mPageName);
        dest.writeParcelable(mPermalink, flags);
        dest.writeInt(mEmploymentStatus == null ? -1 : mEmploymentStatus.ordinal());
        dest.writeInt(mGender == null ? -1 : mGender.ordinal());
        dest.writeSerializable(mBirthday);
        dest.writeString(mActiveEmail);
        dest.writeList(mPremiumServices);
        dest.writeList(mBadges);
        dest.writeStringList(mWants);
        dest.writeStringList(mHaves);
        dest.writeStringList(mInterests);
        dest.writeStringList(mOrganisationMember);
        dest.writeSerializable(mLanguages);
        dest.writeParcelable(mPrivateAddress, flags);
        dest.writeParcelable(mTimeZone, flags);
        dest.writeParcelable(mBusinessAddress, flags);
        dest.writeSerializable(mWebProfiles);
        dest.writeSerializable(mInstantMessagingAccounts);
        dest.writeParcelable(mEducationBackground, flags);
        dest.writeParcelable(mProfessionalExperience, flags);
        dest.writeParcelable(mPhotoUrls, flags);
    }

    public String getId() {
        return mId;
    }

    public void setId(@NonNull final String id) {
        if (TextUtils.isEmpty(id)) {
            throw new IllegalArgumentException("id can not be null neither empty");
        } else {
            mId = id;
        }
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        this.mLastName = lastName;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }

    public Gender getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        this.mGender = EnumMapper.parseEnumFromString(Gender.values(), gender);
    }

    public void setGender(Gender gender) {
        this.mGender = gender;
    }

    public String getPageName() {
        return mPageName;
    }

    public void setPageName(String pageName) {
        mPageName = pageName;
    }

    public Uri getPermalink() {
        return mPermalink;
    }

    public void setPermalink(String permalink) {
        if (!Patterns.WEB_URL.matcher(permalink).matches()) {
            throw new InvalidParameterException(permalink + " is not an url.");
        } else {
            mPermalink = Uri.parse(permalink);
        }
    }

    public void setPermalink(Uri permalink) {
        mPermalink = permalink;
    }

    public EmploymentStatus getEmploymentStatus() {
        return mEmploymentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        mEmploymentStatus = EmploymentStatus.valueOf(employmentStatus);
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        mEmploymentStatus = employmentStatus;
    }

    public XingCalendar getBirthday() {
        return mBirthday;
    }

    public void setBirthday(XingCalendar birthday) {
        mBirthday = birthday;
    }

    public String getActiveEmail() {
        return mActiveEmail;
    }

    public void setActiveEmail(String activeEmail) {
        if (!Patterns.EMAIL_ADDRESS.matcher(activeEmail).matches()) {
            throw new InvalidParameterException(activeEmail + " is not a valid email");
        } else {
            mActiveEmail = activeEmail;
        }
    }

    public List<PremiumService> getPremiumServices() {
        return mPremiumServices;
    }

    public void setPremiumServices(List<PremiumService> premiumServices) {
        mPremiumServices = premiumServices;
    }

    public void setPremiumServicesFromStringList(List<String> premiumServices) {
        if (mPremiumServices == null) {
            mPremiumServices = new ArrayList<>();
        }

        PremiumService premiumService;
        for (String premiumServiceString : premiumServices) {
            premiumService = EnumMapper.parseEnumFromString(PremiumService.values(), premiumServiceString);
            if (premiumService != null) {
                mPremiumServices.add(premiumService);
            }
        }
    }

    public void addPremiumService(PremiumService premiumService) {
        if (mPremiumServices == null) {
            mPremiumServices = new ArrayList<>();
        }
        mPremiumServices.add(premiumService);
    }

    public List<Badge> getBadges() {
        return mBadges;
    }

    public void setBadges(List<Badge> badges) {
        mBadges = badges;
    }

    public void setBadgesFromStringList(List<String> badges) {
        if (mBadges == null) {
            mBadges = new ArrayList<>(badges.size());
        }

        Badge badge;
        for (String badgeString : badges) {
            badge = Badge.valueOf(badgeString);

            if (badge != null) {
                mBadges.add(badge);
            }
        }
    }

    public void addBadges(Badge badge) {
        if (mBadges == null) {
            mBadges = new ArrayList<>();
        }
        mBadges.add(badge);
    }

    public List<String> getWants() {
        return mWants;
    }

    /**
     * Replace the current wants with the ones from the param.
     *
     * @param wants String with wants with the format want1, want2, want3, want4
     */

    public void setWants(String wants) {
        String[] wantsArray = COMMA_SEPARATOR.split(wants);
        mWants = Arrays.asList(wantsArray);
    }

    public void setWants(List<String> wants) {
        mWants = wants;
    }

    /**
     * Adds a new want to the list of wants.
     *
     * @param want The new want to add to the list.
     */
    //TODO consider wants, haves and interests with commas, it can affect.
    public void addWant(String want) {
        if (mWants == null) {
            mWants = new ArrayList<>();
        }

        mWants.add(want);
    }

    public List<String> getHaves() {
        return mHaves;
    }

    /**
     * Replace the current haves with the ones from the param.
     *
     * @param haves String with haves with the format have1, have2, have3, have4
     */

    public void setHaves(String haves) {
        String[] havesArray = COMMA_SEPARATOR.split(haves);
        mHaves = Arrays.asList(havesArray);
    }

    public void setHaves(List<String> haves) {
        mHaves = haves;
    }

    public void addHave(String have) {
        if (mHaves == null) {
            mHaves = new ArrayList<>();
        }

        mHaves.add(have);
    }

    public List<String> getInterests() {
        return mInterests;
    }

    /**
     * Replace the current interests with the ones from the param.
     *
     * @param interests String with interests with the format interest1, interest2, interest3, interest4
     */

    public void setInterests(String interests) {
        String[] interestsArray = COMMA_SEPARATOR.split(interests);
        mInterests = Arrays.asList(interestsArray);
    }

    public void setInterests(List<String> interests) {
        mInterests = interests;
    }

    public void addInterest(String interest) {
        if (mInterests == null) {
            mInterests = new ArrayList<>();
        }

        mInterests.add(interest);
    }

    public List<String> getOrganisationMember() {
        return mOrganisationMember;
    }

    /**
     * Replace the current organisationMember with the ones from the param.
     *
     * @param organisationMember String with interests with the format organisationMember1, organisationMember2, organisationMember3, organisationMember4
     */

    public void setOrganisationMember(String organisationMember) {
        String[] organisationMemberArray = COMMA_SEPARATOR.split(organisationMember);
        mOrganisationMember = Arrays.asList(organisationMemberArray);
    }

    public void setOrganisationMember(List<String> organisationMember) {
        mOrganisationMember = organisationMember;
    }

    public void addOrganisationMember(String organisationMember) {
        if (mOrganisationMember == null) {
            mOrganisationMember = new ArrayList<>();
        }

        mOrganisationMember.add(organisationMember);
    }

    public EnumMap<Language, LanguageSkill> getLanguages() {
        return mLanguages;
    }

    public void setLanguages(EnumMap<Language, LanguageSkill> languages) {
        mLanguages = languages;
    }

    public LanguageSkill getLanguageSkill(@NonNull final Language language) {
        return mLanguages != null ? mLanguages.get(language) : null;
    }

    public void addLanguage(@NonNull final Language language, @Nullable final LanguageSkill languageSkill) {
        if (mLanguages == null) {
            mLanguages = new EnumMap<>(Language.class);
        }

        mLanguages.put(language, languageSkill);
    }

    public void addLanguage(@NonNull final String language, @Nullable final String languageSkill) {
        if (mLanguages == null) {
            mLanguages = new EnumMap<>(Language.class);
        }

        mLanguages.put(Language.valueOf(language), LanguageSkill.valueOf(languageSkill));
    }

    public XingAddress getPrivateAddress() {
        return mPrivateAddress;
    }

    public void setPrivateAddress(XingAddress privateAddress) {
        mPrivateAddress = privateAddress;
    }

    public XingAddress getBusinessAddress() {
        return mBusinessAddress;
    }

    public void setBusinessAddress(XingAddress businessAddress) {
        mBusinessAddress = businessAddress;
    }

    public Map<WebProfile, Set<String>> getWebProfiles() {
        return mWebProfiles;
    }

    public void setWebProfiles(EnumMap<WebProfile, Set<String>> webProfiles) {
        mWebProfiles = webProfiles;
    }

    public void setWebProfiles(@NonNull final WebProfile webProfile, @Nullable final Set<String> profiles) {

        if (mWebProfiles == null) {
            mWebProfiles = new EnumMap<>(WebProfile.class);
        }

        mWebProfiles.put(webProfile, profiles);
    }

    public void addWebProfiles(@NonNull final WebProfile webProfile, @Nullable final Set<String> accounts) {

        if (mWebProfiles == null) {
            mWebProfiles = new EnumMap<>(WebProfile.class);
        }

        if (!mWebProfiles.containsKey(webProfile)) {
            mWebProfiles.put(webProfile, new LinkedHashSet<String>());
        }

        mWebProfiles.get(webProfile).addAll(accounts);
    }

    public void addWebProfile(@NonNull final WebProfile webProfile, @Nullable final String accountName) {

        if (mWebProfiles == null) {
            mWebProfiles = new EnumMap<>(WebProfile.class);
        }

        if (!mWebProfiles.containsKey(webProfile)) {
            mWebProfiles.put(webProfile, new LinkedHashSet<String>());
        }

        mWebProfiles.get(webProfile).add(accountName);

    }

    public EducationalBackground getEducationBackground() {
        return mEducationBackground;
    }

    public void setEducationBackground(EducationalBackground educationBackground) {
        mEducationBackground = educationBackground;
    }

    public XingPhotoUrls getPhotoUrls() {
        return mPhotoUrls;
    }

    public void setPhotoUrls(XingPhotoUrls photoUrls) {
        mPhotoUrls = photoUrls;
    }

    public TimeZone getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        mTimeZone = timeZone;
    }

    public Map<MessagingAccount, String> getInstantMessagingAccounts() {
        return mInstantMessagingAccounts;
    }

    public void setInstantMessagingAccounts(EnumMap<MessagingAccount, String> instantMessagingAccounts) {
        mInstantMessagingAccounts = instantMessagingAccounts;
    }

    public void addInstantMessagingAccount(@NonNull final MessagingAccount account,
                                           @Nullable final String accountValue) {
        if (mInstantMessagingAccounts == null) {
            mInstantMessagingAccounts = new EnumMap<>(MessagingAccount.class);
        }

        mInstantMessagingAccounts.put(account, accountValue);
    }

    public ProfessionalExperience getProfessionalExperience() {
        return mProfessionalExperience;
    }

    public void setProfessionalExperience(ProfessionalExperience professionalExperience) {
        mProfessionalExperience = professionalExperience;
    }

    /**
     * @return The primary institution name
     */
    @Nullable
    public String getPrimaryInstitutionName() {
        String primaryInstitution = null;

        // Check if we have a primary company
        if (mProfessionalExperience != null && mProfessionalExperience.getPrimaryCompany() != null) {
            ExperienceCompany company = mProfessionalExperience.getPrimaryCompany();
            if (!TextUtils.isEmpty(company.getId()) || !TextUtils.isEmpty(company.getName())) {
                primaryInstitution = company.getName();
            }
        }

        // If the primary company not available try to use the primary school (the user may be a student)
        if (TextUtils.isEmpty(primaryInstitution)) {
            if (mEducationBackground != null && mEducationBackground.getPrimarySchool() != null) {
                School primarySchool = mEducationBackground.getPrimarySchool();
                if (!TextUtils.isEmpty(primarySchool.getName())) {
                    primaryInstitution = primarySchool.getName();
                }
            }

        }

        return primaryInstitution;
    }

    /**
     * @return The primary occupation name
     */
    @Nullable
    public String getPrimaryOccupationName() {
        String primaryOccupation = null;

        // Check if we have a primary company
        if (mProfessionalExperience != null && mProfessionalExperience.getPrimaryCompany() != null) {
            ExperienceCompany company = mProfessionalExperience.getPrimaryCompany();
            if (!TextUtils.isEmpty(company.getId()) || !TextUtils.isEmpty(company.getTitle())) {
                primaryOccupation = company.getTitle();
            }
        }

        // If the primary company not available try to use the primary school (the user may be a student)
        if (TextUtils.isEmpty(primaryOccupation)) {
            if (mEducationBackground != null && mEducationBackground.getPrimarySchool() != null) {
                School primarySchool = mEducationBackground.getPrimarySchool();
                if (!TextUtils.isEmpty(primarySchool.getDegree())) {
                    primaryOccupation = primarySchool.getDegree();
                }
            }

        }

        return primaryOccupation;
    }
}
