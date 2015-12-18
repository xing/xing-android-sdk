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
package com.xing.api.model.user;

import android.os.Build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author david.gonzales
 */
@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = Config.NONE)
public class XingPhoneTest {

    @Test
    public void testIsValidCountryCode_nullCountryCode()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = XingPhone.class.getDeclaredMethod("isValidCountryCode", String.class);
        method.setAccessible(true);
        boolean valid = (boolean) method.invoke(null, new Object[]{null});

        assertTrue(!valid);
    }

    @Test
    public void testIsValidCountryCode_emptyCountryCode()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = XingPhone.class.getDeclaredMethod("isValidCountryCode", String.class);
        method.setAccessible(true);
        boolean valid = (boolean) method.invoke(null, "");

        assertTrue(!valid);
    }

    @Test
    public void testIsValidCountryCode_notNumeric()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = XingPhone.class.getDeclaredMethod("isValidCountryCode", String.class);
        method.setAccessible(true);
        boolean valid = (boolean) method.invoke(null, "notNumeric");

        assertTrue(!valid);
    }

    @Test
    public void testIsValidCountryCode_onlyNumbers()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = XingPhone.class.getDeclaredMethod("isValidCountryCode", String.class);
        method.setAccessible(true);
        boolean valid = (boolean) method.invoke(null, "34");

        assertTrue(valid);
    }

    @Test
    public void testIsValidCountryCode_wrongSymbol()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = XingPhone.class.getDeclaredMethod("isValidCountryCode", String.class);
        method.setAccessible(true);
        boolean valid = (boolean) method.invoke(null, "-34");

        assertTrue(!valid);
    }

    @Test
    public void testIsValidCountryCode_onlySymbol()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = XingPhone.class.getDeclaredMethod("isValidCountryCode", String.class);
        method.setAccessible(true);
        boolean valid = (boolean) method.invoke(null, "+");

        assertTrue(!valid);
    }

    @Test
    public void testIsValidCountryCode_symbolAndNumbers()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = XingPhone.class.getDeclaredMethod("isValidCountryCode", String.class);
        method.setAccessible(true);
        boolean valid = (boolean) method.invoke(null, "+34");

        assertTrue(valid);
    }

    @Test
    public void testGetEmptyPhone() throws NoSuchFieldException {
        XingPhone phone = XingPhone.getEmptyPhone();

        assertEquals("", phone.getCountryCode());
        assertEquals("", phone.getAreaCode());
        assertEquals("", phone.getNumber(), "");
    }

    @Test
    public void testGetFormattedPhone_emptyPhone() {
        assertEquals("", XingPhone.getEmptyPhone().getFormattedPhone());
    }

    @Test
    public void testGetFormattedPhone_validPhone() {
        XingPhone phone = new XingPhone("+49", "152", "12345678");
        assertEquals("+49|152|12345678", phone.getFormattedPhone());
    }

    @Test
    public void testCreateXingPhoneWithPipesOnly() {
        String pipes = "||";

        XingPhone phone = XingPhone.createXingPhone(pipes);

        assertNull(phone);
    }

    @Test
    public void testCreateXingPhone() {
        String pipes = "+49|015|2343243533";

        XingPhone phone = XingPhone.createXingPhone(pipes);

        assertNotNull(phone);
        assertEquals("+49", phone.getCountryCode());
        assertEquals("015", phone.getAreaCode());
        assertEquals("2343243533", phone.getNumber());

        assertEquals(pipes, phone.toString());
    }
}
