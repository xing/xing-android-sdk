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
 * Represents an awards fields.
 * <p/>
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/get/users/:id">User Profile</a>
 */
@SuppressWarnings("unused")
public class LanguageField extends Field {
    public static final LanguageField EN = new LanguageField("en");
    public static final LanguageField DE = new LanguageField("de");
    public static final LanguageField ES = new LanguageField("es");
    public static final LanguageField FI = new LanguageField("fi");
    public static final LanguageField FR = new LanguageField("fr");
    public static final LanguageField HU = new LanguageField("hu");
    public static final LanguageField IT = new LanguageField("it");
    public static final LanguageField JA = new LanguageField("ja");
    public static final LanguageField KO = new LanguageField("ko");
    public static final LanguageField NL = new LanguageField("nl");
    public static final LanguageField PL = new LanguageField("pl");
    public static final LanguageField PT = new LanguageField("pt");
    public static final LanguageField RU = new LanguageField("ru");
    public static final LanguageField SV = new LanguageField("sv");
    public static final LanguageField TR = new LanguageField("tr");
    public static final LanguageField ZH = new LanguageField("zh");
    public static final LanguageField RO = new LanguageField("ro");
    public static final LanguageField NO = new LanguageField("no");
    public static final LanguageField CS = new LanguageField("cs");
    public static final LanguageField EL = new LanguageField("el");
    public static final LanguageField DA = new LanguageField("da");
    public static final LanguageField AR = new LanguageField("ar");
    public static final LanguageField HE = new LanguageField("he");


    private LanguageField(String name) {
        super(name);
    }
}
