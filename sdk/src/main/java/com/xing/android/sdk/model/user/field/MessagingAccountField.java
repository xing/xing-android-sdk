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
 * Possible instant messaging accounts.
 * <p/>
 *
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/get/users/me">Instant Messaging Accounts</a>
 */
@SuppressWarnings("unused")
public class MessagingAccountField extends Field {
    public static final MessagingAccountField SKYPE = new MessagingAccountField("skype");
    public static final MessagingAccountField ICQ = new MessagingAccountField("icq");
    public static final MessagingAccountField MSN = new MessagingAccountField("msn");
    public static final MessagingAccountField YAHOO = new MessagingAccountField("yahoo");
    public static final MessagingAccountField AIM = new MessagingAccountField("aim");
    public static final MessagingAccountField JABBER = new MessagingAccountField("jabber");
    public static final MessagingAccountField GOOGLE_TALK = new MessagingAccountField("googletalk");

    private MessagingAccountField(String name) {
        super(name);
    }
}
