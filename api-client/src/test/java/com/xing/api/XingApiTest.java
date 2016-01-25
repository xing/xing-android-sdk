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

import com.squareup.moshi.Moshi;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.SocketPolicy;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@SuppressWarnings("NullableProblems")
public class XingApiTest {
    @Rule
    public final MockWebServer server = new MockWebServer();

    @Test
    public void getRightResourceImpl() throws Exception {
        XingApi api = buildDefaultApi();
        Resource resource = api.resource(LegalTestResource.class);
        assertThat(resource).isInstanceOf(LegalTestResource.class);

        LegalTestResource testResource = api.resource(LegalTestResource.class);
        assertThat(testResource).isSameAs(resource);
    }

    @Test
    public void throwsForNonFinalResourceClass() throws Exception {
        try {
            buildDefaultApi().resource(IllegalTestResource1.class);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class)
                  .hasMessage("Resource class must be declared final.");
        }
    }

    @Test
    public void throwsForNonStaticResourceClass() throws Exception {
        try {
            buildDefaultApi().resource(IllegalTestResource2.class);
            fail("Should fail on non-static class.");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class)
                  .hasMessage("Resource class must be declared static.");
        }
    }

    @Test
    public void throwsForMethodInnerResourceClass() throws Exception {
        final class InnerTestResource extends Resource {
            InnerTestResource(XingApi api) {
                super(api);
            }
        }

        try {
            buildDefaultApi().resource(InnerTestResource.class);
            fail("Should fail on non-static class.");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class)
                  .hasMessage("Resource class must be declared static.");
        }
    }

    @Test
    public void throwsForResourceClassOverridingConstructor() throws Exception {
        try {
            buildDefaultApi().resource(LegalTestResourceOverridesConstructor.class);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class)
                  .hasMessage("Resource class malformed.")
                  .hasCauseInstanceOf(NoSuchMethodException.class);
        }
    }

    @SuppressWarnings("ConstantConditions") // Passes nulls intentionally.
    @Test
    public void builder() throws Exception {
        XingApi.Builder builder = new XingApi.Builder();

        try {
            builder.apiEndpoint((String) null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("apiEndpoint == null");
        }

        try {
            builder.apiEndpoint((HttpUrl) null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("apiEndpoint == null");
        }

        try {
            builder.apiEndpoint("http/invalid-url.org");
            fail("Builder should throw on invalid endpoints.");
        } catch (IllegalArgumentException expected) {
            assertThat(expected.getMessage()).isEqualTo("Illegal endpoint URL: http/invalid-url.org");
        }

        try {
            builder.accessSecret(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("accessSecret == null");
        }

        try {
            builder.accessToken(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("accessToken == null");
        }

        try {
            builder.consumerKey(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("consumerKey == null");
        }

        try {
            builder.consumerSecret(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("consumerSecret == null");
        }

        try {
            builder.client(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("client == null");
        }

        try {
            builder.callbackExecutor(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("callbackExecutor == null");
        }

        try {
            builder.moshi(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("moshi == null");
        }

        builder.moshi(new Moshi.Builder().build());
        try {
            builder.moshi(new Moshi.Builder().build());
            fail("Only one Moshi should be allowed.");
        } catch (IllegalStateException expected) {
            assertThat(expected.getMessage()).isEqualTo("Only one instance of Moshi is allowed");
        }

        try {
            builder.build();
            fail("Build should throw if oauth is not set.");
        } catch (IllegalStateException expected) {
            assertThat(expected.getMessage()).contains("not set");
        }
    }

    @Test
    public void apiEndpointDefault() throws Exception {
        XingApi api = buildDefaultApi();
        assertThat(api.apiEndpoint().toString()).isEqualTo("https://api.xing.com/");
    }

    @Test
    public void apiEndpointPropagated() throws Exception {
        XingApi api1 = new XingApi.Builder()
              .apiEndpoint(HttpUrl.parse("http://test.com/"))
              .loggedOut()
              .build();
        assertThat(api1.apiEndpoint().toString()).isEqualTo("http://test.com/");

        XingApi api2 = new XingApi.Builder()
              .apiEndpoint("https://test2.com/")
              .loggedOut()
              .build();
        assertThat(api2.apiEndpoint().toString()).isEqualTo("https://test2.com/");
    }

    @Test
    public void callbackExecutorNoDefault() throws Exception {
        XingApi api = buildDefaultApi();
        assertThat(api.callbackExecutor()).isNull();
    }

    @Test
    public void callbackExecutorPropagated() throws Exception {
        Executor executor = mock(Executor.class);
        XingApi api = new XingApi.Builder()
              .loggedOut()
              .callbackExecutor(executor)
              .build();
        assertThat(api.callbackExecutor()).isSameAs(executor);
    }

    @Test
    public void callbackExecutorUsedForEnqueueResponse() throws Exception {
        Executor executor = spy(new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        });
        XingApi api = new XingApi.Builder()
              .apiEndpoint(server.url("/"))
              .callbackExecutor(executor)
              .loggedOut()
              .build();

        LegalTestResource resource = api.resource(LegalTestResource.class);
        CallSpec<String, HttpError> spec = resource.simpleSpec();

        server.enqueue(new MockResponse());

        final CountDownLatch latch = new CountDownLatch(1);
        spec.enqueue(new Callback<String, HttpError>() {
            @Override
            public void onResponse(Response response) {
                latch.countDown();
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
        assertTrue(latch.await(2, TimeUnit.SECONDS));

        verify(executor).execute(any(Runnable.class));
        verifyNoMoreInteractions(executor);
    }

    @Test
    public void callbackExecutorUsedForEnqueueFailure() throws Exception {
        Executor executor = spy(new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        });
        XingApi api = new XingApi.Builder()
              .apiEndpoint(server.url("/"))
              .callbackExecutor(executor)
              .loggedOut()
              .build();
        LegalTestResource resource = api.resource(LegalTestResource.class);
        CallSpec<String, HttpError> spec = resource.simpleSpec();

        server.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START));

        final CountDownLatch latch = new CountDownLatch(1);
        spec.enqueue(new Callback<String, HttpError>() {
            @Override
            public void onResponse(Response response) {
                throw new AssertionError();
            }

            @Override
            public void onFailure(Throwable t) {
                latch.countDown();
            }
        });
        assertTrue(latch.await(2, TimeUnit.SECONDS));

        verify(executor).execute(any(Runnable.class));
        verifyNoMoreInteractions(executor);
    }

    @Test
    public void callbackExecutorUsedForAuthError() throws Exception {
        Executor executor = spy(new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        });
        XingApi api = new XingApi.Builder()
              .apiEndpoint(server.url("/"))
              .callbackExecutor(executor)
              .loggedOut()
              .build();
        LegalTestResource resource = api.resource(LegalTestResource.class);
        CallSpec<String, HttpError> spec = resource.simpleSpec();

        final AtomicReference<Response<?, ResponseBody>> ref = new AtomicReference<>();
        api.addAuthErrorCallback(new AuthErrorCallback() {
            @Override
            public void onAuthError(Response<?, ResponseBody> errorResponse) {
                ref.set(errorResponse);
            }
        });

        server.enqueue(new MockResponse().setResponseCode(401).setBody("Ups"));
        try {
            spec.execute();
            fail("Expecting auth error to be thrown.");
        } catch (IOException e) {
            assertThat(e).hasMessage("401 Unauthorized");
        }

        Response<?, ResponseBody> response = ref.get();
        assertThat(response).isNotNull();
        assertThat(response.body()).isNull();
        assertThat(response.error().string()).isEqualTo("Ups");

        verify(executor).execute(any(Runnable.class));
        verifyNoMoreInteractions(executor);
    }

    @SuppressWarnings("unchecked") // We don't care about type safety at this point.
    @Test
    public void allowsMultipleErrorCallbacks() throws Exception {
        XingApi api = new XingApi.Builder()
              .apiEndpoint(server.url("/"))
              .loggedOut()
              .build();

        LegalTestResource resource = api.resource(LegalTestResource.class);
        CallSpec<String, HttpError> spec = resource.simpleSpec();

        AuthErrorCallback callback1 = spy(new AuthErrorCallback() {
            @Override
            public void onAuthError(Response<?, ResponseBody> errorResponse) {
            }
        });
        AuthErrorCallback callback2 = spy(new AuthErrorCallback() {
            @Override
            public void onAuthError(Response<?, ResponseBody> errorResponse) {
            }
        });

        api.addAuthErrorCallback(callback1).addAuthErrorCallback(callback2);
        server.enqueue(new MockResponse().setResponseCode(401));

        try {
            spec.execute();
            fail("Expecting auth error to be thrown.");
        } catch (IOException e) {
            assertThat(e).hasMessage("401 Unauthorized");
        }

        api.removeAuthErrorCallback(callback2);
        server.enqueue(new MockResponse().setResponseCode(401));

        spec = spec.clone();
        try {
            spec.execute();
            fail("Expecting auth error to be thrown.");
        } catch (IOException e) {
            assertThat(e).hasMessage("401 Unauthorized");
        }

        verify(callback1, times(2)).onAuthError(any(Response.class));
        verifyNoMoreInteractions(callback1);
        verify(callback2, times(1)).onAuthError(any(Response.class));
        verifyNoMoreInteractions(callback2);
    }

    private static XingApi buildDefaultApi() {
        return new XingApi.Builder().loggedOut().build();
    }

    static final class LegalTestResource extends Resource {
        LegalTestResource(XingApi api) {
            super(api);
        }

        public CallSpec<String, HttpError> simpleSpec() {
            return Resource.<String, HttpError>newGetSpec(api, "/").responseAs(String.class).build();
        }
    }

    @SuppressWarnings("ConstantConditions") // Intentional.
    static final class LegalTestResourceOverridesConstructor extends Resource {
        LegalTestResourceOverridesConstructor() {
            super(null);
        }
    }

    static class IllegalTestResource1 extends Resource {
        protected IllegalTestResource1(XingApi api) {
            super(api);
        }
    }

    @SuppressWarnings("InnerClassMayBeStatic") // Intentional.
    final class IllegalTestResource2 extends Resource {
        IllegalTestResource2(XingApi api) {
            super(api);
        }
    }
}
