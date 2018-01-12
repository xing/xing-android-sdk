/*
 * Copyright (C) 2018 XING SE (http://xing.com/)
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
import com.xing.api.internal.json.BirthDate;
import com.xing.api.internal.json.CsvCollection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A XING user.
 *
 * @see <a href="https://dev.xing.com/docs/get/users/:id">User Profile Resource</a>
 */
@SuppressWarnings({"unused", "CollectionWithoutInitialCapacity"}) // Public api
public class XingUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "id")
    private /* TODO: make final */ String id;
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
    @Json(name = "top_haves")
    private List<String> topHaves;
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
    @Json(name = "legal_information")
    private LegalInformationPreview legalInformationPreview;

    /**
     * Create an instance of {@linkplain XingUser}.
     *
     * @deprecated A user can not exist without an {@linkplain #id()}. Use {@linkplain XingUser#XingUser(String)}. The
     * user id can be returned {@code null} by the API only if the user is blacklisted, see {@linkplain #isBlacklisted()}.
     */
    @Deprecated
    public XingUser() {
    }

    /** Create an {@linkplain XingUser} instance with the respective user id. */
    public XingUser(String id) {
        this.id = id;
    }

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
        if (topHaves != null ? !topHaves.equals(xingUser.topHaves) : xingUser.topHaves != null) return false;
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
        if (legalInformationPreview != null ? !legalInformationPreview.equals(xingUser.legalInformationPreview)
              : xingUser.legalInformationPreview != null) {
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
        result = 31 * result + (topHaves != null ? topHaves.hashCode() : 0);
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
        result = 31 * result + (legalInformationPreview != null ? legalInformationPreview.hashCode() : 0);
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
              + ", topHaves=" + topHaves
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
              + ", legalInformationPreview=" + legalInformationPreview
              + '}';
    }

    public String id() {
        return id;
    }

    /** @deprecated See {@linkplain XingUser#XingUser()}. */
    @Deprecated
    public XingUser id(String id) {
        this.id = id;
        return this;
    }

    public String academicTitle() {
        return academicTitle;
    }

    public XingUser academicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
        return this;
    }

    public String firstName() {
        return firstName;
    }

    public XingUser firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String lastName() {
        return lastName;
    }

    public XingUser lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String displayName() {
        return displayName;
    }

    public XingUser displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public Gender gender() {
        return gender;
    }

    public XingUser gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public String pageName() {
        return pageName;
    }

    public XingUser pageName(String pageName) {
        this.pageName = pageName;
        return this;
    }

    public String permalink() {
        return permalink;
    }

    public XingUser permalink(String permalink) {
        this.permalink = permalink;
        return this;
    }

    public EmploymentStatus employmentStatus() {
        return employmentStatus;
    }

    public XingUser employmentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
        return this;
    }

    public SafeCalendar birthDate() {
        return birthDate;
    }

    public XingUser birthDate(SafeCalendar birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public String activeEmail() {
        return activeEmail;
    }

    public XingUser activeEmail(String activeEmail) {
        this.activeEmail = activeEmail;
        return this;
    }

    public List<PremiumService> premiumServices() {
        return premiumServices;
    }

    public XingUser premiumServices(List<PremiumService> premiumServices) {
        this.premiumServices = premiumServices;
        return this;
    }

    public XingUser addPremiumService(PremiumService premiumService) {
        if (premiumServices == null) premiumServices = new ArrayList<>(1);
        premiumServices.add(premiumService);
        return this;
    }

    public List<Badge> badges() {
        return badges;
    }

    public XingUser badges(List<Badge> badges) {
        this.badges = badges;
        return this;
    }

    public XingUser addToBadges(Badge badge) {
        if (badges == null) badges = new ArrayList<>(1);
        badges.add(badge);
        return this;
    }

    public XingUser addAllToBadges(List<Badge> badges) {
        if (this.badges == null) {
            this.badges = badges;
        } else {
            this.badges.addAll(badges);
        }
        return this;
    }

    public List<String> wants() {
        return wants;
    }

    public XingUser wants(List<String> wants) {
        this.wants = wants;
        return this;
    }

    public XingUser addToWants(String want) {
        if (wants == null) wants = new ArrayList<>(1);
        wants.add(want);
        return this;
    }

    public XingUser addAllToWants(List<String> wants) {
        if (this.wants == null) {
            this.wants = wants;
        } else {
            this.wants.addAll(wants);
        }
        return this;
    }

    public List<String> haves() {
        return haves;
    }

    public XingUser haves(List<String> haves) {
        this.haves = haves;
        return this;
    }

    public XingUser addToHaves(String has) {
        if (haves == null) haves = new ArrayList<>(1);
        haves.add(has);
        return this;
    }

    public XingUser addAllToHaves(List<String> haves) {
        if (this.haves == null) {
            this.haves = haves;
        } else {
            this.haves.addAll(haves);
        }
        return this;
    }

    public List<String> topHaves() {
        return topHaves;
    }

    public XingUser topHaves(List<String> topHaves) {
        this.topHaves = topHaves;
        return this;
    }

    public XingUser addToTopHaves(String has) {
        if (topHaves == null) topHaves = new ArrayList<>(1);
        topHaves.add(has);
        return this;
    }

    public XingUser addAllToTopHaves(List<String> topHaves) {
        if (this.topHaves == null) {
            this.topHaves = topHaves;
        } else {
            this.topHaves.addAll(topHaves);
        }
        return this;
    }

    public List<String> interests() {
        return interests;
    }

    public XingUser interests(List<String> interests) {
        this.interests = interests;
        return this;
    }

    public XingUser addToInterests(String interest) {
        if (interests == null) interests = new ArrayList<>(1);
        interests.add(interest);
        return this;
    }

    public XingUser addAllToInterests(List<String> interests) {
        if (this.interests == null) {
            this.interests = interests;
        } else {
            this.interests.addAll(interests);
        }
        return this;
    }

    public List<String> organizations() {
        return organizations;
    }

    public XingUser organizations(List<String> organizations) {
        this.organizations = organizations;
        return this;
    }

    public XingUser addToOrganisations(String organization) {
        if (organizations == null) organizations = new ArrayList<>(1);
        organizations.add(organization);
        return this;
    }

    public XingUser addAllToOrganizations(List<String> organizations) {
        if (this.organizations == null) {
            this.organizations = organizations;
        } else {
            this.organizations.addAll(organizations);
        }
        return this;
    }

    public Map<Language, LanguageSkill> languages() {
        return languages;
    }

    public XingUser languages(Map<Language, LanguageSkill> languages) {
        this.languages = languages;
        return this;
    }

    public LanguageSkill languageSkill(Language language) {
        return languages.get(language);
    }

    public XingUser addLanguage(Language language, LanguageSkill languageSkill) {
        if (languages == null) languages = new LinkedHashMap<>();
        languages.put(language, languageSkill);
        return this;
    }

    public Address privateAddress() {
        return privateAddress;
    }

    public XingUser privateAddress(Address privateAddress) {
        this.privateAddress = privateAddress;
        return this;
    }

    public Address businessAddress() {
        return businessAddress;
    }

    public XingUser businessAddress(Address businessAddress) {
        this.businessAddress = businessAddress;
        return this;
    }

    public Map<WebProfile, Set<String>> webProfiles() {
        return webProfiles;
    }

    public XingUser webProfiles(Map<WebProfile, Set<String>> webProfiles) {
        this.webProfiles = webProfiles;
        return this;
    }

    public XingUser webProfiles(WebProfile webProfile, Set<String> profiles) {
        if (webProfiles == null) webProfiles = new LinkedHashMap<>();
        webProfiles.put(webProfile, profiles);
        return this;
    }

    public XingUser addToWebProfile(WebProfile webProfile, String accountName) {
        if (webProfiles == null) webProfiles = new LinkedHashMap<>();
        if (!webProfiles.containsKey(webProfile)) webProfiles.put(webProfile, new LinkedHashSet<String>());
        webProfiles.get(webProfile).add(accountName);
        return this;
    }

    public XingUser addAllToWebProfile(WebProfile webProfile, Set<String> profiles) {
        if (webProfiles == null) webProfiles = new LinkedHashMap<>();
        if (!webProfiles.containsKey(webProfile)) webProfiles.put(webProfile, new LinkedHashSet<String>());
        if (profiles != null) webProfiles.get(webProfile).addAll(profiles);
        return this;
    }

    public EducationalBackground educationBackground() {
        return educationBackground;
    }

    public XingUser educationBackground(EducationalBackground educationBackground) {
        this.educationBackground = educationBackground;
        return this;
    }

    public PhotoUrls photoUrls() {
        return photoUrls;
    }

    public XingUser photoUrls(PhotoUrls photoUrls) {
        this.photoUrls = photoUrls;
        return this;
    }

    public TimeZone timeZone() {
        return timeZone;
    }

    public XingUser timeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public Map<MessagingAccount, String> messagingAccounts() {
        return messagingAccounts;
    }

    public XingUser messagingAccounts(Map<MessagingAccount, String> messagingAccounts) {
        this.messagingAccounts = messagingAccounts;
        return this;
    }

    public XingUser addMessagingAccount(MessagingAccount account, String accountValue) {
        if (messagingAccounts == null) messagingAccounts = new LinkedHashMap<>();
        messagingAccounts.put(account, accountValue);
        return this;
    }

    public ProfessionalExperience professionalExperience() {
        return professionalExperience;
    }

    public XingUser professionalExperience(ProfessionalExperience professionalExperience) {
        this.professionalExperience = professionalExperience;
        return this;
    }

    public LegalInformationPreview legalInformationPreview() {
        return legalInformationPreview;
    }

    public XingUser legalInformationPreview(LegalInformationPreview legalInformationPreview) {
        this.legalInformationPreview = legalInformationPreview;
        return this;
    }

    /**
     * Returns the primary institution name. This method will try to return a primary {@link School} in case the
     * user is a student.
     */
    public String primaryInstitutionName() {
        String primaryInstitution = null;

        // Check if we have a primary company
        if (professionalExperience != null && professionalExperience.primaryCompany() != null) {
            Company company = professionalExperience.primaryCompany();
            primaryInstitution = company.name();
        }

        // If the primary company not available try to use the primary school (the user may be a student)
        if (primaryInstitution == null || primaryInstitution.isEmpty()) {
            if (educationBackground != null && educationBackground.primarySchool() != null) {
                School primarySchool = educationBackground.primarySchool();
                primaryInstitution = primarySchool.name();
            }
        }

        return primaryInstitution;
    }

    /**
     * Returns the user primary occupation. This method will also check the users education fields if the user is a
     * student.
     */
    public String primaryOccupationName() {
        String primaryOccupation = null;

        // Check if we have a primary company
        if (professionalExperience != null && professionalExperience.primaryCompany() != null) {
            Company company = professionalExperience.primaryCompany();
            primaryOccupation = company.title();
        }

        // If the primary company not available try to use the primary school (the user may be a student)
        if (primaryOccupation == null || primaryOccupation.isEmpty()) {
            if (educationBackground != null && educationBackground.primarySchool() != null) {
                School primarySchool = educationBackground.primarySchool();
                primaryOccupation = primarySchool.degree();
            }
        }

        return primaryOccupation;
    }

    /**
     * Returns {@code true} if the user is premium, otherwise {@code false}. The assumption on which this method is based,
     * is that each premium user has at least one {@linkplain PremiumService premium service} enabled.
     */
    public boolean isPremium() {
        return premiumServices != null && !premiumServices.isEmpty();
    }

    /**
     * Returns {@code true} if the user is blacklisted, otherwise {@code false}. The assumption on which this method is
     * based, is that the XING API will always return a <strong>user id</strong> if the user is not blocked or blacklisted.
     */
    public boolean isBlacklisted() {
        return id == null || id.isEmpty();
    }
}
