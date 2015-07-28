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

import android.os.Build;

import com.xing.android.sdk.json.ParserUnitTestBase;
import com.xing.android.sdk.model.user.XingAddress;
import com.xing.android.sdk.model.user.XingPhone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author serj.lotutovici
 */
@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = Config.NONE
)
public class XingAddressMapperTest extends ParserUnitTestBase {

    private static final String TEST_ADDRESS_1 = "{\n" +
            "        \"city\": \"Hamburg\",\n" +
            "        \"country\": \"DE\",\n" +
            "        \"zip_code\": \"20357\",\n" +
            "        \"street\": \"Privatstraße 1\",\n" +
            "        \"phone\": \"49|40|1234560\",\n" +
            "        \"fax\": \"||\",\n" +
            "        \"province\": \"Hamburg\",\n" +
            "        \"email\": \"max@mustermann.de\",\n" +
            "        \"mobile_phone\": \"49|0155|1234567\"\n" +
            "      }";

    private static final String TEST_ADDRESS_2 = "{\n" +
            "        \"city\": \"Hamburg\",\n" +
            "        \"country\": \"DE\",\n" +
            "        \"zip_code\": \"20357\",\n" +
            "        \"street\": \"Geschäftsstraße 1a\",\n" +
            "        \"phone\": \"49|40|1234569\",\n" +
            "        \"fax\": \"49|40|1234561\",\n" +
            "        \"province\": \"Hamburg\",\n" +
            "        \"email\": \"max.mustermann@xing.com\",\n" +
            "        \"mobile_phone\": \"49|160|66666661\"\n" +
            "      }";

    private static final String TEST_ADDRESSES = "[" +
            TEST_ADDRESS_1 + ", " +
            TEST_ADDRESS_2 + "]";


    @Test
    public void parseAddressWithNoFax() throws Exception {
        XingAddress address = XingAddressMapper.parseXingAddress(getReaderForJson(TEST_ADDRESS_1));
        assertNotNull(address);
        assertEquals(address.getCity(), "Hamburg");
        assertEquals(address.getCountry(), "DE");
        assertEquals(address.getZipCode(), "20357");
        assertNotNull(address.getPhone());
        assertEquals(address.getPhone(), new XingPhone("49", "40", "1234560"));
        assertNull(address.getFax());
        assertEquals(address.getMobilePhone(), new XingPhone("49", "0155", "1234567"));
    }

    @Test
    public void parseAddressWithFax() throws Exception {
        XingAddress address = XingAddressMapper.parseXingAddress(getReaderForJson(TEST_ADDRESS_2));
        assertNotNull(address);
        assertEquals(address.getCity(), "Hamburg");
        assertEquals(address.getCountry(), "DE");
        assertEquals(address.getZipCode(), "20357");
        assertEquals(address.getEmail(), "max.mustermann@xing.com");
        assertEquals("49", address.getFax().getCountryCode());
        assertEquals("40", address.getFax().getAreaCode());
        assertEquals("1234561", address.getFax().getNumber());
    }

    @Test
    public void parseListOfAddresses() throws Exception {
        List<XingAddress> addresses = XingAddressMapper.parseXingAddressList(getReaderForJson(TEST_ADDRESSES));
        assertNotNull(addresses);
        assertEquals(addresses.size(), 2);
    }

    @Test
    public void parseAddressAndPassItThroughParcelFlow() throws Exception {
        XingAddress address = XingAddressMapper.parseXingAddress(getReaderForJson(TEST_ADDRESS_1));
        assertNotNull(address);

        // Create copy object via the parcel flow
        XingAddress addressFromParcel = createNewObjectViaParcelFlow(address, XingAddress.CREATOR);
        assertEquals(address.hashCode(), addressFromParcel.hashCode());
        assertEquals(address, addressFromParcel);
    }
}