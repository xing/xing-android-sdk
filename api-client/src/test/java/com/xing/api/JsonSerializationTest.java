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
package com.xing.api;

import android.support.annotation.NonNull;

import com.squareup.moshi.JsonAdapter;
import com.xing.api.model.SafeCalendar;
import com.xing.api.model.user.Address;
import com.xing.api.model.user.Award;
import com.xing.api.model.user.Badge;
import com.xing.api.model.user.CareerLevel;
import com.xing.api.model.user.Company;
import com.xing.api.model.user.CompanySize;
import com.xing.api.model.user.Discipline;
import com.xing.api.model.user.EmploymentStatus;
import com.xing.api.model.user.FormOfEmployment;
import com.xing.api.model.user.Gender;
import com.xing.api.model.user.Industry;
import com.xing.api.model.user.Language;
import com.xing.api.model.user.LanguageSkill;
import com.xing.api.model.user.MessagingAccount;
import com.xing.api.model.user.Phone;
import com.xing.api.model.user.PhotoUrls;
import com.xing.api.model.user.PremiumService;
import com.xing.api.model.user.School;
import com.xing.api.model.user.TimeZone;
import com.xing.api.model.user.WebProfile;
import com.xing.api.model.user.XingUser;

import org.apache.commons.lang3.SerializationUtils;
import org.assertj.core.data.MapEntry;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.xing.api.TestUtils.file;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test all main objects against a "golden" json response. This makes sure that all expected values are parsed and
 * assigned correctly.
 */
public class JsonSerializationTest {
    // XingApi contains all custom json adapters.
    private final XingApi api = new XingApi.Builder().loggedOut().build();

    @Test
    public void user() throws Exception {
        JsonAdapter<XingUser> adapter = api.converter.adapter(XingUser.class);
        String json = file("user.json");

        // Test that the user object reflects the json.
        XingUser user = adapter.fromJson(json);

        // General info.
        assertThat(user.getId()).isEqualTo("123456_abcdef");
        assertThat(user.getAcademicTitle()).isNull();
        assertThat(user.getFirstName()).isEqualTo("Max");
        assertThat(user.getLastName()).isEqualTo("Mustermann");
        assertThat(user.getDisplayName()).isEqualTo("Max Mustermann");
        assertThat(user.getPageName()).isEqualTo("Max_Mustermann");
        assertThat(user.getPermalink()).isEqualTo("https://www.xing.com/profile/Max_Mustermann");
        assertThat(user.getEmploymentStatus()).isEqualTo(EmploymentStatus.EMPLOYEE);
        assertThat(user.getGender()).isEqualTo(Gender.MALE);
        assertThat(user.getBirthDate()).isEqualTo(new SafeCalendar(1963, Calendar.AUGUST, 12));
        assertThat(user.getActiveEmail()).isEqualTo("max.mustermann@xing.com");
        assertThat(user.getTimeZone()).isEqualTo(new TimeZone("Europe/Copenhagen", 2.0f));
        assertThat(user.getPremiumServices()).containsExactly(PremiumService.SEARCH, PremiumService.PRIVATE_MESSAGES);
        assertThat(user.getBadges()).containsExactly(Badge.PREMIUM, Badge.MODERATOR);
        assertThat(user.getWants()).containsExactly("einen neuen Job");
        assertThat(user.getHaves()).containsExactly("viele tolle Skills");
        assertThat(user.getInterests()).containsExactly("Flitzebogen schießen and so on");
        assertThat(user.getOrganizations()).containsExactly("ACM", "GI");
        assertThat(user.getLanguages()).containsExactly(
              MapEntry.entry(Language.DE, LanguageSkill.NATIVE), MapEntry.entry(Language.EN, LanguageSkill.FLUENT),
              MapEntry.entry(Language.FR, null), MapEntry.entry(Language.ZH, LanguageSkill.BASIC));

        // Addresses.
        assertPrivateAddress(user);
        assertBusinessAddress(user);

        // Web profiles.
        assertThat(user.getWebProfiles().keySet()).containsExactly(
              WebProfile.QYPE, WebProfile.GOOGLE_PLUS, WebProfile.OTHER, WebProfile.HOMEPAGE);
        assertThat(user.getWebProfiles().get(WebProfile.QYPE)).containsExactly("http://qype.de/users/foo");
        assertThat(user.getWebProfiles().get(WebProfile.GOOGLE_PLUS)).containsExactly("http://plus.google.com/foo");
        assertThat(user.getWebProfiles().get(WebProfile.OTHER)).containsExactly("http://blog.example.org");
        assertThat(user.getWebProfiles().get(WebProfile.HOMEPAGE)).containsExactly("http://example.org",
              "http://other-example.org");

        // Messaging accounts.
        assertThat(user.getMessagingAccounts()).containsExactly(
              MapEntry.entry(MessagingAccount.SKYPE, "1122334455"),
              MapEntry.entry(MessagingAccount.GOOGLE_TALK, "max.mustermann"));

        // Professional experience.
        assertPrimaryCompany(user);
        assertCompanies(user);
        assertThat(user.getProfessionalExperience().getAwards())
              .containsExactly(new Award("Awesome Dude Of The Year", new SafeCalendar(2007), null));

        // Education background.
        assertThat(user.getEducationBackground().getDegree()).isEqualTo("MSc CE/CS");
        assertSchools(user);
        assertThat(user.getEducationBackground().getQualifications())
              .containsExactly("TOEFLS", "PADI AOWD");

        // Photos.
        assertPhotoUrls(user);

        // Check that the object with all it's set fields is serializable.
        // NOTE: This is NOT fool proof and may be unreliable if a field is not java.util.Serializable
        // and is null.
        XingUser clone = SerializationUtils.clone(user);
        assertThat(clone).isEqualTo(user);
        assertThat(clone.hashCode()).isEqualTo(user.hashCode());
        assertThat(clone.toString()).isEqualTo(user.toString());
    }

    private static void assertBusinessAddress(XingUser user) throws Exception {
        Address businessAddress = new Address();
        businessAddress.setCity("Hamburg");
        businessAddress.setCountry("DE");
        businessAddress.setZipCode("20357");
        businessAddress.setStreet("Geschäftsstraße 1a");
        businessAddress.setPhone(new Phone("49", "40", "1234569"));
        businessAddress.setFax(new Phone("49", "40", "1234561"));
        businessAddress.setMobilePhone(new Phone("49", "160", "66666661"));
        businessAddress.setProvince("Hamburg");
        businessAddress.setEmail("max.mustermann@xing.com");
        assertThat(user.getBusinessAddress()).isEqualTo(businessAddress);
    }

    private static void assertPrivateAddress(XingUser user) throws Exception {
        Address privateAddress = new Address();
        privateAddress.setCity("Hamburg");
        privateAddress.setCountry("DE");
        privateAddress.setZipCode("20357");
        privateAddress.setStreet("Privatstraße 1");
        privateAddress.setPhone(new Phone("49", "40", "1234560"));
        privateAddress.setFax(new Phone("", "", ""));
        privateAddress.setMobilePhone(new Phone("49", "0155", "1234567"));
        privateAddress.setProvince("Hamburg");
        privateAddress.setEmail("max@mustermann.de");
        assertThat(user.getPrivateAddress()).isEqualTo(privateAddress);
    }

    private static void assertPrimaryCompany(XingUser user) {
        Company primaryCompany = buildPrimaryCompany();
        assertThat(user.getProfessionalExperience().getPrimaryCompany()).isEqualTo(primaryCompany);
    }

    private static void assertCompanies(XingUser user) throws Exception {
        Company first = buildPrimaryCompany();

        Company second = new Company();
        second.setId("24_abcdef");
        second.setName("Ninja Ltd.");
        second.setTitle("DevOps");
        second.setTag("NINJA");
        second.setUrl("http://www.ninja-ltd.co.uk");
        second.setBeginDate(new SafeCalendar(2009, Calendar.APRIL));
        second.setEndDate(new SafeCalendar(2010, Calendar.JULY));
        second.setIndustries(Collections.singletonList(new Industry(220800, "Athletes, organisers and associations")));
        second.setFormOfEmployment(FormOfEmployment.OWNER);
        second.setDiscipline(new Discipline("12_05e01e", "IT_AND_SOFTWARE_DEVELOPMENT"));

        Company third = new Company();
        third.setId("45_abcdef");
        third.setTitle("Wiss. Mitarbeiter");
        third.setTag("OFFIS");
        third.setUrl("http://www.uni.de");
        third.setBeginDate(new SafeCalendar(2007));
        third.setEndDate(new SafeCalendar(2008));
        third.setIndustries(Collections.singletonList(new Industry(20400, "Fashion and textiles")));
        third.setFormOfEmployment(FormOfEmployment.PART_TIME_EMPLOYEE);
        third.setDiscipline(new Discipline("9_5e231a", "TEACHING_R_AND_D"));

        Company forth = new Company();
        forth.setId("176_abcdef");
        forth.setTitle("TEST NINJA");
        forth.setCompanySize(CompanySize.SIZE_201_500);
        forth.setTag("TESTCOMPANY");
        forth.setCareerLevel(CareerLevel.ENTRY_LEVEL);
        forth.setBeginDate(new SafeCalendar(1998, Calendar.DECEMBER));
        forth.setEndDate(new SafeCalendar(1999, Calendar.MAY));
        forth.setIndustries(Collections.singletonList(new Industry(220800, "Athletes, organisers and associations")));
        forth.setFormOfEmployment(FormOfEmployment.INTERN);

        assertThat(user.getProfessionalExperience().getCompanies())
              .containsExactly(first, second, third, forth);
    }

    private static void assertSchools(XingUser user) {
        School primarySchool = new School();
        primarySchool.setId("42_abcdef");
        primarySchool.setName("Carl-von-Ossietzky Universtät Schellenburg");
        primarySchool.setDegree("MSc CE/CS");
        List<String> notes = new ArrayList<>();
        notes.add("CS");
        notes.add("IT");
        notes.add("Android");
        primarySchool.setNotes(notes);
        primarySchool.setBeginDate(new SafeCalendar(1998, Calendar.AUGUST));
        primarySchool.setEndDate(new SafeCalendar(2005, Calendar.FEBRUARY));
        assertThat(user.getEducationBackground().getPrimarySchool()).isEqualTo(primarySchool);

        School otherSchool = new School();
        otherSchool.setId("42_abcdef");
        otherSchool.setName("Carl-von-Ossietzky Universtät Schellenburg");
        otherSchool.setDegree("MSc CE/CS");
        otherSchool.setSubject("CE");
        otherSchool.setBeginDate(new SafeCalendar(1998, Calendar.AUGUST));
        otherSchool.setEndDate(new SafeCalendar(2005, Calendar.FEBRUARY));
        assertThat(user.getEducationBackground().getSchools())
              .containsExactly(otherSchool);
    }

    private static void assertPhotoUrls(XingUser user) {
        PhotoUrls urls = new PhotoUrls();
        urls.setPhotoLargeUrl("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.140x185.jpg");
        urls.setPhotoMaxiThumbUrl("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.70x93.jpg");
        urls.setPhotoMediumThumbUrl("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.57x75.jpg");
        urls.setPhotoMiniThumbUrl("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.18x24.jpg");
        urls.setPhotoThumbUrl("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.30x40.jpg");
        urls.setPhotoSize32Url("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.32x32.jpg");
        urls.setPhotoSize48Url("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.48x48.jpg");
        urls.setPhotoSize64Url("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.64x64.jpg");
        urls.setPhotoSize96Url("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.96x96.jpg");
        urls.setPhotoSize128Url("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.128x128.jpg");
        urls.setPhotoSize192Url("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.192x192.jpg");
        urls.setPhotoSize256Url("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.256x256.jpg");
        urls.setPhotoSize1024Url("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.1024x1024.jpg");
        urls.setPhotoSizeOriginalUrl("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.original.jpg");
        assertThat(user.getPhotoUrls()).isEqualTo(urls);
    }

    @NonNull
    private static Company buildPrimaryCompany() {
        Company primaryCompany = new Company();
        primaryCompany.setId("1_abcdef");
        primaryCompany.setName("XING AG");
        primaryCompany.setTitle("Softwareentwickler");
        primaryCompany.setCompanySize(CompanySize.SIZE_201_500);
        primaryCompany.setUrl("http://www.xing.com");
        primaryCompany.setCareerLevel(CareerLevel.PROFESSIONAL_EXPERIENCED);
        primaryCompany.setBeginDate(new SafeCalendar(2010, Calendar.JANUARY));
        primaryCompany.setIndustries(Collections.singletonList(new Industry(90700, "Internet and online media")));
        primaryCompany.setFormOfEmployment(FormOfEmployment.FULL_TIME_EMPLOYEE);
        primaryCompany.setUntilNow(true);
        primaryCompany.setDiscipline(new Discipline("12_05e01e", "IT_AND_SOFTWARE_DEVELOPMENT"));
        return primaryCompany;
    }
}
