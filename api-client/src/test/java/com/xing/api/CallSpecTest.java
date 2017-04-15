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

import com.xing.api.CallSpec.Builder;
import com.xing.api.HttpError.Error.Reason;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.mockwebserver.SocketPolicy;
import okio.Buffer;
import okio.BufferedSink;
import rx.observables.BlockingObservable;
import rx.observers.TestSubscriber;
import rx.singles.BlockingSingle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assume.assumeThat;

@SuppressWarnings({"MagicNumber", "ConstantConditions"})
public class CallSpecTest {
    @Rule
    public final MockWebServer server = new MockWebServer();
    @Rule
    public final RecordingSubscriber.Rule subscriberRule = new RecordingSubscriber.Rule();
    @Rule
    public final RxRecordingSubscriber.Rule rxSubscriberRule = new RxRecordingSubscriber.Rule();
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    public XingApi mockApi;
    public HttpUrl httpUrl;

    @Before
    public void setUp() throws Exception {
        httpUrl = server.url("/");
        mockApi = new XingApi.Builder()
              .custom()
              .apiEndpoint(httpUrl)
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
        CallSpec callSpec = builder.build();

        Request request = callSpec.request();
        assertThat(request.method()).isEqualTo(HttpMethod.GET.method());
        assertThat(request.url().toString()).isEqualTo(httpUrl + "test1/test2");
        assertThat(request.body()).isNull();
    }

    @Test
    public void builderAcceptsPathParamsAsVarList() throws Exception {
        CallSpec.Builder builder = builder(HttpMethod.GET, "/{params}", false)
              .responseAs(Object.class)
              .pathParam("params", "one", "two", "three");
        CallSpec callSpec = builder.build();

        Request request = callSpec.request();
        assertThat(request.method()).isEqualTo(HttpMethod.GET.method());
        assertThat(request.url().toString()).isEqualTo(httpUrl + "one,two,three");
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
        CallSpec callSpec = builder.build();

        Request request = callSpec.request();
        assertThat(request.method()).isEqualTo(HttpMethod.GET.method());
        assertThat(request.url().toString()).isEqualTo(httpUrl + "one,two,three");
        assertThat(request.body()).isNull();
    }

    @Test
    public void builderAcceptsHeaders() throws Exception {
        CallSpec.Builder builder = builder(HttpMethod.GET, "/", false)
              .responseAs(Object.class)
              .header("Test1", "hello")
              .header("Test2", "hm");
        CallSpec spec = builder.build();
        spec.header("Test3", "world");

        Request request = spec.request();
        assertThat(request.method()).isEqualTo(HttpMethod.GET.method());
        assertThat(request.headers().names())
              .contains("Test1")
              .contains("Test2")
              .contains("Test3");
        assertThat(request.headers().values("Test1")).isNotEmpty().hasSize(1).contains("hello");
        assertThat(request.headers().values("Test2")).isNotEmpty().hasSize(1).contains("hm");
        assertThat(request.headers().values("Test3")).isNotEmpty().hasSize(1).contains("world");
    }

    @Test
    public void builderSetsAcceptHeader() throws Exception {
        CallSpec.Builder builder = builder(HttpMethod.GET, "/", false)
              .responseAs(Object.class);
        CallSpec callSpec = builder.build();

        Request request = callSpec.request();
        assertThat(request.headers().values("Accept")).containsExactly("application/json");
    }

    @Test
    public void builderAllowsCustomAcceptHeader() throws Exception {
        CallSpec.Builder builder = builder(HttpMethod.GET, "/", false)
              .responseAs(Object.class)
              .header("Accept", "some.value");
        CallSpec callSpec = builder.build();

        Request request = callSpec.request();
        assertThat(request.headers().values("Accept")).containsExactly("some.value");
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
        CallSpec callSpec = builder.build().queryParam("w", "test2");

        Request request = callSpec.request();
        assertThat(request.method()).isEqualTo(HttpMethod.GET.method());
        assertThat(request.url().toString()).isEqualTo(httpUrl + "?q=test1&w=test2");
        assertThat(request.body()).isNull();
    }

    @Test
    public void builderAttachesQueryParamsAsVarList() throws Exception {
        CallSpec.Builder builder = builder(HttpMethod.GET, "", false)
              .responseAs(Object.class)
              .queryParam("q", "testL", "testL");
        // Build the CallSpec so that we don't test this behaviour twice.
        CallSpec callSpec = builder.build().queryParam("w", "testL", "testL");

        Request request = callSpec.request();
        assertThat(request.method()).isEqualTo(HttpMethod.GET.method());
        assertThat(request.url().toString()).isEqualTo(httpUrl + "?q=testL%2CtestL&w=testL%2CtestL");
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
        CallSpec callSpec = builder.build().queryParam("w", query);

        Request request = callSpec.request();
        assertThat(request.method()).isEqualTo(HttpMethod.GET.method());
        assertThat(request.url().toString()).isEqualTo(httpUrl + "?q=testL%2CtestL&w=testL%2CtestL");
        assertThat(request.body()).isNull();
    }

    @Test
    public void builderAttachesFormFields() throws Exception {
        CallSpec.Builder builder = builder(HttpMethod.PUT, "", true)
              .responseAs(Object.class)
              .formField("a", "true")
              .formField("b", 1);
        // Build the CallSpec so that we don't test this behaviour twice.
        CallSpec callSpec = builder.build()
              .formField("c", "false")
              .formField("d", true);

        Request request = callSpec.request();
        assertThat(request.method()).isEqualTo(HttpMethod.PUT.method());
        assertThat(request.url()).isEqualTo(httpUrl);
        assertThat(request.body()).isNotNull();

        RequestBody body = request.body();
        assertThat(body.contentType()).isEqualTo(MediaType.parse("application/x-www-form-urlencoded"));

        Buffer buffer = new Buffer();
        body.writeTo(buffer);
        assertThat(buffer.readUtf8()).isEqualTo("a=true&b=1&c=false&d=true");
    }

    @Test
    public void builderEncodesStringFromFields() throws Exception {
        CallSpec.Builder builder = builder(HttpMethod.PUT, "", true)
              .responseAs(Object.class)
              .formField("a", "some_value", true)
              .formField("b", "second/value");
        // Build the CallSpec so that we don't test this behaviour twice.
        CallSpec callSpec = builder.build()
              .formField("c", "https://www.xing.com/some_path/20533046", true)
              .formField("d", "fourth/value");

        Request request = callSpec.request();
        assertThat(request.method()).isEqualTo(HttpMethod.PUT.method());
        assertThat(request.url()).isEqualTo(httpUrl);
        assertThat(request.body()).isNotNull();

        RequestBody body = request.body();
        assertThat(body.contentType()).isEqualTo(MediaType.parse("application/x-www-form-urlencoded"));

        Buffer buffer = new Buffer();
        body.writeTo(buffer);
        assertThat(buffer.readUtf8())
              .isEqualTo("a=some_value"
                    + "&b=second%2Fvalue"
                    + "&c=https%253A%252F%252Fwww.xing.com%252Fsome_path%252F20533046"
                    + "&d=fourth%2Fvalue");
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
        CallSpec callSpec = builder.build().formField("e", field).formField("d", "test5", "test6");

        Request request = callSpec.request();
        assertThat(request.method()).isEqualTo(HttpMethod.PUT.method());
        assertThat(request.url()).isEqualTo(httpUrl);
        assertThat(request.body()).isNotNull();

        RequestBody body = request.body();
        assertThat(body.contentType()).isEqualTo(MediaType.parse("application/x-www-form-urlencoded"));

        Buffer buffer = new Buffer();
        body.writeTo(buffer);
        assertThat(buffer.readUtf8()).isEqualTo("f=test1%2C%20test2&e=test3%2C%20test4&d=test5%2C%20test6");
    }

    @Test
    public void builderEnsuresEmptyBody() throws Exception {
        CallSpec.Builder builder = builder(HttpMethod.PUT, "", false).responseAs(Object.class);
        // Build the CallSpec so that we can build the request.
        CallSpec callSpec = builder.build();

        Request request = callSpec.request();
        assertThat(request.method()).isEqualTo(HttpMethod.PUT.method());
        assertThat(request.url()).isEqualTo(httpUrl);
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
        CallSpec callSpec = builder.build();

        Request request = callSpec.request();
        RequestBody body = request.body();
        assertThat(body.contentLength()).isEqualTo(4);
        assertThat(body.contentType().subtype()).isEqualTo("text");

        Buffer buffer = new Buffer();
        body.writeTo(buffer);
        assertThat(buffer.readUtf8()).isEqualTo("Hey!");
    }

    @Test
    public void builderAllowsJsonBody() throws Exception {
        TestMsg expectedBody = new TestMsg("Hey!", 42);
        CallSpec.Builder builder = builder(HttpMethod.PUT, "", false).responseAs(Object.class)
              .body(TestMsg.class, expectedBody);
        // Build the CallSpec so that we can build the request.
        CallSpec callSpec = builder.build();
        assertRequestHasBody(callSpec.request(), expectedBody, 24);
    }

    @Test
    public void builderEnsuresDeleteRequestDoesNotHaveABody() throws Exception {
        CallSpec.Builder builder = builder(HttpMethod.DELETE, "", false).responseAs(Object.class);
        // Build the CallSpec so that we can build the request.
        CallSpec callSpec = builder.build();

        Request request = callSpec.request();
        assertThat(request.body()).isNull();
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
        server.enqueue(new MockResponse().setResponseCode(200)
              .setBody("{\n"
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
    public void specHandlesRangeHeader() throws Exception {
        server.enqueue(new MockResponse().addHeader("Xing-Content-Range: items 1-10/20"));

        CallSpec spec = builder(HttpMethod.GET, "/", false).responseAs(Void.class).build();

        Response response = spec.execute();
        assertThat(response.range()).isNotNull().isEqualTo(new ContentRange(1, 10, 20));
    }

    @Test
    public void specHandlesSuccessResponseAsVoid() throws Exception {
        server.enqueue(new MockResponse().setBody("Hello"));

        CallSpec<Void, Object> spec = this.<Void, Object>builder(HttpMethod.GET, "/", false)
              .responseAs(Void.class)
              .build();

        Response<Void, Object> response = spec.execute();
        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.body()).isNull();
    }

    @Test
    public void specHandlesSuccessResponseAsString() throws Exception {
        server.enqueue(new MockResponse().setBody("Hello"));

        CallSpec<String, Object> spec = this.<String, Object>builder(HttpMethod.GET, "/", false)
              .responseAs(String.class)
              .build();

        Response<String, Object> response = spec.execute();
        assertThat(response.body()).isEqualTo("Hello");
    }

    @Test
    public void specHandlesSuccessResponseAsResponseBody() throws Exception {
        server.enqueue(new MockResponse().setBody("Testing"));

        CallSpec<ResponseBody, Object> spec = this.<ResponseBody, Object>builder(HttpMethod.GET, "/", false)
              .responseAs(ResponseBody.class)
              .build();

        Response<ResponseBody, Object> response = spec.execute();
        assertThat(response.body().string()).isEqualTo("Testing");
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
    public void specHandlesVoidAsEmptyBodyResponse() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("Test"));
        CallSpec<Void, Object> spec = this.<Void, Object>builder(HttpMethod.GET, "/", false)
              .responseAs(Void.class)
              .build();
        assertNoBodySuccessResponse(spec.execute(), 200);

        server.enqueue(new MockResponse().setResponseCode(200).setBody("Test"));
        assertNoBodySuccessResponseAsync(spec.clone(), 200);
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
    public void specHandlesErrorResponseWithoutBody() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(300));

        CallSpec<Object, TestMsg> spec = this.<Object, TestMsg>builder(HttpMethod.GET, "/", false)
              .responseAs(Object.class)
              .errorAs(TestMsg.class)
              .build();

        Response<Object, TestMsg> response = spec.execute();
        assertEmptyErrorResponse(response, 300);
    }

    @Test
    public void specThrowsIfUnauthorized() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(401));

        CallSpec spec = builder(HttpMethod.GET, "/", false)
              .responseAs(Object.class)
              .build();

        try {
            spec.execute();
            fail("Spec should throw on auth error.");
        } catch (IOException e) {
            assertThat(e).hasMessage("401 Unauthorized");
        }
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
    public void specHandlesRangeHeaderAsync() throws Exception {
        server.enqueue(new MockResponse().addHeader("Xing-Content-Range: items 20-30/42"));

        CallSpec spec = builder(HttpMethod.GET, "/", false).responseAs(Void.class).build();

        final AtomicReference<Response<Object, Object>> responseRef = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        //noinspection unchecked we don't care at this point
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
        assertThat(responseRef.get().range()).isNotNull().isEqualTo(new ContentRange(20, 30, 42));
    }

    @Test
    public void specHandlesSuccessResponseAsResponseBodyAsync() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("Hello"));

        CallSpec<ResponseBody, Object> spec = this.<ResponseBody, Object>builder(HttpMethod.GET, "/", false)
              .responseAs(ResponseBody.class)
              .build();

        final AtomicReference<Response<ResponseBody, Object>> responseRef = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        spec.enqueue(new Callback<ResponseBody, Object>() {
            @Override
            public void onResponse(Response<ResponseBody, Object> response) {
                responseRef.set(response);
                latch.countDown();
            }

            @Override
            public void onFailure(Throwable t) {
                fail("unexpected #onFailure() call");
            }
        });

        latch.await(2, TimeUnit.SECONDS);
        Response<ResponseBody, Object> response = responseRef.get();
        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.error()).isNull();
        assertThat(response.body().string()).isEqualTo("Hello");
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

    @SuppressWarnings("unchecked") // We don't care about type safety at this point.
    @Test
    public void specThrowsIfUnauthorizedAsync() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(401));

        CallSpec spec = builder(HttpMethod.GET, "/", false)
              .responseAs(Object.class)
              .build();

        final AtomicReference<Throwable> responseRef = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        spec.enqueue(new Callback() {
            @Override
            public void onResponse(Response response) {
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
        assertThat(t).isInstanceOf(IOException.class).hasMessage("401 Unauthorized");
    }

    @Test
    public void specSingleRawResponseRespectsBackpressure() throws Exception {
        server.enqueue(new MockResponse().setBody("Hi"));

        RecordingSubscriber<Response<String, Object>> subscriber = subscriberRule.createWithInitialRequest(0);
        CallSpec<String, Object> spec = this.<String, Object>builder(HttpMethod.GET, "/", false)
              .responseAs(String.class)
              .errorAs(Object.class)
              .build();

        spec.singleRawResponse().toFlowable().subscribe(subscriber);
        assertThat(server.getRequestCount()).isEqualTo(1);
        subscriber.assertNoEvents();

        subscriber.requestMore(1);
        subscriber.assertAnyValue().assertCompleted();

        subscriber.requestMore(Long.MAX_VALUE); // Subsequent requests do not trigger HTTP requests.
        assertThat(server.getRequestCount()).isEqualTo(1);
    }

    @Test
    public void specSingleRawResponseSuccessResponse() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{\n"
              + "  \"msg\": \"success\",\n"
              + "  \"code\": 200\n"
              + '}'));

        CallSpec<TestMsg, Object> spec = this.<TestMsg, Object>builder(HttpMethod.GET, "/", false)
              .responseAs(TestMsg.class)
              .build();

        Response<TestMsg, Object> response = spec.singleRawResponse().blockingGet();

        assertThat(response.body()).isNotNull();
        assertThat(response.body().code).isEqualTo(200);
        assertThat(response.body().msg).isEqualTo("success");
    }

    @Test
    public void specSingleRawResponseErrorResponse() throws Exception {
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

        Response<Object, HttpError> response = spec.singleRawResponse().blockingGet();

        assertThat(response.isSuccessful()).isFalse();
        assertThat(response.error()).isNotNull();
        assertThat(response.error().message()).isEqualTo("Terrible Error.");
        assertThat(response.error().name()).isEqualTo("TEST_ERROR");
        assertThat(response.error().errors().get(0))
              .isEqualTo(new HttpError.Error("no_field", Reason.UNEXPECTED));
    }

    @Test
    public void specSingleRawResponseError() throws Exception {
        server.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST));

        CallSpec<Object, Object> spec = builder(HttpMethod.GET, "/", false)
              .responseAs(Object.class)
              .build();

        try {
            spec.singleRawResponse().blockingGet();
            fail("This should throw an error.");
        } catch (Throwable t) {
            assertThat(t.getCause()).isInstanceOf(IOException.class);
        }
    }

    @Test
    public void specSingleRawResponseErrorUnauthorized() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(401));

        CallSpec<Object, Object> spec = builder(HttpMethod.GET, "/", false)
              .responseAs(Object.class)
              .build();

        try {
            spec.singleRawResponse().blockingGet();
            fail("This should throw an error.");
        } catch (Throwable t) {
            assertThat(t.getCause())
                  .isInstanceOf(IOException.class)
                  .hasMessage("401 Unauthorized");
        }
    }

    @Test
    public void specSingleResponseRespectsBackpressure() throws Exception {
        server.enqueue(new MockResponse().setBody("Hi"));

        RecordingSubscriber<String> subscriber = subscriberRule.createWithInitialRequest(0);

        CallSpec<String, Object> spec = this.<String, Object>builder(HttpMethod.GET, "/", false)
              .responseAs(String.class)
              .errorAs(Object.class)
              .build();

        spec.singleResponse().toFlowable().subscribe(subscriber);
        assertThat(server.getRequestCount()).isEqualTo(1);
        subscriber.assertNoEvents();

        subscriber.requestMore(1);
        subscriber.assertAnyValue().assertCompleted();

        subscriber.requestMore(Long.MAX_VALUE); // Subsequent requests do not trigger HTTP requests.
        assertThat(server.getRequestCount()).isEqualTo(1);
    }

    @Test
    public void specSingleResponseSuccessResponse() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{\n"
              + "  \"msg\": \"Hey!\",\n"
              + "  \"code\": 42\n"
              + '}'));

        CallSpec<TestMsg, Object> spec = this.<TestMsg, Object>builder(HttpMethod.GET, "/", false)
              .responseAs(TestMsg.class)
              .build();

        TestMsg result = spec.singleResponse().blockingGet();

        assertThat(result.msg).isEqualTo("Hey!");
        assertThat(result.code).isEqualTo(42);
    }

    @Test
    public void specSingleResponseErrorResponse() throws Exception {
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

        try {
            spec.singleResponse().blockingGet();
            fail("This should throw an error.");
        } catch (Throwable t) {
            assertThat(t.getCause()).isInstanceOf(HttpException.class);

            HttpException exception = (HttpException) t.getCause();
            assertThat(exception.code()).isEqualTo(405);
            assertThat(exception.message()).isEqualTo("Client Error");

            HttpError error = (HttpError) exception.error();
            assertThat(error.name()).isEqualTo("TEST_ERROR2");
            assertThat(error.message()).isEqualTo("Yet another error.");
            assertThat(error.errors().get(0)).isEqualTo(new HttpError.Error("some_field", Reason.FIELD_DEPRECATED));
        }
    }

    @Test
    public void specSingleResponseError() throws Exception {
        server.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST));

        CallSpec<Object, Object> spec = builder(HttpMethod.PUT, "/", false)
              .responseAs(Object.class)
              .build();

        try {
            spec.singleResponse().blockingGet();
            fail("Observable should throw.");
        } catch (Throwable t) {
            assertThat(t.getCause()).isInstanceOf(IOException.class);
        }
    }

    @Test
    public void specSingleResponseErrorUnauthorized() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(401));

        CallSpec<Object, Object> spec = builder(HttpMethod.GET, "/", false)
              .responseAs(Object.class)
              .build();

        try {
            spec.singleResponse().blockingGet();
            fail("This should throw an error.");
        } catch (Throwable t) {
            assertThat(t.getCause())
                  .isInstanceOf(IOException.class)
                  .hasMessage("401 Unauthorized");
        }
    }


    // we cannot test the connect timeout, because MockWebServer immediately accepts connections
    // and provides no callback to delay this, but we can emulate a read timeout by stalling the response
    // indefinitely
    @Test
    public void canOverrideReadTimeoutPerCall() throws Exception {

        server.setDispatcher(new Dispatcher() {
            @Override public MockResponse dispatch(RecordedRequest request) {
                return new MockResponse().setResponseCode(200).setSocketPolicy(SocketPolicy.NO_RESPONSE);
            }
        });

        // all in seconds
        int readTimeout = 1;
        // the estimated execution time on the client side, plus a buffer
        // of some milliseconds in case we're slower
        int executionTime = 200;

        Builder<ResponseBody, Object> builder = builder(HttpMethod.GET, "", false);
        Single<ResponseBody> singleResponse =
              builder.responseAs(ResponseBody.class).readTimeout(readTimeout)
                    .build().singleResponse();

        long before = System.currentTimeMillis();
        TestObserver<ResponseBody> testObserver = singleResponse.test();
        long diff = System.currentTimeMillis() - before;

        testObserver.assertError(SocketTimeoutException.class);
        assertThat(diff).isLessThan((readTimeout * 1000) + executionTime);
    }

    @Test
    public void canOverrideWriteTimeoutPerCall() throws Exception {
        // for some unknown reason the timeout is not applied properly when run on Travis CI
        // so we skip this test on the CI and only execute it locally
        assumeThat(System.getenv("TRAVIS"), not(equalTo("true")));

        // throttle the server request reading to one byte per second
        server.enqueue(new MockResponse().throttleBody(1, 1, TimeUnit.SECONDS));

        RequestBody streamingBody = new RequestBody() {
            @Override public MediaType contentType() {
                return MediaType.parse("*/*");
            }

            @Override public void writeTo(BufferedSink sink) throws IOException {
                // write two megabytes at once
                byte[] data = new byte[2 * 1024 * 1024];
                sink.write(data);
            }
        };

        // all in seconds
        int writeTimeout = 1;
        // the estimated execution time on the client side, plus a buffer
        // of some milliseconds in case we're slower
        int executionTime = 500;

        Builder<ResponseBody, Object> builder = builder(HttpMethod.POST, "", false);
        Single<ResponseBody> singleResponse =
              builder.body(streamingBody).responseAs(ResponseBody.class).writeTimeout(writeTimeout)
                    .build().singleResponse();

        long before = System.currentTimeMillis();
        TestObserver<ResponseBody> testObserver = singleResponse.test();
        long diff = System.currentTimeMillis() - before;

        testObserver.assertError(SocketTimeoutException.class);
        assertThat(diff).isLessThan((writeTimeout * 1000) + executionTime);
    }

    @Test
    public void specRawStreamRespectsBackpressure() throws Exception {
        server.enqueue(new MockResponse().setBody("Hi"));

        RxRecordingSubscriber<Response<String, Object>> subscriber = rxSubscriberRule.createWithInitialRequest(0);
        CallSpec<String, Object> spec = this.<String, Object>builder(HttpMethod.GET, "/", false)
                .responseAs(String.class)
                .errorAs(Object.class)
                .build();

        spec.rawStream().unsafeSubscribe(subscriber);
        assertThat(server.getRequestCount()).isEqualTo(1);
        subscriber.assertNoEvents();

        subscriber.requestMore(1);
        subscriber.assertAnyValue().assertCompleted();

        subscriber.requestMore(Long.MAX_VALUE); // Subsequent requests do not trigger HTTP requests.
        assertThat(server.getRequestCount()).isEqualTo(1);
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

        assertThat(response.body()).isNotNull();
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

        assertThat(response.isSuccessful()).isFalse();
        assertThat(response.error()).isNotNull();
        assertThat(response.error().message()).isEqualTo("Terrible Error.");
        assertThat(response.error().name()).isEqualTo("TEST_ERROR");
        assertThat(response.error().errors().get(0))
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
        } catch (Throwable t) {
            assertThat(t.getCause()).isInstanceOf(IOException.class);
        }
    }

    @Test
    public void specRawStreamErrorUnauthorized() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(401));

        CallSpec<Object, Object> spec = builder(HttpMethod.GET, "/", false)
                .responseAs(Object.class)
                .build();

        BlockingObservable<Response<Object, Object>> blocking = spec.rawStream().toBlocking();
        try {
            blocking.first();
            fail("This should throw an error.");
        } catch (Throwable t) {
            assertThat(t.getCause())
                    .isInstanceOf(IOException.class)
                    .hasMessage("401 Unauthorized");
        }
    }

    @Test
    public void specStreamRespectsBackpressure() throws Exception {
        server.enqueue(new MockResponse().setBody("Hi"));

        RxRecordingSubscriber<String> subscriber = rxSubscriberRule.createWithInitialRequest(0);

        CallSpec<String, Object> spec = this.<String, Object>builder(HttpMethod.GET, "/", false)
                .responseAs(String.class)
                .errorAs(Object.class)
                .build();

        spec.stream().unsafeSubscribe(subscriber);
        assertThat(server.getRequestCount()).isEqualTo(1);
        subscriber.assertNoEvents();

        subscriber.requestMore(1);
        subscriber.assertAnyValue().assertCompleted();

        subscriber.requestMore(Long.MAX_VALUE); // Subsequent requests do not trigger HTTP requests.
        assertThat(server.getRequestCount()).isEqualTo(1);
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
            assertThat(exception.message()).isEqualTo("Client Error");

            HttpError error = (HttpError) exception.error();
            assertThat(error.name()).isEqualTo("TEST_ERROR2");
            assertThat(error.message()).isEqualTo("Yet another error.");
            assertThat(error.errors().get(0)).isEqualTo(new HttpError.Error("some_field", Reason.FIELD_DEPRECATED));
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

    @Test
    public void specStreamErrorUnauthorized() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(401));

        CallSpec<Object, Object> spec = builder(HttpMethod.GET, "/", false)
                .responseAs(Object.class)
                .build();

        BlockingObservable<Object> blocking = spec.stream().toBlocking();
        try {
            blocking.first();
            fail("This should throw an error.");
        } catch (Throwable t) {
            assertThat(t.getCause())
                    .isInstanceOf(IOException.class)
                    .hasMessage("401 Unauthorized");
        }
    }

    @Test
    public void specSingleStreamsSuccessResponse() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{\n"
                + "  \"msg\": \"Hey!\",\n"
                + "  \"code\": 42\n"
                + '}'));

        CallSpec<TestMsg, Object> spec = this.<TestMsg, Object>builder(HttpMethod.GET, "/", false)
                .responseAs(TestMsg.class)
                .build();

        BlockingSingle<TestMsg> blocking = spec.singleStream().toBlocking();
        TestMsg result = blocking.value();

        assertThat(result.msg).isEqualTo("Hey!");
        assertThat(result.code).isEqualTo(42);
    }

    @Test
    public void specSingleStreamsErrorResponse() throws Exception {
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

        BlockingSingle<Object> blocking = spec.singleStream().toBlocking();
        try {
            blocking.value();
            fail("This should throw an error.");
        } catch (Throwable t) {
            assertThat(t.getCause()).isInstanceOf(HttpException.class);

            HttpException exception = (HttpException) t.getCause();
            assertThat(exception.code()).isEqualTo(405);
            assertThat(exception.message()).isEqualTo("Client Error");

            HttpError error = (HttpError) exception.error();
            assertThat(error.name()).isEqualTo("TEST_ERROR2");
            assertThat(error.message()).isEqualTo("Yet another error.");
            assertThat(error.errors().get(0)).isEqualTo(new HttpError.Error("some_field", Reason.FIELD_DEPRECATED));
        }
    }

    @Test
    public void specSingleStreamsError() throws Exception {
        server.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST));

        CallSpec<Object, Object> spec = builder(HttpMethod.PUT, "/", false)
                .responseAs(Object.class)
                .build();

        BlockingSingle<Object> blocking = spec.singleStream().toBlocking();
        try {
            blocking.value();
            fail("Observable should throw.");
        } catch (Throwable t) {
            assertThat(t.getCause()).isInstanceOf(IOException.class);
        }
    }

    @Test
    public void specSingleStreamErrorUnauthorized() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(401));

        CallSpec<Object, Object> spec = builder(HttpMethod.GET, "/", false)
                .responseAs(Object.class)
                .build();

        BlockingSingle<Object> blocking = spec.singleStream().toBlocking();
        try {
            blocking.value();
            fail("This should throw an error.");
        } catch (Throwable t) {
            assertThat(t.getCause())
                    .isInstanceOf(IOException.class)
                    .hasMessage("401 Unauthorized");
        }
    }

    @Test
    public void specCompletableStreamsSuccessResponse() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{\n"
                + "  \"msg\": \"Hey!\",\n"
                + "  \"code\": 42\n"
                + '}'));

        CallSpec<TestMsg, Object> spec = this.<TestMsg, Object>builder(HttpMethod.GET, "/", false)
                .responseAs(TestMsg.class)
                .build();

        TestSubscriber subscriber = new TestSubscriber();
        spec.completableStream().subscribe(subscriber);

        subscriber.assertCompleted();
        subscriber.assertNoErrors();
    }

    @Test
    public void specCompletableStreamsErrorResponse() throws Exception {
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

        TestSubscriber subscriber = new TestSubscriber();
        spec.completableStream().subscribe(subscriber);

        subscriber.assertError(HttpException.class);

        HttpException exception = (HttpException) subscriber.getOnErrorEvents().get(0);
        assertThat(exception.code()).isEqualTo(405);
        assertThat(exception.message()).isEqualTo("Client Error");

        HttpError error = (HttpError) exception.error();
        assertThat(error.name()).isEqualTo("TEST_ERROR2");
        assertThat(error.message()).isEqualTo("Yet another error.");
        assertThat(error.errors().get(0)).isEqualTo(new HttpError.Error("some_field", Reason.FIELD_DEPRECATED));
    }

    @Test
    public void specCompletableStreamsError() throws Exception {
        server.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST));

        CallSpec<Object, Object> spec = builder(HttpMethod.PUT, "/", false)
                .responseAs(Object.class)
                .build();

        TestSubscriber subscriber = new TestSubscriber();
        spec.completableStream().subscribe(subscriber);

        subscriber.assertError(IOException.class);
    }

    @Test
    public void specCompletableStreamErrorUnauthorized() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(401));

        CallSpec<Object, Object> spec = builder(HttpMethod.GET, "/", false)
                .responseAs(Object.class)
                .build();

        TestSubscriber subscriber = new TestSubscriber();
        spec.completableStream().subscribe(subscriber);

        subscriber.assertError(IOException.class);

        IOException exception = (IOException) subscriber.getOnErrorEvents().get(0);
        assertThat(exception).hasMessage("401 Unauthorized");
    }

    @Test
    public void disallowNegativeConnectTimeout() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        builder(HttpMethod.GET, "", false)
              .responseAs(Object.class)
              .connectTimeout(-1);
    }

    @Test
    public void disallowNegativeReadTimeout() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        builder(HttpMethod.GET, "", false)
              .responseAs(Object.class)
              .readTimeout(-1);
    }

    @Test
    public void disallowNegativeWriteTimeout() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        builder(HttpMethod.GET, "", false)
              .responseAs(Object.class)
              .writeTimeout(-1);
    }

    private static void assertSuccessResponse(Response<TestMsg, Object> response, TestMsg expected) {
        assertThat(response.code()).isEqualTo(200);
        assertThat(response.error()).isNull();
        assertThat(response.message()).isEqualTo("OK");
        assertThat(response.headers()).isNotNull();

        TestMsg msg = response.body();
        assertThat(msg).isNotNull();
        assertThat(msg.msg).isEqualTo(expected.msg);
        assertThat(msg.code).isEqualTo(expected.code);
    }

    private static void assertErrorResponse(Response<Object, TestMsg> response, TestMsg expected, int code) {
        assertThat(response.isSuccessful()).isFalse();
        assertThat(response.code()).isEqualTo(code);
        assertThat(response.body()).isNull();

        TestMsg body = response.error();
        assertThat(body).isNotNull();
        assertThat(body.msg).isEqualTo(expected.msg);
        assertThat(body.code).isEqualTo(expected.code);
    }

    private static void assertEmptyErrorResponse(Response<Object, TestMsg> response, int code) {
        assertThat(response.isSuccessful()).isFalse();
        assertThat(response.code()).isEqualTo(code);
        assertThat(response.body()).isNull();
        assertThat(response.error()).isNull();
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

    private static void assertNoBodySuccessResponse(Response response, int code) {
        assertThat(response.code()).isEqualTo(code);
        assertThat(response.error()).isNull();
        assertThat(response.body()).isNull();
        assertThat(response.isSuccessful()).isTrue();

        ResponseBody body = response.raw().body();
        assertThat(body).isNotNull();
        // User may want to ignore the content of another response.
        if (code == 204 || code == 205) assertThat(body.contentLength()).isEqualTo(0L);
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
