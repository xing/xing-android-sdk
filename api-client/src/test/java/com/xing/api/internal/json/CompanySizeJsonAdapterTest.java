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

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.xing.api.model.user.CompanySize;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import okio.Buffer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author daniel.hartwich
 */
public class CompanySizeJsonAdapterTest {
    public static final String COMPANY_SIZE_1 = "{\n"
          + "  \"company_size\":\"1\"\n"
          + '}';
    public static final String COMPANY_SIZE_1_10 = "{\n"
          + "  \"company_size\":\"1-10\"\n"
          + '}';
    public static final String COMPANY_SIZE_11_50 = "{\n"
          + "  \"company_size\":\"11-50\"\n"
          + '}';
    public static final String COMPANY_SIZE_51_200 = "{\n"
          + "  \"company_size\":\"51-200\"\n"
          + '}';
    public static final String COMPANY_SIZE_201_500 = "{\n"
          + "  \"company_size\":\"201-500\"\n"
          + '}';
    public static final String COMPANY_SIZE_501_1000 = "{\n"
          + "  \"company_size\":\"501-1000\"\n"
          + '}';
    public static final String COMPANY_SIZE_1001_5000 = "{\n"
          + "  \"company_size\":\"1001-5000\"\n"
          + '}';
    public static final String COMPANY_SIZE_5001_10000 = "{\n"
          + "  \"company_size\":\"5001-10000\"\n"
          + '}';
    public static final String COMPANY_SIZE_10001PLUS = "{\n"
          + "  \"company_size\":\"10001+\"\n"
          + '}';
    public static final String COMPANY_SIZE_101_200 = "{\n"
          + "  \"company_size\":\"123\"\n"
          + '}';

    private Moshi moshi;

    @Before
    public void setUp() throws Exception {
        moshi = new Moshi.Builder()
              .add(CompanySizeJsonAdapter.FACTORY)
              .build();
    }

    @After
    public void tearDown() throws Exception {
        moshi = null;
    }

    @Test
    public void testFromJson() throws Exception {
        JsonAdapter<CompanySizeHelper> adapter = moshi.adapter(CompanySizeHelper.class);

        CompanySizeHelper sizeHelper1 = adapter.fromJson(COMPANY_SIZE_1);
        assertThat(sizeHelper1.companySize).isEqualTo(CompanySize.SIZE_1);

        CompanySizeHelper sizeHelper1_10 = adapter.fromJson(COMPANY_SIZE_1_10);
        assertThat(sizeHelper1_10.companySize).isEqualTo(CompanySize.SIZE_1_10);

        CompanySizeHelper sizeHelper11_50 = adapter.fromJson(COMPANY_SIZE_11_50);
        assertThat(sizeHelper11_50.companySize).isEqualTo(CompanySize.SIZE_11_50);

        CompanySizeHelper sizeHelper51_200 = adapter.fromJson(COMPANY_SIZE_51_200);
        assertThat(sizeHelper51_200.companySize).isEqualTo(CompanySize.SIZE_51_200);

        CompanySizeHelper sizeHelper201_500 = adapter.fromJson(COMPANY_SIZE_201_500);
        assertThat(sizeHelper201_500.companySize).isEqualTo(CompanySize.SIZE_201_500);

        CompanySizeHelper sizeHelper501_1000 = adapter.fromJson(COMPANY_SIZE_501_1000);
        assertThat(sizeHelper501_1000.companySize).isEqualTo(CompanySize.SIZE_501_1000);

        CompanySizeHelper sizeHelper1001_5000 = adapter.fromJson(COMPANY_SIZE_1001_5000);
        assertThat(sizeHelper1001_5000.companySize).isEqualTo(CompanySize.SIZE_1001_5000);

        CompanySizeHelper sizeHelper5001_10000 = adapter.fromJson(COMPANY_SIZE_5001_10000);
        assertThat(sizeHelper5001_10000.companySize).isEqualTo(CompanySize.SIZE_5001_10000);

        CompanySizeHelper sizeHelper10001_plus = adapter.fromJson(COMPANY_SIZE_10001PLUS);
        assertThat(sizeHelper10001_plus.companySize).isEqualTo(CompanySize.SIZE_10001PLUS);

        CompanySizeHelper sizeHelperError = adapter.fromJson(COMPANY_SIZE_101_200);
        assertThat(sizeHelperError.companySize).isNull();
    }

    @Test
    public void testToJson() throws Exception {
        Buffer buffer = new Buffer();
        JsonWriter writer = JsonWriter.of(buffer);
        writer.setLenient(true);
        moshi.adapter(CompanySize.class).toJson(writer, CompanySize.SIZE_10001PLUS);
        writer.flush();
        String bufferedString = buffer.readUtf8();
        assertThat(bufferedString).isEqualTo('"' + CompanySize.SIZE_10001PLUS.toString() + '"');
    }

    static class CompanySizeHelper {
        @Json(name = "company_size")
        public CompanySize companySize;
    }
}
