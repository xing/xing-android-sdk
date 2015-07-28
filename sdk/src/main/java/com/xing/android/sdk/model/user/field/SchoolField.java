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
 * Represents a school.
 * <p/>
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/post/users/me/educational_background/schools">School</a>
 */
@SuppressWarnings("unused")
public class SchoolField extends Field {
    public static final SchoolField ID = new SchoolField("id");
    public static final SchoolField NAME = new SchoolField("name");
    public static final SchoolField DEGREE = new SchoolField("degree");
    public static final SchoolField NOTES = new SchoolField("notes");
    public static final SchoolField SUBJECT = new SchoolField("subject");
    public static final SchoolField BEGIN_DATE = new SchoolField("begin_date");
    public static final SchoolField END_DATE = new SchoolField("end_date");

    private SchoolField(String name) {
        super(name);
    }
}
