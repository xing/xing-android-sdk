package com.xing.api.data.profile;

import com.xing.api.data.SafeCalendar;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author daniel.hartwich
 */
public class XingUserTest {
    private XingUser user;

    @Before
    public void setUp() throws Exception {
        user = new XingUser()
              .addPremiumService(PremiumService.PRIVATE_MESSAGES)
              .professionalExperience(
                    new ProfessionalExperience()
                          .primaryCompany(new Company()
                                .id("1231241")
                                .name("Test Company")
                                .title("Test Worker")
                                .description("Test Description")
                                .companySize(CompanySize.SIZE_1_10)
                                .beginDate(new SafeCalendar(2013))
                                .endDate(new SafeCalendar(2016))));
    }

    @Test
    public void testPrimaryInstitutionName() throws Exception {
        String primaryInstutionName = "Test Company";
        assertThat(user.primaryInstitutionName()).isEqualTo(primaryInstutionName);
        user.professionalExperience(null);
        user.educationBackground(new EducationalBackground()
              .primarySchool(new School()
                    .id("123123")
                    .name("Universe School")
                    .beginDate(new SafeCalendar(2001))
                    .endDate(new SafeCalendar(2015))
                    .degree("Master of the Universe")
                    .subject("Universe")));
        assertThat(user.primaryInstitutionName()).isEqualTo("Universe School");
    }

    @Test
    public void testPrimaryOccupationName() throws Exception {
        String primaryOccupationName = "Test Worker";
        assertThat(user.primaryOccupationName()).isEqualTo(primaryOccupationName);
        user.professionalExperience(null);
        user.educationBackground(new EducationalBackground()
              .primarySchool(new School()
                    .id("123123")
                    .name("Universe School")
                    .beginDate(new SafeCalendar(2001))
                    .endDate(new SafeCalendar(2015))
                    .degree("Master of the Universe")
                    .subject("Universe")));
        assertThat(user.primaryOccupationName()).isEqualTo("Master of the Universe");
    }

    @Test
    public void testIsPremium() throws Exception {
        assertThat(user.isPremium()).isEqualTo(true);
        XingUser basicUser = new XingUser().premiumServices(Collections.<PremiumService>emptyList());
        assertThat(basicUser.isPremium()).isEqualTo(false);
        XingUser nullUser = new XingUser().premiumServices(null);
        assertThat(nullUser.isPremium()).isEqualTo(false);
    }

    @Test
    public void testIsBlackListed() throws Exception {
        assertThat(new XingUser().id(null).isBlacklisted()).isTrue();
        assertThat(new XingUser().id("").isBlacklisted()).isTrue();
        assertThat(new XingUser().id("some_id").isBlacklisted()).isFalse();
    }
}
