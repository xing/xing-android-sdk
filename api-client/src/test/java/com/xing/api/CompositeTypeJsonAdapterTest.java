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
package com.xing.api;

import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;
import com.xing.api.CompositeType.Structure;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @author daniel.hartwich
 */
@SuppressWarnings({"ConstantConditions", "MagicNumber", "NullArgumentToVariableArgMethod"})
public class CompositeTypeJsonAdapterTest {
    // We can leverage form CompositeType only if it's tied with moshi.
    private final Moshi moshi = new Moshi.Builder().add(CompositeTypeJsonAdapter.FACTORY).build();

    @Test
    public void parsesSingleObjects() throws Exception {
        Type compositeType = compose(TestData.class, Structure.SINGLE);
        TestData data = parse(compositeType, "{\n"
              + "  \"str\": \"test\",\n"
              + "  \"val\": 42\n"
              + '}');

        assertThat(data).isNotNull();
        assertThat(data.str).isEqualTo("test");
        assertThat(data.val).isEqualTo(42);
    }

    @Test
    public void parsesSingleObjectsWithRoots() throws Exception {
        Type compositeType = compose(TestData.class, Structure.SINGLE, "findMe");
        TestData data = parse(compositeType, "{\n"
              + "  \"findMe\": {\n"
              + "    \"str\": \"test1\",\n"
              + "    \"val\": 43\n"
              + "  }\n"
              + '}');

        assertThat(data).isNotNull();
        assertThat(data.str).isEqualTo("test1");
        assertThat(data.val).isEqualTo(43);
    }

    @Test
    public void parsesSingleObjectsWithMultipleRoots() throws Exception {
        Type compositeType = compose(TestData.class, Structure.SINGLE, "findMe", "andMe");
        TestData data = parse(compositeType, "{\n"
              + "  \"findMe\": {\n"
              + "    \"andMe\": {\n"
              + "      \"str\": \"test2\",\n"
              + "      \"val\": 44\n"
              + "    }\n"
              + "  }\n"
              + '}');

        assertThat(data).isNotNull();
        assertThat(data.str).isEqualTo("test2");
        assertThat(data.val).isEqualTo(44);
    }

    @Test
    public void parseSingleObjectsWithNullObjectInside() throws Exception {
        Type compositeType = compose(TestData.class, Structure.SINGLE, "findMe", "andMe");
        TestData data = parse(compositeType, "{\n"
              + "  \"findMe\": {\n"
              + "    \"andMe\": {\n"
              + "    }\n"
              + "  }\n"
              + '}');

        assertThat(data).isNotNull();
        assertThat(data.str).isNullOrEmpty();
        assertThat(data.val).isEqualTo(0);
    }

    @Test
    public void parseSingleObjectsWithRootsNull() throws Exception {
        Type compositeType = compose(TestData.class, Structure.SINGLE, null);
        TestData data = parse(compositeType, "{\n"
              + "  \"findMe\": {\n"
              + "    \"andMe\": {\n"
              + "    }\n"
              + "  }\n"
              + '}');

        assertThat(data).isNotNull();
        assertThat(data.str).isNullOrEmpty();
        assertThat(data.val).isEqualTo(0);
    }

    @Test
    public void parseListObject() throws Exception {
        Type compositeType = compose(TestData.class, Structure.LIST, "content");
        List<TestData> listData = parse(compositeType, "{\n"
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

        assertThat(listData).isNotNull();
        assertThat(listData).hasSize(3);
        assertThat(listData.get(0).str).isEqualTo("zero");
        assertThat(listData.get(1).str).isEqualTo("one");
        assertThat(listData.get(2).str).isEqualTo("two");
        assertThat(listData.get(0).val).isEqualTo(0);
        assertThat(listData.get(1).val).isEqualTo(1);
        assertThat(listData.get(2).val).isEqualTo(2);
    }

    @Test
    public void parseListObjectWrongRoots() throws Exception {
        Type compositeType = compose(TestData.class, Structure.LIST, "asdasdasd");
        try {
            parse(compositeType, "{\n"
                  + "  \"content\": []\n"
                  + "}");

            fail("Adapter should throw.");
        } catch (IOException ioe) {
            assertThat(ioe).hasMessage("Json does not match expected structure for roots [asdasdasd].");
        }
    }

    @Test(expected = JsonDataException.class)
    public void parseListObjectWithoutRoot() throws Exception {
        Type compositeType = compose(TestData.class, Structure.LIST);
        parse(compositeType, "{\n"
              + "  \"content\": []\n"
              + "}");
    }

    @Test(expected = JsonDataException.class)
    public void parseListObjectMultipleRoots() throws Exception {
        Type compositeType = compose(TestData.class, Structure.LIST, "users", "companies");
        parse(compositeType, "{\n"
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
        Type compositeType = compose(TestData.class, Structure.SINGLE, "root");
        TestData data = parse(compositeType, "{\n"
              + "  \"root\": null\n"
              + "}");
        assertThat(data).isNull();
    }

    @Test
    public void parsesFirstInList() throws Exception {
        Type compositeType = compose(TestData.class, Structure.FIRST, "content");
        TestData data = parse(compositeType, "{\n"
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
    public void parsesFirstAsNullIfListEmpty() throws Exception {
        Type compositeType1 = compose(TestData.class, Structure.FIRST, "empty");
        assertThat(parse(compositeType1, "{\n"
              + "  \"empty\": null\n"
              + "}")).isNull();

        Type compositeType2 = compose(TestData.class, Structure.FIRST, "empty");
        assertThat(parse(compositeType2, "{\n"
              + "  \"empty\": []\n"
              + "}")).isNull();
    }

    @Test
    public void parsesListOfCompositeType() throws Exception {
        Type innerType = compose(TestData.class, Structure.SINGLE, "secondDegree");
        Type compositeType = compose(innerType, Structure.LIST, "firstDegree");

        List<TestData> listData = parse(compositeType, "{\n"
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
              + "}");

        assertThat(listData).isNotNull();
        assertThat(listData.size()).isEqualTo(2);
        assertThat(listData.get(0).str).isEqualTo("one");
        assertThat(listData.get(0).val).isEqualTo(1);
        assertThat(listData.get(1).str).isEqualTo("two");
        assertThat(listData.get(1).val).isEqualTo(2);
    }

    @SuppressWarnings("unchecked") // This is the callers responsibility.
    private <T> T parse(Type type, String json) throws Exception {
        if (json == null) return null;
        return (T) moshi.adapter(type).fromJson(json);
    }
    
    private static Type compose(Type searchFor, Structure structure, String... roots) {
        return new CompositeType(null, searchFor, structure, roots);
    }

    static class TestData {
        String str;
        int val;
    }
}
