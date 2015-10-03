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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.xing.android.sdk.model.JsonEnum;

/**
 * Contains method for parsing enum from json.
 *
 * @author serj.lotutovici
 */
public final class EnumMapper {
    /**
     * Parse a {@link JsonEnum} from a string value received from server.
     *
     * @param enums The enums to search in
     * @param value The string value to map to
     * @param <T> Generic type for json enums
     * @return The enum corresponding to the string value, if the value is null or empty then null,
     * if no corresponding enum was found also null
     */
    @Nullable
    public static <T extends JsonEnum> T parseEnumFromString(@NonNull T[] enums, @Nullable String value) {

        if (TextUtils.isEmpty(value)) {
            return null;
        } else {
            for (T e : enums) {
                if (value.equals(e.getJsonValue())) {
                    return e;
                }
            }
            return null;
        }
    }

    private EnumMapper() {
        throw new AssertionError("No instances.");
    }
}
