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
 * Represents an Experience Company fields.
 * <p/>
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/get/users/:id">User Profile</a>
 */
@SuppressWarnings("unused")
public class ExperienceCompanyField extends Field {
    public static final ExperienceCompanyField ID = new ExperienceCompanyField("id");
    public static final ExperienceCompanyField BEGIN_DATE = new ExperienceCompanyField("begin_date");
    public static final ExperienceCompanyField CAREER_LEVEL = new ExperienceCompanyField("career_level");
    public static final ExperienceCompanyField COMPANY_SIZE = new ExperienceCompanyField("company_size");
    public static final ExperienceCompanyField DESCRIPTION = new ExperienceCompanyField("description");
    public static final ExperienceCompanyField END_DATE = new ExperienceCompanyField("end_date");
    public static final ExperienceCompanyField FORM_OF_EMPLOYMENT = new ExperienceCompanyField("form_of_employment");
    public static final ExperienceCompanyField INDUSTRY = new ExperienceCompanyField("industry");
    public static final ExperienceCompanyField NAME = new ExperienceCompanyField("name");
    public static final ExperienceCompanyField TITLE = new ExperienceCompanyField("title");
    public static final ExperienceCompanyField TAG = new ExperienceCompanyField("tag");
    public static final ExperienceCompanyField UNTIL_NOW = new ExperienceCompanyField("until_now");
    public static final ExperienceCompanyField URL = new ExperienceCompanyField("url");

    private ExperienceCompanyField(String name) {
        super(name);
    }
}
