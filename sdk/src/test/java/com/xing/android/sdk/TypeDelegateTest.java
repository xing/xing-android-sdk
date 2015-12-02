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
import com.squareup.okhttp.ResponseBody;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @author daniel.hartwich
 */
@SuppressWarnings({"ConstantConditions", "MagicNumber", "NullArgumentToVariableArgMethod"})
public class TypeDelegateTest {
    private static final String SINGLE_TEST_CONTENT = "{\n"
          + "  \"str\": \"test\",\n"
          + "  \"val\": 42\n"
          + '}';

    private static final String SINGLE_ONE_ROOT_TEST_CONTENT = "{\n"
          + "  \"findMe\": {\n"
          + "    \"str\": \"test1\",\n"
          + "    \"val\": 43\n"
          + "  }\n"
          + '}';

    private static final String SINGLE_MULTIPLE_ROOT_TEST_CONTENT = "{\n"
          + "  \"findMe\": {\n"
          + "    \"andMe\": {\n"
          + "      \"str\": \"test2\",\n"
          + "      \"val\": 44\n"
          + "    }\n"
          + "  }\n"
          + '}';

    private static final String SINGLE_MULTIPLE_ROOT_WITH_NULL_TEST_CONTENT = "{\n"
          + "  \"findMe\": {\n"
          + "    \"andMe\": {\n"
          + "    }\n"
          + "  }\n"
          + '}';
    private static final String LIST_TEST_CONTENT = "{\n"
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
          + '}';

    private static final String LIST_TEST_CONTENT_MULTIPLE_ROOTS_RIGHT = "{\n"
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
          + '}';

    private static final String SINGLE_TEST_WITH_NULL_OBJECT = "{\n"
          + "  \"root\": null\n"
          + "}";

    private Moshi moshi;

    @Before
    public void setUp() throws Exception {
        moshi = new Moshi.Builder().build();
    }

    @Test
    public void typeDelegateParsesSingleObjects() throws Exception {
        ResponseBody body = ResponseBody.create(CallSpec.Builder.MEDIA_TYPE, SINGLE_TEST_CONTENT);
        TypeDelegate delegate = TypeDelegate.single(TestData.class);

        TestData data = delegate.from(moshi, body);
        assertThat(data).isNotNull();
        assertThat(data.str).isEqualTo("test");
        assertThat(data.val).isEqualTo(42);
    }

    @Test
    public void typeDelegateParsesSingleObjectsWithRoots() throws Exception {
        ResponseBody body = ResponseBody.create(CallSpec.Builder.MEDIA_TYPE, SINGLE_ONE_ROOT_TEST_CONTENT);
        TypeDelegate delegate = TypeDelegate.single(TestData.class, "findMe");

        TestData data = delegate.from(moshi, body);
        assertThat(data).isNotNull();
        assertThat(data.str).isEqualTo("test1");
        assertThat(data.val).isEqualTo(43);
    }

    @Test
    public void typeDelegateParsesSingleObjectsWithMultipleRoots() throws Exception {
        ResponseBody body = ResponseBody.create(CallSpec.Builder.MEDIA_TYPE, SINGLE_MULTIPLE_ROOT_TEST_CONTENT);
        TypeDelegate delegate = TypeDelegate.single(TestData.class, "findMe", "andMe");

        TestData data = delegate.from(moshi, body);
        assertThat(data).isNotNull();
        assertThat(data.str).isEqualTo("test2");
        assertThat(data.val).isEqualTo(44);
    }

    @Test
    public void typeDelegateParseSingleObjectsWithNullObjectInside() throws Exception {
        ResponseBody body = ResponseBody.create(CallSpec.Builder.MEDIA_TYPE, SINGLE_MULTIPLE_ROOT_WITH_NULL_TEST_CONTENT);
        TypeDelegate delegate = TypeDelegate.single(TestData.class, "findMe", "andMe");

        TestData data = delegate.from(moshi, body);
        assertThat(data).isNotNull();
        assertThat(data.str).isNullOrEmpty();
        assertThat(data.val).isEqualTo(0);
    }

    @Test
    public void typeDelegateParseSingleObjectsWithRootsNull() throws Exception {
        ResponseBody body = ResponseBody.create(CallSpec.Builder.MEDIA_TYPE, SINGLE_MULTIPLE_ROOT_WITH_NULL_TEST_CONTENT);
        TypeDelegate delegate = TypeDelegate.single(TestData.class, null);

        TestData data = delegate.from(moshi, body);
        assertThat(data).isNotNull();
        assertThat(data.str).isNullOrEmpty();
        assertThat(data.val).isEqualTo(0);
    }

    @Test
    public void typeDelegateParseListObject() throws Exception {
        ResponseBody body = ResponseBody.create(CallSpec.Builder.MEDIA_TYPE, LIST_TEST_CONTENT);
        TypeDelegate delegate = TypeDelegate.list(TestData.class, "content");

        List<TestData> listData = delegate.from(moshi, body);
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
    public void typeDelegateParseListObjectWrongRoots() throws Exception {
        ResponseBody body = ResponseBody.create(CallSpec.Builder.MEDIA_TYPE, LIST_TEST_CONTENT);
        TypeDelegate delegate = TypeDelegate.list(TestData.class, "asdasdasd");

        try {
            delegate.from(moshi, body);
            fail("This should not go here...");
        } catch (IOException ioe) {
            assertThat(ioe).hasMessage("Json does not match expected structure for roots [asdasdasd].");
        }
    }

    @Test(expected = JsonDataException.class)
    public void typeDelegateParseListObjectWithoutRoot() throws Exception {
        ResponseBody body = ResponseBody.create(CallSpec.Builder.MEDIA_TYPE, LIST_TEST_CONTENT);
        TypeDelegate delegate = TypeDelegate.list(TestData.class);

        List<TestData> listData = delegate.from(moshi, body);
        assertThat(listData).isNotNull();
        assertThat(listData).hasSize(3);
        assertThat(listData.get(0).str).isEqualTo("zero");
        assertThat(listData.get(1).str).isEqualTo("one");
        assertThat(listData.get(2).str).isEqualTo("two");
        assertThat(listData.get(0).val).isEqualTo(0);
        assertThat(listData.get(1).val).isEqualTo(1);
        assertThat(listData.get(2).val).isEqualTo(2);
    }

    @Test(expected = JsonDataException.class)
    public void typeDelegateParseListObjectMultipleRoots() throws Exception {
        ResponseBody body = ResponseBody.create(CallSpec.Builder.MEDIA_TYPE, LIST_TEST_CONTENT_MULTIPLE_ROOTS_RIGHT);
        TypeDelegate delegate = TypeDelegate.list(TestData.class, "users", "companies");
        delegate.from(moshi, body);
    }

    @Test
    public void typeDelegateObjectWithNull() throws Exception {
        ResponseBody body = ResponseBody.create(CallSpec.Builder.MEDIA_TYPE, SINGLE_TEST_WITH_NULL_OBJECT);
        TypeDelegate delegate = TypeDelegate.single(TestData.class, "root");
        TestData data = delegate.from(moshi, body);
        assertThat(data).isNull();
    }

    static class TestData {
        String str;
        int val;
    }
}
