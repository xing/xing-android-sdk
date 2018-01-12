/*
 * Copyright (С) 2018 XING SE (http://xing.com/)
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
package com.xing.api.data;

import com.squareup.moshi.Json;

import java.io.Serializable;

public class Location implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "geo_code")
    private GeoCode geoCode;
    @Json(name = "city")
    private String city;
    @Json(name = "country")
    private String country;
    @Json(name = "street")
    private String street;
    @Json(name = "zip_code")
    private String zipCode;
    @Json(name = "region")
    private String region;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;
        return geoCode != null ? geoCode.equals(location.geoCode) : location.geoCode == null
              && (city != null ? city.equals(location.city) : location.city == null
              && (country != null ? country.equals(location.country) : location.country == null
              && (street != null ? street.equals(location.street) : location.street == null
              && (zipCode != null ? zipCode.equals(location.zipCode) : location.zipCode == null
              && (region != null ? region.equals(location.region) : location.region == null)))));
    }

    @Override
    public int hashCode() {
        int result = geoCode != null ? geoCode.hashCode() : 0;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (zipCode != null ? zipCode.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Location{"
              + "geoCode=" + geoCode
              + ", city='" + city + '\''
              + ", country='" + country + '\''
              + ", street='" + street + '\''
              + ", zipCode='" + zipCode + '\''
              + ", region='" + region + '\''
              + '}';
    }

    public GeoCode geoCode() {
        return geoCode;
    }

    public Location geoCode(GeoCode geoCode) {
        this.geoCode = geoCode;
        return this;
    }

    public String city() {
        return city;
    }

    public Location city(String city) {
        this.city = city;
        return this;
    }

    public String country() {
        return country;
    }

    public Location country(String country) {
        this.country = country;
        return this;
    }

    public String street() {
        return street;
    }

    public Location street(String street) {
        this.street = street;
        return this;
    }

    public String zipCode() {
        return zipCode;
    }

    public Location zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public String region() {
        return region;
    }

    public Location region(String region) {
        this.region = region;
        return this;
    }
}
