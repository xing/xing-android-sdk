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

package com.xing.android.sdk.model.user;

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
        manifest = Config.NONE
)
public class XingPhoneTest {

    @Test
    public void testIsValidCountryCode_nullCountryCode() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = XingPhone.class.getDeclaredMethod("isValidCountryCode", String.class);
        method.setAccessible(true);
        boolean valid = (boolean) method.invoke(null, new Object[]{null});

        assertTrue(!valid);
    }

    @Test
    public void testIsValidCountryCode_emptyCountryCode() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = XingPhone.class.getDeclaredMethod("isValidCountryCode", String.class);
        method.setAccessible(true);
        boolean valid = (boolean) method.invoke(null, "");

        assertTrue(!valid);
    }

    @Test
    public void testIsValidCountryCode_notNumeric() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = XingPhone.class.getDeclaredMethod("isValidCountryCode", String.class);
        method.setAccessible(true);
        boolean valid = (boolean) method.invoke(null, "notNumeric");

        assertTrue(!valid);
    }

    @Test
    public void testIsValidCountryCode_onlyNumbers() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = XingPhone.class.getDeclaredMethod("isValidCountryCode", String.class);
        method.setAccessible(true);
        boolean valid = (boolean) method.invoke(null, "34");

        assertTrue(valid);
    }

    @Test
    public void testIsValidCountryCode_wrongSymbol() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = XingPhone.class.getDeclaredMethod("isValidCountryCode", String.class);
        method.setAccessible(true);
        boolean valid = (boolean) method.invoke(null, "-34");

        assertTrue(!valid);
    }

    @Test
    public void testIsValidCountryCode_onlySymbol() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = XingPhone.class.getDeclaredMethod("isValidCountryCode", String.class);
        method.setAccessible(true);
        boolean valid = (boolean) method.invoke(null, "+");

        assertTrue(!valid);
    }

    @Test
    public void testIsValidCountryCode_symbolAndNumbers() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
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
