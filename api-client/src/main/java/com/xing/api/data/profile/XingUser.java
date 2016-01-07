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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Patterns;

import com.squareup.moshi.Json;
import com.xing.api.internal.json.BirthDate;
import com.xing.api.internal.json.CsvCollection;
import com.xing.api.data.SafeCalendar;

import java.io.Serializable;
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
 * @see <a href="https://dev.xing.com/docs/get/users/:id">User Profile Resource</a>
 */
@SuppressWarnings({"unused", "CollectionWithoutInitialCapacity"}) // Public api
public class XingUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "id")
    private String id;
    @Json(name = "academic_title")
    private String academicTitle;
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
    @BirthDate
    @Json(name = "birth_date")
    private SafeCalendar birthDate;
    @Json(name = "active_email")
    private String activeEmail;
    @Json(name = "time_zone")
    private TimeZone timeZone;
    @Json(name = "premium_services")
    private List<PremiumService> premiumServices;
    @Json(name = "badges")
    private List<Badge> badges;
    @CsvCollection
    @Json(name = "wants")
    private List<String> wants;
    @CsvCollection
    @Json(name = "haves")
    private List<String> haves;
    @CsvCollection
    @Json(name = "interests")
    private List<String> interests;
    @CsvCollection
    @Json(name = "organisation_member")
    private List<String> organizations;
    @Json(name = "languages")
    private Map<Language, LanguageSkill> languages;
    @Json(name = "private_address")
    private Address privateAddress;
    @Json(name = "business_address")
    private Address businessAddress;
    @Json(name = "web_profiles")
    private Map<WebProfile, Set<String>> webProfiles;
    @Json(name = "instant_messaging_accounts")
    private Map<MessagingAccount, String> messagingAccounts;
    @Json(name = "professional_experience")
    private ProfessionalExperience professionalExperience;
    @Json(name = "educational_background")
    private EducationalBackground educationBackground;
    @Json(name = "photo_urls")
    private PhotoUrls photoUrls;

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XingUser xingUser = (XingUser) o;

        if (id != null ? !id.equals(xingUser.id) : xingUser.id != null) return false;
        if (academicTitle != null ? !academicTitle.equals(xingUser.academicTitle) : xingUser.academicTitle != null) {
            return false;
        }
        if (firstName != null ? !firstName.equals(xingUser.firstName) : xingUser.firstName != null) return false;
        if (lastName != null ? !lastName.equals(xingUser.lastName) : xingUser.lastName != null) return false;
        if (displayName != null ? !displayName.equals(xingUser.displayName) : xingUser.displayName != null) {
            return false;
        }
        if (pageName != null ? !pageName.equals(xingUser.pageName) : xingUser.pageName != null) return false;
        if (permalink != null ? !permalink.equals(xingUser.permalink) : xingUser.permalink != null) return false;
        if (employmentStatus != xingUser.employmentStatus) return false;
        if (gender != xingUser.gender) return false;
        if (birthDate != null ? !birthDate.equals(xingUser.birthDate) : xingUser.birthDate != null) return false;
        if (activeEmail != null ? !activeEmail.equals(xingUser.activeEmail) : xingUser.activeEmail != null) {
            return false;
        }
        if (timeZone != null ? !timeZone.equals(xingUser.timeZone) : xingUser.timeZone != null) return false;
        if (premiumServices != null ? !premiumServices.equals(xingUser.premiumServices)
              : xingUser.premiumServices != null) {
            return false;
        }
        if (badges != null ? !badges.equals(xingUser.badges) : xingUser.badges != null) return false;
        if (wants != null ? !wants.equals(xingUser.wants) : xingUser.wants != null) return false;
        if (haves != null ? !haves.equals(xingUser.haves) : xingUser.haves != null) return false;
        if (interests != null ? !interests.equals(xingUser.interests) : xingUser.interests != null) return false;
        if (organizations != null ? !organizations.equals(xingUser.organizations) : xingUser.organizations != null) {
            return false;
        }
        if (languages != null ? !languages.equals(xingUser.languages) : xingUser.languages != null) return false;
        if (privateAddress != null ? !privateAddress.equals(xingUser.privateAddress)
              : xingUser.privateAddress != null) {
            return false;
        }
        if (businessAddress != null ? !businessAddress.equals(xingUser.businessAddress)
              : xingUser.businessAddress != null) {
            return false;
        }
        if (webProfiles != null ? !webProfiles.equals(xingUser.webProfiles) : xingUser.webProfiles != null) {
            return false;
        }
        if (messagingAccounts != null ? !messagingAccounts.equals(xingUser.messagingAccounts)
              : xingUser.messagingAccounts != null) {
            return false;
        }
        if (professionalExperience != null ? !professionalExperience.equals(xingUser.professionalExperience)
              : xingUser.professionalExperience != null) {
            return false;
        }
        if (educationBackground != null ? !educationBackground.equals(xingUser.educationBackground)
              : xingUser.educationBackground != null) {
            return false;
        }
        return photoUrls != null ? photoUrls.equals(xingUser.photoUrls) : xingUser.photoUrls == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (academicTitle != null ? academicTitle.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (pageName != null ? pageName.hashCode() : 0);
        result = 31 * result + (permalink != null ? permalink.hashCode() : 0);
        result = 31 * result + (employmentStatus != null ? employmentStatus.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        result = 31 * result + (activeEmail != null ? activeEmail.hashCode() : 0);
        result = 31 * result + (timeZone != null ? timeZone.hashCode() : 0);
        result = 31 * result + (premiumServices != null ? premiumServices.hashCode() : 0);
        result = 31 * result + (badges != null ? badges.hashCode() : 0);
        result = 31 * result + (wants != null ? wants.hashCode() : 0);
        result = 31 * result + (haves != null ? haves.hashCode() : 0);
        result = 31 * result + (interests != null ? interests.hashCode() : 0);
        result = 31 * result + (organizations != null ? organizations.hashCode() : 0);
        result = 31 * result + (languages != null ? languages.hashCode() : 0);
        result = 31 * result + (privateAddress != null ? privateAddress.hashCode() : 0);
        result = 31 * result + (businessAddress != null ? businessAddress.hashCode() : 0);
        result = 31 * result + (webProfiles != null ? webProfiles.hashCode() : 0);
        result = 31 * result + (messagingAccounts != null ? messagingAccounts.hashCode() : 0);
        result = 31 * result + (professionalExperience != null ? professionalExperience.hashCode() : 0);
        result = 31 * result + (educationBackground != null ? educationBackground.hashCode() : 0);
        result = 31 * result + (photoUrls != null ? photoUrls.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "XingUser{"
              + "id='" + id + '\''
              + ", academicTitle='" + academicTitle + '\''
              + ", firstName='" + firstName + '\''
              + ", lastName='" + lastName + '\''
              + ", displayName='" + displayName + '\''
              + ", pageName='" + pageName + '\''
              + ", permalink=" + permalink
              + ", employmentStatus=" + employmentStatus
              + ", gender=" + gender
              + ", birthDate=" + birthDate
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
              + ", messagingAccounts=" + messagingAccounts
              + ", educationBackground=" + educationBackground
              + ", professionalExperience=" + professionalExperience
              + ", photoUrls=" + photoUrls
              + '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
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
            throw new IllegalArgumentException(permalink + " is not an url.");
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

    public SafeCalendar getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(SafeCalendar birthDate) {
        this.birthDate = birthDate;
    }

    public String getActiveEmail() {
        return activeEmail;
    }

    public void setActiveEmail(String activeEmail) {
        if (!Patterns.EMAIL_ADDRESS.matcher(activeEmail).matches()) {
            throw new IllegalArgumentException(activeEmail + " is not a valid email");
        }
        this.activeEmail = activeEmail;
    }

    public List<PremiumService> getPremiumServices() {
        return premiumServices;
    }

    public void setPremiumServices(List<PremiumService> premiumServices) {
        this.premiumServices = premiumServices;
    }

    public void addPremiumService(PremiumService premiumService) {
        if (premiumServices == null) premiumServices = new ArrayList<>(1);
        premiumServices.add(premiumService);
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    public void addToBadges(Badge badge) {
        if (badges == null) badges = new ArrayList<>(1);
        badges.add(badge);
    }

    public void addAllToBadges(List<Badge> badges) {
        if (this.badges == null) {
            this.badges = badges;
        } else {
            this.badges.addAll(badges);
        }
    }

    public List<String> getWants() {
        return wants;
    }

    public void setWants(List<String> wants) {
        this.wants = wants;
    }

    public void addToWants(String want) {
        if (wants == null) wants = new ArrayList<>(1);
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
        if (haves == null) haves = new ArrayList<>(1);
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
        if (interests == null) interests = new ArrayList<>(1);
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
        if (organizations == null) organizations = new ArrayList<>(1);
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

    public LanguageSkill getLanguageSkill(Language language) {
        return languages.get(language);
    }

    public void addLanguage(Language language, LanguageSkill languageSkill) {
        if (languages == null) languages = new LinkedHashMap<>();
        languages.put(language, languageSkill);
    }

    public Address getPrivateAddress() {
        return privateAddress;
    }

    public void setPrivateAddress(Address privateAddress) {
        this.privateAddress = privateAddress;
    }

    public Address getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(Address businessAddress) {
        this.businessAddress = businessAddress;
    }

    public Map<WebProfile, Set<String>> getWebProfiles() {
        return webProfiles;
    }

    public void setWebProfiles(Map<WebProfile, Set<String>> webProfiles) {
        this.webProfiles = webProfiles;
    }

    public void setWebProfiles(WebProfile webProfile, Set<String> profiles) {
        if (webProfiles == null) webProfiles = new LinkedHashMap<>();
        webProfiles.put(webProfile, profiles);
    }

    public void addTpWebProfile(WebProfile webProfile, String accountName) {
        if (webProfiles == null) webProfiles = new LinkedHashMap<>();
        if (!webProfiles.containsKey(webProfile)) webProfiles.put(webProfile, new LinkedHashSet<String>());
        webProfiles.get(webProfile).add(accountName);
    }

    public void addAllToWebProfile(WebProfile webProfile, Set<String> profiles) {
        if (webProfiles == null) webProfiles = new LinkedHashMap<>();
        if (!webProfiles.containsKey(webProfile)) webProfiles.put(webProfile, new LinkedHashSet<String>());
        if (profiles != null) webProfiles.get(webProfile).addAll(profiles);
    }

    public EducationalBackground getEducationBackground() {
        return educationBackground;
    }

    public void setEducationBackground(EducationalBackground educationBackground) {
        this.educationBackground = educationBackground;
    }

    public PhotoUrls getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(PhotoUrls photoUrls) {
        this.photoUrls = photoUrls;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public Map<MessagingAccount, String> getMessagingAccounts() {
        return messagingAccounts;
    }

    public void setMessagingAccounts(Map<MessagingAccount, String> messagingAccounts) {
        this.messagingAccounts = messagingAccounts;
    }

    public void addMessagingAccount(@NonNull MessagingAccount account, String accountValue) {
        if (messagingAccounts == null) messagingAccounts = new LinkedHashMap<>();
        messagingAccounts.put(account, accountValue);
    }

    public ProfessionalExperience getProfessionalExperience() {
        return professionalExperience;
    }

    public void setProfessionalExperience(ProfessionalExperience professionalExperience) {
        this.professionalExperience = professionalExperience;
    }

    /**
     * Returns the primary institution name. This method will try to return a primary {@link School} in case the
     * user is a student.
     */
    @Nullable
    public String getPrimaryInstitutionName() {
        String primaryInstitution = null;

        // Check if we have a primary company
        if (professionalExperience != null && professionalExperience.getPrimaryCompany() != null) {
            Company company = professionalExperience.getPrimaryCompany();
            primaryInstitution = company.getName();
        }

        // If the primary company not available try to use the primary school (the user may be a student)
        if (primaryInstitution == null || primaryInstitution.isEmpty()) {
            if (educationBackground != null && educationBackground.getPrimarySchool() != null) {
                School primarySchool = educationBackground.getPrimarySchool();
                primaryInstitution = primarySchool.getName();
            }
        }

        return primaryInstitution;
    }

    /**
     * Returns the user primary occupation. This method will also check the users education fields if the user is a
     * student.
     */
    @Nullable
    public String getPrimaryOccupationName() {
        String primaryOccupation = null;

        // Check if we have a primary company
        if (professionalExperience != null && professionalExperience.getPrimaryCompany() != null) {
            Company company = professionalExperience.getPrimaryCompany();
            primaryOccupation = company.getTitle();
        }

        // If the primary company not available try to use the primary school (the user may be a student)
        if (primaryOccupation == null || primaryOccupation.isEmpty()) {
            if (educationBackground != null && educationBackground.getPrimarySchool() != null) {
                School primarySchool = educationBackground.getPrimarySchool();
                primaryOccupation = primarySchool.getDegree();
            }
        }

        return primaryOccupation;
    }
}
