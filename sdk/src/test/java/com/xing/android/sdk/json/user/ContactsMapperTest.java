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
import com.xing.android.sdk.model.user.XingUser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * @author declan.mccormack
 */
@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = Config.NONE
)
public class ContactsMapperTest extends ParserUnitTestBase {

    private static final String TEST_CONTACTS = "{\n" +
            "  \"contacts\": {\n" +
            "    \"total\": 47,\n" +
            "    \"users\": [\n" +
            "      {\n" +
            "        \"display_name\": \"Christina Ahnefeld\",\n" +
            "        \"id\": \"64060_b12067\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"display_name\": \"Hasso Sieber\",\n" +
            "        \"id\": \"3167095_eb35c2\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"display_name\": \"Elmar Hinsenkamp\",\n" +
            "        \"id\": \"3217478_24d6c7\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"display_name\": \"Prof. Tammo Freese\",\n" +
            "        \"id\": \"3228492_025d41\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"display_name\": \"Jonas Wunderlich\",\n" +
            "        \"id\": \"3312533_101654\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            '}';

    private static final String TEST_CONTACT_IDS = "{\n" +
            "  \"contact_ids\": {\n" +
            "    \"items\": [\n" +
            "      \"3582_f213af\",\n" +
            "      \"385526_b94012\",\n" +
            "      \"391722_0ab373\"\n" +
            "    ]\n" +
            "  }\n" +
            '}';

    private static final String TEST_ASSIGNED_TAGS = "{\n" +
            "  \"tags\": {\n" +
            "    \"total\": 2,\n" +
            "    \"items\": [\n" +
            "      {\n" +
            "        \"tag\": \"Business opportunities in München/Munich\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"tag\": \"friends\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            '}';

    private static final String TEST_SHARED_CONTACTS = "{\n" +
            "  \"shared_contacts\": {\n" +
            "    \"total\": 47,\n" +
            "    \"users\": [\n" +
            "      {\n" +
            "        \"display_name\": \"Christina Ahnefeld\",\n" +
            "        \"id\": \"64060_b12067\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"display_name\": \"Hasso Sieber\",\n" +
            "        \"id\": \"3167095_eb35c2\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"display_name\": \"Elmar Hinsenkamp\",\n" +
            "        \"id\": \"3217478_24d6c7\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"display_name\": \"Prof. Tammo Freese\",\n" +
            "        \"id\": \"3228492_025d41\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"display_name\": \"Jonas Wunderlich\",\n" +
            "        \"id\": \"3312533_101654\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            '}';


    @Test
    public void parseContacts() throws Exception {
        List<XingUser> contacts = ContactsMapper.parseContactsFromRequest(TEST_CONTACTS);
        assertNotNull(contacts);
        assertFalse(contacts.isEmpty());
        assertEquals(5, contacts.size());

        XingUser user = contacts.get(0);
        assertNotNull(user);
        assertEquals("64060_b12067", user.getId());
        assertEquals("Christina Ahnefeld", user.getDisplayName());
    }

    @Test
    public void parseContactIds() throws Exception {
        List<String> contactIds = ContactsMapper.parseContactIds(TEST_CONTACT_IDS);
        assertNotNull(contactIds);
        assertFalse(contactIds.isEmpty());
        assertEquals(3, contactIds.size());

        String contactId = contactIds.get(0);
        assertNotNull(contactId);
        assertEquals("3582_f213af", contactId);
    }

    @Test
    public void parseAssignedTags() throws Exception {
        List<String> assignedTags = ContactsMapper.parseAssignedTags(TEST_ASSIGNED_TAGS);
        assertNotNull(assignedTags);
        assertFalse(assignedTags.isEmpty());
        assertEquals(2, assignedTags.size());

        String assignedTag = assignedTags.get(0);
        assertNotNull(assignedTag);
        assertEquals("Business opportunities in München/Munich", assignedTag);
    }

    @Test
    public void parseSharedContacts() throws Exception {
        List<XingUser> sharedContacts = ContactsMapper.parseSharedContacts(TEST_SHARED_CONTACTS);
        assertNotNull(sharedContacts);
        assertFalse(sharedContacts.isEmpty());
        assertEquals(5, sharedContacts.size());

        XingUser user = sharedContacts.get(1);
        assertNotNull(user);
        assertEquals("3167095_eb35c2", user.getId());
        assertEquals("Hasso Sieber", user.getDisplayName());
    }
}