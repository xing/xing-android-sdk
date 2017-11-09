/*
 * Copyright (C) 2016 XING SE (http://xing.com/)
 * Copyright (C) 2009 The Guava Authors
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
package com.xing.api;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link UrlEscapeUtils} class.
 *
 * <b>Note: </b> This is a ripoff of Guava's actual {@code UtlEscapersTest}.
 *
 * @author David Beaumont
 * @author Sven Mawson
 */
public class UrlEscapeUtilsTest {
    @Test
    public void actsAsUrlFormParameterEscaper() {
        try {
            UrlEscapeUtils.escape(null);
            fail("Escaping null string should throw exception");
        } catch (NullPointerException x) {
            // pass
        }

        // 0-9, A-Z, a-z should be left unescaped
        assertUnescaped('a');
        assertUnescaped('z');
        assertUnescaped('A');
        assertUnescaped('Z');
        assertUnescaped('0');
        assertUnescaped('9');

        // Unreserved characters used in java.net.URLEncoder
        assertUnescaped('-');
        assertUnescaped('_');
        assertUnescaped('.');
        assertUnescaped('*');

        assertEscaping("%3D", '=');
        assertEscaping("%2F", '/');
        assertEscaping("%00", '\u0000');       // nul
        assertEscaping("%7F", '\u007f');       // del
        assertEscaping("%C2%80", '\u0080');    // xx-00010,x-000000
        assertEscaping("%DF%BF", '\u07ff');    // xx-11111,x-111111
        assertEscaping("%E0%A0%80", '\u0800'); // xxx-0000,x-100000,x-00,0000
        assertEscaping("%EF%BF%BF", '\uffff'); // xxx-1111,x-111111,x-11,1111
        assertUnicodeEscaping("%F0%90%80%80", '\uD800', '\uDC00');
        assertUnicodeEscaping("%F4%8F%BF%BF", '\uDBFF', '\uDFFF');

        assertThat(UrlEscapeUtils.escape("")).isEqualTo("");
        assertThat(UrlEscapeUtils.escape("safestring")).isEqualTo("safestring");
        assertThat(UrlEscapeUtils.escape("embedded\0null")).isEqualTo("embedded%00null");
        assertThat(UrlEscapeUtils.escape("max\uffffchar")).isEqualTo("max%EF%BF%BFchar");

        // Specified as safe by RFC 2396 but not by java.net.URLEncoder.
        assertEscaping("%21", '!');
        assertEscaping("%28", '(');
        assertEscaping("%29", ')');
        assertEscaping("%7E", '~');
        assertEscaping("%27", '\'');

        // Plus for spaces
        assertEscaping("%20", ' ');
        assertEscaping("%2B", '+');

        assertThat(UrlEscapeUtils.escape("safe with spaces")).isEqualTo("safe%20with%20spaces");
        assertThat(UrlEscapeUtils.escape("foo@bar.com")).isEqualTo("foo%40bar.com");
    }

    /**
     * Asserts that {@link UrlEscapeUtils} escapes the given character.
     *
     * @param expected the expected escape result
     * @param c the character to test
     */
    private static void assertEscaping(String expected, char c) {
        String escaped = computeReplacement(c);
        assertThat(escaped)
              .isNotNull()
              .isEqualTo(expected);
    }

    /**
     * Asserts that {@link UrlEscapeUtils} does not escape the given character.
     *
     * @param c the character to test
     */
    private static void assertUnescaped(char c) {
        assertThat(computeReplacement(c)).isNull();
    }

    /**
     * Asserts that {@link UrlEscapeUtils} escapes the given hi/lo surrogate pair into
     * the expected string.
     *
     * @param expected the expected output string
     * @param hi the high surrogate pair character
     * @param lo the low surrogate pair character
     */
    private static void assertUnicodeEscaping(String expected, char hi, char lo) {
        int cp = Character.toCodePoint(hi, lo);
        String escaped = computeReplacement(cp);
        assertThat(escaped)
              .isNotNull()
              .isEqualTo(expected);
    }

    private static String computeReplacement(char c) {
        return stringOrNull(UrlEscapeUtils.escape(c));
    }

    private static String computeReplacement(int cp) {
        return stringOrNull(UrlEscapeUtils.escape(cp));
    }

    private static String stringOrNull(char[] chars) {
        return (chars == null) ? null : new String(chars);
    }
}
