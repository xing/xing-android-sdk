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

import java.io.Serializable;

/**
 * Represents the geo code object returned with several response objects.
 * <p>
 * When parsed from the api response, the internal converter will <strong>not</strong> return a malformed geo code.
 * Instead {@code null} will returned. Minimum validation rule is non {@code null} <strong>longitude</strong> and
 * <strong>latitude</strong> values.
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
}
