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

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.xing.api.data.profile.Phone;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

/**
 * @author serj.lotutovici
 */
@SuppressWarnings("ConstantConditions")
public class PhoneJsonAdapterTest {
    private final Moshi moshi = new Moshi.Builder().build();

    @Test
    public void ignoresOtherTypes() throws Exception {
        JsonAdapter<?> adapter1 = PhoneJsonAdapter.FACTORY.create(
              String.class, Collections.<Annotation>emptySet(), moshi);
        assertNull(adapter1);

        Set<Annotation> annotations = new LinkedHashSet<>(1);
        annotations.add(mock(Annotation.class));
        JsonAdapter<?> adapter2 = PhoneJsonAdapter.FACTORY.create(
              Phone.class, annotations, moshi);
        assertNull(adapter2);
    }

    @Test
    public void phone() throws Exception {
        Phone phone = new Phone("49", "177", "1234567");

        String toJson = phoneAdapter().toJson(phone);
        assertThat(toJson).isEqualTo("\"49|177|1234567\"");

        Phone fromJson = phoneAdapter().fromJson("\"+1|23|4567890\"");
        assertThat(fromJson.countryCode()).isEqualTo("+1");
        assertThat(fromJson.areaCode()).isEqualTo("23");
        assertThat(fromJson.number()).isEqualTo("4567890");
        assertThat(fromJson.toString()).isEqualTo("+1|23|4567890");
    }

    @Test
    public void emptyPhone() throws Exception {
        Phone emptyPhone = new Phone("", "", "");

        String toJson = phoneAdapter().toJson(emptyPhone);
        assertThat(toJson).isEqualTo("\"||\"");

        Phone fromJson = phoneAdapter().fromJson("\"||\"");
        assertThat(fromJson.areaCode()).isEmpty();
        assertThat(fromJson.countryCode()).isEmpty();
        assertThat(fromJson.number()).isEmpty();
        assertThat(fromJson.toString()).isEqualTo("||");
    }

    @SuppressWarnings("unchecked")
    private JsonAdapter<Phone> phoneAdapter() {
        return (JsonAdapter<Phone>) PhoneJsonAdapter.FACTORY.create(
              Phone.class, Collections.<Annotation>emptySet(), moshi).lenient();
    }
}
