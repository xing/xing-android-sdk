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

package com.xing.android.sdk.json;

import java.util.List;

/**
 * @author serj.lotutovici
 */
public final class FieldUtils {

    /**
     * Private constructor for utility class
     */
    private FieldUtils() {
    }

    /**
     * Format all list element to a simple string where all elements are divided by a comma.
     *
     * @param fields The list of fields
     * @return A string containing all fields divided by a comma
     */
    public static String formatFieldsToString(final List<? extends Field> fields) {
        StringBuilder output = new StringBuilder();

        if (fields != null && !fields.isEmpty()) {
            // Get the location of the last element
            int numFieldsLessOne = fields.size() - 1;
            // Iterate throw all elements except the last one
            for (int iterator = 0; iterator < numFieldsLessOne; iterator++) {
                output.append(fields.get(iterator)).append(',');
            }
            // Append last element (if the size is one, only this append will be executed)
            output.append(fields.get(numFieldsLessOne));
        }

        return output.toString();
    }
}
