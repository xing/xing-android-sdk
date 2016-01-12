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

import org.junit.Test;

import java.util.Collections;

import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/** Only test methods that represent some functionality. */
public class CompanyTest {
    @Test
    public void setNameThrowsIfLimitExited() throws Exception {
        Company company = new Company();
        try {
            company.name(leftPad("longName", Company.LIMIT_NAME + 1, '*'));
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Argument Name too long. "
                  + Company.LIMIT_NAME
                  + " characters is the maximum.");
        }
    }

    @Test
    public void setTitleThrowsIfLimitExited() throws Exception {
        Company company = new Company();
        try {
            company.title(leftPad("longTitle", Company.LIMIT_TITLE + 1, '*'));
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Argument Title too long. "
                  + Company.LIMIT_TITLE
                  + " characters is the maximum.");
        }
    }

    @Test
    public void setUrlThrowsIfLimitExited() throws Exception {
        Company company = new Company();
        try {
            company.url(leftPad("longUrl", Company.LIMIT_URL + 1, '*'));
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Argument Url too long. "
                  + Company.LIMIT_URL
                  + " characters is the maximum.");
        }
    }

    @Test
    public void setDescriptionThrowsIfLimitExited() throws Exception {
        Company company = new Company();
        try {
            company.description(leftPad("longDescription", Company.LIMIT_DESCRIPTION + 1, '*'));
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Argument Description too long. "
                  + Company.LIMIT_DESCRIPTION
                  + " characters is the maximum.");
        }
    }

    @Test
    public void companyReadyToBeAdded() throws Exception {
        Company company = new Company();
        assertFalse(company.isFilledForAddCompany());

        company.name("Test Name");
        assertFalse(company.isFilledForAddCompany());

        company.title("Test Title");
        assertFalse(company.isFilledForAddCompany());

        company.industries(Collections.singletonList(new Industry(22202, "TEST_IND")));
        assertFalse(company.isFilledForAddCompany());

        company.formOfEmployment(FormOfEmployment.PARTNER);
        assertTrue(company.isFilledForAddCompany());
    }
}
