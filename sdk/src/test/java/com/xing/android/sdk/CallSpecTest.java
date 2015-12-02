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

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.xing.android.sdk.internal.Http;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import okio.Buffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @author serj.lotutovici
 */
public class CallSpecTest {
    private XingApi mockApi;

    @Before
    public void setUp() throws Exception {
        mockApi = new XingApi.Builder()
              .apiEndpoint("http://test.test")
              .loggedOut()
              .build();
    }

    @After
    public void tearDown() throws Exception {
        mockApi = null;
    }

    @Test
    public void builderAcceptsPathParams() throws Exception {
        CallSpec.Builder<Object, Object> builder = builder(Http.HTTP_GET, "/{param1}/{param2}", false)
              .pathParam("param1", "test1")
              .pathParam("param2", "test2");
        builder.build();

        Request request = builder.request();
        assertThat(request.method()).isEqualTo(Http.HTTP_GET);
        assertThat(request.urlString()).isEqualTo("http://test.test/test1/test2");
        assertThat(request.body()).isNull();
    }

    @Test
    public void builderFailsOnMalformedParams() throws Exception {
        CallSpec.Builder builder = builder(Http.HTTP_GET, "/{%sdf}/", false);
        try {
            builder.pathParam("%sdf", "any");
            fail("Builder should fail on malformed params.");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
            assertThat(e).hasMessageContaining("Path parameter name must match")
                  .hasMessageEndingWith("Found: %sdf");
        }
    }

    @Test
    public void builderFailsIfPathParamNotExpected() throws Exception {
        CallSpec.Builder builder = builder(Http.HTTP_GET, "/{test1}/", false);
        try {
            builder.pathParam("test2", "first");
            fail("Builder should fail on non expected params.");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class)
                  .hasMessageStartingWith("Resource path \"/{test1}/\" does not contain \"{test2}\".")
                  .hasMessageEndingWith("Or the path parameter has been already set.");
        }
    }

    @Test
    public void builderFailsIfPathParamsNotSet() throws Exception {
        CallSpec.Builder builder = builder(Http.HTTP_GET, "/{test2}", false);
        try {
            builder.build();
            fail("Builder should fail if path params not set.");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalStateException.class)
                  .hasMessage("Not all path params where set. Found 1 unsatisfied parameter(s)");
        }
    }

    @Test
    public void builderFailsOnAddingPathParamsAfterQueryParams() throws Exception {
        CallSpec.Builder builder = builder(Http.HTTP_GET, "/{test1}", false);
        builder.queryParam("q", "test");
        try {
            builder.pathParam("test1", "test");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalStateException.class)
                  .hasMessage("Path params must be set before query params.");
        }
    }

    @Test
    public void builderAttachesQueryParams() throws Exception {
        CallSpec.Builder builder = builder(Http.HTTP_GET, "", false).queryParam("q", "test1");
        // Build the CallSpec so that we don't test this behaviour twice.
        builder.build().queryParam("w", "test2");

        Request request = builder.request();
        assertThat(request.method()).isEqualTo(Http.HTTP_GET);
        assertThat(request.urlString()).isEqualTo("http://test.test/?q=test1&w=test2");
        assertThat(request.body()).isNull();
    }

    @Test
    public void builderAttachesFormFields() throws Exception {
        CallSpec.Builder builder = builder(Http.HTTP_PUT, "", true).formField("f", "true");
        // Build the CallSpec so that we don't test this behaviour twice.
        builder.build().formField("e", "false");

        Request request = builder.request();
        assertThat(request.method()).isEqualTo(Http.HTTP_PUT);
        assertThat(request.urlString()).isEqualTo("http://test.test/");
        assertThat(request.body()).isNotNull();

        RequestBody body = request.body();
        assertThat(body.contentType()).isEqualTo(MediaType.parse("application/x-www-form-urlencoded"));

        Buffer buffer = new Buffer();
        body.writeTo(buffer);
        assertThat(buffer.readUtf8()).isEqualTo("f=true&e=false");
    }

    private <RT, ET> CallSpec.Builder<RT, ET> builder(String http, String path, boolean formEncoded) {
        return new CallSpec.Builder<>(mockApi, http, path, !http.equals(Http.HTTP_GET), formEncoded);
    }
}
