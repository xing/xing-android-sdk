/*
 * Copyright (С) 2016 XING AG (http://xing.com/)
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

import com.squareup.moshi.JsonAdapter;
import com.xing.api.data.SafeCalendar;
import com.xing.api.data.profile.Address;
import com.xing.api.data.profile.Award;
import com.xing.api.data.profile.Badge;
import com.xing.api.data.profile.CareerLevel;
import com.xing.api.data.profile.Company;
import com.xing.api.data.profile.CompanySize;
import com.xing.api.data.profile.Discipline;
import com.xing.api.data.profile.EmploymentStatus;
import com.xing.api.data.profile.FormOfEmployment;
import com.xing.api.data.profile.Gender;
import com.xing.api.data.profile.Industry;
import com.xing.api.data.profile.Language;
import com.xing.api.data.profile.LanguageSkill;
import com.xing.api.data.profile.MessagingAccount;
import com.xing.api.data.profile.Phone;
import com.xing.api.data.profile.PhotoUrls;
import com.xing.api.data.profile.PremiumService;
import com.xing.api.data.profile.ProfileVisit;
import com.xing.api.data.profile.ProfileVisit.Type;
import com.xing.api.data.profile.School;
import com.xing.api.data.profile.TimeZone;
import com.xing.api.data.profile.WebProfile;
import com.xing.api.data.profile.XingUser;

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
        JsonAdapter<XingUser> adapter = api.moshi().adapter(XingUser.class);
        String json = file("user.json");

        // Test that the user object reflects the json.
        XingUser user = adapter.fromJson(json);

        // General info.
        assertThat(user.id()).isEqualTo("123456_abcdef");
        assertThat(user.academicTitle()).isNull();
        assertThat(user.firstName()).isEqualTo("Max");
        assertThat(user.lastName()).isEqualTo("Mustermann");
        assertThat(user.displayName()).isEqualTo("Max Mustermann");
        assertThat(user.pageName()).isEqualTo("Max_Mustermann");
        assertThat(user.permalink()).isEqualTo("https://www.xing.com/profile/Max_Mustermann");
        assertThat(user.employmentStatus()).isEqualTo(EmploymentStatus.EMPLOYEE);
        assertThat(user.gender()).isEqualTo(Gender.MALE);
        assertThat(user.birthDate()).isEqualTo(new SafeCalendar(1963, Calendar.AUGUST, 12));
        assertThat(user.activeEmail()).isEqualTo("max.mustermann@xing.com");
        assertThat(user.timeZone()).isEqualTo(new TimeZone("Europe/Copenhagen", 2.0f));
        assertThat(user.premiumServices()).containsExactly(PremiumService.SEARCH, PremiumService.PRIVATE_MESSAGES);
        assertThat(user.badges()).containsExactly(Badge.PREMIUM, Badge.MODERATOR, Badge.EXECUTIVE);
        assertThat(user.wants()).containsExactly("einen neuen Job", "android");
        assertThat(user.haves()).containsExactly("viele tolle Skills");
        assertThat(user.topHaves()).containsExactly("viele tolle top Skills");
        assertThat(user.interests()).containsExactly("Flitzebogen schießen and so on");
        assertThat(user.organizations()).containsExactly("ACM", "GI");
        assertThat(user.languages()).containsExactly(
              MapEntry.entry(Language.DE, LanguageSkill.NATIVE), MapEntry.entry(Language.EN, LanguageSkill.FLUENT),
              MapEntry.entry(Language.FR, null), MapEntry.entry(Language.ZH, LanguageSkill.BASIC));
        assertThat(user.legalInformationPreview().previewContent())
              .isEqualTo("Max Mustermann\nMuster AG\nMusterstraße 123\n22992 Musterdorf");


        // Addresses.
        assertPrivateAddress(user);
        assertBusinessAddress(user);

        // Web profiles.
        assertThat(user.webProfiles().keySet()).containsExactly(
              WebProfile.QYPE, WebProfile.GOOGLE_PLUS, WebProfile.OTHER, WebProfile.HOMEPAGE);
        assertThat(user.webProfiles().get(WebProfile.QYPE)).containsExactly("http://qype.de/users/foo");
        assertThat(user.webProfiles().get(WebProfile.GOOGLE_PLUS)).containsExactly("http://plus.google.com/foo");
        assertThat(user.webProfiles().get(WebProfile.OTHER)).containsExactly("http://blog.example.org");
        assertThat(user.webProfiles().get(WebProfile.HOMEPAGE)).containsExactly("http://example.org",
              "http://other-example.org");

        // Messaging accounts.
        assertThat(user.messagingAccounts()).containsExactly(
              MapEntry.entry(MessagingAccount.SKYPE, "1122334455"),
              MapEntry.entry(MessagingAccount.GOOGLE_TALK, "max.mustermann"));

        // Professional experience.
        assertPrimaryCompany(user);
        assertCompanies(user);
        assertThat(user.professionalExperience().awards())
              .containsExactly(new Award("Awesome Dude Of The Year", new SafeCalendar(2007), null));

        // Education background.
        assertThat(user.educationBackground().degree()).isEqualTo("MSc CE/CS");
        assertSchools(user);
        assertThat(user.educationBackground().qualifications())
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

    @Test
    public void userNoBirthday() throws Exception {
        JsonAdapter<XingUser> adapter = api.moshi().adapter(XingUser.class);
        String json = file("user_no_birthdate.json");

        // Test that the user object reflects the json.
        XingUser user = adapter.fromJson(json);
        assertThat(user.birthDate()).isNotEqualTo(new SafeCalendar(1970, Calendar.JANUARY, 1));
    }

    @Test
    public void profileVisit() throws Exception {
        JsonAdapter<ProfileVisit> adapter = api.moshi().adapter(ProfileVisit.class);
        String json = file("visit.json");

        // Test that the visit reflects the json
        ProfileVisit visit = adapter.fromJson(json);

        assertThat(visit.userId()).isEqualTo("3205259_e868c1");
        assertThat(visit.companyName()).isEqualTo("XING AG");
        assertThat(visit.visitedAtEncrypted()).isEqualTo("_GhT3Lr1lyqtubPe+TvRzKAHFmxgM34cii57r0nKJJ52=");
        assertThat(visit.reason()).isEqualTo(new ProfileVisit.Reason("Search for interests, city"));
        assertThat(visit.displayName()).isEqualTo("Matthias Männich");
        assertThat(visit.visitedAt()).isEqualTo(new SafeCalendar(2011, Calendar.JANUARY, 13));
        assertThat(visit.visitCount()).isEqualTo(1);
        assertThat(visit.gender()).isEqualTo(Gender.MALE);
        assertThat(visit.distance()).isEqualTo(0);
        assertThat(visit.type()).isEqualTo(Type.LOGGED_IN);
        assertThat(visit.sharedContacts()).isEmpty();
        assertThat(visit.jobTitle()).isEqualTo("Software Developer");

        assertPhotoUrls(visit);

        // Check that the object with all it's set fields is serializable.
        // NOTE: This is NOT fool proof and may be unreliable if a field is not java.util.Serializable
        // and is null.
        ProfileVisit clone = SerializationUtils.clone(visit);
        assertThat(clone).isEqualTo(visit);
        assertThat(clone.hashCode()).isEqualTo(visit.hashCode());
        assertThat(clone.toString()).isEqualTo(visit.toString());
    }

    private static void assertBusinessAddress(XingUser user) throws Exception {
        assertThat(user.businessAddress()).isEqualTo(
              new Address()
                    .city("Hamburg")
                    .country("DE")
                    .zipCode("20357")
                    .street("Geschäftsstraße 1a")
                    .phone(new Phone("49", "40", "1234569"))
                    .fax(new Phone("49", "40", "1234561"))
                    .mobilePhone(new Phone("49", "160", "66666661"))
                    .province("Hamburg")
                    .email("max.mustermann@xing.com"));
    }

    private static void assertPrivateAddress(XingUser user) throws Exception {
        assertThat(user.privateAddress()).isEqualTo(
              new Address()
                    .city("Hamburg")
                    .country("DE")
                    .zipCode("20357")
                    .street("Privatstraße 1")
                    .phone(new Phone("49", "40", "1234560"))
                    .fax(new Phone("", "", ""))
                    .mobilePhone(new Phone("49", "0155", "1234567"))
                    .province("Hamburg")
                    .email("max@mustermann.de"));
    }

    private static void assertPrimaryCompany(XingUser user) {
        Company primaryCompany = buildPrimaryCompany();
        assertThat(user.professionalExperience().primaryCompany()).isEqualTo(primaryCompany);
    }

    private static void assertCompanies(XingUser user) throws Exception {
        Company first = buildPrimaryCompany();

        Company second = new Company()
              .id("24_abcdef")
              .name("Ninja Ltd.")
              .title("DevOps")
              .tag("NINJA")
              .url("http://www.ninja-ltd.co.uk")
              .beginDate(new SafeCalendar(2009, Calendar.APRIL))
              .endDate(new SafeCalendar(2010, Calendar.JULY))
              .industries(Collections.singletonList(new Industry(220800, "Athletes, organisers and associations")))
              .formOfEmployment(FormOfEmployment.OWNER)
              .discipline(new Discipline("12_05e01e", "IT_AND_SOFTWARE_DEVELOPMENT"));

        Company third = new Company()
              .id("45_abcdef")
              .title("Wiss. Mitarbeiter")
              .tag("OFFIS")
              .url("http://www.uni.de")
              .beginDate(new SafeCalendar(2007))
              .endDate(new SafeCalendar(2008))
              .industries(Collections.singletonList(new Industry(20400, "Fashion and textiles")))
              .formOfEmployment(FormOfEmployment.PART_TIME_EMPLOYEE)
              .discipline(new Discipline("9_5e231a", "TEACHING_R_AND_D"));

        Company forth = new Company()
              .id("176_abcdef")
              .title("TEST NINJA")
              .companySize(CompanySize.SIZE_201_500)
              .tag("TESTCOMPANY")
              .careerLevel(CareerLevel.ENTRY_LEVEL)
              .beginDate(new SafeCalendar(1998, Calendar.DECEMBER))
              .endDate(new SafeCalendar(1999, Calendar.MAY))
              .industries(Collections.singletonList(new Industry(220800, "Athletes, organisers and associations")))
              .formOfEmployment(FormOfEmployment.INTERN);

        assertThat(user.professionalExperience().companies())
              .containsExactly(first, second, third, forth);
    }

    private static void assertSchools(XingUser user) {
        School primarySchool = new School();
        primarySchool.id("42_abcdef");
        primarySchool.name("Carl-von-Ossietzky Universtät Schellenburg");
        primarySchool.degree("MSc CE/CS");
        List<String> notes = new ArrayList<>(3);
        notes.add("CS");
        notes.add("IT");
        notes.add("Android");
        primarySchool.notes(notes);
        primarySchool.beginDate(new SafeCalendar(1998, Calendar.AUGUST));
        primarySchool.endDate(new SafeCalendar(2005, Calendar.FEBRUARY));
        assertThat(user.educationBackground().primarySchool()).isEqualTo(primarySchool);

        School otherSchool = new School();
        otherSchool.id("42_abcdef");
        otherSchool.name("Carl-von-Ossietzky Universtät Schellenburg");
        otherSchool.degree("MSc CE/CS");
        otherSchool.subject("CE");
        otherSchool.beginDate(new SafeCalendar(1998, Calendar.AUGUST));
        otherSchool.endDate(new SafeCalendar(2005, Calendar.FEBRUARY));
        assertThat(user.educationBackground().schools())
              .containsExactly(otherSchool);
    }

    private static void assertPhotoUrls(XingUser user) {
        PhotoUrls urls = new PhotoUrls()
              .photoLargeUrl("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.140x185.jpg")
              .photoMaxiThumbUrl("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.70x93.jpg")
              .photoMediumThumbUrl("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.57x75.jpg")
              .photoMiniThumbUrl("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.18x24.jpg")
              .photoThumbUrl("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.30x40.jpg")
              .photoSize32Url("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.32x32.jpg")
              .photoSize48Url("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.48x48.jpg")
              .photoSize64Url("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.64x64.jpg")
              .photoSize96Url("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.96x96.jpg")
              .photoSize128Url("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.128x128.jpg")
              .photoSize192Url("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.192x192.jpg")
              .photoSize256Url("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.256x256.jpg")
              .photoSize1024Url("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.1024x1024.jpg")
              .photoSizeOriginalUrl("http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.original.jpg");
        assertThat(user.photoUrls()).isEqualTo(urls);
    }

    private static void assertPhotoUrls(ProfileVisit visit) {
        PhotoUrls urls = new PhotoUrls()
              .photoLargeUrl("https://x1.xingassets.com/img/users/8/a/e/6a71d7eb9.6666666.jpg")
              .photoMaxiThumbUrl("https://x1.xingassets.com/img/users/8/a/e/6a71d7eb9.6666666_s3.jpg")
              .photoMediumThumbUrl("https://x1.xingassets.com/img/users/8/a/e/6a71d7eb9.6666666_s4.jpg")
              .photoMiniThumbUrl("https://x1.xingassets.com/img/users/8/a/e/6a71d7eb9.6666666_s1.jpg")
              .photoThumbUrl("https://x1.xingassets.com/img/users/8/a/e/6a71d7eb9.6666666_s2.jpg")
              .photoSize32Url("https://x1.xingassets.com/img/users/8/a/e/6a71d7eb9.6666666_s3.jpg")
              .photoSize48Url("https://x1.xingassets.com/img/users/8/a/e/6a71d7eb9.6666666_s3.jpg")
              .photoSize64Url("https://x1.xingassets.com/img/users/8/a/e/6a71d7eb9.6666666_s3.jpg")
              .photoSize96Url("https://x1.xingassets.com/img/users/8/a/e/6a71d7eb9.6666666_s3.jpg")
              .photoSize128Url("https://x1.xingassets.com/img/users/8/a/e/6a71d7eb9.6666666_s3.jpg")
              .photoSize192Url("https://x1.xingassets.com/img/users/8/a/e/6a71d7eb9.6666666_s3.jpg")
              .photoSize256Url("https://x1.xingassets.com/img/users/8/a/e/6a71d7eb9.6666666_s3.jpg")
              .photoSize1024Url("https://x1.xingassets.com/img/users/8/a/e/6a71d7eb9.6666666_s3.jpg")
              .photoSizeOriginalUrl("https://x1.xingassets.com/img/users/8/a/e/6a71d7eb9.6666666_s3.jpg");
        assertThat(visit.photoUrls()).isEqualTo(urls);
    }

    private static Company buildPrimaryCompany() {
        SafeCalendar calendar = new SafeCalendar(2010, Calendar.JANUARY);
        calendar.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));

        return new Company()
              .id("1_abcdef")
              .name("XING AG")
              .title("Softwareentwickler")
              .companySize(CompanySize.SIZE_201_500)
              .url("http://www.xing.com")
              .careerLevel(CareerLevel.PROFESSIONAL_EXPERIENCED)
              .beginDate(calendar)
              .industries(Collections.singletonList(new Industry(90700, "Internet and online media")))
              .formOfEmployment(FormOfEmployment.FULL_TIME_EMPLOYEE)
              .untilNow(true)
              .discipline(new Discipline("12_05e01e", "IT_AND_SOFTWARE_DEVELOPMENT"));
    }
}
