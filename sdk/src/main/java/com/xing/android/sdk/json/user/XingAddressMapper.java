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

package com.xing.android.sdk.json.user;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.xing.android.sdk.model.user.XingAddress;
import com.xing.android.sdk.model.user.XingPhone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser that gets the XingAddress from a JsonReader
 *
 * @author david.gonzalez
 */
public final class XingAddressMapper {
    private final static String TAG = "XingAddressMapper";

    /**
     * Extracts the XingAddress out of the JsonReader
     *
     * @param reader The JsonReader containing the TimeZone
     * @return The XingAddress object
     * @throws IOException
     */
    public static XingAddress parseXingAddress(JsonReader reader) throws IOException {
        XingAddress xingaddress = new XingAddress();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "email": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        xingaddress.setEmail(reader.nextString());
                    }
                    break;
                }
                case "city": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        xingaddress.setCity(reader.nextString());
                    }
                    break;
                }
                case "country": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        xingaddress.setCountry(reader.nextString());
                    }
                    break;
                }
                case "fax": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        String rawFax = reader.nextString();
                        try {
                            xingaddress.setFax(rawFax);
                        } catch (XingPhone.InvalidPhoneException e) {
                            Log.d(TAG, rawFax + " is not a valid XING phone number");
                        }
                    }
                    break;
                }
                case "mobile_phone": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        final String rawMobilePhone = reader.nextString();
                        try {
                            xingaddress.setMobilePhone(rawMobilePhone);
                        } catch (XingPhone.InvalidPhoneException e) {
                            Log.d(TAG, rawMobilePhone + " is not a valid XING phone number");
                        }
                    }
                    break;
                }
                case "phone": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        final String rawPhone = reader.nextString();
                        try {
                            xingaddress.setPhone(rawPhone);
                        } catch (XingPhone.InvalidPhoneException e) {
                            Log.d(TAG, rawPhone + " is not a valid XING phone number");
                        }
                    }
                    break;
                }
                case "province": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        xingaddress.setProvince(reader.nextString());
                    }
                    break;
                }
                case "street": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        xingaddress.setStreet(reader.nextString());
                    }
                    break;
                }
                case "zip_code": {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                    } else {
                        xingaddress.setZipCode(reader.nextString());
                    }
                    break;
                }
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return xingaddress;
    }

    /**
     * Same as {@link XingAddressMapper#parseXingAddress(JsonReader)} but for multiple XingAddresses
     *
     * @param reader The JsonReader
     * @return A list with TimeZone objects
     * @throws IOException
     */
    public static List<XingAddress> parseXingAddressList(JsonReader reader) throws IOException {
        List<XingAddress> xingAddressList = new ArrayList<>(0);
        reader.beginArray();
        while (reader.hasNext()) {
            xingAddressList.add(parseXingAddress(reader));
        }
        reader.endArray();
        return xingAddressList;
    }
}
