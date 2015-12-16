/*
 * Copyright (c) 2015 XING AG (http://xing.com/)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.xing.android.sdk.internal.json;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.xing.android.sdk.model.user.CompanySize;

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
        assertThat(bufferedString).isEqualTo('"' + CompanySize.SIZE_10001PLUS.getJsonValue() + '"');
    }

    static class CompanySizeHelper {
        @Json(name = "company_size")
        public CompanySize companySize;
    }
}
