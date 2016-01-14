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
import com.squareup.moshi.Moshi;
import com.xing.api.data.SafeCalendar;

import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author serj.lotutovici
 */
@SuppressWarnings("ConstantConditions")
public class BirthDateJsonAdapterTest {
    private static final Set<Annotation> WITH_NO_ANNOTATIONS = Collections.emptySet();
    private static final Set<Annotation> WITH_BD_ANNOTATION = new HashSet<>(1);
    private static final Set<Annotation> WITH_NO_BD_ANNOTATION = new HashSet<>(1);
    private static final Set<Annotation> WITH_NO_BD_ANNOTATION2 = new HashSet<>(2);

    static {
        WITH_BD_ANNOTATION.add(new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return BirthDate.class;
            }
        });

        WITH_NO_BD_ANNOTATION.add(new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        });

        WITH_NO_BD_ANNOTATION2.addAll(WITH_NO_BD_ANNOTATION);
        WITH_NO_BD_ANNOTATION2.add(new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Nullable.class;
            }
        });
    }

    private final Moshi moshi = new Moshi.Builder().build();

    @Test
    public void ignoresUnsupportedTypes() throws Exception {
        assertThat(birthdayAdapter(String.class, WITH_NO_ANNOTATIONS)).isNull();
        assertThat(birthdayAdapter(List.class, WITH_NO_ANNOTATIONS)).isNull();
        assertThat(birthdayAdapter(List.class, WITH_NO_BD_ANNOTATION)).isNull();
        assertThat(birthdayAdapter(Collection.class, WITH_NO_BD_ANNOTATION2)).isNull();
        assertThat(birthdayAdapter(List.class, WITH_BD_ANNOTATION)).isNull();
        assertThat(birthdayAdapter(Set.class, WITH_BD_ANNOTATION)).isNull();
    }

    @Test
    public void failsOnInvalidBirthday() throws Exception {
        try {
            validBirthdayAdapter().fromJson("{\n"
                  + "  \"day\": 1,\n"
                  + "  \"nope\": 3\n"
                  + '}');
        } catch (IOException ex) {
            assertThat(ex.getMessage()).isEqualTo("birthday should contain 'year', 'month' and/or 'day', found $nope");
        }
    }

    @Test
    public void birthDate() throws Exception {
        SafeCalendar calendar = new SafeCalendar();
        calendar.set(Calendar.YEAR, 1977);
        calendar.set(Calendar.MONTH, Calendar.MAY);
        calendar.set(Calendar.DAY_OF_MONTH, 18);

        String toJson = validBirthdayAdapter().toJson(calendar);
        assertThat(toJson).isEqualTo('{'
              + "\"year\":1977,"
              + "\"month\":5,"
              + "\"day\":18"
              + '}');

        SafeCalendar fromJson = validBirthdayAdapter().fromJson("{\n"
              + "  \"year\": 1989,\n"
              + "  \"month\": 4,\n"
              + "  \"day\": 1\n"
              + '}');
        assertThat(fromJson.get(Calendar.YEAR)).isEqualTo(1989);
        assertThat(fromJson.get(Calendar.MONTH)).isEqualTo(Calendar.APRIL);
        assertThat(fromJson.get(Calendar.DAY_OF_MONTH)).isEqualTo(1);
    }

    /** Some users prefer to show only the year and day. */
    @Test
    public void birthDateWithNoDay() throws Exception {
        SafeCalendar calendar = new SafeCalendar();
        calendar.set(Calendar.YEAR, 1990);
        calendar.set(Calendar.MONTH, Calendar.FEBRUARY);

        String toJson = validBirthdayAdapter().toJson(calendar);
        assertThat(toJson).isEqualTo("{\"year\":1990,\"month\":2,\"day\":null}");

        SafeCalendar fromJson = validBirthdayAdapter().fromJson("{\n"
              + "  \"year\": 1987,\n"
              + "  \"month\": 3\n"
              + '}');
        assertThat(fromJson.get(Calendar.YEAR)).isEqualTo(1987);
        assertThat(fromJson.get(Calendar.MONTH)).isEqualTo(Calendar.MARCH);
        assertThat(fromJson.isSet(Calendar.DAY_OF_MONTH)).isFalse();
    }

    /** For some cases the api may return {"year":null,"month":null,"day":null}. */
    @Test
    public void nullBirthDay() throws Exception {
        SafeCalendar fromJson = validBirthdayAdapter().fromJson("{\n"
              + "  \"year\":null,\n"
              + "  \"month\":null,\n"
              + "  \"day\":null\n"
              + '}');

        assertThat(fromJson.isSet(Calendar.YEAR)).isFalse();
        assertThat(fromJson.isSet(Calendar.MONTH)).isFalse();
        assertThat(fromJson.isSet(Calendar.DAY_OF_MONTH)).isFalse();
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private JsonAdapter<SafeCalendar> validBirthdayAdapter() {
        return birthdayAdapter(SafeCalendar.class, WITH_BD_ANNOTATION);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private <T> JsonAdapter<T> birthdayAdapter(Type type,
          Set<? extends Annotation> annotations) {
        return (JsonAdapter<T>) BirthDateJsonAdapter.FACTORY.create(type, annotations, moshi);
    }
}
