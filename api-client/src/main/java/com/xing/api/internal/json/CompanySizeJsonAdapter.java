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
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.xing.api.model.user.CompanySize;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * @author daniel.hartwich
 */
public final class CompanySizeJsonAdapter extends JsonAdapter<CompanySize> {
    public static final Factory FACTORY = new Factory() {
        @Nullable
        @Override
        public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
            if (!annotations.isEmpty()) return null;
            Class<?> rawType = Types.getRawType(type);
            if (rawType != CompanySize.class) return null;
            return new CompanySizeJsonAdapter().nullSafe();
        }
    };

    @Nullable
    @Override
    public CompanySize fromJson(JsonReader reader) throws IOException {
        String companySize = reader.nextString();
        switch (companySize) {
            case "1":
                return CompanySize.SIZE_1;
            case "1-10":
                return CompanySize.SIZE_1_10;
            case "11-50":
                return CompanySize.SIZE_11_50;
            case "51-200":
                return CompanySize.SIZE_51_200;
            case "201-500":
                return CompanySize.SIZE_201_500;
            case "501-1000":
                return CompanySize.SIZE_501_1000;
            case "1001-5000":
                return CompanySize.SIZE_1001_5000;
            case "5001-10000":
                return CompanySize.SIZE_5001_10000;
            case "10001+":
                return CompanySize.SIZE_10001PLUS;
            default:
                return null;
        }
    }

    @Override
    public void toJson(JsonWriter writer, CompanySize value) throws IOException {
        writer.value(value.getJsonValue());
    }
}
