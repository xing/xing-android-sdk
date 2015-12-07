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

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.xing.android.sdk.internal.HttpMethod;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import okio.Buffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @author serj.lotutovici
 */
public class CallSpecTest {
    @Rule
    public final MockWebServer server = new MockWebServer();
    public XingApi mockApi;
    public HttpUrl httpUrl;

    @Before
    public void setUp() throws Exception {
        httpUrl = server.url("/");
        mockApi = new XingApi.Builder()
              .apiEndpoint(httpUrl)
              .loggedOut()
              .build();
    }

    @After
    public void tearDown() throws Exception {
        mockApi = null;
    }

    @Test
    public void builderAcceptsPathParams() throws Exception {
        CallSpec.Builder builder = builder(HttpMethod.GET, "/{param1}/{param2}", false)
              .responseAs(Object.class)
              .pathParam("param1", "test1")
              .pathParam("param2", "test2");
        builder.build();

        Request request = builder.request();
        assertThat(request.method()).isEqualTo(HttpMethod.GET.method());
        assertThat(request.urlString()).isEqualTo(httpUrl + "test1/test2");
        assertThat(request.body()).isNull();
    }

    @Test
    public void builderFailsOnMalformedParams() throws Exception {
        CallSpec.Builder builder = builder(HttpMethod.GET, "/{%sdf}/", false);
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
        CallSpec.Builder builder = builder(HttpMethod.GET, "/{test1}/", false);
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
        CallSpec.Builder builder = builder(HttpMethod.GET, "/{test2}", false);
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
        CallSpec.Builder builder = builder(HttpMethod.GET, "/{test1}", false);
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
        CallSpec.Builder builder = builder(HttpMethod.GET, "", false).responseAs(Object.class).queryParam("q", "test1");
        // Build the CallSpec so that we don't test this behaviour twice.
        builder.build().queryParam("w", "test2");

        Request request = builder.request();
        assertThat(request.method()).isEqualTo(HttpMethod.GET.method());
        assertThat(request.urlString()).isEqualTo(httpUrl + "?q=test1&w=test2");
        assertThat(request.body()).isNull();
    }

    @Test
    public void builderAttachesFormFields() throws Exception {
        CallSpec.Builder builder = builder(HttpMethod.PUT, "", true).responseAs(Object.class).formField("f", "true");
        // Build the CallSpec so that we don't test this behaviour twice.
        builder.build().formField("e", "false");

        Request request = builder.request();
        assertThat(request.method()).isEqualTo(HttpMethod.PUT.method());
        assertThat(request.httpUrl()).isEqualTo(httpUrl);
        assertThat(request.body()).isNotNull();

        RequestBody body = request.body();
        assertThat(body.contentType()).isEqualTo(MediaType.parse("application/x-www-form-urlencoded"));

        Buffer buffer = new Buffer();
        body.writeTo(buffer);
        assertThat(buffer.readUtf8()).isEqualTo("f=true&e=false");
    }

    @Test
    public void builderEnsuresEmptyBody() throws Exception {
        CallSpec.Builder builder = builder(HttpMethod.PUT, "", false).responseAs(Object.class);
        // Build the CallSpec so that we can build the request.
        builder.build();

        Request request = builder.request();
        assertThat(request.method()).isEqualTo(HttpMethod.PUT.method());
        assertThat(request.httpUrl()).isEqualTo(httpUrl);
        assertThat(request.body()).isNotNull();

        RequestBody body = request.body();
        assertThat(body.contentType()).isNull();
        assertThat(body.contentLength()).isEqualTo(0);

        Buffer buffer = new Buffer();
        body.writeTo(buffer);
        assertThat(buffer.readUtf8()).isEmpty();
    }

    private <RT, ET> CallSpec.Builder<RT, ET> builder(HttpMethod httpMethod, String path, boolean formEncoded) {
        return new CallSpec.Builder<>(mockApi, httpMethod, path, formEncoded);
    }
}
