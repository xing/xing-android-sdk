/*
 * Copyright (С) 2016 XING AG (http://xing.com/)
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
import com.xing.api.data.profile.XingUser;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author serj.lotutovici
 */
public final class ContactPathJsonAdapter extends JsonAdapter<List<List<XingUser>>> {
    public static final JsonAdapter.Factory FACTORY = new JsonAdapter.Factory() {
        @Nullable
        @Override
        public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
            if (annotations.isEmpty() || annotations.size() != 1
                  // At this point we know that the set contains only one entry.
                  || annotations.iterator().next().annotationType() != ContactPath.class) {
                return null;
            }

            // Check that the host type is a list.
            Class<?> rawType = Types.getRawType(type);
            if (rawType != List.class) return null;

            // Check that the generic parameter is a list.
            Type paramType = Types.collectionElementType(type, List.class);
            Class<?> rawParamType = Types.getRawType(paramType);
            if (rawParamType != List.class) return null;

            // Check that the generic parameter of paramType is XingUser.
            Type innerType = Types.collectionElementType(paramType, List.class);
            Class<?> rawInnerType = Types.getRawType(innerType);
            if (rawInnerType != XingUser.class) return null;

            // Create the adapter. At this point we know that paramType is the pathType.
            return new ContactPathJsonAdapter(moshi, paramType).nullSafe();
        }
    };

    private final JsonAdapter<List<XingUser>> pathAdapter;

    ContactPathJsonAdapter(Moshi moshi, Type pathType) {
        pathAdapter = moshi.adapter(pathType);
    }

    @Override
    public List<List<XingUser>> fromJson(JsonReader reader) throws IOException {
        List<List<XingUser>> paths = new LinkedList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if ("users".equals(name)) {
                    paths.add(pathAdapter.fromJson(reader));
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        }
        reader.endArray();
        return paths;
    }

    @Override
    public void toJson(JsonWriter writer, List<List<XingUser>> value) throws IOException {

    }
}
