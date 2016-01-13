package com.xing.api.data;

import com.squareup.moshi.Json;
import com.xing.api.internal.json.NullDouble;
import com.xing.api.internal.json.NullInt;

import java.io.Serializable;

/**
 * @author cristian.monforte
 */
public class GeoCode implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "accuracy")
    @NullInt
    private final int accuracy;
    @Json(name = "latitude")
    @NullDouble
    private final double latitude;
    @Json(name = "longitude")
    @NullDouble
    private final double longitude;

    public GeoCode(int accuracy, double latitude, double longitude) {
        this.accuracy = accuracy;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int accuracy() {
        return accuracy;
    }

    public double latitude() {
        return latitude;
    }

    public double longitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeoCode geoCode = (GeoCode) o;

        if (accuracy != geoCode.accuracy) return false;
        if (Double.compare(geoCode.latitude, latitude) != 0) return false;
        return Double.compare(geoCode.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = accuracy;
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "GeoCode{" +
              "accuracy=" + accuracy +
              ", latitude=" + latitude +
              ", longitude=" + longitude +
              '}';
    }
}
