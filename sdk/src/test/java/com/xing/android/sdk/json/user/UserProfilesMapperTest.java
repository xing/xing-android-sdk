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

import android.net.Uri;
import android.os.Build;
import android.util.JsonReader;

import com.xing.android.sdk.json.ParserUnitTestBase;
import com.xing.android.sdk.model.user.ProfileMessage;
import com.xing.android.sdk.model.user.XingUser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.EOFException;
import java.io.StringReader;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.annotation.Config.NONE;

/**
 * @author serj.lotutovici
 */
@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = NONE)
public class UserProfilesMapperTest extends ParserUnitTestBase {

    private static final String LEGAL_INFO_JSON = "{\n" +
            "  \"legal_information\": {\n" +
            "    \"content\": \"Max Mustermann, Mustermann AG\"\n" +
            "  }\n" +
            "}";

    private static final String PROFILE_MESSAGE_JSON = "{\n" +
            "  \"profile_message\": {\n" +
            "    \"updated_at\": \"2011-07-18T11:40:19Z\",\n" +
            "    \"message\": \"My new profile message.\"\n" +
            "  }\n" +
            "}";

    @Test
    public void parseUsersFromRequest() throws Exception {
        // Read the json source
        final String json = UserParserTestValueStubs.LIST_OF_FULL_USERS;
        assertNotNull(json);

        // Get the list of users
        List<XingUser> users = UserProfilesMapper.parseUsersFromRequest(getReaderForJson(json));
        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertEquals(3, users.size());

        // Check simple values, all data should be tested in a separated test
        XingUser user = users.get(1);
        assertNotNull(user);
        assertEquals("123456_abcdef", user.getId());
        assertEquals("Max Mustermann", user.getDisplayName());
//        assertEquals("XING AG", user.getProfessionalExperience().getPrimaryCompany().getName());
    }

    @Test(expected = EOFException.class)
    public void parseUsersFromNullResponse() throws Exception {
        JsonReader reader = new JsonReader(new StringReader(""));
        UserProfilesMapper.parseUsersFromRequest(reader);
    }

    @Test
    public void parseUsersFromFindRequest() throws Exception {
        // Read the json source
        final String json = UserParserTestValueStubs.LIST_OF_USERS_FROM_FIND;
        assertNotNull(json);

        // Get the list of users
        List<XingUser> users = UserProfilesMapper.parseUsersFromFindRequest(getReaderForJson(json));
        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertEquals(2, users.size());

        // Check simple values
        XingUser user = users.get(1);
        assertNotNull(user);
        assertEquals("123sd456_ab793ef", user.getId());
        assertEquals("Max Freeman", user.getDisplayName());
        assertNotNull(user.getPhotoUrls());
        assertNotNull(user.getPhotoUrls().getPhotoSize256Url());
    }

    @Test
    public void parseUsersFromFindEmptyResponse() throws Exception {
        JsonReader reader = new JsonReader(new StringReader("{}"));
        List<XingUser> users = UserProfilesMapper.parseUsersFromFindRequest(reader);
        assertNull(users);
    }

    @Test
    public void parseUserFromIdCard() throws Exception {
        // Read the json source
        final String json = UserParserTestValueStubs.USER_FROM_ID_CARD;
        assertNotNull(json);

        // Get the user from id card
        XingUser user = UserProfilesMapper.parseUserFromIdCard(getReaderForJson(json));
        assertNotNull(user);
        assertEquals(Uri.parse("https://www.xing.com/profile/Max_Mustermann"), user.getPermalink());
        assertNotNull(user.getPhotoUrls());
        assertNotNull(user.getPhotoUrls().getPhotoSizeOriginalUrl());
    }

    @Test
    public void parseUserFromSearchByEmailWhereEmailNotHashed() throws Exception {
        // Read the json source
        final String json = UserParserTestValueStubs.LIST_OF_USERS_FROM_EMAIL_SEARCH_EMAIL;
        assertNotNull(json);

        // Get the map of users
        Map<String, XingUser> users = UserProfilesMapper.parseUsersFromSearchByEmailRequest(getReaderForJson(json));
        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertEquals(2, users.size());
        assertTrue(users.containsKey("existing_user@xing.com"));
        assertTrue(users.containsKey("unknown_user@xing.com"));
        assertEquals("10368_ddec16", users.get("existing_user@xing.com").getId());
        assertNull(users.get("unknown_user@xing.com"));
    }

    @Test
    public void parseUserFromSearchByEmailWhereEmailHashed() throws Exception {
        // Read the json source
        final String json = UserParserTestValueStubs.LIST_OF_USERS_FROM_EMAIL_SEARCH_HASH;
        assertNotNull(json);

        // Get the map of users
        Map<String, XingUser> users = UserProfilesMapper.parseUsersFromSearchByEmailRequest(getReaderForJson(json));
        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertEquals(2, users.size());
        assertTrue(users.containsKey("1a83d7e553085e340f0f08887182d49a"));
        assertTrue(users.containsKey("e2854c86a4d2064e77f0ae3567cad297"));
        assertEquals("10368_ddec16", users.get("1a83d7e553085e340f0f08887182d49a").getId());
        assertNull(users.get("e2854c86a4d2064e77f0ae3567cad297"));
    }

    @Test
    public void parseUserLegalInfo() throws Exception {
        String legalInfo = UserProfilesMapper.parseLegalInfo(getReaderForJson(LEGAL_INFO_JSON));
        assertNotNull(legalInfo);
        assertEquals("Max Mustermann, Mustermann AG", legalInfo);
    }

    @Test
    public void parseProfileMessage() throws Exception {
        ProfileMessage message = UserProfilesMapper.parseProfileMessage(getReaderForJson(PROFILE_MESSAGE_JSON));
        assertNotNull(message);
        assertNotNull(message.getUpdatedAt());
        assertEquals(2011, message.getUpdatedAt().get(Calendar.YEAR));
        assertEquals(Calendar.JULY, message.getUpdatedAt().get(Calendar.MONTH));
        assertEquals(18, message.getUpdatedAt().get(Calendar.DAY_OF_MONTH));
        assertEquals("My new profile message.", message.getMessage());
    }
}
