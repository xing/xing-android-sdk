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
package com.xing.api;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * @author daniel.hartwich
 */
@SuppressWarnings({"ConstantConditions", "MagicNumber", "NullArgumentToVariableArgMethod"})
public class ConverterTest {
    private Moshi moshi;
    private Converter converter;

    @Before
    public void setUp() throws Exception {
        moshi = new Moshi.Builder().build();
        converter = new Converter(moshi);
    }

    @After
    public void tearDown() throws Exception {
        converter = null;
        moshi = null;
    }

    @Test
    public void singleObjects() throws Exception {
        Type type = Converter.single(TestData.class);
        TestData fromJson = fromJson(type, "{\n"
              + "  \"str\": \"test\",\n"
              + "  \"val\": 42\n"
              + '}');

        assertThat(fromJson).isNotNull();
        assertThat(fromJson.str).isEqualTo("test");
        assertThat(fromJson.val).isEqualTo(42);

        String toJson = toJson(type, fromJson);
        assertThat(toJson).isEqualTo("{\"str\":\"test\",\"val\":42}");
    }

    @Test
    public void singleObjectsWithRoots() throws Exception {
        Type type = Converter.single(TestData.class, "findMe");
        TestData fromJson = fromJson(type, "{\n"
              + "  \"findMe\": {\n"
              + "    \"str\": \"test1\",\n"
              + "    \"val\": 43\n"
              + "  }\n"
              + '}');

        assertThat(fromJson).isNotNull();
        assertThat(fromJson.str).isEqualTo("test1");
        assertThat(fromJson.val).isEqualTo(43);

        String toJson = toJson(type, fromJson);
        assertThat(toJson).isEqualTo("{\"findMe\":{\"str\":\"test1\",\"val\":43}}");
    }

    @Test
    public void singleObjectsWithMultipleRoots() throws Exception {
        Type type = Converter.single(TestData.class, "findMe", "andMe");
        TestData fromJson = fromJson(type, "{\n"
              + "  \"findMe\": {\n"
              + "    \"andMe\": {\n"
              + "      \"str\": \"test2\",\n"
              + "      \"val\": 44\n"
              + "    }\n"
              + "  }\n"
              + '}');

        assertThat(fromJson).isNotNull();
        assertThat(fromJson.str).isEqualTo("test2");
        assertThat(fromJson.val).isEqualTo(44);

        String toJson = toJson(type, fromJson);
        assertThat(toJson).isEqualTo("{\"findMe\":{\"andMe\":{\"str\":\"test2\",\"val\":44}}}");
    }

    @Test
    public void singleObjectsWithNullObjectInside() throws Exception {
        Type type = Converter.single(TestData.class, "findMe", "andMe");
        TestData fromJson = fromJson(type, "{\n"
              + "  \"findMe\": {\n"
              + "    \"andMe\": {\n"
              + "    }\n"
              + "  }\n"
              + '}');

        assertThat(fromJson).isNotNull();
        assertThat(fromJson.str).isNullOrEmpty();
        assertThat(fromJson.val).isEqualTo(0);

        String toJson = toJson(Converter.single(Object.class, "findMe", "andMe"), new Object());
        assertThat(toJson).isEqualTo("{\"findMe\":{\"andMe\":{}}}");
    }

    @Test
    public void singleObjectsWithRootsNull() throws Exception {
        Type type = Converter.single(TestData.class, (String[]) null);
        TestData fromJson = fromJson(type, "{\n"
              + "  \"findMe\": {\n"
              + "    \"andMe\": {\n"
              + "    }\n"
              + "  }\n"
              + '}');

        assertThat(fromJson).isNotNull();
        assertThat(fromJson.str).isNullOrEmpty();
        assertThat(fromJson.val).isEqualTo(0);

        String toJson = toJson(type, fromJson);
        assertThat(toJson).isEqualTo("{\"val\":0}");
    }

    @Test
    public void listObject() throws Exception {
        Type type = Converter.list(TestData.class, "content");
        List<TestData> fromJson = fromJson(type, "{\n"
              + "  \"content\": [\n"
              + "    {\n"
              + "      \"str\": \"zero\",\n"
              + "      \"val\": 0\n"
              + "    },\n"
              + "    {\n"
              + "      \"str\": \"one\",\n"
              + "      \"val\": 1\n"
              + "    },\n"
              + "    {\n"
              + "      \"str\": \"two\",\n"
              + "      \"val\": 2\n"
              + "    }\n"
              + "  ]\n"
              + '}');

        assertThat(fromJson).isNotNull();
        assertThat(fromJson).hasSize(3);
        assertThat(fromJson.get(0).str).isEqualTo("zero");
        assertThat(fromJson.get(1).str).isEqualTo("one");
        assertThat(fromJson.get(2).str).isEqualTo("two");
        assertThat(fromJson.get(0).val).isEqualTo(0);
        assertThat(fromJson.get(1).val).isEqualTo(1);
        assertThat(fromJson.get(2).val).isEqualTo(2);

        String toJson = toJson(type, fromJson);
        assertThat(toJson).isEqualTo("{\"content\":["
              + "{\"str\":\"zero\",\"val\":0},"
              + "{\"str\":\"one\",\"val\":1},"
              + "{\"str\":\"two\",\"val\":2}]"
              + '}');
    }

    @Test
    public void listObjectWrongRoots() throws Exception {
        Type type = Converter.list(TestData.class, "asdasdasd");
        try {
            fromJson(type, "{\n"
                  + "  \"content\": []\n"
                  + '}');

            fail();
        } catch (IOException ioe) {
            assertThat(ioe).hasMessage("Json does not match expected structure for roots [asdasdasd].");
        }
    }

    @Test(expected = JsonDataException.class)
    public void listObjectWithoutRoot() throws Exception {
        Type type = Converter.list(TestData.class);
        fromJson(type, "{\n"
              + "  \"content\": []\n"
              + '}');
    }

    @Test(expected = JsonDataException.class)
    public void listObjectMultipleRoots() throws Exception {
        Type type = Converter.list(TestData.class, "users", "companies");
        fromJson(type, "{\n"
              + "  \"users\": [\n"
              + "    {\n"
              + "      \"companies\": [\n"
              + "        {\n"
              + "          \"str\": \"company1\",\n"
              + "          \"val\": 1\n"
              + "        },\n"
              + "        {\n"
              + "          \"str\": \"company1\",\n"
              + "          \"val\": 1\n"
              + "        },\n"
              + "        {\n"
              + "          \"str\": \"company1\",\n"
              + "          \"val\": 1\n"
              + "        }\n"
              + "      ]\n"
              + "    }\n"
              + "  ]\n"
              + '}');
    }

    @Test
    public void objectWithNull() throws Exception {
        Type type = Converter.single(TestData.class, "root");
        TestData fromJson = fromJson(type, "{\n"
              + "  \"root\": null\n"
              + '}');
        assertThat(fromJson).isNull();
    }

    @Test
    public void firstInList() throws Exception {
        Type compositeType = Converter.first(TestData.class, "content");
        TestData data = fromJson(compositeType, "{\n"
              + "  \"content\": [\n"
              + "    {\n"
              + "      \"str\": \"zero\",\n"
              + "      \"val\": 0\n"
              + "    },\n"
              + "    {\n"
              + "      \"str\": \"one\",\n"
              + "      \"val\": 1\n"
              + "    },\n"
              + "    {\n"
              + "      \"str\": \"two\",\n"
              + "      \"val\": 2\n"
              + "    }\n"
              + "  ]\n"
              + '}');

        assertThat(data).isNotNull();
        assertThat(data.str).isEqualTo("zero");
        assertThat(data.val).isEqualTo(0);
    }

    @Test
    public void firstAsNullIfListEmpty() throws Exception {
        Type compositeType1 = Converter.first(TestData.class, "empty");
        assertThat(fromJson(compositeType1, "{\n"
              + "  \"empty\": null\n"
              + '}')).isNull();

        Type compositeType2 = Converter.first(TestData.class, "empty");
        assertThat(fromJson(compositeType2, "{\n"
              + "  \"empty\": []\n"
              + '}')).isNull();
    }

    @Test
    public void listOfCompositeType() throws Exception {
        Type innerType = Converter.single(TestData.class, "secondDegree");
        Type type = Converter.list(innerType, "firstDegree");

        List<TestData> fromJson = fromJson(type, "{\n"
              + "  \"firstDegree\": [\n"
              + "    {\n"
              + "      \"secondDegree\": {\n"
              + "        \"str\": \"one\",\n"
              + "        \"val\": 1\n"
              + "      }\n"
              + "    },\n"
              + "    {\n"
              + "      \"secondDegree\": {\n"
              + "        \"str\": \"two\",\n"
              + "        \"val\": 2\n"
              + "      }\n"
              + "    }\n"
              + "  ]\n"
              + '}');

        assertThat(fromJson).isNotNull();
        assertThat(fromJson.size()).isEqualTo(2);
        assertThat(fromJson.get(0).str).isEqualTo("one");
        assertThat(fromJson.get(0).val).isEqualTo(1);
        assertThat(fromJson.get(1).str).isEqualTo("two");
        assertThat(fromJson.get(1).val).isEqualTo(2);

        String toJson = toJson(type, fromJson);
        assertThat(toJson).isEqualTo("{\"firstDegree\":["
              + "{\"secondDegree\":{\"str\":\"one\",\"val\":1}},"
              + "{\"secondDegree\":{\"str\":\"two\",\"val\":2}}"
              + "]}");
    }

    @Test
    public void cachingJsonAdapters() throws Exception {
        Type type = Converter.list(String.class, "one", "two");
        JsonAdapter<String> adapter1 = converter.findAdapter(type);
        JsonAdapter<String> adapter2 = converter.findAdapter(type);
        assertThat(adapter1).isSameAs(adapter2);
    }

    @SuppressWarnings("unchecked") // This is the callers responsibility.
    private <T> T fromJson(Type type, String json) throws Exception {
        if (json == null) return null;
        return (T) converter.convertFromBody(type, ResponseBody.create(Converter.MEDIA_TYPE_JSON, json));
    }

    private <T> String toJson(Type type, T value) throws Exception {
        RequestBody requestBody = converter.convertToBody(type, value);
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        return buffer.readUtf8();
    }

    static class TestData {
        String str;
        int val;
    }
}
