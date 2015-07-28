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

import android.content.Context;
import android.net.http.HttpResponseCache;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.xing.android.sdk.network.info.Optional;
import com.xing.android.sdk.network.oauth.OauthSigner;
import com.xing.android.sdk.network.request.Request;
import com.xing.android.sdk.network.request.exception.NetworkException;
import com.xing.android.sdk.task.PrioritizedThreadPoolExecutor;
import com.xing.android.sdk.task.RunnableComparator;
import com.xing.android.sdk.task.RunnableExecutor;
import com.xing.android.sdk.task.Task;
import com.xing.android.sdk.task.TaskManager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Central controller that manages the request execution
 *
 * @author david.gonzalez
 * @author serj.lotutovici
 */
public class XingController {
    private static final long HTTP_CACHE_SIZE = 10 * 1024 * 1024;   // 10 MiB

    private static volatile XingController sController;

    private final RequestExecutor mRequestExecutor;
    private final TaskManager mTaskManager;

    /**
     * Create instance of {@link XingController}
     */
    private XingController(@NonNull RequestExecutor requestExecutor,
                           @NonNull TaskManager taskManager) {
        mRequestExecutor = requestExecutor;
        mTaskManager = taskManager;
    }

    /**
     * Get a {@link XingController} instance
     */
    public static XingController getInstance() {
        if (sController == null) {
            throw new InitializationException();
        }
        return sController;
    }

    /**
     * Reset {@link XingController}. The API will be unusable until
     * {@link XingController#setup()} is called
     */
    public static void flush() {
        sController = null;
    }

    /**
     * Enable HTTP cache
     */
    private static void enableCache(final Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Can not enabled HTTP cache with null context");
        }

        File httpCacheDir = new File(context.getCacheDir(), "http");
        try {
            HttpResponseCache.install(httpCacheDir, HTTP_CACHE_SIZE);
        } catch (IOException ignored) {
        }
    }

    /**
     * Start the {@link XingController} setup
     */
    public static Setup setup() {
        return new Setup();
    }

    public RequestExecutor getRequestExecutor() {
        return mRequestExecutor;
    }

    public TaskManager getTaskManager() {
        return mTaskManager;
    }

    /**
     * Execute request
     */
    public String execute(Request request) throws NetworkException, OauthSigner.XingOauthException {
        return mRequestExecutor.execute(request);
    }

    /**
     * Execute request with the specific requestConfig
     */
    public String execute(Request request, RequestConfig requestConfig) throws NetworkException, OauthSigner.XingOauthException {
        return mRequestExecutor.execute(request, requestConfig);
    }

    public <T> Future executeAsync(Task<T> task) {
        return mTaskManager.executeAsync(task);
    }

    /**
     * Cancel the execution of all the tasks for the specific key.
     *
     * @param key Key of the tasks to be canceled.
     */
    public void cancelExecution(@NonNull Object key) {
        mTaskManager.cancelExecution(key);
    }

    /**
     * Remove the listeners from all the TaggedRunnables. When a key is not listened anymore, it can not be cancelled.
     *
     * @param key Key of the tasks which the listener has to be removed.
     */
    public void stopListening(@NonNull Object key) {
        mTaskManager.stopListening(key);
    }

    /**
     * Setup builder like class for {@link XingController}
     * To initialize the request controller 4 mandatory parameters should be set
     * before calling {@link Setup#init()}
     * <p/>
     * Usage:
     * <pre>
     *     XingRequestController.setup()
     *          .setConsumerKey("YOUR_CONSUMER_KEY")
     *          .setConsumerSecret("YOUR_CONSUMER_SECRET")
     *          .setToken("TOKEN_RECEIVED_AFTER_USER_AUTH")
     *          .setTokenSecret("TOKEN_SECRET_RECEIVED_AFTER_USER_AUTH")
     *          .init();
     * </pre>
     * <p/>
     * <b>WARNING: </b>In case when the controller was not initialized any attend to execute a request
     * to the XING API via the {@link XingController} will result in a
     * {@link InitializationException}
     * <p/>
     * In case XING requested additional headers to be passed with every request (ex. you have early
     * access to experimental APIs or partner APIs) you can do it by using one of the following method:
     * <pre>
     *     XingRequestController.setup()
     *          .addHeader("HEADER_KEY", "HEADER_VALUE") // ex. "Accept-Language:en_EN" -> key["Accept-Language"]; value["en_EN"]
     *          // or
     *          .addHeaders(aListOfHeaders)
     *          // ...
     *          .init();
     * </pre>
     * <b>DISCLAIMER: </b> By default XING does not require additional headers to be set by the user of
     * this API and will not guaranty correct work of this API if done so, unless XING or any
     * other XING representative has requested you to do so.
     *
     * @author serj.lotutovici
     */
    public final static class Setup {

        RequestExecutor mExecutor;
        RunnableExecutor mRunnableExecutor;
        DefaultRequestConfig.Builder mDefaultRequestConfigBuilder;

        /**
         * Should not be instantiated from outside
         */
        private Setup() {
            mDefaultRequestConfigBuilder = new DefaultRequestConfig.Builder();
        }

        /**
         * Set a {@link RequestExecutor} instead of the default one
         * <p/>
         * <b>Disclaimer: </b> Currently there is no appropriate way to use this method.
         * Please initialize the controller by passing to the {@link Setup}
         * all mandatory values
         */
        @Optional
        public Setup setExecutor(RequestExecutor executor) {
            if (executor == null) {
                throw new IllegalArgumentException("Executor can not be null");
            }
            mExecutor = executor;

            return this;
        }

        /**
         * Set a {@link RunnableExecutor} instead of the default one
         * <p/>
         * <b>Disclaimer: </b> Currently there is no appropriate way to use this method.
         * Please initialize the controller by passing to the {@link Setup}
         * all mandatory values
         */
        @Optional
        public Setup setRunnableExecutor(RunnableExecutor runnableExecutor) {
            if (runnableExecutor == null) {
                throw new IllegalArgumentException("RunnableExecutor can not be null");
            }
            mRunnableExecutor = runnableExecutor;

            return this;
        }

        /**
         * Enable cache
         *
         * @param context Application context. Required in case enabled is true
         * @param enabled Flag that shows the status of the cache (pass true to enable)
         */
        @Optional
        public final Setup cacheEnabled(Context context, boolean enabled) {
            if (enabled) {
                enableCache(context);
            }

            return this;
        }

        /**
         * Set consumer key
         */
        public Setup setConsumerKey(String consumerKey) {
            mDefaultRequestConfigBuilder.setConsumerKey(consumerKey);

            return this;
        }

        /**
         * Set consumer secret
         */
        public Setup setConsumerSecret(String consumerSecret) {
            mDefaultRequestConfigBuilder.setConsumerSecret(consumerSecret);

            return this;
        }

        /**
         * Set token
         */
        public Setup setToken(String token) {
            mDefaultRequestConfigBuilder.setToken(token);

            return this;
        }

        /**
         * Set token secret
         */
        public Setup setTokenSecret(String tokenSecret) {
            mDefaultRequestConfigBuilder.setTokenSecret(tokenSecret);

            return this;
        }

        /**
         * Set loggedOut. This avoids the check of oauth values.
         */
        public Setup setLoggedOut(boolean loggedOut) {
            mDefaultRequestConfigBuilder.setLoggedOut(loggedOut);

            return this;
        }

        /**
         * Add global param
         */
        @Optional
        public Setup addParam(String key, String value) {
            mDefaultRequestConfigBuilder.addParam(key, value);

            return this;
        }

        /**
         * Add global params
         */
        @Optional
        public Setup addParams(List<Pair<String, String>> params) {
            mDefaultRequestConfigBuilder.addParams(params);

            return this;
        }

        /**
         * Add global header
         */
        @Optional
        public Setup addHeader(String key, String value) {
            mDefaultRequestConfigBuilder.addHeader(key, value);

            return this;
        }

        /**
         * Add global headers
         */
        @Optional
        public Setup addHeaders(List<Pair<String, String>> headers) {
            mDefaultRequestConfigBuilder.addHeaders(headers);

            return this;
        }

        /**
         * Validate the input and initialize the {@link XingController}
         *
         * @throws IllegalArgumentException In case one of mandatory parameters is not initialized
         * @throws IllegalStateException    In case the controller was initialized and not flushed
         */
        public final void init() {
            /* Check if request executor was set, if not initialize executor */
            if (mExecutor == null) {
                mExecutor = new RequestExecutor(mDefaultRequestConfigBuilder.build());
            }

            /* Check if the background executor was set, if not initialize default */
            if (mRunnableExecutor == null) {
                int numThreads = Runtime.getRuntime().availableProcessors() + 1;
                mRunnableExecutor = new PrioritizedThreadPoolExecutor(
                        numThreads,
                        numThreads,
                        0,
                        TimeUnit.MILLISECONDS,
                        new PriorityBlockingQueue<>(numThreads, new RunnableComparator())
                );
            }

            synchronized (XingController.class) {
                if (sController != null) {
                    throw new IllegalStateException("A controller was initialized already, call XingRequestExecutor#flush() first");
                }

                sController = new XingController(mExecutor, new TaskManager(mRunnableExecutor));
            }
        }
    }

    /**
     * Exception thrown when {@link XingController} not initialized
     */
    public static class InitializationException extends IllegalStateException {

        private static final long serialVersionUID = 2987179177751910361L;

        @Override
        public String getMessage() {
            return "First setup and initialize XingController. Start by calling XingController#setup()";
        }
    }
}
