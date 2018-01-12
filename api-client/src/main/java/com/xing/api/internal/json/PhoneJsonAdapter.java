/*
 * Copyright (С) 2018 XING SE (http://xing.com/)
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

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.xing.api.data.profile.Phone;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

public class PhoneJsonAdapter extends JsonAdapter<Phone> {
    public static final JsonAdapter.Factory FACTORY = new Factory() {
        @Override
        public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
            if (!annotations.isEmpty()) return null;
            Class<?> rawType = Types.getRawType(type);
            if (rawType != Phone.class) return null;
            return new PhoneJsonAdapter().nullSafe();
        }
    };

    PhoneJsonAdapter() {
    }

    @Override
    public Phone fromJson(JsonReader reader) throws IOException {
        String phone = reader.nextString();
        String[] pieces = phone.split("\\|");
        if (pieces.length == 3) return new Phone(pieces[0], pieces[1], pieces[2]);
        // Avoid returning a null phone if the server didn't return null.
        return new Phone("", "", "");
    }

    @Override
    public void toJson(JsonWriter writer, Phone value) throws IOException {
        writer.value(value.toString());
    }
}
