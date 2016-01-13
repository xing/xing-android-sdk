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
package com.xing.api.data.profile;

import java.io.Serializable;

/**
 * Java representation of a {@linkplain XingUser user's} phone number.
 * <p>
 * XWS returns and accepts phone values only as a string formatted '[country_code]|[area_code]|[number]'. This class
 * simplifies working with these values. The internal api will ensure the conversion form/to json is done properly.
 *
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/put/users/me/private_address">Private Address Resource</a>
 */
public class Phone implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String countryCode;
    private final String areaCode;
    private final String number;

    /**
     * Creates a new phone object.
     *
     * @param countryCode Code of the country. Must be numeric and can contain the symbol + at the beginning.
     * @param areaCode Code of the area. Must be numeric.
     * @param number Number. Must be numeric.
     */
    public Phone(String countryCode, String areaCode, String number) {
        this.countryCode = countryCode;
        this.areaCode = areaCode;
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Phone phone = (Phone) o;

        return (countryCode != null ? countryCode.equals(phone.countryCode) : phone.countryCode == null)
              && (areaCode != null ? areaCode.equals(phone.areaCode) : phone.areaCode == null)
              && (number != null ? number.equals(phone.number) : phone.number == null);
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

    /** Returns the phones country code. */
    public String countryCode() {
        return countryCode;
    }

    /** Returns the phones area code. */
    public String areaCode() {
        return areaCode;
    }

    /** Returns the phone number without country and area code. */
    public String number() {
        return number;
    }
}
