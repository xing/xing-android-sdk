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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/** Adapter that parses comma separated value strings into a collection. */
public abstract class CsvCollectionJsonAdapter<C extends Collection<String>> extends JsonAdapter<C> {
    private static final String COMMA_DELIMITER = ",";
    private static final Pattern COMMA_SEPARATOR = Pattern.compile(COMMA_DELIMITER);

    private static final String COMMA_SPACE_DELIMITER = ", ";
    private static final Pattern COMMA_SPACE_SEPARATOR = Pattern.compile(COMMA_SPACE_DELIMITER);

    /** Comma separated values adapter factory. */
    public static final JsonAdapter.Factory FACTORY = new Factory() {
        @Override
        public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
            if (annotations.isEmpty() || annotations.size() != 1
                  // At this point we know that the set contains only one entry.
                  || annotations.iterator().next().annotationType() != CsvCollection.class) {
                return null;
            }

            Class<?> rawType = Types.getRawType(type);
            // This adapter supports only List<String>, Collection<String> and Set<String>.
            if ((rawType == List.class || rawType == Collection.class) && elementTypeIsString(type)) {
                return newArrayListStringAdapter().nullSafe();
            }
            if (rawType == Set.class && elementTypeIsString(type)) {
                return newLinkedHashSetStringAdapter().nullSafe();
            }
            // No adapter for this type.
            return null;
        }

        boolean elementTypeIsString(Type type) {
            Type elementType = Types.collectionElementType(type, Collection.class);
            Class<?> rawElementType = Types.getRawType(elementType);
            return rawElementType == String.class;
        }
    };

    /** Returns an array list collection adapter for comma separated value strings. */
    static JsonAdapter<Collection<String>> newArrayListStringAdapter() {
        return new CsvCollectionJsonAdapter<Collection<String>>() {
            @SuppressWarnings("CollectionWithoutInitialCapacity")
            @Override
            Collection<String> newCollection() {
                return new ArrayList<>();
            }
        };
    }

    /** Returns a set collection adapter for comma separated value strings. */
    static JsonAdapter<Set<String>> newLinkedHashSetStringAdapter() {
        return new CsvCollectionJsonAdapter<Set<String>>() {
            @SuppressWarnings("CollectionWithoutInitialCapacity")
            @Override
            Set<String> newCollection() {
                return new LinkedHashSet<>();
            }
        };
    }

    CsvCollectionJsonAdapter() {
    }

    @Override
    public C fromJson(JsonReader reader) throws IOException {
        C result = newCollection();
        String csString = reader.nextString();
        String removeSpaces = COMMA_SPACE_SEPARATOR.matcher(csString).replaceAll(COMMA_DELIMITER);
        String[] strings = COMMA_SEPARATOR.split(removeSpaces);
        for (int index = 0, size = strings.length; index < size; index++) {
            result.add(strings[index]);
        }
        return result;
    }

    @Override
    public void toJson(JsonWriter writer, C value) throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (String token : value) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(COMMA_SPACE_DELIMITER);
            }
            sb.append(token);
        }

        writer.value(sb.toString());
    }

    abstract C newCollection();

    @Override
    public String toString() {
        return "JsonAdapter(String).csvCollection()";
    }
}
