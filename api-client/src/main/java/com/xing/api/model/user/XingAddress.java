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

import com.squareup.moshi.Json;

import java.io.Serializable;

/**
 * Represents the address of a user.
 *
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/put/users/me/private_address">Phone</a>
 */
@SuppressWarnings("unused") // Public api
public class XingAddress implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "city")
    private String city;
    @Json(name = "country")
    private String country;
    @Json(name = "email")
    private String email;
    @Json(name = "fax")
    private XingPhone fax;
    @Json(name = "mobile_phone")
    private String mobilePhone;
    @Json(name = "phone")
    private XingPhone phone;
    @Json(name = "province")
    private String province;
    @Json(name = "street")
    private String street;
    @Json(name = "zip_code")
    private String zipCode;

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

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
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
