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
 * Represents the address of a user.
 *
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/put/users/me/private_address">Phone</a>
 */
@SuppressWarnings("unused")
public final class XingAddressField extends Field {

    public static final XingAddressField CITY = new XingAddressField("city");
    public static final XingAddressField COUNTRY = new XingAddressField("country");
    public static final XingAddressField EMAIL = new XingAddressField("email");
    public static final XingAddressField FAX = new XingAddressField("fax");
    public static final XingAddressField MOBILE_PHONE = new XingAddressField("mobile_phone");
    public static final XingAddressField PHONE = new XingAddressField("phone");
    public static final XingAddressField PROVINCE = new XingAddressField("province");
    public static final XingAddressField STREET = new XingAddressField("street");
    public static final XingAddressField ZIP_CODE = new XingAddressField("zip_code");
    
    private XingAddressField(String name) {
        super(name);
    }
}
