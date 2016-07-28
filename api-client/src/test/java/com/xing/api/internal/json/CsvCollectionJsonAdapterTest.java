/*
 * Copyright (c) 2016 XING AG (http://xing.com/)
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
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import okio.Buffer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author serj.lotutovici
 */
@SuppressWarnings("CollectionWithoutInitialCapacity")
public class CsvCollectionJsonAdapterTest {
    private static final Set<Annotation> WITH_NO_ANNOTATIONS = Collections.emptySet();
    private static final Set<Annotation> WITH_CS_ANNOTATION = new HashSet<>(1);
    private static final Set<Annotation> WITH_NO_CS_ANNOTATION = new HashSet<>(1);
    private static final Set<Annotation> WITH_NO_CS_ANNOTATION2 = new HashSet<>(2);

    static {
        WITH_CS_ANNOTATION.add(new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return CsvCollection.class;
            }
        });

        WITH_NO_CS_ANNOTATION.add(new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        });

        WITH_NO_CS_ANNOTATION2.addAll(WITH_NO_CS_ANNOTATION);
        WITH_NO_CS_ANNOTATION2.add(new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
    }

    private final Moshi moshi = new Moshi.Builder().build();

    @Test
    public void ignoresUnsupportedTypes() throws Exception {
        assertThat(csAdapter(String.class, String.class, WITH_NO_ANNOTATIONS)).isNull();
        assertThat(csAdapter(List.class, String.class, WITH_NO_ANNOTATIONS)).isNull();
        assertThat(csAdapter(List.class, String.class, WITH_NO_CS_ANNOTATION)).isNull();
        assertThat(csAdapter(Collection.class, String.class, WITH_NO_CS_ANNOTATION2)).isNull();
        assertThat(csAdapter(List.class, Integer.class, WITH_CS_ANNOTATION)).isNull();
        assertThat(csAdapter(Set.class, Object.class, WITH_CS_ANNOTATION)).isNull();
    }

    @Test
    public void asList() throws Exception {
        assertCollection(List.class, new ArrayList<>());
    }

    @Test
    public void asCollection() throws Exception {
        assertCollection(Collection.class, new ArrayList<>());
    }

    @Test
    public void asSet() throws Exception {
        assertCollection(Set.class, new LinkedHashSet<>());
    }

    private <C extends Collection<String>> void assertCollection(Class<C> collectionClass, C instance)
          throws Exception {
        instance.add("one");
        instance.add("two");
        instance.add("three");

        String toJson = toJson(collectionClass, instance);
        assertThat(toJson).isEqualTo("\"one, two, three\"");

        C fromJson = fromJson(collectionClass, "\"test1, test2, test3\"");
        assertThat(fromJson).containsExactly("test1", "test2", "test3");

        C fromJsonNoSpace = fromJson(collectionClass, "\"test1,test2,test3\"");
        assertThat(fromJsonNoSpace).containsExactly("test1", "test2", "test3");
    }

    private <C extends Collection<String>> String toJson(Type collectionType, C collection) throws IOException {
        JsonAdapter<C> jsonAdapter = validCsAdapter(collectionType);
        Buffer buffer = new Buffer();
        JsonWriter jsonWriter = JsonWriter.of(buffer);
        jsonWriter.setSerializeNulls(true);
        //noinspection ConstantConditions At this point the adapter should not be null.
        jsonAdapter.lenient().toJson(jsonWriter, collection);
        return buffer.readUtf8();
    }

    private <C extends Collection<String>> C fromJson(Type collectionType, String json) throws IOException {
        JsonAdapter<C> jsonAdapter = validCsAdapter(collectionType);
        //noinspection ConstantConditions At this point the adapter should not be null.
        return jsonAdapter.lenient().fromJson(json);
    }

    @SuppressWarnings("unchecked")
    private <C extends Collection<String>> JsonAdapter<C> validCsAdapter(Type collectionType) {
        return this.<C, String>csAdapter(collectionType, String.class, WITH_CS_ANNOTATION);
    }

    @SuppressWarnings("unchecked")
    private <C extends Collection<T>, T> JsonAdapter<C> csAdapter(Type collectionType, Type elementType,
          Set<? extends Annotation> annotations) {
        return (JsonAdapter<C>) CsvCollectionJsonAdapter.FACTORY.create(
              Types.newParameterizedType(collectionType, elementType), annotations, moshi);
    }
}
