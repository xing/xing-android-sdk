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

package com.xing.android.sdk.network;

import android.net.Uri;
import android.os.Build;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.xing.android.sdk.network.oauth.OauthSigner;
import com.xing.android.sdk.network.request.Request;
import com.xing.android.sdk.network.request.exception.NetworkException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Handler;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author serj.lotutovici
 */
@SuppressWarnings("MagicNumber")
@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = Config.NONE)
public class RequestExecutorTest {

    RequestExecutor testExecutor;

    private RequestConfig mockAdapter;
    private MockWebServer mockServer;

    @Before
    public void setUp() throws Exception {
        XingController.flush();

        OauthSigner mockSigner = mock(OauthSigner.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(mockSigner).sign(Mockito.<HttpURLConnection>any());
        mockAdapter = mock(RequestConfig.class);
        when(mockAdapter.getOauthSigner()).thenReturn(mockSigner);
        when(mockAdapter.getCommonHeaders()).thenReturn(null);
        when(mockAdapter.getBaseUri()).thenReturn(
                new Uri.Builder().scheme("https").authority("blabla.test.com").build());

        Logger logger = Logger.getLogger(MockWebServer.class.getName());
        Handler[] handlers = logger.getHandlers();
        for (Handler handler : handlers) {
            logger.removeHandler(handler);
        }

        mockServer = new MockWebServer();
        mockServer.start();

        testExecutor = new RequestExecutor(mockAdapter, new MockUrlFactory(mockServer));
    }

    @After
    public void tearDown() throws Exception {
        mockServer.shutdown();
    }

    @Test
    public void executeThrowsNetworkExceptionOn400() throws Exception {
        mockServer.enqueue(new MockResponse().setResponseCode(400).setBody("Mock error message"));

        Request testRequest = new Request.Builder(Request.Method.GET).setUri(Uri.parse("/test/error")).build();

        try {
            testExecutor.execute(testRequest);
            fail();
        } catch (NetworkException ne) {
            assertEquals(400, ne.getErrorCode());
            assertEquals("Mock error message", ne.getResponse());
        }
    }

    @Test
    public void executeThrowsNetworkExceptionOn600() throws Exception {
        mockServer.enqueue(new MockResponse().setResponseCode(600).setBody("Second Mock error message"));

        Request testRequest = new Request.Builder(Request.Method.GET).setUri(Uri.parse("/test/error2")).build();

        try {
            testExecutor.execute(testRequest);
            fail();
        } catch (NetworkException ne) {
            assertEquals(600, ne.getErrorCode());
            assertEquals("Second Mock error message", ne.getResponse());
        }
    }

    @Test
    public void executeThrowsNetworkExceptionOnIOException() throws Exception {
        final IOException ioException = new IOException();
        OauthSigner mockSigner = mock(OauthSigner.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                throw ioException;
            }
        }).when(mockSigner).sign(Mockito.<HttpURLConnection>any());
        when(mockAdapter.getOauthSigner()).thenReturn(mockSigner);

        mockServer.enqueue(new MockResponse());

        Request testRequest = new Request.Builder(Request.Method.GET).setUri(Uri.parse("/test/this_will_fail")).build();

        try {
            testExecutor.execute(testRequest);
            fail();
        } catch (NetworkException ne) {
            assertEquals(NetworkException.NULL_ERROR_CODE, ne.getErrorCode());
            assertEquals(ioException, ne.getCause());
        }
    }

    @Test(expected = OauthSigner.XingOauthException.class)
    public void executeThrowsOauthException() throws Exception {
        OauthSigner mockSigner = mock(OauthSigner.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                throw new OauthSigner.XingOauthException(new Exception());
            }
        }).when(mockSigner).sign(Mockito.<HttpURLConnection>any());
        when(mockAdapter.getOauthSigner()).thenReturn(mockSigner);

        mockServer.enqueue(new MockResponse());

        Request testRequest =
                new Request.Builder(Request.Method.POST).setUri(Uri.parse("/test/auth_will_fail")).build();

        testExecutor.execute(testRequest);
    }

    @Test
    public void executeReturnsStringResponse() throws Exception {
        mockServer.enqueue(new MockResponse().setResponseCode(200).setBody("Response OK"));

        Request testRequest = new Request.Builder(Request.Method.GET).setUri(Uri.parse("/")).build();

        String response = testExecutor.execute(testRequest);
        assertEquals("Response OK", response);
    }

    @Test
    public void addExtraConfig() throws Exception {
        RequestConfig requestConfig = new DefaultRequestConfig.Builder().setConsumerKey("dsf")
                .setConsumerSecret("ds")
                .setToken("dsf")
                .setTokenSecret("dfdf")
                .build();

        RequestExecutor requestExecutor = new RequestExecutor(requestConfig);

        RequestConfig loggedOutRequestConfig =
                new DefaultRequestConfig.Builder().setLoggedOut(true).addParam("key", "value").build();

        requestExecutor.addRequestConfig("LoggedOut", loggedOutRequestConfig);

        //        assertEquals(1, requestExecutor.);
    }
}
