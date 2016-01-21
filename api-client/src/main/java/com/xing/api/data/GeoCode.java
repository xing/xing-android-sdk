package com.xing.api.data;

import java.io.Serializable;

/**
 * Represents the geo code object returned with several response objects.
 * <p>
 * When parsed from the api response, the internal converter will <strong>not</strong> return a malformed geo code.
 * Instead {@code null} will returned. Minimum validation rule is non {@code null} <strong>longitude</strong> and
 * <strong>latitude</strong> values.
 *
 * @author cristian.monforte
 */
public class GeoCode implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int accuracy;
    private final double latitude;
    private final double longitude;

    public GeoCode(int accuracy, double latitude, double longitude) {
        this.accuracy = accuracy;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /** Returns the geo codes accuracy. */
    public int accuracy() {
        return accuracy;
    }

    /** Returns the coordinate latitude. */
    public double latitude() {
        return latitude;
    }

    /** Returns the coordinate longitude. */
    public double longitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeoCode geoCode = (GeoCode) o;
        return accuracy == geoCode.accuracy
              && Double.compare(geoCode.latitude, latitude) == 0
              && Double.compare(geoCode.longitude, longitude) == 0;
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
        return "GeoCode{"
              + "accuracy=" + accuracy
              + ", latitude=" + latitude
              + ", longitude=" + longitude
              + '}';
    }
}
