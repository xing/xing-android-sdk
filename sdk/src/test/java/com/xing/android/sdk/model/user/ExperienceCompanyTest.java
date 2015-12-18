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
package com.xing.android.sdk.model.user;

import android.os.Build;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

/**
 * @author david.gonzales
 */
@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = Config.NONE)
public class ExperienceCompanyTest {

    @Test(expected = IllegalArgumentException.class)
    @Ignore
    public void addLongUrlToCompany() throws Exception {
        ExperienceCompany experienceCompany = new ExperienceCompany();
//        experienceCompany.setUrl(ParserUtils.stringToUri(
//                "http://www.xing"
//                        + ".com/gdrgdgdfbdfbdffdvfdggfdgdfhdfgdfbhdfgdfhdfgdfhrdfbfdghdbnfghfdbfdfdgfdbndfgrpgoitjghreigjfgiohremgiodfhmreigomreiohgrtmgoiregmrtiohgmeriogmthioergmtiohmrtiohmtoib"));
    }

    @Test
    public void setNullUrlToCompany() throws Exception {
        ExperienceCompany experienceCompany = new ExperienceCompany();
        experienceCompany.setUrl(null);
        assertEquals(null, experienceCompany.getUrl());
    }
}
