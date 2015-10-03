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

package com.xing.android.sdk.network.request.exception;

import android.text.TextUtils;

// TODO documentation
public class NetworkException extends Exception {

    public static final int NULL_ERROR_CODE = -1;
    private static final long serialVersionUID = -1010785074029413729L;
    private static final String ERROR_MESSAGE_STATUS_CODE_PART = "The HTTP Status Code is %1$d.";
    private static final String ERROR_MESSAGE_RESPONSE_PART = "The content of the response is %1$s.";
    private static final String ERROR_MESSAGE_REASON_PART = "The reason is %1$s.";

    private final int mErrorCode;
    private final String mResponse;
    private final String mReason;
    private String mLocalizedMessage;

    public NetworkException(int errorCode, String reason) {
        this(errorCode, null, reason);
    }

    public NetworkException(int errorCode, String response, String reason) {
        mErrorCode = errorCode;
        mResponse = response;
        mReason = reason;
    }

    public NetworkException(int errorCode, String response, Throwable cause) {
        super(cause);
        mErrorCode = errorCode;
        mResponse = response;
        mReason = cause.getMessage();
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public String getResponse() {
        return mResponse;
    }

    @Override
    public String getMessage() {
        StringBuilder messageBuilder = new StringBuilder();
        boolean emptyMessageBuilder = true;

        if (mErrorCode != NULL_ERROR_CODE) {
            messageBuilder.append(String.format(ERROR_MESSAGE_STATUS_CODE_PART, mErrorCode));
            emptyMessageBuilder = false;
        }

        if (!TextUtils.isEmpty(mResponse)) {
            if (!emptyMessageBuilder) {
                messageBuilder.append(' ');
            }
            messageBuilder.append(String.format(ERROR_MESSAGE_RESPONSE_PART, mResponse));
            emptyMessageBuilder = false;
        }

        if (!TextUtils.isEmpty(mReason)) {
            if (!emptyMessageBuilder) {
                messageBuilder.append(' ');
            }
            messageBuilder.append(String.format(ERROR_MESSAGE_REASON_PART, mReason));
        }

        return messageBuilder.toString();
    }

    /**
     * Obtains the localized message of the network exception.
     *
     * @return The localized message parsed from the json of the response, null otherwise.
     */
    @Override
    public String getLocalizedMessage() {
        return mLocalizedMessage;
    }

    /**
     * Sets the localized message for the exception.
     *
     * @param localizedMessage The message to set.
     */
    public void setLocalizedMessage(String localizedMessage) {
        mLocalizedMessage = localizedMessage;
    }
}
