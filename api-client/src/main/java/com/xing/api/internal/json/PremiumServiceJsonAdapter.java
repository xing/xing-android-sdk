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
package com.xing.api.internal.json;

import android.support.annotation.Nullable;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.xing.api.model.user.PremiumService;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * @author daniel.hartwich
 */
public class PremiumServiceJsonAdapter extends EnumJsonAdapter<PremiumService> {
    public static final Factory FACTORY = new Factory() {
        @Nullable
        @Override
        public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
            if (!annotations.isEmpty()) return null;
            Class<?> rawType = Types.getRawType(type);
            if (rawType != PremiumService.class) return null;
            return new PremiumServiceJsonAdapter().nullSafe();
        }
    };

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
    public String toString() {
        return "JsonAdapter(" + PremiumService.class + ')';
    }
}
