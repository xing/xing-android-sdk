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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Patterns;

import com.squareup.moshi.Json;
import com.xing.api.internal.json.CsvCollection;
import com.xing.api.model.XingCalendar;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Java representation of a XING user.
 *
 * @author david.gonzalez
 * @author serj.lotutovici
 * @author daniel.hartwich
 * @see <a href="https://dev.xing.com/docs/get/users/:id">https://dev.xing.com/docs/get/users/:id</a>
 */
@SuppressWarnings({"unused", "CollectionWithoutInitialCapacity"}) // Public api
public class XingUser implements Serializable {
    private static final long serialVersionUID = 1L;

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
    @Json(name = "wants")
    @CsvCollection
    private List<String> wants;
    @Json(name = "haves")
    @CsvCollection
    private List<String> haves;
    @Json(name = "interests")
    @CsvCollection
    private List<String> interests;
    @Json(name = "organisation_member")
    @CsvCollection
    private List<String> organizations;
    @Json(name = "languages")
    private Map<Language, LanguageSkill> languages;
    @Json(name = "private_address")
    private XingAddress privateAddress;
    @Json(name = "business_address")
    private XingAddress businessAddress;
    @Json(name = "web_profiles")
    private Map<WebProfile, Set<String>> webProfiles;
    @Json(name = "instant_messaging_accounts")
    private Map<MessagingAccount, String> instantMessagingAccounts;
    //    // TODO Handle Dates more correctly
    @Json(name = "professional_experience")
    private ProfessionalExperience professionalExperience;
    //    // TODO Handle Dates more correctly
    //    @Json(name = "educational_background")
    //    private EducationalBackground educationBackground;
    @Json(name = "photo_urls")
    private XingPhotoUrls photoUrls;

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
        result = 31 * result + (organizations != null ? organizations.hashCode() : 0);
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
              + ", organizations=" + organizations
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

    public List<String> getWants() {
        return wants;
    }

    public void setWants(List<String> wants) {
        this.wants = wants;
    }

    public void addToWants(String want) {
        if (wants == null) wants = new ArrayList<>();
        wants.add(want);
    }

    public void addAllToWants(List<String> wants) {
        if (this.wants == null) {
            this.wants = wants;
        } else {
            this.wants.addAll(wants);
        }
    }

    public List<String> getHaves() {
        return haves;
    }

    public void setHaves(List<String> haves) {
        this.haves = haves;
    }

    public void addToHaves(String has) {
        if (haves == null) haves = new ArrayList<>();
        haves.add(has);
    }

    public void addAllToHaves(List<String> haves) {
        if (this.haves == null) {
            this.haves = haves;
        } else {
            this.haves.addAll(haves);
        }
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public void addToInterests(String interest) {
        if (interests == null) interests = new ArrayList<>();
        interests.add(interest);
    }

    public void addAllToInterests(List<String> interests) {
        if (this.interests == null) {
            this.interests = interests;
        } else {
            this.interests.addAll(interests);
        }
    }

    public List<String> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<String> organizations) {
        this.organizations = organizations;
    }

    public void addToOrganisations(String organization) {
        if (organizations == null) organizations = new ArrayList<>();
        organizations.add(organization);
    }

    public void addAllToOrganizations(List<String> organizations) {
        if (this.organizations == null) {
            this.organizations = organizations;
        } else {
            this.organizations.addAll(organizations);
        }
    }

    public Map<Language, LanguageSkill> getLanguages() {
        return languages;
    }

    public void setLanguages(Map<Language, LanguageSkill> languages) {
        this.languages = languages;
    }

    @Nullable
    public LanguageSkill getLanguageSkill(@NonNull Language language) {
        return languages != null ? languages.get(language) : null;
    }

    public void addLanguage(@NonNull Language language, @Nullable LanguageSkill languageSkill) {
        if (languages == null) {
            languages = new LinkedHashMap<>();
        }

        languages.put(language, languageSkill);
    }

    public void addLanguage(@NonNull String language, @NonNull String languageSkill) {
        if (languages == null) {
            languages = new LinkedHashMap<>();
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

    public void setWebProfiles(Map<WebProfile, Set<String>> webProfiles) {
        this.webProfiles = webProfiles;
    }

    public void setWebProfiles(@NonNull WebProfile webProfile, @Nullable Set<String> profiles) {
        if (webProfiles == null) {
            webProfiles = new LinkedHashMap<>();
        }

        webProfiles.put(webProfile, profiles);
    }

    public void addWebProfiles(@NonNull WebProfile webProfile, @Nullable Set<String> accounts) {
        if (webProfiles == null) {
            webProfiles = new LinkedHashMap<>();
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
            webProfiles = new LinkedHashMap<>();
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

    public void setInstantMessagingAccounts(Map<MessagingAccount, String> instantMessagingAccounts) {
        this.instantMessagingAccounts = instantMessagingAccounts;
    }

    public void addInstantMessagingAccount(@NonNull MessagingAccount account, @Nullable String accountValue) {
        if (instantMessagingAccounts == null) {
            instantMessagingAccounts = new LinkedHashMap<>();
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
