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

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.Serializable;
import java.security.InvalidParameterException;

/**
 * Represents the phone of a user.
 *
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/put/users/me/private_address">Address</a>
 */
public class XingPhone implements Serializable, Parcelable {

    private static final long serialVersionUID = -973963783219359383L;

    public static final Creator<XingPhone> CREATOR = new Creator<XingPhone>() {
        @Override
        public XingPhone createFromParcel(Parcel parcel) {
            return new XingPhone(parcel);
        }

        @Override
        public XingPhone[] newArray(int size) {
            return new XingPhone[size];
        }
    };

    private static final char SEPARATOR = '|';

    private String mCountryCode;
    private String mAreaCode;
    private String mNumber;

    /**
     * Constructor for Phone.
     *
     * @param countryCode Code of the country. Must be numeric and can contain the symbol + at
     *                    the beginning.
     * @param areaCode    Code of the area. Must be numeric.
     * @param number      Number. Must be numeric.
     * @throws InvalidParameterException The conditions
     */
    public XingPhone(String countryCode, String areaCode, String number) throws InvalidPhoneException {
        InvalidPhoneException exception = null;

        if (isValidCountryCode(countryCode)) {
            mCountryCode = countryCode;
        } else {
            exception = new InvalidPhoneException();
            exception.mCountryCode = countryCode;
        }

        if (TextUtils.isDigitsOnly(areaCode)) {
            mAreaCode = areaCode;
        } else {
            if (exception == null) {
                exception = new InvalidPhoneException();
            }
            exception.mAreaCode = areaCode;
        }

        if (TextUtils.isDigitsOnly(number)) {
            mNumber = number;
        } else {
            if (exception == null) {
                exception = new InvalidPhoneException();
            }
            exception.mNumber = number;
        }

        if (exception != null) {
            throw exception;
        }
    }

    /**
     * Private constructor used by getEmptyPhone.
     */
    private XingPhone() {

    }

    /**
     * Create {@link XingPhone} from {@link Parcel}
     *
     * @param parcel Input {@link Parcel}
     */
    public XingPhone(Parcel parcel) {
        mCountryCode = parcel.readString();
        mAreaCode = parcel.readString();
        mNumber = parcel.readString();
    }

    /**
     * Generates an empty phone, necessary to delete it.
     *
     * @return phone with empty data.
     */
    public static XingPhone getEmptyPhone() {
        XingPhone emptyPhone = new XingPhone();
        emptyPhone.mCountryCode = emptyPhone.mAreaCode = emptyPhone.mNumber = "";

        return emptyPhone;
    }

    public static XingPhone createXingPhone(final String phoneString) throws InvalidPhoneException {
        final String[] splitPhone = phoneString.split("\\|");
        if (splitPhone.length != 3) {
            return null;
        } else {
            return new XingPhone(splitPhone[0], splitPhone[1], splitPhone[2]);
        }
    }

    /**
     * Check if the {@code countryCode} is a valid country code.
     *
     * @param countryCode the country code to check.
     * @return true if it is valid, false otherwise.
     */
    private static boolean isValidCountryCode(String countryCode) {
        boolean valid = false;

        if (!TextUtils.isEmpty(countryCode)) {
            if (countryCode.charAt(0) == '+') {
                valid = countryCode.length() > 1 && TextUtils.isDigitsOnly(countryCode.substring(1));
            } else {
                valid = TextUtils.isDigitsOnly(countryCode);
            }
        }

        return valid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCountryCode);
        dest.writeString(mAreaCode);
        dest.writeString(mNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof XingPhone)) {
            return false;
        }

        XingPhone xingPhone = (XingPhone) obj;
        return hashCode() == xingPhone.hashCode();
    }

    @Override
    public int hashCode() {
        int result = mCountryCode != null ? mCountryCode.hashCode() : 0;
        result = 31 * result + (mAreaCode != null ? mAreaCode.hashCode() : 0);
        result = 31 * result + (mNumber != null ? mNumber.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return mCountryCode + '|' + mAreaCode + '|' + mNumber;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public String getAreaCode() {
        return mAreaCode;
    }

    public String getNumber() {
        return mNumber;
    }

    /**
     * Formats the phone for requests. If the phone os empty generates an empty String, if not
     * generates an String with format countryCode|areaCode|number
     *
     * @return String with the format countryCode|areaCode|number.
     */
    public String getFormattedPhone() {
        StringBuilder stringBuilder;
        String empty = "";

        //  checking one is enough, it is not possible to have only one empty field.
        if (empty.equals(mCountryCode)) {
            stringBuilder = new StringBuilder(empty);
        } else {
            stringBuilder = new StringBuilder(mCountryCode);
            stringBuilder.append(SEPARATOR);
            stringBuilder.append(mAreaCode);
            stringBuilder.append(SEPARATOR);
            stringBuilder.append(mNumber);
        }

        return stringBuilder.toString();
    }

    public static class InvalidPhoneException extends IllegalArgumentException {

        private static final long serialVersionUID = 6067569936834405505L;

        private String mCountryCode;
        private String mAreaCode;
        private String mNumber;

        public InvalidPhoneException() {
            mCountryCode = null;
            mAreaCode = null;
            mNumber = null;
        }

        /* the message is generated every time instead of in the constructor because of the possibility of changing the language in runtime.*/
        @Override
        public String getMessage() {
            StringBuilder stringBuilder = new StringBuilder("The phone number is not valid.");

            if (!TextUtils.isEmpty(mCountryCode)) {
                stringBuilder.append("The country code ").append(mCountryCode).append(" is not valid. Must be numeric and can contain the symbol + at the beginning.");
            }

            if (!TextUtils.isEmpty(mAreaCode)) {
                stringBuilder.append("The area code ").append(mCountryCode).append(" is not valid. Must be numeric.");
            }

            if (!TextUtils.isEmpty(mNumber)) {
                stringBuilder.append("The number ").append(mCountryCode).append(" is not valid. Must be numeric.");
            }

            return stringBuilder.toString();
        }

    }
}