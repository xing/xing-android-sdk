/*
 * Copyright (С) 2016 XING SE (http://xing.com/)
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
import com.xing.api.Resource.Factory;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.SocketPolicy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
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
    public void getResourceViaFactory() throws Exception {
        XingApi api = new XingApi.Builder()
              .custom()
              .addResourceFactory(new Factory(FactoryResource.class) {
                  @Override public Resource create(XingApi api) {
                      return new FactoryResource(api, "test");
                  }
              }).build();
        Resource resource = api.resource(FactoryResource.class);
        assertThat(resource).isInstanceOf(FactoryResource.class);

        FactoryResource testResource = api.resource(FactoryResource.class);
        assertThat(testResource).isSameAs(resource);
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
            fail("Should fail on overridden constructor.");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class)
                  .hasMessage("Resource class malformed.")
                  .hasCauseInstanceOf(NoSuchMethodException.class);
        }
    }

    @SuppressWarnings("ConstantConditions") // Passes nulls intentionally.
    @Test
    public void builderOAuth1() throws Exception {
        XingApi.OAuth1Step oAuth1Step = new XingApi.Builder().oauth1();
        assertBuildStep(oAuth1Step);

        try {
            oAuth1Step.accessSecret(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("accessSecret == null");
        }

        try {
            oAuth1Step.accessToken(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("accessToken == null");
        }

        try {
            oAuth1Step.consumerKey(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("consumerKey == null");
        }

        try {
            oAuth1Step.consumerSecret(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("consumerSecret == null");
        }

        try {
            oAuth1Step.build();
            fail("Build should throw if oauth is not set.");
        } catch (IllegalStateException expected) {
            assertThat(expected.getMessage()).contains("not set");
        }
    }

    @Test
    public void builderLoggedOut() throws Exception {
        XingApi.LoggedOutStep loggedOutStep = new XingApi.Builder().loggedOut();
        assertBuildStep(loggedOutStep);
    }

    @Test
    public void builderCustom() throws Exception {
        XingApi.CustomStep customStep = new XingApi.Builder().custom();
        assertBuildStep(customStep);
    }

    @Test
    public void apiEndpointDefault() throws Exception {
        XingApi api = buildDefaultApi();
        assertThat(api.apiEndpoint().toString()).isEqualTo("https://api.xing.com/");
    }

    @Test
    public void apiEndpointPropagated() throws Exception {
        XingApi api1 = new XingApi.Builder()
              .custom()
              .apiEndpoint(HttpUrl.parse("http://test.com/"))
              .build();
        assertThat(api1.apiEndpoint().toString()).isEqualTo("http://test.com/");

        XingApi api2 = new XingApi.Builder()
              .custom()
              .apiEndpoint("https://test2.com/")
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
              .custom()
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
              .custom()
              .apiEndpoint(server.url("/"))
              .callbackExecutor(executor)
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
        assertThat(latch.await(2, TimeUnit.SECONDS)).isTrue();

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
              .custom()
              .apiEndpoint(server.url("/"))
              .callbackExecutor(executor)
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
        assertThat(latch.await(2, TimeUnit.SECONDS)).isTrue();

        verify(executor).execute(any(Runnable.class));
        verifyNoMoreInteractions(executor);
    }

    @Test
    public void callbackExecutorSupportsNullCallbacks() throws Exception {
        Executor executor = spy(new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        });
        XingApi api = new XingApi.Builder()
              .custom()
              .apiEndpoint(server.url("/"))
              .callbackExecutor(executor)
              .build();
        LegalTestResource resource = api.resource(LegalTestResource.class);
        CallSpec<String, HttpError> spec = resource.simpleSpec();

        server.enqueue(new MockResponse());
        spec.enqueue(null);

        // FIXME this is error prone, we need a better solution for this.
        Thread.sleep(500);

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
              .custom()
              .apiEndpoint(server.url("/"))
              .callbackExecutor(executor)
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
              .custom()
              .apiEndpoint(server.url("/"))
              .build();

        LegalTestResource resource = api.resource(LegalTestResource.class);
        CallSpec<String, HttpError> spec = resource.simpleSpec();

        AuthErrorCallback callback1 = spy(AuthErrorCallback.class);
        AuthErrorCallback callback2 = spy(AuthErrorCallback.class);

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

    private static <T extends XingApi.BuildStep> void assertBuildStep(T buildStep) throws Exception {
        try {
            buildStep.apiEndpoint((String) null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("apiEndpoint == null");
        }

        try {
            buildStep.apiEndpoint((HttpUrl) null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("apiEndpoint == null");
        }

        try {
            buildStep.apiEndpoint("http/invalid-url.org");
            fail("Builder should throw on invalid endpoints.");
        } catch (IllegalArgumentException expected) {
            assertThat(expected).hasMessage("Illegal endpoint URL: http/invalid-url.org");
        }

        try {
            buildStep.callbackExecutor(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("callbackExecutor == null");
        }

        try {
            buildStep.moshi(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("moshi == null");
        }

        buildStep.moshi(new Moshi.Builder().build());
        try {
            buildStep.moshi(new Moshi.Builder().build());
            fail("Only one Moshi should be allowed.");
        } catch (IllegalStateException expected) {
            assertThat(expected).hasMessage("Only one instance of Moshi is allowed");
        }

        try {
            buildStep.addResourceFactory(null);
            fail("Should fail on null factory");
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("factory == null");
        }
    }

    static final class LegalTestResource extends Resource {
        LegalTestResource(XingApi api) {
            super(api);
        }

        public CallSpec<String, HttpError> simpleSpec() {
            return Resource.<String, HttpError>newGetSpec(api, "/").responseAs(String.class).build();
        }
    }

    @SuppressWarnings("unused")
    static class FactoryResource extends Resource {
        FactoryResource(XingApi api, String someValue) {
            super(api);
        }

        FactoryResource(XingApi api) {
            super(api);
            throw new AssertionError("Resource should be created via factory");
        }
    }

    @SuppressWarnings("ConstantConditions") // Intentional.
    static final class LegalTestResourceOverridesConstructor extends Resource {
        LegalTestResourceOverridesConstructor() {
            super(null);
        }
    }

    @SuppressWarnings("InnerClassMayBeStatic") // Intentional.
    final class IllegalTestResource2 extends Resource {
        IllegalTestResource2(XingApi api) {
            super(api);
        }
    }
}
