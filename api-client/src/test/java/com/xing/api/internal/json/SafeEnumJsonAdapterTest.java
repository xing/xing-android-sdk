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

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.xing.api.XingApi;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class SafeEnumJsonAdapterTest {
    private final Moshi moshi = new XingApi.Builder().custom().build().moshi();

    @Test
    public void enumAdapter() throws Exception {
        JsonAdapter<Roshambo> adapter = moshi.adapter(Roshambo.class).lenient();
        assertThat(adapter.fromJson("\"ROCK\"")).isEqualTo(Roshambo.ROCK);
        assertThat(adapter.toJson(Roshambo.PAPER)).isEqualTo("\"PAPER\"");
    }

    @Test
    public void annotatedEnum() throws Exception {
        JsonAdapter<Roshambo> adapter = moshi.adapter(Roshambo.class).lenient();
        assertThat(adapter.fromJson("\"scr\"")).isEqualTo(Roshambo.SCISSORS);
        assertThat(adapter.toJson(Roshambo.SCISSORS)).isEqualTo("\"scr\"");
    }

    @Test
    public void invalidEnum() throws Exception {
        JsonAdapter<Roshambo> adapter = moshi.adapter(Roshambo.class).lenient();
        assertThat(adapter.fromJson("\"SPOCK\"")).isNull();
    }

    @Test
    public void nullEnum() throws Exception {
        JsonAdapter<Roshambo> adapter = moshi.adapter(Roshambo.class).lenient();
        assertThat(adapter.fromJson("null")).isNull();
        assertThat(adapter.toJson(null)).isEqualTo("null");
    }

    enum Roshambo {
        ROCK,
        PAPER,
        @Json(name = "scr")SCISSORS
    }
}
