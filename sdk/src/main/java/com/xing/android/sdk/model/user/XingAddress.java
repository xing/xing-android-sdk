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

import com.squareup.moshi.Json;

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

    @Json(name = "city")
    private String city;
    @Json(name = "country")
    private String country;
    @Json(name = "email")
    private String email;
    @Json(name = "fax")
    private XingPhone fax;
    @Json(name = "mobile_phone")
    private XingPhone mobilePhone;
    @Json(name = "phone")
    private XingPhone phone;
    @Json(name = "province")
    private String province;
    @Json(name = "street")
    private String street;
    @Json(name = "zip_code")
    private String zipCode;

    /** Create a simple XingAddress object with empty fields. */
    public XingAddress() {
    }

    /**
     * Create {@link XingAddress} from {@link Parcel}.
     *
     * @param in Input {@link Parcel}
     */
    private XingAddress(Parcel in) {
        city = in.readString();
        country = in.readString();
        email = in.readString();
        fax = in.readParcelable(XingPhone.class.getClassLoader());
        mobilePhone = in.readParcelable(XingPhone.class.getClassLoader());
        phone = in.readParcelable(XingPhone.class.getClassLoader());
        province = in.readString();
        street = in.readString();
        zipCode = in.readString();
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
        int result = city != null ? city.hashCode() : 0;
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (fax != null ? fax.hashCode() : 0);
        result = 31 * result + (mobilePhone != null ? mobilePhone.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (province != null ? province.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (zipCode != null ? zipCode.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeString(country);
        dest.writeString(email);
        dest.writeParcelable(fax, flags);
        dest.writeParcelable(mobilePhone, flags);
        dest.writeParcelable(phone, flags);
        dest.writeString(province);
        dest.writeString(street);
        dest.writeString(zipCode);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public XingPhone getFax() {
        return fax;
    }

    public void setFax(String fax) throws XingPhone.InvalidPhoneException {
        this.fax = XingPhone.createXingPhone(fax);
    }

    public void setFax(XingPhone fax) {
        this.fax = fax;
    }

    public void setFax(String countryCode, String areaCode, String number)
    throws XingPhone.InvalidPhoneException {
        fax = new XingPhone(countryCode, areaCode, number);
    }

    public XingPhone getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) throws XingPhone.InvalidPhoneException {
        this.mobilePhone = XingPhone.createXingPhone(mobilePhone);
    }

    public void setMobilePhone(XingPhone mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public void setMobilePhone(String countryCode, String areaCode, String number)
    throws XingPhone.InvalidPhoneException {
        mobilePhone = new XingPhone(countryCode, areaCode, number);
    }

    public XingPhone getPhone() {
        return phone;
    }

    public void setPhone(String phone) throws XingPhone.InvalidPhoneException {
        this.phone = XingPhone.createXingPhone(phone);
    }

    public void setPhone(XingPhone phone) {
        this.phone = phone;
    }

    public void setPhone(String countryCode, String areaCode, String number)
    throws XingPhone.InvalidPhoneException {
        phone = new XingPhone(countryCode, areaCode, number);
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
