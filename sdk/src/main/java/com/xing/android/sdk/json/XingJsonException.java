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

package com.xing.android.sdk.json;

import android.text.TextUtils;

/**
 * An exception that gets thrown whenever something is wrong with the JSON.
 * For example when a JSON can not be created because of the values inside.
 *
 * @author david.gonzalez
 */
public class XingJsonException extends Exception {
    private static final long serialVersionUID = -5873006148116122766L;

    private final String mClass;
    private final String mMethod;
    private final String mReason;

    public XingJsonException(String clazz, String method, String reason) {
        mClass = clazz;
        mMethod = method;
        mReason = reason;
    }

    /**
     * Gets the message for the exception overriding the default implementation from the super class.
     *
     * @return A String object, which is the message, describing where and why the exception occurred
     */
    @Override
    public String getMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(mClass)) {
            stringBuilder.append("Class: ").append(mClass).append(". ");
        }

        if (!TextUtils.isEmpty(mMethod)) {
            stringBuilder.append("Method: ").append(mMethod).append(". ");
        }

        if (!TextUtils.isEmpty(mReason)) {
            stringBuilder.append("Reason: ").append(mReason);
        }

        return stringBuilder.toString();
    }
}
