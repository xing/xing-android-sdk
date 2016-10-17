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
import com.xing.api.data.GeoCode;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public final class GeoCodeJsonAdapterTest {
    private final Moshi moshi = new Moshi.Builder().build();

    @Test
    public void ignoresOtherTypes() throws Exception {
        assertThat(geoCodeAdapter(String.class)).isNull();
        assertThat(geoCodeAdapter(Double.class)).isNull();
        assertThat(geoCodeAdapter(GeoCode.class)).isNotNull();
    }

    @Test
    public void validGeoCode() throws Exception {
        JsonAdapter<GeoCode> adapter = geoCodeAdapter(GeoCode.class);

        GeoCode fromJson = adapter.fromJson("{\n"
              + "  \"accuracy\": 12,\n"
              + "  \"latitude\": 53.558572,\n"
              + "  \"longitude\": 9.927822\n"
              + '}');
        assertThat(fromJson.accuracy()).isEqualTo(12);
        assertThat(fromJson.latitude()).isEqualTo(53.558572);
        assertThat(fromJson.longitude()).isEqualTo(9.927822);

        String toJson = adapter.toJson(new GeoCode(3, 23.456, 456.345));
        assertThat(toJson).isEqualTo("{\"accuracy\":3,\"latitude\":23.456,\"longitude\":456.345}");
    }

    @Test
    public void ignoresInvalidGeoCode() throws Exception {
        JsonAdapter<GeoCode> adapter = geoCodeAdapter(GeoCode.class);

        assertThat(adapter.fromJson("{\"accuracy\":12,\"latitude\":null,\"longitude\":null}")).isNull();
        assertThat(adapter.fromJson("{\"latitude\":34.2,\"longitude\":null}")).isNull();
        assertThat(adapter.fromJson("{\"latitude\":null,\"longitude\":24.45}")).isNull();
        assertThat(adapter.fromJson("{\"unknown\":null,\"longitude\":24.45}")).isNull();
        assertThat(adapter.fromJson("{}")).isNull();
    }

    @SuppressWarnings("unchecked") // It's the callers responsibility.
    private <T> JsonAdapter<T> geoCodeAdapter(Type type) {
        return (JsonAdapter<T>) GeoCodeJsonAdapter.FACTORY.create(type, Collections.<Annotation>emptySet(), moshi);
    }
}
