package com.xing.api.data;

import com.squareup.moshi.Json;

import java.io.Serializable;

/**
 * @author cristian.monforte
 */
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
    @Json(name = "zipcode")
    private String zipCode;
    @Json(name = "region")
    private String region;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (geoCode != null ? !geoCode.equals(location.geoCode) : location.geoCode != null) return false;
        if (city != null ? !city.equals(location.city) : location.city != null) return false;
        if (country != null ? !country.equals(location.country) : location.country != null) return false;
        if (street != null ? !street.equals(location.street) : location.street != null) return false;
        if (zipCode != null ? !zipCode.equals(location.zipCode) : location.zipCode != null) return false;
        return !(region != null ? !region.equals(location.region) : location.region != null);
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
        return "Location{" +
              "geoCode=" + geoCode +
              ", city='" + city + '\'' +
              ", country='" + country + '\'' +
              ", street='" + street + '\'' +
              ", zipCode='" + zipCode + '\'' +
              ", region='" + region + '\'' +
              '}';
    }
}
