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

package com.xing.android.sdk;

import android.support.annotation.Nullable;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.xing.android.sdk.model.user.PremiumService;

import java.io.IOException;

/**
 * @author daniel.hartwich
 */
public class PremiumServiceJsonAdapter extends JsonAdapter<PremiumService> {
    @Nullable
    @Override
    public PremiumService fromJson(JsonReader reader) throws IOException {
        String premiumService = reader.nextString();
        switch (premiumService) {
            case "SEARCH":
                return PremiumService.SEARCH;
            case "PRIVATEMESSAGES":
                return PremiumService.PRIVATE_MESSAGES;
            case "NOADVERTISING":
                return PremiumService.NO_ADVERTISING;
            default:
                return null;
        }
    }

    @Override
    public void toJson(JsonWriter writer, PremiumService value) throws IOException {
        writer.value(value.getJsonValue());
    }
}
