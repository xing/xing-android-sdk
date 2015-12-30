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
 * Java representation of an address object. Primary used for the {@link XingUser} model, to represent the user's
 * business nad private addresses.
 *
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/put/users/me/private_address">Private Address Resource</a>
 */
@SuppressWarnings("unused") // Public api
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "city")
    private String city;
    @Json(name = "country")
    private String country;
    @Json(name = "email")
    private String email;
    @Json(name = "fax")
    private Phone fax;
    @Json(name = "mobile_phone")
    private Phone mobilePhone;
    @Json(name = "phone")
    private Phone phone;
    @Json(name = "province")
    private String province;
    @Json(name = "street")
    private String street;
    @Json(name = "zip_code")
    private String zipCode;

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        return (city != null ? city.equals(address.city) : address.city == null)
              && (country != null ? country.equals(address.country) : address.country == null)
              && (email != null ? email.equals(address.email) : address.email == null)
              && (fax != null ? fax.equals(address.fax) : address.fax == null)
              && (mobilePhone != null ? mobilePhone.equals(address.mobilePhone) : address.mobilePhone == null)
              && (phone != null ? phone.equals(address.phone) : address.phone == null)
              && (province != null ? province.equals(address.province) : address.province == null)
              && (street != null ? street.equals(address.street) : address.street == null)
              && (zipCode != null ? zipCode.equals(address.zipCode) : address.zipCode == null);
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
    public String toString() {
        return "Address{"
              + "city='" + city + '\''
              + ", country='" + country + '\''
              + ", email='" + email + '\''
              + ", fax=" + fax
              + ", mobilePhone=" + mobilePhone
              + ", phone=" + phone
              + ", province='" + province + '\''
              + ", street='" + street + '\''
              + ", zipCode='" + zipCode + '\''
              + '}';
    }

    /** Returns the city name. */
    public String getCity() {
        return city;
    }

    /** Set the city name. */
    public void setCity(String city) {
        this.city = city;
    }

    /** Returns the country name or abbreviation (example: 'DE' for Germany). */
    public String getCountry() {
        return country;
    }

    /** Set the county name. */
    public void setCountry(String country) {
        this.country = country;
    }

    /** Returns the user's email associated with <strong>this</strong> address. */
    public String getEmail() {
        return email;
    }

    /** Set the user's email associated with <strong>this</strong> address. */
    public void setEmail(String email) {
        this.email = email;
    }

    /** Returns the fax phone number associated with <strong>this</strong> address. */
    public Phone getFax() {
        return fax;
    }

    /** Set the fax phone number associated with <strong>this</strong> address. */
    public void setFax(Phone fax) {
        this.fax = fax;
    }

    /** Returns the mobile phone number associated with <strong>this</strong> address. */
    public Phone getMobilePhone() {
        return mobilePhone;
    }

    /** Set the mobile phone number associated with <strong>this</strong> address. */
    public void setMobilePhone(Phone mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    /** Returns the phone number associated with <strong>this</strong> address. */
    public Phone getPhone() {
        return phone;
    }

    /** Set the phone number associated with <strong>this</strong> address. */
    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    /** Returns the province. */
    public String getProvince() {
        return province;
    }

    /** Set the province. */
    public void setProvince(String province) {
        this.province = province;
    }

    /** Returns the address street. This string also may contain the house number. */
    public String getStreet() {
        return street;
    }

    /** Set the street. This string also may contain the house number. */
    public void setStreet(String street) {
        this.street = street;
    }

    /** Returns the zip code as a string. */
    public String getZipCode() {
        return zipCode;
    }

    /** Set the zip code as a string. */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
