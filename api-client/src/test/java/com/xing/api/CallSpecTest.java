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

import com.squareup.okhttp.Call;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.SocketPolicy;
import com.xing.api.CallSpec.ExceptionCatchingRequestBody;
import com.xing.api.HttpError.Error.Reason;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import okio.Buffer;
import okio.BufferedSource;
import rx.observables.BlockingObservable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertNotNull;

/**
 * @author serj.lotutovici
 */
@SuppressWarnings({"MagicNumber", "ConstantConditions"})
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
    public void builderFailsWithoutSpec() throws Exception {
        try {
            builder(HttpMethod.DELETE, "", false).request();
            fail("#request() should fail");
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).isEqualTo("#request() can be called only after #build()");
        }
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
    public void builderAcceptsPathParamsAsVarList() throws Exception {
        CallSpec.Builder builder = builder(HttpMethod.GET, "/{params}", false)
              .responseAs(Object.class)
              .pathParam("params", "one", "two", "three");
        builder.build();

        Request request = builder.request();
        assertThat(request.method()).isEqualTo(HttpMethod.GET.method());
        assertThat(request.urlString()).isEqualTo(httpUrl + "one,two,three");
        assertThat(request.body()).isNull();
    }

    @Test
    public void builderAcceptsPathParamsAsList() throws Exception {
        List<String> params = new ArrayList<>(3);
        params.add("one");
        params.add("two");
        params.add("three");

        CallSpec.Builder builder = builder(HttpMethod.GET, "/{params}", false)
              .responseAs(Object.class)
              .pathParam("params", params);
        builder.build();

        Request request = builder.request();
        assertThat(request.method()).isEqualTo(HttpMethod.GET.method());
        assertThat(request.urlString()).isEqualTo(httpUrl + "one,two,three");
        assertThat(request.body()).isNull();
    }

    @Test
    public void builderAcceptsHeaders() throws Exception {
        CallSpec.Builder builder = builder(HttpMethod.GET, "/", false)
              .responseAs(Object.class)
              .header("Test1", "hello")
              .header("Test2", "hm");
        builder.build();

        Request request = builder.request();
        assertThat(request.method()).isEqualTo(HttpMethod.GET.method());
        assertThat(request.headers().names()).contains("Test1").contains("Test2");
        assertThat(request.headers().values("Test1")).isNotEmpty().hasSize(1).contains("hello");
        assertThat(request.headers().values("Test2")).isNotEmpty().hasSize(1).contains("hm");
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
        CallSpec.Builder builder = builder(HttpMethod.GET, "", false)
              .responseAs(Object.class)
              .queryParam("q", "test1");
        // Build the CallSpec so that we don't test this behaviour twice.
        builder.build().queryParam("w", "test2");

        Request request = builder.request();
        assertThat(request.method()).isEqualTo(HttpMethod.GET.method());
        assertThat(request.urlString()).isEqualTo(httpUrl + "?q=test1&w=test2");
        assertThat(request.body()).isNull();
    }

    @Test
    public void builderAttachesQueryParamsAsVarList() throws Exception {
        CallSpec.Builder builder = builder(HttpMethod.GET, "", false)
              .responseAs(Object.class)
              .queryParam("q", "testL", "testL");
        // Build the CallSpec so that we don't test this behaviour twice.
        builder.build().queryParam("w", "testL", "testL");

        Request request = builder.request();
        assertThat(request.method()).isEqualTo(HttpMethod.GET.method());
        assertThat(request.urlString()).isEqualTo(httpUrl + "?q=testL%2CtestL&w=testL%2CtestL");
        assertThat(request.body()).isNull();
    }

    @Test
    public void builderAttachesQueryParamsAsList() throws Exception {
        List<String> query = new ArrayList<>(2);
        query.add("testL");
        query.add("testL");

        CallSpec.Builder builder = builder(HttpMethod.GET, "", false)
              .responseAs(Object.class)
              .queryParam("q", query);
        // Build the CallSpec so that we don't test this behaviour twice.
        builder.build().queryParam("w", query);

        Request request = builder.request();
        assertThat(request.method()).isEqualTo(HttpMethod.GET.method());
        assertThat(request.urlString()).isEqualTo(httpUrl + "?q=testL%2CtestL&w=testL%2CtestL");
        assertThat(request.body()).isNull();
    }

    @Test
    public void builderAttachesFormFields() throws Exception {
        CallSpec.Builder builder = builder(HttpMethod.PUT, "", true)
              .responseAs(Object.class)
              .formField("f", "true");
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
    public void builderAttachesFormFieldsAsLists() throws Exception {
        CallSpec.Builder builder = builder(HttpMethod.PUT, "", true)
              .responseAs(Object.class)
              .formField("f", "test1", "test2");
        // Build the CallSpec so that we don't test this behaviour twice.
        List<String> field = new ArrayList<>(2);
        field.add("test3");
        field.add("test4");
        builder.build().formField("e", field);

        Request request = builder.request();
        assertThat(request.method()).isEqualTo(HttpMethod.PUT.method());
        assertThat(request.httpUrl()).isEqualTo(httpUrl);
        assertThat(request.body()).isNotNull();

        RequestBody body = request.body();
        assertThat(body.contentType()).isEqualTo(MediaType.parse("application/x-www-form-urlencoded"));

        Buffer buffer = new Buffer();
        body.writeTo(buffer);
        assertThat(buffer.readUtf8()).isEqualTo("f=test1%2Ctest2&e=test3%2Ctest4");
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

    @Test
    public void builderAllowsRawBody() throws Exception {
        CallSpec.Builder builder = builder(HttpMethod.PUT, "", false).responseAs(Object.class)
              .body(RequestBody.create(MediaType.parse("application/text"), "Hey!"));
        // Build the CallSpec so that we can build the request.
        builder.build();

        Request request = builder.request();
        RequestBody body = request.body();
        assertThat(body.contentLength()).isEqualTo(4);
        assertThat(body.contentType().subtype()).isEqualTo("text");

        Buffer buffer = new Buffer();
        body.writeTo(buffer);
        assertThat(buffer.readUtf8()).isEqualTo("Hey!");
    }

    @Test
    public void builderAndSpecAllowJsonBody() throws Exception {
        TestMsg expectedBody1 = new TestMsg("Hey!", 42);
        CallSpec.Builder builder1 = builder(HttpMethod.PUT, "", false).responseAs(Object.class)
              .body(TestMsg.class, expectedBody1);
        // Build the CallSpec so that we can build the request.
        builder1.build();
        assertRequestHasBody(builder1.request(), expectedBody1, 24);

        TestMsg expectedBody2 = new TestMsg("Fallout 4", 111);
        CallSpec.Builder builder2 = builder(HttpMethod.PUT, "", false).responseAs(Object.class);
        // Build the CallSpec so that we can build the request.
        CallSpec spec = builder2.build();
        spec.body(TestMsg.class, expectedBody2);
        assertRequestHasBody(builder2.request(), expectedBody2, 30);
    }

    @Test
    public void specThrowsIfCanceled() throws Exception {
        CallSpec spec = builder(HttpMethod.GET, "", false).responseAs(Object.class).build();
        spec.cancel();
        assertThat(spec.isCanceled()).isTrue();

        try {
            spec.execute();
            fail("#execute() should throw");
        } catch (IOException ex) {
            assertThat(ex.getMessage()).isNotEmpty().isEqualTo("Canceled");
        }

        // We need to rebuild the call, since #execute() will mark the call as executed.
        spec = builder(HttpMethod.GET, "", false).responseAs(Object.class).build();
        spec.cancel();

        final AtomicReference<Throwable> responseRef = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        //noinspection unchecked
        spec.enqueue(new Callback() {
            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onFailure(Throwable t) {
                responseRef.set(t);
                latch.countDown();
            }
        });

        assertThat(latch.await(2, TimeUnit.SECONDS)).isTrue();

        Throwable th = responseRef.get();
        assertThat(th).isInstanceOf(IOException.class);
        assertThat(th.getMessage()).isNotEmpty().isEqualTo("Canceled");
    }

    @Test
    public void specThrowsIfExecutedTwice() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(204));

        CallSpec spec = builder(HttpMethod.DELETE, "", false).responseAs(Object.class).build();
        Response response = spec.execute();

        assertThat(spec.isExecuted()).isTrue();
        assertThat(response.code()).isEqualTo(204);
        assertThat(response.body()).isNull();
        assertThat(response.raw()).isNotNull();

        try {
            spec.execute();
            fail("#execute() should throw");
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).isNotEmpty().isEqualTo("Call already executed");
        }

        try {
            //noinspection unchecked,ConstantConditions
            spec.enqueue(null);
            fail("#enqueue() should throw");
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).isNotEmpty().isEqualTo("Call already executed");
        }
    }

    @Test
    public void specHandlesSuccessResponse() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{\n"
              + "  \"msg\": \"success\",\n"
              + "  \"code\": 42\n"
              + '}'));

        CallSpec<TestMsg, Object> spec = this.<TestMsg, Object>builder(HttpMethod.GET, "/", false)
              .responseAs(TestMsg.class)
              .build();

        Response<TestMsg, Object> response = spec.execute();
        assertSuccessResponse(response, new TestMsg("success", 42));
    }

    @Test
    public void specHandlesEmptyBodyResponse() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(204));
        CallSpec spec = builder(HttpMethod.GET, "/", false).responseAs(Object.class).build();
        assertNoBodySuccessResponse(spec.execute(), 204);

        server.enqueue(new MockResponse().setResponseCode(205));
        spec = builder(HttpMethod.GET, "/", false).responseAs(Object.class).build();
        assertNoBodySuccessResponse(spec.execute(), 205);
    }

    @Test
    public void specHandlesErrorResponse() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(300).setBody("{\n"
              + "  \"msg\": \"error\",\n"
              + "  \"code\": 13\n"
              + '}'));

        CallSpec<Object, TestMsg> spec = this.<Object, TestMsg>builder(HttpMethod.GET, "/", false)
              .responseAs(Object.class)
              .errorAs(TestMsg.class)
              .build();

        Response<Object, TestMsg> response = spec.execute();
        assertErrorResponse(response, new TestMsg("error", 13), 300);
    }

    @Test
    public void specHandlesSuccessResponseAsync() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{\n"
              + "  \"msg\": \"success\",\n"
              + "  \"code\": 42\n"
              + '}'));

        CallSpec<TestMsg, Object> spec = this.<TestMsg, Object>builder(HttpMethod.GET, "/", false)
              .responseAs(TestMsg.class)
              .build();

        final AtomicReference<Response<TestMsg, Object>> responseRef = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        spec.enqueue(new Callback<TestMsg, Object>() {
            @Override
            public void onResponse(Response<TestMsg, Object> response) {
                responseRef.set(response);
                latch.countDown();
            }

            @Override
            public void onFailure(Throwable t) {
                fail("unexpected #onFailure() call");
            }
        });

        latch.await(2, TimeUnit.SECONDS);
        assertSuccessResponse(responseRef.get(), new TestMsg("success", 42));
    }

    @Test
    public void specHandlesEmptyBodyResponseAsync() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(204));
        CallSpec spec = builder(HttpMethod.GET, "/", false).responseAs(Object.class).build();
        assertNoBodySuccessResponseAsync(spec, 204);

        server.enqueue(new MockResponse().setResponseCode(205));
        spec = builder(HttpMethod.GET, "/", false).responseAs(Object.class).build();
        assertNoBodySuccessResponseAsync(spec, 205);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void specHandlesRequestBuildFailure() throws Exception {
        XingApi failingApi = new XingApi.Builder()
              .loggedOut()
              .apiEndpoint(httpUrl)
              .client(new OkHttpClient() {
                  @Override
                  public Call newCall(Request request) {
                      throw new UnsupportedOperationException("I'm broken!");
                  }
              }).build();

        CallSpec spec = new CallSpec.Builder(failingApi, HttpMethod.GET, "/", false)
              .responseAs(Object.class).build();

        final AtomicReference<Throwable> responseRef = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        spec.enqueue(new Callback<Object, Object>() {
            @Override
            public void onResponse(Response<Object, Object> response) {
                fail("unexpected #onSuccess() call");
            }

            @Override
            public void onFailure(Throwable t) {
                responseRef.set(t);
                latch.countDown();
            }
        });

        latch.await(2, TimeUnit.SECONDS);
        assertThat(responseRef.get())
              .isInstanceOf(UnsupportedOperationException.class)
              .hasMessage("I'm broken!");
    }

    @Test
    public void specHandlesErrorResponseAsync() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(189).setBody("{\n"
              + "  \"msg\": \"error\",\n"
              + "  \"code\": 14\n"
              + '}'));

        CallSpec<Object, TestMsg> spec = this.<Object, TestMsg>builder(HttpMethod.GET, "/", false)
              .responseAs(Object.class)
              .errorAs(TestMsg.class)
              .build();

        final AtomicReference<Response<Object, TestMsg>> responseRef = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        spec.enqueue(new Callback<Object, TestMsg>() {
            @Override
            public void onResponse(Response<Object, TestMsg> response) {
                responseRef.set(response);
                latch.countDown();
            }

            @Override
            public void onFailure(Throwable t) {
                fail("unexpected #onFailure() call");
            }
        });

        latch.await(2, TimeUnit.SECONDS);
        assertErrorResponse(responseRef.get(), new TestMsg("error", 14), 189);
    }

    @Test
    public void specHandlesIOExceptionAsFailureAsync() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("\\}"));

        CallSpec<TestMsg, Object> spec = this.<TestMsg, Object>builder(HttpMethod.GET, "/", false)
              .responseAs(TestMsg.class)
              .build();

        final AtomicReference<Throwable> responseRef = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        spec.enqueue(new Callback<TestMsg, Object>() {
            @Override
            public void onResponse(Response<TestMsg, Object> response) {
                fail("unexpected #onResponse() call");
            }

            @Override
            public void onFailure(Throwable t) {
                responseRef.set(t);
                latch.countDown();
            }
        });

        latch.await(2, TimeUnit.SECONDS);
        Throwable t = responseRef.get();
        assertThat(t).isInstanceOf(IOException.class);
        assertThat(t.getMessage()).contains("malformed JSON");
    }

    @Test
    public void exceptionCatchingBodyThrows() throws Exception {
        ResponseBody throwingBody = new ResponseBody() {
            @Override
            public MediaType contentType() {
                //noinspection ConstantConditions
                return MediaType.parse("application/h");
            }

            @Override
            public long contentLength() throws IOException {
                throw new IOException("Broken body!");
            }

            @Override
            public BufferedSource source() throws IOException {
                throw new IOException("Broken body!");
            }
        };

        // Test content length throws
        ExceptionCatchingRequestBody body = new ExceptionCatchingRequestBody(throwingBody);
        assertThat(body.contentType()).isEqualTo(throwingBody.contentType());
        try {
            body.contentLength();
        } catch (IOException ignored) {
        }
        try {
            body.throwIfCaught();
        } catch (IOException e) {
            assertThat(e.getMessage()).isEqualTo("Broken body!");
        }

        // Test source throws, here we need new object
        body = new ExceptionCatchingRequestBody(throwingBody);
        assertThat(body.contentType()).isEqualTo(throwingBody.contentType());
        try {
            body.source();
        } catch (IOException ignored) {
        }
        try {
            body.throwIfCaught();
        } catch (IOException e) {
            assertThat(e.getMessage()).isEqualTo("Broken body!");
        }
    }

    @Test
    public void specRawStreamsSuccessResponse() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{\n"
              + "  \"msg\": \"success\",\n"
              + "  \"code\": 200\n"
              + '}'));

        CallSpec<TestMsg, Object> spec = this.<TestMsg, Object>builder(HttpMethod.GET, "/", false)
              .responseAs(TestMsg.class)
              .build();

        BlockingObservable<Response<TestMsg, Object>> blocking = spec.rawStream().toBlocking();
        Response<TestMsg, Object> response = blocking.first();

        assertNotNull(response.body());
        assertThat(response.body().code).isEqualTo(200);
        assertThat(response.body().msg).isEqualTo("success");
    }

    @Test
    public void specRawStreamsErrorResponse() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(405).setBody("{\n"
              + "  \"error_name\": \"TEST_ERROR\",\n"
              + "  \"message\": \"Terrible Error.\",\n"
              + "  \"errors\": [\n"
              + "    {\n"
              + "      \"field\": \"no_field\",\n"
              + "      \"reason\": \"UNEXPECTED\"\n"
              + "    }\n"
              + "  ]\n"
              + '}'));

        CallSpec<Object, HttpError> spec = this.<Object, HttpError>builder(HttpMethod.GET, "/", false)
              .responseAs(Object.class)
              .build();

        BlockingObservable<Response<Object, HttpError>> blocking = spec.rawStream().toBlocking();
        Response<Object, HttpError> response = blocking.first();

        assertThat(response.isSuccess()).isFalse();
        assertNotNull(response.error());
        assertThat(response.error().getErrorMessage()).isEqualTo("Terrible Error.");
        assertThat(response.error().getErrorName()).isEqualTo("TEST_ERROR");
        assertThat(response.error().getErrors().get(0))
              .isEqualTo(new HttpError.Error("no_field", Reason.UNEXPECTED));
    }

    @Test
    public void specRawStreamsError() throws Exception {
        server.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST));

        CallSpec<Object, Object> spec = builder(HttpMethod.GET, "/", false)
              .responseAs(Object.class)
              .build();

        BlockingObservable<Response<Object, Object>> blocking = spec.rawStream().toBlocking();
        try {
            blocking.first();
            fail("This should throw an error.");
        } catch (RuntimeException t) {
            assertThat(t.getCause()).isInstanceOf(IOException.class);
        }
    }

    @Test
    public void specStreamsSuccessResponse() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{\n"
              + "  \"msg\": \"Hey!\",\n"
              + "  \"code\": 42\n"
              + '}'));

        CallSpec<TestMsg, Object> spec = this.<TestMsg, Object>builder(HttpMethod.GET, "/", false)
              .responseAs(TestMsg.class)
              .build();

        BlockingObservable<TestMsg> blocking = spec.stream().toBlocking();
        TestMsg result = blocking.first();

        assertThat(result.msg).isEqualTo("Hey!");
        assertThat(result.code).isEqualTo(42);
    }

    @Test
    public void specStreamsErrorResponse() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(405).setBody("{\n"
              + "  \"error_name\": \"TEST_ERROR2\",\n"
              + "  \"message\": \"Yet another error.\",\n"
              + "  \"errors\": [\n"
              + "    {\n"
              + "      \"field\": \"some_field\",\n"
              + "      \"reason\": \"FIELD_DEPRECATED\"\n"
              + "    }\n"
              + "  ]\n"
              + '}'));

        CallSpec<Object, Object> spec = builder(HttpMethod.DELETE, "/", false)
              .responseAs(Object.class)
              .build();

        BlockingObservable<Object> blocking = spec.stream().toBlocking();
        try {
            blocking.first();
            fail("This should throw an error.");
        } catch (Throwable t) {
            assertThat(t.getCause()).isInstanceOf(HttpException.class);

            HttpException exception = (HttpException) t.getCause();
            assertThat(exception.code()).isEqualTo(405);
            assertThat(exception.message()).isEqualTo("OK");

            HttpError error = (HttpError) exception.error();
            assertThat(error.getErrorName()).isEqualTo("TEST_ERROR2");
            assertThat(error.getErrorMessage()).isEqualTo("Yet another error.");
            assertThat(error.getErrors().get(0)).isEqualTo(new HttpError.Error("some_field", Reason.FIELD_DEPRECATED));
        }
    }

    @Test
    public void specStreamsError() throws Exception {
        server.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST));

        CallSpec<Object, Object> spec = builder(HttpMethod.PUT, "/", false)
              .responseAs(Object.class)
              .build();

        BlockingObservable<Object> blocking = spec.stream().toBlocking();
        try {
            blocking.first();
            fail("Observable should throw.");
        } catch (Throwable t) {
            assertThat(t.getCause()).isInstanceOf(IOException.class);
        }
    }

    private static void assertSuccessResponse(Response<TestMsg, Object> response, TestMsg expected) {
        assertThat(response.code()).isEqualTo(200);
        assertThat(response.error()).isNull();
        assertThat(response.message()).isEqualTo("OK");
        assertThat(response.headers()).isNotNull();

        TestMsg msg = response.body();
        assertNotNull(msg);
        assertThat(msg.msg).isEqualTo(expected.msg);
        assertThat(msg.code).isEqualTo(expected.code);
    }

    private static void assertErrorResponse(Response<Object, TestMsg> response, TestMsg expected, int code) {
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.code()).isEqualTo(code);
        assertThat(response.body()).isNull();

        TestMsg body = response.error();
        assertNotNull(body);
        assertThat(body.msg).isEqualTo(expected.msg);
        assertThat(body.code).isEqualTo(expected.code);
    }

    private static void assertRequestHasBody(Request request, TestMsg expected, int contentLength) throws IOException {
        RequestBody body = request.body();
        assertThat(body.contentLength()).isEqualTo(contentLength);
        assertThat(body.contentType().subtype()).isEqualTo("json");

        Buffer buffer = new Buffer();
        body.writeTo(buffer);
        assertThat(buffer.readUtf8())
              .contains("\"msg\":\"" + expected.msg + '"')
              .contains("\"code\":" + expected.code)
              .startsWith("{")
              .endsWith("}")
              .hasSize(contentLength);
    }

    private static void assertNoBodySuccessResponse(Response response, int code) throws IOException {
        assertThat(response.code()).isEqualTo(code);
        assertThat(response.error()).isNull();
        assertThat(response.body()).isNull();
        assertThat(response.isSuccess()).isTrue();

        ResponseBody body = response.raw().body();
        assertThat(body).isNotNull();
        assertThat(body.contentLength()).isEqualTo(0L);
        assertThat(body.contentType()).isNull();

        try {
            body.source();
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).isEqualTo("Cannot read raw response body of a parsed body.");
        }
    }

    private static void assertNoBodySuccessResponseAsync(CallSpec spec, int code) throws Exception {
        final AtomicReference<Response<Object, Object>> responseRef = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        //noinspection unchecked
        spec.enqueue(new Callback<Object, Object>() {
            @Override
            public void onResponse(Response<Object, Object> response) {
                responseRef.set(response);
                latch.countDown();
            }

            @Override
            public void onFailure(Throwable t) {
                fail("unexpected #onFailure() call");
            }
        });
        latch.await(2, TimeUnit.SECONDS);
        assertNoBodySuccessResponse(responseRef.get(), code);
    }

    private <RT, ET> CallSpec.Builder<RT, ET> builder(HttpMethod httpMethod, String path, boolean formEncoded) {
        return new CallSpec.Builder<>(mockApi, httpMethod, path, formEncoded);
    }

    static final class TestMsg {
        final String msg;
        final int code;

        public TestMsg(String msg, int code) {
            this.msg = msg;
            this.code = code;
        }
    }
}
