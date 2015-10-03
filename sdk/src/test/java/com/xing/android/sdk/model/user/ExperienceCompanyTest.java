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

package com.xing.android.sdk.model.user;

import android.os.Build;

import com.xing.android.sdk.json.ParserUtils;

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
    public void addLongUrlToCompany() throws Exception {
        ExperienceCompany experienceCompany = new ExperienceCompany();
        experienceCompany.setUrl(ParserUtils.stringToUri(
                "http://www.xing"
                        + ".com/gdrgdgdfbdfbdffdvfdggfdgdfhdfgdfbhdfgdfhdfgdfhrdfbfdghdbnfghfdbfdfdgfdbndfgrpgoitjghreigjfgiohremgiodfhmreigomreiohgrtmgoiregmrtiohgmeriogmthioergmtiohmrtiohmtoib"));
    }

    @Test
    public void setNullUrlToCompany() throws Exception {
        ExperienceCompany experienceCompany = new ExperienceCompany();
        experienceCompany.setUrl(null);
        assertEquals(null, experienceCompany.getUrl());
    }
}
