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
package com.xing.api.model.user;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * Represents the phone of a user.
 *
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/put/users/me/private_address">Address</a>
 */
public class XingPhone implements Serializable, Parcelable {
    private static final long serialVersionUID = -973963783219359383L;
    private static final char SEPARATOR = '|';
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

    private String countryCode;
    private String areaCode;
    private String number;

    /**
     * Constructor for Phone.
     *
     * @param countryCode Code of the country. Must be numeric and can contain the symbol + at the beginning.
     * @param areaCode Code of the area. Must be numeric.
     * @param number Number. Must be numeric.
     * @throws InvalidPhoneException The conditions
     */
    public XingPhone(String countryCode, String areaCode, String number) throws InvalidPhoneException {
        InvalidPhoneException exception = null;

        if (isValidCountryCode(countryCode)) {
            this.countryCode = countryCode;
        } else {
            exception = new InvalidPhoneException();
            exception.mCountryCode = countryCode;
        }

        if (TextUtils.isDigitsOnly(areaCode)) {
            this.areaCode = areaCode;
        } else {
            if (exception == null) {
                exception = new InvalidPhoneException();
            }
            exception.mAreaCode = areaCode;
        }

        if (TextUtils.isDigitsOnly(number)) {
            this.number = number;
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

    /** Private constructor used by getEmptyPhone. */
    private XingPhone() {
    }

    /**
     * Create {@link XingPhone} from {@link Parcel}.
     *
     * @param parcel Input {@link Parcel}
     */
    public XingPhone(Parcel parcel) {
        countryCode = parcel.readString();
        areaCode = parcel.readString();
        number = parcel.readString();
    }

    /**
     * Generates an empty phone, necessary to delete it.
     *
     * @return phone with empty data.
     */
    public static XingPhone getEmptyPhone() {
        XingPhone emptyPhone = new XingPhone();
        emptyPhone.countryCode = emptyPhone.areaCode = emptyPhone.number = "";

        return emptyPhone;
    }

    @Nullable
    public static XingPhone createXingPhone(String phoneString) throws InvalidPhoneException {
        String[] splitPhone = phoneString.split("\\|");
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
        dest.writeString(countryCode);
        dest.writeString(areaCode);
        dest.writeString(number);
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
        int result = countryCode != null ? countryCode.hashCode() : 0;
        result = 31 * result + (areaCode != null ? areaCode.hashCode() : 0);
        result = 31 * result + (number != null ? number.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return countryCode + '|' + areaCode + '|' + number;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getNumber() {
        return number;
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
        if (empty.equals(countryCode)) {
            stringBuilder = new StringBuilder(empty);
        } else {
            stringBuilder = new StringBuilder(countryCode);
            stringBuilder.append(SEPARATOR);
            stringBuilder.append(areaCode);
            stringBuilder.append(SEPARATOR);
            stringBuilder.append(number);
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

        /* the message is generated every time instead of in the constructor because of the possibility of changing
        the language in runtime.*/
        @Override
        public String getMessage() {
            StringBuilder stringBuilder = new StringBuilder("The phone number is not valid.");

            if (!TextUtils.isEmpty(mCountryCode)) {
                stringBuilder.append("The country code ")
                      .append(mCountryCode)
                      .append(" is not valid. Must be numeric and can contain the symbol + at the beginning.");
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
