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

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Patterns;

import com.squareup.moshi.Json;
import com.xing.android.sdk.model.XingCalendar;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Java representation of a XING user object.
 *
 * @author david.gonzalez
 * @author serj.lotutovici
 * @author daniel.hartwich
 * @see <a href="https://dev.xing.com/docs/get/users/:id">https://dev.xing.com/docs/get/users/:id</a>
 */
@SuppressWarnings("unused") // Public api
public class XingUser implements Serializable, Parcelable {
    private static final long serialVersionUID = 3037193617271688856L;
    private static final Pattern COMMA_SEPARATOR = Pattern.compile(", ");
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

    @Json(name = "id")
    private String id;
    @Json(name = "first_name")
    private String firstName;
    @Json(name = "last_name")
    private String lastName;
    @Json(name = "display_name")
    private String displayName;
    @Json(name = "page_name")
    private String pageName;
    @Json(name = "permalink")
    private String permalink;
    @Json(name = "employment_status")
    private EmploymentStatus employmentStatus;
    @Json(name = "gender")
    private Gender gender;
    //FIXME annotate with birth_date
    private volatile XingCalendar birthday;
    @Json(name = "active_email")
    private String activeEmail;
    @Json(name = "time_zone")
    private TimeZone timeZone;
    @Json(name = "premium_services")
    private List<PremiumService> premiumServices;
    @Json(name = "badges")
    private List<Badge> badges;
    // TODO Custom Adapter for Comma-Seperated values
    @Json(name = "wants")
    private String wants;
    // TODO Custom Adapter for Comma-Seperated values
    @Json(name = "haves")
    private String haves;
    // TODO Custom Adapter for Comma-Seperated values
    @Json(name = "interests")
    private String interests;
    // TODO Custom Adapter for Comma-Seperated values
    @Json(name = "organisation_member")
    private String organisationMember;
    @Json(name = "languages")
    private EnumMap<Language, LanguageSkill> languages;
    @Json(name = "private_address")
    private XingAddress privateAddress;
    @Json(name = "business_address")
    private XingAddress businessAddress;
    @Json(name = "web_profiles")
    private EnumMap<WebProfile, Set<String>> webProfiles;
    @Json(name = "instant_messaging_accounts")
    private EnumMap<MessagingAccount, String> instantMessagingAccounts;
    //    // TODO Handle Dates more correctly
    @Json(name = "professional_experience")
    private ProfessionalExperience professionalExperience;
    //    // TODO Handle Dates more correctly
    //    @Json(name = "educational_background")
    //    private EducationalBackground educationBackground;
    @Json(name = "photo_urls")
    private XingPhotoUrls photoUrls;

    public XingUser(@NonNull String id) {
        if (TextUtils.isEmpty(id)) {
            throw new IllegalArgumentException("id can not be null neither empty");
        } else {
            this.id = id;
        }
    }

    public XingUser() {
    }

    /**
     * Create {@link XingUser} from {@link Parcel}.
     *
     * @param in Input {@link Parcel}
     */
    @SuppressWarnings("unchecked")
    private XingUser(Parcel in) {
        id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        displayName = in.readString();
        pageName = in.readString();
        permalink = in.readString();
        int tmpMEmploymentStatus = in.readInt();
        employmentStatus = tmpMEmploymentStatus == -1 ? null : EmploymentStatus.values()[tmpMEmploymentStatus];
        int tmpMGender = in.readInt();
        gender = tmpMGender == -1 ? null : Gender.values()[tmpMGender];
        birthday = (XingCalendar) in.readSerializable();
        activeEmail = in.readString();
        premiumServices = new ArrayList<>(0);
        in.readList(premiumServices, ArrayList.class.getClassLoader());
        badges = new ArrayList<>(0);
        in.readList(badges, ArrayList.class.getClassLoader());
        wants = in.readString();
        haves = in.readString();
        interests = in.readString();
        //        organisationMember = new ArrayList<>(0);
        //        in.readStringList(organisationMember);
        languages = (EnumMap<Language, LanguageSkill>) in.readSerializable();
        privateAddress = in.readParcelable(XingAddress.class.getClassLoader());
        timeZone = in.readParcelable(TimeZone.class.getClassLoader());
        businessAddress = in.readParcelable(XingAddress.class.getClassLoader());
        webProfiles = (EnumMap<WebProfile, Set<String>>) in.readSerializable();
        instantMessagingAccounts = (EnumMap<MessagingAccount, String>) in.readSerializable();
        //        educationBackground = in.readParcelable(EducationalBackground.class.getClassLoader());
        professionalExperience = in.readParcelable(ProfessionalExperience.class.getClassLoader());
        photoUrls = in.readParcelable(XingPhotoUrls.class.getClassLoader());
    }

    public static boolean isUserIdValid(String userId) {
        return !isUserIdInValid(userId);
    }

    public static boolean isUserIdInValid(String userId) {
        return TextUtils.isEmpty(userId);
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
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (pageName != null ? pageName.hashCode() : 0);
        result = 31 * result + (permalink != null ? permalink.hashCode() : 0);
        result = 31 * result + (employmentStatus != null ? employmentStatus.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (activeEmail != null ? activeEmail.hashCode() : 0);
        result = 31 * result + (premiumServices != null ? premiumServices.hashCode() : 0);
        result = 31 * result + (badges != null ? badges.hashCode() : 0);
        result = 31 * result + (wants != null ? wants.hashCode() : 0);
        result = 31 * result + (haves != null ? haves.hashCode() : 0);
        result = 31 * result + (interests != null ? interests.hashCode() : 0);
        result = 31 * result + (organisationMember != null ? organisationMember.hashCode() : 0);
        result = 31 * result + (languages != null ? languages.hashCode() : 0);
        result = 31 * result + (privateAddress != null ? privateAddress.hashCode() : 0);
        result = 31 * result + (timeZone != null ? timeZone.hashCode() : 0);
        result = 31 * result + (businessAddress != null ? businessAddress.hashCode() : 0);
        result = 31 * result + (webProfiles != null ? webProfiles.hashCode() : 0);
        result = 31 * result + (instantMessagingAccounts != null ? instantMessagingAccounts.hashCode() : 0);
        //        result = 31 * result + (educationBackground != null ? educationBackground.hashCode() : 0);
        result = 31 * result + (professionalExperience != null ? professionalExperience.hashCode() : 0);
        result = 31 * result + (photoUrls != null ? photoUrls.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "XingUser{"
              + "id='" + id + '\''
              + ", firstName='" + firstName + '\''
              + ", lastName='" + lastName + '\''
              + ", displayName='" + displayName + '\''
              + ", pageName='" + pageName + '\''
              + ", permalink=" + permalink
              + ", employmentStatus=" + employmentStatus
              + ", gender=" + gender
              + ", birthday=" + birthday
              + ", activeEmail='" + activeEmail + '\''
              + ", premiumServices=" + premiumServices
              + ", badges=" + badges
              + ", wants=" + wants
              + ", haves=" + haves
              + ", interests=" + interests
              + ", organisationMember=" + organisationMember
              + ", languages=" + languages
              + ", privateAddress=" + privateAddress
              + ", timeZone=" + timeZone
              + ", businessAddress=" + businessAddress
              + ", webProfiles=" + webProfiles
              + ", instantMessagingAccounts=" + instantMessagingAccounts
              //              + ", educationBackground=" + educationBackground
              + ", professionalExperience=" + professionalExperience
              + ", photoUrls=" + photoUrls
              + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(displayName);
        dest.writeString(pageName);
        dest.writeString(permalink);
        dest.writeInt(employmentStatus == null ? -1 : employmentStatus.ordinal());
        dest.writeInt(gender == null ? -1 : gender.ordinal());
        dest.writeSerializable(birthday);
        dest.writeString(activeEmail);
        dest.writeList(premiumServices);
        dest.writeList(badges);
        dest.writeString(wants);
        dest.writeString(haves);
        dest.writeString(interests);
        dest.writeString(organisationMember);
        dest.writeSerializable(languages);
        dest.writeParcelable(privateAddress, flags);
        dest.writeParcelable(timeZone, flags);
        dest.writeParcelable(businessAddress, flags);
        dest.writeSerializable(webProfiles);
        dest.writeSerializable(instantMessagingAccounts);
        //        dest.writeParcelable(educationBackground, flags);
        dest.writeParcelable(professionalExperience, flags);
        dest.writeParcelable(photoUrls, flags);
    }

    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        if (TextUtils.isEmpty(id)) {
            throw new IllegalArgumentException("id can not be null neither empty");
        } else {
            this.id = id;
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        if (!Patterns.WEB_URL.matcher(permalink).matches()) {
            throw new InvalidParameterException(permalink + " is not an url.");
        }

        this.permalink = permalink;
    }

    public EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = EmploymentStatus.valueOf(employmentStatus);
    }

    public XingCalendar getBirthday() {
        return birthday;
    }

    public void setBirthday(XingCalendar birthday) {
        this.birthday = birthday;
    }

    public String getActiveEmail() {
        return activeEmail;
    }

    public void setActiveEmail(String activeEmail) {
        if (!Patterns.EMAIL_ADDRESS.matcher(activeEmail).matches()) {
            throw new InvalidParameterException(activeEmail + " is not a valid email");
        } else {
            this.activeEmail = activeEmail;
        }
    }

    public List<PremiumService> getPremiumServices() {
        return premiumServices;
    }

    public void setPremiumServices(List<PremiumService> premiumServices) {
        this.premiumServices = premiumServices;
    }

    public void addPremiumService(PremiumService premiumService) {
        if (premiumServices == null) {
            premiumServices = new ArrayList<>(1);
        }
        premiumServices.add(premiumService);
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    public void setBadgesFromStringList(List<String> badges) {
        if (this.badges == null) {
            this.badges = new ArrayList<>(badges.size());
        }

        Badge badge;
        for (String badgeString : badges) {
            badge = Badge.valueOf(badgeString);

            if (badge != null) {
                this.badges.add(badge);
            }
        }
    }

    public void addBadges(Badge badge) {
        if (badges == null) {
            badges = new ArrayList<>(1);
        }
        badges.add(badge);
    }

    public String getWants() {
        return wants;
    }

    public void setWants(String wants) {
        this.wants = wants;
    }

    //    /**
    //     * Replace the current wants with the ones from the param.
    //     *
    //     * @param wants String with wants with the format want1, want2, want3, want4
    //     */
    //
    //    public void setWants(String wants) {
    //        String[] wantsArray = COMMA_SEPARATOR.split(wants);
    //        this.wants = Arrays.asList(wantsArray);
    //    }

    //    /**
    //     * Adds a new want to the list of wants.
    //     *
    //     * @param want The new want to add to the list.
    //     */
    //    //TODO consider wants, haves and interests with commas, it can affect.
    //    public void addWant(String want) {
    //        if (wants == null) {
    //            wants = new ArrayList<>(1);
    //        }
    //
    //        wants.add(want);
    //    }

    public String getHaves() {
        return haves;
    }

    public void setHaves(String haves) {
        this.haves = haves;
    }

    /**
     * Replace the current haves with the ones from the param.
     *
     * //     * @param haves String with haves with the format have1, have2, have3, have4
     */

    //    public void setHaves(String haves) {
    //        String[] havesArray = COMMA_SEPARATOR.split(haves);
    //        this.haves = Arrays.asList(havesArray);
    //    }
    //
    //    public void addHave(String have) {
    //        if (haves == null) {
    //            haves = new ArrayList<>(1);
    //        }
    //
    //        haves.add(have);
    //    }
    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }
    //
    //    /**
    //     * Replace the current interests with the ones from the param.
    //     *
    //     * @param interests String with interests with the format interest1, interest2, interest3, interest4
    //     */
    //
    //    public void setInterests(String interests) {
    //        String[] interestsArray = COMMA_SEPARATOR.split(interests);
    //        this.interests = Arrays.asList(interestsArray);
    //    }
    //
    //    public void addInterest(String interest) {
    //        if (interests == null) {
    //            interests = new ArrayList<>(1);
    //        }
    //
    //        interests.add(interest);
    //    }

    public String getOrganisationMember() {
        return organisationMember;
    }

    public void setOrganisationMember(String organisationMember) {
        this.organisationMember = organisationMember;
    }

    //    /**
    //     * Replace the current organisationMember with the ones from the param.
    //     *
    //     * @param organisationMember String with interests with the format organisationMember1, organisationMember2,
    //     * organisationMember3, organisationMember4
    //     */
    //
    //    public void setOrganisationMember(String organisationMember) {
    //        String[] organisationMemberArray = COMMA_SEPARATOR.split(organisationMember);
    //        this.organisationMember = Arrays.asList(organisationMemberArray);
    //    }
    //
    //    public void addOrganisationMember(String organisationMember) {
    //        if (this.organisationMember == null) {
    //            this.organisationMember = new ArrayList<>(1);
    //        }
    //
    //        this.organisationMember.add(organisationMember);
    //    }

    public EnumMap<Language, LanguageSkill> getLanguages() {
        return languages;
    }

    public void setLanguages(EnumMap<Language, LanguageSkill> languages) {
        this.languages = languages;
    }

    @Nullable
    public LanguageSkill getLanguageSkill(@NonNull Language language) {
        return languages != null ? languages.get(language) : null;
    }

    public void addLanguage(@NonNull Language language, @Nullable LanguageSkill languageSkill) {
        if (languages == null) {
            languages = new EnumMap<>(Language.class);
        }

        languages.put(language, languageSkill);
    }

    public void addLanguage(@NonNull String language, @NonNull String languageSkill) {
        if (languages == null) {
            languages = new EnumMap<>(Language.class);
        }

        languages.put(Language.valueOf(language), LanguageSkill.valueOf(languageSkill));
    }

    public XingAddress getPrivateAddress() {
        return privateAddress;
    }

    public void setPrivateAddress(XingAddress privateAddress) {
        this.privateAddress = privateAddress;
    }

    public XingAddress getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(XingAddress businessAddress) {
        this.businessAddress = businessAddress;
    }

    public Map<WebProfile, Set<String>> getWebProfiles() {
        return webProfiles;
    }

    public void setWebProfiles(EnumMap<WebProfile, Set<String>> webProfiles) {
        this.webProfiles = webProfiles;
    }

    public void setWebProfiles(@NonNull WebProfile webProfile, @Nullable Set<String> profiles) {
        if (webProfiles == null) {
            webProfiles = new EnumMap<>(WebProfile.class);
        }

        webProfiles.put(webProfile, profiles);
    }

    public void addWebProfiles(@NonNull WebProfile webProfile, @Nullable Set<String> accounts) {
        if (webProfiles == null) {
            webProfiles = new EnumMap<>(WebProfile.class);
        }

        if (!webProfiles.containsKey(webProfile)) {
            webProfiles.put(webProfile, new LinkedHashSet<String>(1));
        }

        if (accounts != null) {
            webProfiles.get(webProfile).addAll(accounts);
        }
    }

    public void addWebProfile(@NonNull WebProfile webProfile, @Nullable String accountName) {
        if (webProfiles == null) {
            webProfiles = new EnumMap<>(WebProfile.class);
        }

        if (!webProfiles.containsKey(webProfile)) {
            webProfiles.put(webProfile, new LinkedHashSet<String>(1));
        }

        webProfiles.get(webProfile).add(accountName);
    }

    //    public EducationalBackground getEducationBackground() {
    //        return educationBackground;
    //    }
    //
    //    public void setEducationBackground(EducationalBackground educationBackground) {
    //        this.educationBackground = educationBackground;
    //    }

    public XingPhotoUrls getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(XingPhotoUrls photoUrls) {
        this.photoUrls = photoUrls;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public Map<MessagingAccount, String> getInstantMessagingAccounts() {
        return instantMessagingAccounts;
    }

    public void setInstantMessagingAccounts(EnumMap<MessagingAccount, String> instantMessagingAccounts) {
        this.instantMessagingAccounts = instantMessagingAccounts;
    }

    public void addInstantMessagingAccount(@NonNull MessagingAccount account, @Nullable String accountValue) {
        if (instantMessagingAccounts == null) {
            instantMessagingAccounts = new EnumMap<>(MessagingAccount.class);
        }

        instantMessagingAccounts.put(account, accountValue);
    }

    public ProfessionalExperience getProfessionalExperience() {
        return professionalExperience;
    }

    public void setProfessionalExperience(ProfessionalExperience professionalExperience) {
        this.professionalExperience = professionalExperience;
    }

    /**
     * @return The primary institution name
     */
    @Nullable
    public String getPrimaryInstitutionName() {
        String primaryInstitution = null;

        //         Check if we have a primary company
        if (professionalExperience != null && professionalExperience.getPrimaryCompany() != null) {
            ExperienceCompany company = professionalExperience.getPrimaryCompany();
            if (!TextUtils.isEmpty(company.getId()) || !TextUtils.isEmpty(company.getName())) {
                primaryInstitution = company.getName();
            }
        }

        //        // If the primary company not available try to use the primary school (the user may be a student)
        //        if (TextUtils.isEmpty(primaryInstitution)) {
        //            if (educationBackground != null && educationBackground.getPrimarySchool() != null) {
        //                School primarySchool = educationBackground.getPrimarySchool();
        //                if (!TextUtils.isEmpty(primarySchool.getName())) {
        //                    primaryInstitution = primarySchool.getName();
        //                }
        //            }
        //        }

        return primaryInstitution;
    }

    /**
     * @return The primary occupation name
     */
    @Nullable
    public String getPrimaryOccupationName() {
        String primaryOccupation = null;

        // Check if we have a primary company
        if (professionalExperience != null && professionalExperience.getPrimaryCompany() != null) {
            ExperienceCompany company = professionalExperience.getPrimaryCompany();
            if (!TextUtils.isEmpty(company.getId()) || !TextUtils.isEmpty(company.getTitle())) {
                primaryOccupation = company.getTitle();
            }
        }

        //        // If the primary company not available try to use the primary school (the user may be a student)
        //        if (TextUtils.isEmpty(primaryOccupation)) {
        //            if (educationBackground != null && educationBackground.getPrimarySchool() != null) {
        //                School primarySchool = educationBackground.getPrimarySchool();
        //                if (!TextUtils.isEmpty(primarySchool.getDegree())) {
        //                    primaryOccupation = primarySchool.getDegree();
        //                }
        //            }
        //        }

        return primaryOccupation;
    }
}
