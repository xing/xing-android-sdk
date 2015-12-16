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

package com.xing.android.sdk;

import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;
import com.xing.android.sdk.CompositeType.Structure;

import org.junit.Test;

import java.io.IOException;
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
        CompositeType compositeType = new CompositeType(TestData.class, Structure.SINGLE);
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
        CompositeType compositeType = new CompositeType(TestData.class, Structure.SINGLE, "findMe");
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
        CompositeType compositeType = new CompositeType(TestData.class, Structure.SINGLE, "findMe", "andMe");
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
        CompositeType compositeType = new CompositeType(TestData.class, Structure.SINGLE, "findMe", "andMe");
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
        CompositeType compositeType = new CompositeType(TestData.class, Structure.SINGLE, null);
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
        CompositeType compositeType = new CompositeType(TestData.class, Structure.LIST, "content");
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
        CompositeType compositeType = new CompositeType(TestData.class, Structure.LIST, "asdasdasd");
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
        CompositeType compositeType = new CompositeType(TestData.class, Structure.LIST);
        parse(compositeType, "{\n"
              + "  \"content\": []\n"
              + "}");
    }

    @Test(expected = JsonDataException.class)
    public void parseListObjectMultipleRoots() throws Exception {
        CompositeType compositeType = new CompositeType(TestData.class, Structure.LIST, "users", "companies");
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
        CompositeType compositeType = new CompositeType(TestData.class, Structure.SINGLE, "root");
        TestData data = parse(compositeType, "{\n"
              + "  \"root\": null\n"
              + "}");
        assertThat(data).isNull();
    }

    @Test
    public void parsesFirstInList() throws Exception {
        CompositeType compositeType = new CompositeType(TestData.class, Structure.FIRST, "content");
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
        CompositeType compositeType1 = new CompositeType(TestData.class, Structure.FIRST, "empty");
        assertThat(parse(compositeType1, "{\n"
              + "  \"empty\": null\n"
              + "}")).isNull();

        CompositeType compositeType2 = new CompositeType(TestData.class, Structure.FIRST, "empty");
        assertThat(parse(compositeType2, "{\n"
              + "  \"empty\": []\n"
              + "}")).isNull();
    }

    @Test
    public void parsesListOfCompositeType() throws Exception {
        CompositeType innerType = new CompositeType(TestData.class, Structure.SINGLE, "secondDegree");
        CompositeType compositeType = new CompositeType(innerType, Structure.LIST, "firstDegree");

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
    private <T> T parse(CompositeType type, String json) throws Exception {
        if (json == null) return null;
        return (T) moshi.adapter(type).fromJson(json);
    }

    static class TestData {
        String str;
        int val;
    }
}
