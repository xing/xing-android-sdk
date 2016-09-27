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

import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;
import com.xing.api.CompositeType.Structure;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * @author daniel.hartwich
 */
@SuppressWarnings({"ConstantConditions", "MagicNumber", "NullArgumentToVariableArgMethod"})
public class CompositeTypeJsonAdapterTest {
    // We can leverage form CompositeType only if it's tied with moshi.
    private final Moshi moshi = new Moshi.Builder().add(CompositeTypeJsonAdapter.FACTORY).build();

    @Test
    public void singleObjects() throws Exception {
        Type type = compose(TestData.class, Structure.SINGLE);
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
        Type type = compose(TestData.class, Structure.SINGLE, "findMe");
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
        Type type = compose(TestData.class, Structure.SINGLE, "findMe", "andMe");
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
        Type type = compose(TestData.class, Structure.SINGLE, "findMe", "andMe");
        TestData fromJson = fromJson(type, "{\n"
              + "  \"findMe\": {\n"
              + "    \"andMe\": {\n"
              + "    }\n"
              + "  }\n"
              + '}');

        assertThat(fromJson).isNotNull();
        assertThat(fromJson.str).isNullOrEmpty();
        assertThat(fromJson.val).isEqualTo(0);

        String toJson = toJson(compose(Object.class, Structure.SINGLE, "findMe", "andMe"), new Object());
        assertThat(toJson).isEqualTo("{\"findMe\":{\"andMe\":{}}}");
    }

    @Test
    public void singleObjectsWithRootsNull() throws Exception {
        Type type = compose(TestData.class, Structure.SINGLE, (String[]) null);
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
        Type type = compose(TestData.class, Structure.LIST, "content");
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
        Type type = compose(TestData.class, Structure.LIST, "asdasdasd");
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
        Type type = compose(TestData.class, Structure.LIST);
        fromJson(type, "{\n"
              + "  \"content\": []\n"
              + '}');
    }

    @Test(expected = JsonDataException.class)
    public void listObjectMultipleRoots() throws Exception {
        Type type = compose(TestData.class, Structure.LIST, "users", "companies");
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
        Type type = compose(TestData.class, Structure.SINGLE, "root");
        TestData fromJson = fromJson(type, "{\n"
              + "  \"root\": null\n"
              + '}');
        assertThat(fromJson).isNull();
    }

    @Test
    public void firstInList() throws Exception {
        Type compositeType = compose(TestData.class, Structure.FIRST, "content");
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
        Type compositeType1 = compose(TestData.class, Structure.FIRST, "empty");
        assertNull(fromJson(compositeType1, "{\n"
              + "  \"empty\": null\n"
              + '}'));

        Type compositeType2 = compose(TestData.class, Structure.FIRST, "empty");
        assertNull(fromJson(compositeType2, "{\n"
              + "  \"empty\": []\n"
              + '}'));
    }

    @Test
    public void listOfCompositeType() throws Exception {
        Type innerType = compose(TestData.class, Structure.SINGLE, "secondDegree");
        Type type = compose(innerType, Structure.LIST, "firstDegree");

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

    @SuppressWarnings("unchecked") // This is the callers responsibility.
    private <T> T fromJson(Type type, String json) throws Exception {
        if (json == null) return null;
        return (T) moshi.adapter(type).fromJson(json);
    }

    private <T> String toJson(Type type, T value) throws Exception {
        return moshi.adapter(type).toJson(value);
    }

    private static Type compose(Type searchFor, Structure structure, String... roots) {
        return new CompositeType(null, searchFor, structure, roots);
    }

    static class TestData {
        String str;
        int val;
    }
}
