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

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author serj.lotutovici
 */
@SuppressWarnings("ConstantConditions")
public class NullIntJsonAdapterTest {
    private static final Set<Annotation> WITH_NO_ANNOTATIONS = Collections.emptySet();
    private static final Set<Annotation> WITH_NI_ANNOTATION = new HashSet<>(1);
    private static final Set<Annotation> WITH_NO_NI_ANNOTATION = new HashSet<>(1);
    private static final Set<Annotation> WITH_NO_NI_ANNOTATION2 = new HashSet<>(2);

    static {
        WITH_NI_ANNOTATION.add(new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return NullInt.class;
            }
        });

        WITH_NO_NI_ANNOTATION.add(new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        });

        WITH_NO_NI_ANNOTATION2.addAll(WITH_NO_NI_ANNOTATION);
        WITH_NO_NI_ANNOTATION2.add(new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
    }

    private final Moshi moshi = new Moshi.Builder().build();

    @Test
    public void ignoresUnsupportedTypes() throws Exception {
        assertThat(nullIntAdapter(int.class, WITH_NO_ANNOTATIONS)).isNull();
        assertThat(nullIntAdapter(Integer.class, WITH_NO_ANNOTATIONS)).isNull();
        assertThat(nullIntAdapter(int.class, WITH_NO_NI_ANNOTATION)).isNull();
        assertThat(nullIntAdapter(int.class, WITH_NO_NI_ANNOTATION2)).isNull();
        assertThat(nullIntAdapter(String.class, WITH_NI_ANNOTATION)).isNull();
    }

    @Test
    public void intValues() throws Exception {
        assertThat(fromJson("0")).isEqualTo(0);
        assertThat(fromJson("3")).isEqualTo(3);
        assertThat(fromJson("-56")).isEqualTo(-56);

        assertThat(toJson(0)).isEqualTo("0");
        assertThat(toJson(2)).isEqualTo("2");
        assertThat(toJson(-42)).isEqualTo("-42");
    }

    @Test
    public void nullValues() throws Exception {
        assertThat(fromJson("null")).isEqualTo(-1);
        assertThat(toJson(-1)).isEqualTo("null");
        assertThat(toJson(null)).isEqualTo("null");
    }

    protected String toJson(Integer value) throws IOException {
        JsonAdapter<Integer> adapter = nullIntAdapter(int.class, WITH_NI_ANNOTATION);
        return adapter.lenient().toJson(value);
    }

    private Integer fromJson(String json) throws IOException {
        JsonAdapter<Integer> adapter = nullIntAdapter(int.class, WITH_NI_ANNOTATION);
        return adapter.lenient().fromJson(json);
    }

    @SuppressWarnings("unchecked")
    private <T> JsonAdapter<T> nullIntAdapter(Type type,
          Set<? extends Annotation> annotations) {
        return (JsonAdapter<T>) NullIntJsonAdapter.FACTORY.create(type, annotations, moshi);
    }
}
