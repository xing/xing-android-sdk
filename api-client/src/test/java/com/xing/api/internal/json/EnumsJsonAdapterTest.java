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

import android.support.annotation.NonNull;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public final class EnumsJsonAdapterTest {
    private final Moshi moshi = new Moshi.Builder().build();

    @Test
    public void enumAdapter() throws Exception {
        JsonAdapter<Roshambo> adapter = enumAdapter(Roshambo.class);
        assertThat(adapter.fromJson("\"ROCK\"")).isEqualTo(Roshambo.ROCK);
        assertThat(adapter.toJson(Roshambo.PAPER)).isEqualTo("\"PAPER\"");
    }

    @Test
    public void annotatedEnum() throws Exception {
        JsonAdapter<Roshambo> adapter = enumAdapter(Roshambo.class);
        assertThat(adapter.fromJson("\"scr\"")).isEqualTo(Roshambo.SCISSORS);
        assertThat(adapter.toJson(Roshambo.SCISSORS)).isEqualTo("\"scr\"");
    }

    @Test
    public void invalidEnum() throws Exception {
        JsonAdapter<Roshambo> adapter = enumAdapter(Roshambo.class);
        try {
            adapter.fromJson("\"SPOCK\"");
            fail();
        } catch (JsonDataException expected) {
            assertThat(expected).hasMessage(
                  "Expected one of [ROCK, PAPER, scr] but was SPOCK at path $");
        }
    }

    @Test
    public void nullEnum() throws Exception {
        JsonAdapter<Roshambo> adapter = enumAdapter(Roshambo.class);
        assertThat(adapter.fromJson("null")).isNull();
        assertThat(adapter.toJson(null)).isEqualTo("null");
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private <T extends Enum<T>> JsonAdapter<T> enumAdapter(Class<T> type) {
        //noinspection ConstantConditions
        return (JsonAdapter<T>) EnumJsonAdapter.FACTORY.create(type, Collections.<Annotation>emptySet(), moshi)
              .lenient();
    }

    enum Roshambo {
        ROCK,
        PAPER,
        @Json(name = "scr")SCISSORS
    }
}
