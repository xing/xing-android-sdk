/*
 * Copyright (C) 2016 XING AG (http://xing.com/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import com.xing.api.data.profile.TimeZone;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public final class TimeZoneJsonAdapterTest {
    private final Moshi moshi = new Moshi.Builder().build();

    @Test
    public void ignoresOtherTypes() throws Exception {
        assertThat(timeZoneAdapter(String.class)).isNull();
        assertThat(timeZoneAdapter(Double.class)).isNull();
        assertThat(timeZoneAdapter(TimeZone.class)).isNotNull();
    }

    @Test
    public void validTimeZone() throws Exception {
        JsonAdapter<TimeZone> adapter = timeZoneAdapter(TimeZone.class);

        TimeZone fromJson = adapter.fromJson("{\n"
              + "  \"name\": \"Uff\",\n"
              + "  \"utc_offset\": 0.34\n"
              + '}');
        assertThat(fromJson.name()).isEqualTo("Uff");
        assertThat(fromJson.utcOffset()).isEqualTo(0.34);

        String toJson = adapter.toJson(new TimeZone("Hey", 45.34));
        assertThat(toJson).isEqualTo("{\"name\":\"Hey\",\"utc_offset\":45.34}");
    }

    @Test
    public void ignoresInvalidTimeZone() throws Exception {
        JsonAdapter<TimeZone> adapter = timeZoneAdapter(TimeZone.class);

        assertThat(adapter.fromJson("{\"name\":\"hey\",\"utc_offset\":null}")).isNull();
        assertThat(adapter.fromJson("{\"name\":null,\"utc_offset\":null}")).isNull();
        assertThat(adapter.fromJson("{\"latitude\":null,\"longitude\":24.45}")).isNull();
        assertThat(adapter.fromJson("{}")).isNull();
    }

    @SuppressWarnings("unchecked") // It's the callers responsibility.
    private <T> JsonAdapter<T> timeZoneAdapter(Type type) {
        return (JsonAdapter<T>) TimeZoneJsonAdapter.FACTORY.create(type, Collections.<Annotation>emptySet(), moshi);
    }
}
