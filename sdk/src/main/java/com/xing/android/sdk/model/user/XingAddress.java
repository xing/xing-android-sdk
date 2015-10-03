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

import java.io.Serializable;

/**
 * Represents the address of a user.
 *
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/put/users/me/private_address">Phone</a>
 */
@SuppressWarnings("unused") // Public api
public class XingAddress implements Serializable, Parcelable {
    private static final long serialVersionUID = -6309755823721877973L;
    /** Creator object for Parcelable contract. */
    public static final Creator<XingAddress> CREATOR = new Creator<XingAddress>() {
        @Override
        public XingAddress createFromParcel(Parcel source) {
            return new XingAddress(source);
        }

        @Override
        public XingAddress[] newArray(int size) {
            return new XingAddress[size];
        }
    };

    private String mCity;
    private String mCountry;
    private String mEmail;
    private XingPhone mFax;
    private XingPhone mMobilePhone;
    private XingPhone mPhone;
    private String mProvince;
    private String mStreet;
    private String mZipCode;

    /** Create a simple XingAddress object with empty fields. */
    public XingAddress() {
    }

    /**
     * Create {@link XingAddress} from {@link Parcel}.
     *
     * @param in Input {@link Parcel}
     */
    private XingAddress(Parcel in) {
        mCity = in.readString();
        mCountry = in.readString();
        mEmail = in.readString();
        mFax = in.readParcelable(XingPhone.class.getClassLoader());
        mMobilePhone = in.readParcelable(XingPhone.class.getClassLoader());
        mPhone = in.readParcelable(XingPhone.class.getClassLoader());
        mProvince = in.readString();
        mStreet = in.readString();
        mZipCode = in.readString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof XingAddress)) {
            return false;
        }

        XingAddress address = (XingAddress) obj;
        return hashCode() == address.hashCode();
    }

    @Override
    public int hashCode() {
        int result = mCity != null ? mCity.hashCode() : 0;
        result = 31 * result + (mCountry != null ? mCountry.hashCode() : 0);
        result = 31 * result + (mEmail != null ? mEmail.hashCode() : 0);
        result = 31 * result + (mFax != null ? mFax.hashCode() : 0);
        result = 31 * result + (mMobilePhone != null ? mMobilePhone.hashCode() : 0);
        result = 31 * result + (mPhone != null ? mPhone.hashCode() : 0);
        result = 31 * result + (mProvince != null ? mProvince.hashCode() : 0);
        result = 31 * result + (mStreet != null ? mStreet.hashCode() : 0);
        result = 31 * result + (mZipCode != null ? mZipCode.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCity);
        dest.writeString(mCountry);
        dest.writeString(mEmail);
        dest.writeParcelable(mFax, flags);
        dest.writeParcelable(mMobilePhone, flags);
        dest.writeParcelable(mPhone, flags);
        dest.writeString(mProvince);
        dest.writeString(mStreet);
        dest.writeString(mZipCode);
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(final String city) {
        mCity = city;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(final String country) {
        mCountry = country;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(final String email) {
        mEmail = email;
    }

    public XingPhone getFax() {
        return mFax;
    }

    public void setFax(final String fax) throws XingPhone.InvalidPhoneException {
        mFax = XingPhone.createXingPhone(fax);
    }

    public void setFax(final XingPhone fax) {
        mFax = fax;
    }

    public void setFax(final String countryCode, final String areaCode, final String number)
            throws XingPhone.InvalidPhoneException {
        mFax = new XingPhone(countryCode, areaCode, number);
    }

    public XingPhone getMobilePhone() {
        return mMobilePhone;
    }

    public void setMobilePhone(final String mobilePhone) throws XingPhone.InvalidPhoneException {
        mMobilePhone = XingPhone.createXingPhone(mobilePhone);
    }

    public void setMobilePhone(final XingPhone mobilePhone) {
        mMobilePhone = mobilePhone;
    }

    public void setMobilePhone(final String countryCode, final String areaCode, final String number)
            throws XingPhone.InvalidPhoneException {
        mMobilePhone = new XingPhone(countryCode, areaCode, number);
    }

    public XingPhone getPhone() {
        return mPhone;
    }

    public void setPhone(final String phone) throws XingPhone.InvalidPhoneException {
        mPhone = XingPhone.createXingPhone(phone);
    }

    public void setPhone(final XingPhone phone) {
        mPhone = phone;
    }

    public void setPhone(final String countryCode, final String areaCode, final String number)
            throws XingPhone.InvalidPhoneException {
        mPhone = new XingPhone(countryCode, areaCode, number);
    }

    public String getProvince() {
        return mProvince;
    }

    public void setProvince(final String province) {
        mProvince = province;
    }

    public String getStreet() {
        return mStreet;
    }

    public void setStreet(final String street) {
        mStreet = street;
    }

    public String getZipCode() {
        return mZipCode;
    }

    public void setZipCode(final String zipCode) {
        mZipCode = zipCode;
    }
}
