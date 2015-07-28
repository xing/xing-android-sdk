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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Pair;

import com.xing.android.sdk.network.oauth.OauthSigner;
import com.xing.android.sdk.network.request.Request;
import com.xing.android.sdk.network.request.RequestUtils;
import com.xing.android.sdk.network.request.exception.NetworkException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default request executor that creates an {@link HttpURLConnection}, signs the request
 * and executes it to the base api url
 *
 * @author david.gonzalez
 * @author serj.lotutovici
 */
public class RequestExecutor {
    private static final int SMALLER_ERROR_CODE = 400;
    private static final int BIGGER_ERROR_CODE = 600;

    private RequestConfig mRequestConfig;
    private final AbsUrlFactory mUrlFactory;
    private final Map<String, RequestConfig> mRequestConfigurations;

    protected RequestExecutor(@NonNull final RequestConfig requestConfig) {
        this(requestConfig, new DefaultUrlFactory());
    }

    protected RequestExecutor(
            @NonNull final RequestConfig requestConfig,
            @NonNull final AbsUrlFactory factory) {
        mRequestConfig = requestConfig;
        mUrlFactory = factory;
        mRequestConfigurations = new HashMap<>();
    }

    /**
     * Saves the configuration with the tag, so can be easily used in future request just specifying the tag.
     *
     * @param tag           Tag to identify the configuration.
     * @param requestConfig Non-default configuration that can be used for executing requests.
     */
    public void addRequestConfig(@NonNull String tag, @NonNull RequestConfig requestConfig) {
        if (mRequestConfigurations.containsKey(tag)) {
            throw new IllegalArgumentException("This tag is already in use.");
        } else {
            mRequestConfigurations.put(tag, requestConfig);
        }
    }

    /**
     * Removes the configuration for the specific tag.
     *
     * @param tag The tag of the configuration to be removed.
     */
    public void removeRequestConfig(@NonNull String tag) {
        mRequestConfigurations.remove(tag);
    }

    /**
     * Establish as the default config the one assigned to the tag received as a param.
     *
     * @param tag Tag of the desired default config.
     * @return true if there is any config for the tag, false otherwise.
     */
    public boolean setDefaultRequestConfig(@NonNull String tag) {
        boolean output = mRequestConfigurations.containsKey(tag);
        if (output) {
            mRequestConfig = mRequestConfigurations.get(tag);
        }

        return output;
    }

    /**
     * Establish the config as the default one.
     *
     * @param config Configuration to be established as the default one.
     */
    public void setDefaultRequestConfig(@NonNull RequestConfig config) {
        mRequestConfig = config;
    }


    /**
     * Execute the passed request
     *
     * @param tag     The tag of the requestConfig that the requestExecutor should use.
     * @param request The request to execute.
     * @return The string contents of the request response.
     * @throws NetworkException               In case an network/execution error occurred or the server returned an unexpected response.
     * @throws OauthSigner.XingOauthException In case the {@link OauthSigner} was not initialized properly.
     */
    public String execute(@NonNull String tag, @NonNull Request request) throws NetworkException, OauthSigner.XingOauthException {
        RequestConfig requestConfig = mRequestConfigurations.get(tag);
        if (requestConfig == null) {
            throw new IllegalArgumentException("no saved request configuration for the specified tag");
        } else {
            return execute(request, requestConfig);
        }
    }

    /**
     * Execute the passed request
     *
     * @param request The request to execute.
     * @return The string contents of the request response.
     * @throws NetworkException               In case an network/execution error occurred or the server returned an unexpected response.
     * @throws OauthSigner.XingOauthException In case the {@link OauthSigner} was not initialized properly.
     */
    public String execute(Request request) throws NetworkException, OauthSigner.XingOauthException {
        return execute(request, mRequestConfig);
    }

    /**
     * @param request       The request to execute.
     * @param requestConfig The request configuration that will be used fir this specific request.
     * @return The string contents of the request response.
     * @throws NetworkException               In case an network/execution error occurred or the server returned an unexpected response.
     * @throws OauthSigner.XingOauthException In case the {@link OauthSigner} was not initialized properly.
     */
    public String execute(Request request, RequestConfig requestConfig) throws NetworkException, OauthSigner.XingOauthException {
        HttpURLConnection connection = null;
        try {

            // Prepare the full path url
            Uri.Builder uriBuilder = requestConfig.getBaseUri().buildUpon();
            uriBuilder.appendEncodedPath(request.getUri().toString());

            // adds the params of the requestConfig, it means, the basic params that are added to
            // all the requests which uses this configuration.
            RequestUtils.appendParamsToBuilder(uriBuilder, requestConfig.getCommonParams());

            // If GET request append params
            if (request.getMethod() == Request.Method.GET) {
                RequestUtils.appendParamsToBuilder(uriBuilder, request.getParams());
            }
            // Open connection
            connection = mUrlFactory.open(new URL(uriBuilder.build().toString()));

            // Set request method
            connection.setRequestMethod(request.getMethod().value);

            // Set additional headers
            connection.setRequestProperty("Accept", "application/json");
            switch (request.getMethod()) {
                case PUT:
                case POST: {
                    if (TextUtils.isEmpty(request.getBody())) {
                        break;
                    }
                }
                case DELETE: {
                    connection.setRequestProperty("Content-Type", "application/json; charset=utf8");
                }
            }

            addHeadersToConnection(connection, requestConfig.getCommonHeaders());
            addHeadersToConnection(connection, request.getHeaders());

            // Signs the request only if the requestConfig contains an oauthSigner.
            OauthSigner oauthSigner = requestConfig.getOauthSigner();
            if (oauthSigner != null) {
                oauthSigner.sign(connection);
            }

            // If post or put send body
            if (request.getMethod() == Request.Method.POST || request.getMethod() == Request.Method.PUT) {
                if (!TextUtils.isEmpty(request.getBody())) {
                    connection.setFixedLengthStreamingMode(request.getBody().getBytes().length);
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                    bufferedWriter.write(request.getBody());
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
            }

            // Read response code
            final int responseCode = connection.getResponseCode();
            if (responseCode >= SMALLER_ERROR_CODE && responseCode <= BIGGER_ERROR_CODE) {
                final String response = RequestUtils.inputStreamToString(connection.getErrorStream());
                throw new NetworkException(responseCode, response, connection.getResponseMessage());
            } else {
                return RequestUtils.inputStreamToString(connection.getInputStream());
            }

        } catch (IOException ex) {
            throw new NetworkException(NetworkException.NULL_ERROR_CODE, null, ex);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Add headers to {@link HttpURLConnection}
     *
     * @param connection The connection that requires headers
     * @param headers    Headers to add
     */
    private static void addHeadersToConnection(
            @Nullable final HttpURLConnection connection,
            @Nullable final List<Pair<String, String>> headers) {
        if (connection != null && headers != null) {
            for (Pair<String, String> header : headers) {
                connection.setRequestProperty(header.first, header.second);
            }
        }
    }
}
