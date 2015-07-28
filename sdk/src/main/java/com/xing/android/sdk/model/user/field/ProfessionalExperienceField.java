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

package com.xing.android.sdk.model.user.field;

import com.xing.android.sdk.json.Field;

/**
 * Represents a users professional experience.
 * <p/>
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/get/users/me">Professional Experience</a>
 */
@SuppressWarnings("unused")
public class ProfessionalExperienceField extends Field {
    public static final ProfessionalExperienceField PRIMARY_COMPANY = new ProfessionalExperienceField("primary_company");
    public static final ProfessionalExperienceField COMPANIES = new ProfessionalExperienceField("companies");
    public static final ProfessionalExperienceField AWARDS = new ProfessionalExperienceField("awards");

    private ProfessionalExperienceField(String name) {
        super(name);
    }

    public static ProfessionalExperienceField primaryCompany(ExperienceCompanyField experienceCompanyField) {
        return new ProfessionalExperienceField(PRIMARY_COMPANY + "." + experienceCompanyField);
    }

    public static ProfessionalExperienceField companies(ExperienceCompanyField experienceCompanyField) {
        return new ProfessionalExperienceField(COMPANIES + "." + experienceCompanyField);
    }

    public static ProfessionalExperienceField awards(AwardField awardField) {
        return new ProfessionalExperienceField(AWARDS + "." + awardField);
    }
}
