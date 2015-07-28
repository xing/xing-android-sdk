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

package com.xing.android.sdk.json.user;

import android.net.Uri;
import android.os.Build;

import com.xing.android.sdk.json.ParserUnitTestBase;
import com.xing.android.sdk.model.user.Badge;
import com.xing.android.sdk.model.user.EmploymentStatus;
import com.xing.android.sdk.model.user.Gender;
import com.xing.android.sdk.model.user.Language;
import com.xing.android.sdk.model.user.LanguageSkill;
import com.xing.android.sdk.model.user.MessagingAccount;
import com.xing.android.sdk.model.user.PremiumService;
import com.xing.android.sdk.model.user.WebProfile;
import com.xing.android.sdk.model.user.XingUser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author serj.lotutovici
 */
@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = Config.NONE
)
public class XingUserMapperTest extends ParserUnitTestBase {

    private static final String TEST_XING_USER = "{\n" +
            "      \"id\": \"123456_abcdef\",\n" +
            "      \"first_name\": \"Max\",\n" +
            "      \"last_name\": \"Mustermann\",\n" +
            "      \"display_name\": \"Max Mustermann\",\n" +
            "      \"page_name\": \"Max_Mustermann\",\n" +
            "      \"permalink\": \"https://www.xing.com/profile/Max_Mustermann\",\n" +
            "      \"employment_status\": \"EMPLOYEE\",\n" +
            "      \"gender\": \"m\",\n" +
            "      \"birth_date\": {\n" +
            "        \"day\": 12,\n" +
            "        \"month\": 8,\n" +
            "        \"year\": 1963\n" +
            "      },\n" +
            "      \"active_email\": \"max.mustermann@xing.com\",\n" +
            "      \"time_zone\": {\n" +
            "        \"name\": \"Europe/Copenhagen\",\n" +
            "        \"utc_offset\": 2.0\n" +
            "      },\n" +
            "      \"premium_services\": [\n" +
            "        \"SEARCH\",\n" +
            "        \"PRIVATEMESSAGES\"\n" +
            "      ],\n" +
            "      \"badges\": [\n" +
            "        \"PREMIUM\",\n" +
            "        \"MODERATOR\"\n" +
            "      ],\n" +
            "      \"wants\": \"einen neuen Job\",\n" +
            "      \"haves\": \"viele tolle Skills\",\n" +
            "      \"interests\": \"Flitzebogen schießen and so on\",\n" +
            "      \"organisation_member\": \"ACM, GI\",\n" +
            "      \"languages\": {\n" +
            "        \"de\": \"NATIVE\",\n" +
            "        \"en\": \"FLUENT\",\n" +
            "        \"fr\": null,\n" +
            "        \"zh\": \"BASIC\"\n" +
            "      },\n" +
            "      \"private_address\": {\n" +
            "        \"city\": \"Hamburg\",\n" +
            "        \"country\": \"DE\",\n" +
            "        \"zip_code\": \"20357\",\n" +
            "        \"street\": \"Privatstraße 1\",\n" +
            "        \"phone\": \"49|40|1234560\",\n" +
            "        \"fax\": \"||\",\n" +
            "        \"province\": \"Hamburg\",\n" +
            "        \"email\": \"max@mustermann.de\",\n" +
            "        \"mobile_phone\": \"49|0155|1234567\"\n" +
            "      },\n" +
            "      \"business_address\": {\n" +
            "        \"city\": \"Hamburg\",\n" +
            "        \"country\": \"DE\",\n" +
            "        \"zip_code\": \"20357\",\n" +
            "        \"street\": \"Geschäftsstraße 1a\",\n" +
            "        \"phone\": \"49|40|1234569\",\n" +
            "        \"fax\": \"49|40|1234561\",\n" +
            "        \"province\": \"Hamburg\",\n" +
            "        \"email\": \"max.mustermann@xing.com\",\n" +
            "        \"mobile_phone\": \"49|160|66666661\"\n" +
            "      },\n" +
            "      \"web_profiles\": {\n" +
            "        \"qype\": [\n" +
            "          \"http://qype.de/users/foo\"\n" +
            "        ],\n" +
            "        \"google+\": [\n" +
            "          \"http://plus.google.com/foo\"\n" +
            "        ],\n" +
            "        \"other\": [\n" +
            "          \"http://blog.example.org\"\n" +
            "        ],\n" +
            "        \"homepage\": [\n" +
            "          \"http://example.org\",\n" +
            "          \"http://other-example.org\"\n" +
            "        ]\n" +
            "      },\n" +
            "      \"instant_messaging_accounts\": {\n" +
            "        \"skype\": \"1122334455\",\n" +
            "        \"googletalk\": \"max.mustermann\"\n" +
            "      },\n" +
            "      \"professional_experience\": {\n" +
            "        \"primary_company\": {\n" +
            "          \"id\": \"1_abcdef\",\n" +
            "          \"name\": \"XING AG\",\n" +
            "          \"title\": \"Softwareentwickler\",\n" +
            "          \"company_size\": \"201-500\",\n" +
            "          \"tag\": null,\n" +
            "          \"url\": \"http://www.xing.com\",\n" +
            "          \"career_level\": \"PROFESSIONAL_EXPERIENCED\",\n" +
            "          \"begin_date\": \"2010-01\",\n" +
            "          \"description\": null,\n" +
            "          \"end_date\": null,\n" +
            "          \"industry\": \"AEROSPACE\",\n" +
            "          \"form_of_employment\": \"FULL_TIME_EMPLOYEE\",\n" +
            "          \"until_now\": true\n" +
            "        },\n" +
            "        \"companies\": [\n" +
            "          {\n" +
            "            \"id\": \"1_abcdef\",\n" +
            "            \"name\": \"XING AG\",\n" +
            "            \"title\": \"Softwareentwickler\",\n" +
            "            \"company_size\": \"201-500\",\n" +
            "            \"tag\": null,\n" +
            "            \"url\": \"http://www.xing.com\",\n" +
            "            \"career_level\": \"PROFESSIONAL_EXPERIENCED\",\n" +
            "            \"begin_date\": \"2010-01\",\n" +
            "            \"description\": null,\n" +
            "            \"end_date\": null,\n" +
            "            \"industry\": \"AEROSPACE\",\n" +
            "            \"form_of_employment\": \"FULL_TIME_EMPLOYEE\",\n" +
            "            \"until_now\": true\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"24_abcdef\",\n" +
            "            \"name\": \"Ninja Ltd.\",\n" +
            "            \"title\": \"DevOps\",\n" +
            "            \"company_size\": null,\n" +
            "            \"tag\": \"NINJA\",\n" +
            "            \"url\": \"http://www.ninja-ltd.co.uk\",\n" +
            "            \"career_level\": null,\n" +
            "            \"begin_date\": \"2009-04\",\n" +
            "            \"description\": null,\n" +
            "            \"end_date\": \"2010-07\",\n" +
            "            \"industry\": \"ALTERNATIVE_MEDICINE\",\n" +
            "            \"form_of_employment\": \"OWNER\",\n" +
            "            \"until_now\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"45_abcdef\",\n" +
            "            \"name\": null,\n" +
            "            \"title\": \"Wiss. Mitarbeiter\",\n" +
            "            \"company_size\": null,\n" +
            "            \"tag\": \"OFFIS\",\n" +
            "            \"url\": \"http://www.uni.de\",\n" +
            "            \"career_level\": null,\n" +
            "            \"begin_date\": \"2007\",\n" +
            "            \"description\": null,\n" +
            "            \"end_date\": \"2008\",\n" +
            "            \"industry\": \"APPAREL_AND_FASHION\",\n" +
            "            \"form_of_employment\": \"PART_TIME_EMPLOYEE\",\n" +
            "            \"until_now\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"176_abcdef\",\n" +
            "            \"name\": null,\n" +
            "            \"title\": \"TEST NINJA\",\n" +
            "            \"company_size\": \"201-500\",\n" +
            "            \"tag\": \"TESTCOMPANY\",\n" +
            "            \"url\": null,\n" +
            "            \"career_level\": \"ENTRY_LEVEL\",\n" +
            "            \"begin_date\": \"1998-12\",\n" +
            "            \"description\": null,\n" +
            "            \"end_date\": \"1999-05\",\n" +
            "            \"industry\": \"ARTS_AND_CRAFTS\",\n" +
            "            \"form_of_employment\": \"INTERN\",\n" +
            "            \"until_now\": false\n" +
            "          }\n" +
            "        ],\n" +
            "        \"awards\": [\n" +
            "          {\n" +
            "            \"name\": \"Awesome Dude Of The Year\",\n" +
            "            \"date_awarded\": 2007,\n" +
            "            \"url\": null\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      \"educational_background\": {\n" +
            "        \"degree\": \"MSc CE/CS\",\n" +
            "        \"primary_school\": {\n" +
            "          \"id\": \"42_abcdef\",\n" +
            "          \"name\": \"Carl-von-Ossietzky Universtät Schellenburg\",\n" +
            "          \"degree\": \"MSc CE/CS\",\n" +
            "          \"notes\": null,\n" +
            "          \"subject\": null,\n" +
            "          \"begin_date\": \"1998-08\",\n" +
            "          \"end_date\": \"2005-02\"\n" +
            "        },\n" +
            "        \"schools\": [\n" +
            "          {\n" +
            "            \"id\": \"42_abcdef\",\n" +
            "            \"name\": \"Carl-von-Ossietzky Universtät Schellenburg\",\n" +
            "            \"degree\": \"MSc CE/CS\",\n" +
            "            \"notes\": null,\n" +
            "            \"subject\": null,\n" +
            "            \"begin_date\": \"1998-08\",\n" +
            "            \"end_date\": \"2005-02\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"qualifications\": [\n" +
            "          \"TOEFLS\",\n" +
            "          \"PADI AOWD\"\n" +
            "        ]\n" +
            "      },\n" +
            "      \"photo_urls\": {\n" +
            "        \"large\": \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.140x185.jpg\",\n" +
            "        \"maxi_thumb\": \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.70x93.jpg\",\n" +
            "        \"medium_thumb\": \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.57x75.jpg\",\n" +
            "        \"mini_thumb\": \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.18x24.jpg\",\n" +
            "        \"thumb\": \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.30x40.jpg\",\n" +
            "        \"size_32x32\": \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.32x32.jpg\",\n" +
            "        \"size_48x48\": \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.48x48.jpg\",\n" +
            "        \"size_64x64\": \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.64x64.jpg\",\n" +
            "        \"size_96x96\": \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.96x96.jpg\",\n" +
            "        \"size_128x128\": \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.128x128.jpg\",\n" +
            "        \"size_192x192\": \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.192x192.jpg\",\n" +
            "        \"size_256x256\": \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.256x256.jpg\",\n" +
            "        \"size_1024x1024\": \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.1024x1024.jpg\",\n" +
            "        \"size_original\": \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.original.jpg\"\n" +
            "      }\n" +
            "    }";

    private static final String TEST_XING_USER_SMALL = "{\n" +
            "      \"id\": \"123456_ab3ef\",\n" +
            "      \"display_name\": \"Serj Takien\",\n" +
            "      \"active_email\": \"takien.at@xing.com\",\n" +
            "      \"professional_experience\": {\n" +
            "        \"primary_company\": {\n" +
            "          \"id\": \"31985131_9a9eb2\",\n" +
            "          \"name\": \"XING AG\",\n" +
            "          \"url\": \"https://www.xing.com/\",\n" +
            "          \"tag\": \"XINGAG\",\n" +
            "          \"title\": \"Android Developer\",\n" +
            "          \"begin_date\": \"2014-12\",\n" +
            "          \"end_date\": null,\n" +
            "          \"description\": null,\n" +
            "          \"until_now\": true,\n" +
            "          \"industry\": \"COMPUTER_SOFTWARE\",\n" +
            "          \"company_size\": \"501-1000\",\n" +
            "          \"career_level\": \"PROFESSIONAL_EXPERIENCED\",\n" +
            "          \"form_of_employment\": \"FULL_TIME_EMPLOYEE\"\n" +
            "        }\n" +
            "      }\n" +
            "    }";

    private static final String TEST_XING_USER_STUDENT = "{\n" +
            "   \"id\" : \"12hu56_a23nu89ef\",\n" +
            "   \"display_name\" : \"Alex Black\",\n" +
            "   \"active_email\" : \"alex.black@black.com\",\n" +
            "   \"professional_experience\" :\n" +
            "      {\n" +
            "         \"primary_company\" :\n" +
            "            {\n" +
            "               \"id\" : null,\n" +
            "               \"name\" : null,\n" +
            "               \"url\" : null,\n" +
            "               \"tag\" : null,\n" +
            "               \"title\" : null,\n" +
            "               \"begin_date\" : null,\n" +
            "               \"end_date\" : null,\n" +
            "               \"description\" : null,\n" +
            "               \"until_now\" : null,\n" +
            "               \"industry\" : null,\n" +
            "               \"company_size\" : null,\n" +
            "               \"career_level\" : null,\n" +
            "               \"form_of_employment\" : null\n" +
            "            }\n" +
            "      },\n" +
            "   \"educational_background\" :\n" +
            "      {\n" +
            "         \"primary_school\" :\n" +
            "            {\n" +
            "               \"id\" : \"42_abcdef\",\n" +
            "               \"name\" : \"SkyNet University\",\n" +
            "               \"degree\" : \"MSc Applied Physics\",\n" +
            "               \"notes\" : \"Main field of study time travel\",\n" +
            "               \"subject\" : null,\n" +
            "               \"begin_date\" : \"1998-08\",\n" +
            "               \"end_date\" : \"2005-02\"\n" +
            "            }\n" +
            "      }\n" +
            "}";

    @Test
    public void parseUserFromJson() throws Exception {
        XingUser user = XingUserMapper.parseXingUser(getReaderForJson(TEST_XING_USER));
        assertNotNull(user);
        assertEquals(user.getId(), "123456_abcdef");
        assertEquals(user.getFirstName(), "Max");
        assertEquals(user.getLastName(), "Mustermann");
        assertEquals(user.getPageName(), "Max_Mustermann");
        assertEquals(user.getDisplayName(), "Max Mustermann");
        assertEquals(user.getPermalink(), Uri.parse("https://www.xing.com/profile/Max_Mustermann"));
        assertEquals(user.getActiveEmail(), "max.mustermann@xing.com");
        assertEquals(user.getEmploymentStatus(), EmploymentStatus.EMPLOYEE);
        assertEquals(user.getGender(), Gender.MALE);

        // Check birth date
        assertNotNull(user.getBirthday());
        assertEquals(12, user.getBirthday().get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.AUGUST, user.getBirthday().get(Calendar.MONTH));
        assertEquals(1963, user.getBirthday().get(Calendar.YEAR));

        // Check time zone
        assertNotNull(user.getTimeZone());
        assertEquals("Europe/Copenhagen", user.getTimeZone().getName());
        assertEquals(2.0, user.getTimeZone().getUtcOffset(), 0.0);

        // Check premium services
        assertNotNull(user.getPremiumServices());
        assertEquals(user.getPremiumServices().size(), 2);
        assertTrue(user.getPremiumServices().contains(PremiumService.SEARCH));
        assertTrue(user.getPremiumServices().contains(PremiumService.PRIVATE_MESSAGES));

        // Check badges
        assertNotNull(user.getBadges());
        assertEquals(user.getBadges().size(), 2);
        assertTrue(user.getBadges().contains(Badge.PREMIUM));
        assertTrue(user.getBadges().contains(Badge.MODERATOR));

        // Check languages (this removes the need for a separate test for LanguageParser)
        assertNotNull(user.getLanguages());
        assertEquals(user.getLanguages().size(), 4);
        assertEquals(user.getLanguages().get(Language.DE), LanguageSkill.NATIVE);
        assertEquals(user.getLanguages().get(Language.EN), LanguageSkill.FLUENT);
        assertEquals(user.getLanguages().get(Language.FR), null);

        // Check web profiles
        assertNotNull(user.getWebProfiles());
        assertEquals(user.getWebProfiles().size(), 4);
        assertNotNull(user.getWebProfiles().get(WebProfile.QYPE));
        assertEquals(user.getWebProfiles().get(WebProfile.QYPE).iterator().next(), "http://qype.de/users/foo");
        assertEquals(user.getWebProfiles().get(WebProfile.HOMEPAGE).size(), 2);

        // Check instance messaging profiles
        assertNotNull(user.getInstantMessagingAccounts());
        assertTrue(user.getInstantMessagingAccounts().containsKey(MessagingAccount.SKYPE));
        assertTrue(user.getInstantMessagingAccounts().containsKey(MessagingAccount.GOOGLE_TALK));
        assertEquals("max.mustermann", user.getInstantMessagingAccounts().get(MessagingAccount.GOOGLE_TALK));

        // Check that some objects are just not null
        // The proper reading of those objects should be done in separated classes
        assertNotNull(user.getProfessionalExperience());
        assertNotNull(user.getEducationBackground());
        assertNotNull(user.getPhotoUrls());
        assertNotNull(user.getBusinessAddress());
        assertNotNull(user.getPrivateAddress());

        // Check small stuff
        assertNotNull(user.getWants());
        assertEquals(1, user.getWants().size());
        assertEquals("einen neuen Job", user.getWants().get(0));
        assertNotNull(user.getHaves());
        assertEquals(1, user.getHaves().size());
        assertEquals("viele tolle Skills", user.getHaves().get(0));
        assertNotNull(user.getInterests());
        assertEquals(1, user.getInterests().size());
        assertEquals("Flitzebogen schießen and so on", user.getInterests().get(0));
        assertNotNull(user.getOrganisationMember());
        assertEquals(2, user.getOrganisationMember().size());
        assertEquals("ACM", user.getOrganisationMember().get(0));
    }

    @Test
    public void parseSmallUserFromJson() throws Exception {
        XingUser user = XingUserMapper.parseXingUser(getReaderForJson(TEST_XING_USER_SMALL));
        assertNotNull(user);
        assertEquals("123456_ab3ef", user.getId());
        assertEquals("Serj Takien", user.getDisplayName());
        assertEquals("takien.at@xing.com", user.getActiveEmail());

        assertNotNull(user.getProfessionalExperience());
        assertNotNull(user.getProfessionalExperience().getPrimaryCompany());
        assertEquals("31985131_9a9eb2", user.getProfessionalExperience().getPrimaryCompany().getId());

        assertEquals("XING AG", user.getPrimaryInstitutionName());
        assertEquals("Android Developer", user.getPrimaryOccupationName());
    }

    @Test
    public void parseStudentUserFormJson() throws Exception {
        XingUser user = XingUserMapper.parseXingUser(getReaderForJson(TEST_XING_USER_STUDENT));
        assertNotNull(user);
        assertEquals("12hu56_a23nu89ef", user.getId());
        assertEquals("Alex Black", user.getDisplayName());
        assertEquals("alex.black@black.com", user.getActiveEmail());

        assertNotNull(user.getProfessionalExperience());
        assertNotNull(user.getProfessionalExperience().getPrimaryCompany());
        assertNull(user.getProfessionalExperience().getPrimaryCompany().getId());

        assertNotNull(user.getEducationBackground());
        assertNotNull(user.getEducationBackground().getPrimarySchool());
        assertNotNull(user.getEducationBackground().getPrimarySchool().getId());

        assertEquals("SkyNet University", user.getPrimaryInstitutionName());
        assertEquals("MSc Applied Physics", user.getPrimaryOccupationName());
    }

    @Test
    public void parseUserAndPassItThroughParcelFlow() throws Exception {
        // Read user from json
        // The accuracy of the parser should be tested in another method
        XingUser user = XingUserMapper.parseXingUser(getReaderForJson(TEST_XING_USER));
        assertNotNull(user);

        // Create copy object via the parcel flow
        XingUser userFromParcel = createNewObjectViaParcelFlow(user, XingUser.CREATOR);
        assertEquals(user.hashCode(), userFromParcel.hashCode());
        assertTrue(user.equals(userFromParcel));
    }

}
