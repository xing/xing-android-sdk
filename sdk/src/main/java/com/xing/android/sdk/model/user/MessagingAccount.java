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

import android.support.annotation.NonNull;

import com.xing.android.sdk.model.JsonEnum;

/**
 * Possible instant messaging accounts.
 *
 * @author serj.lotutovici
 * @see <a href="https://dev.xing.com/docs/get/users/me">Instant Messaging Accounts</a>
 */
public enum MessagingAccount implements JsonEnum {
    SKYPE("skype"),
    ICQ("icq"),
    MSN("msn"),
    YAHOO("yahoo"),
    AIM("aim"),
    JABBER("jabber"),
    GOOGLE_TALK("googletalk");

    /** The name value received from the json response. */
    private final String mName;

    MessagingAccount(@NonNull String accountName) {
        mName = accountName;
    }

    @Override
    public String getJsonValue() {
        return mName;
    }
}
