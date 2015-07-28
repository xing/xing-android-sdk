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
package com.xing.android.sdk.network.request;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Basic representation of a HTTP/HTTPS request
 *
 * @author serj.lotutovici
 */
// TODO doc & tests?
public class Request {

    protected Uri uri;
    protected Method method;
    protected String body;
    protected List<Pair<String, String>> headers;
    protected List<Pair<String, String>> params;

    @SuppressWarnings("unchecked")
    protected Request(@NonNull Builder builder) {
        this.method = builder.method;
        this.uri = builder.uri;
        this.body = builder.body;
        this.params = builder.params;
        this.headers = builder.headers;
    }

    public Uri getUri() {
        return uri;
    }

    public Method getMethod() {
        return method;
    }

    public String getBody() {
        return body;
    }

    public List<Pair<String, String>> getHeaders() {
        return headers;
    }

    public List<Pair<String, String>> getParams() {
        return params;
    }

    public boolean hasBody() {
        return body != null;
    }

    public boolean hasHeaders() {
        return headers != null;
    }

    public boolean hasParams() {
        return params != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Request)) {
            return false;
        }

        Request request = (Request) o;

        return (body != null ? body.equals(request.body) : request.body == null) &&
                (headers != null ? headers.equals(request.headers) : request.headers == null) &&
                method == request.method &&
                (params != null ? params.equals(request.params) : request.params == null) &&
                uri.equals(request.uri);

    }

    @Override
    public int hashCode() {
        int result = uri.hashCode();
        result = 31 * result + method.hashCode();
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        result = 31 * result + (params != null ? params.hashCode() : 0);
        return result;
    }

    /**
     * Represents the request method value wrapper
     */
    public enum Method {

        GET("GET"),

        PUT("PUT"),

        POST("POST"),

        DELETE("DELETE");

        public final String value;

        Method(String value) {
            this.value = value;
        }
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends Builder> {

        private final Method method;
        private Uri uri;
        private String body;
        private final List<Pair<String, String>> params;
        private final List<Pair<String, String>> headers;

        public Builder(Method method) {
            this.method = method;
            this.params = new ArrayList<>();
            this.headers = new ArrayList<>();
        }

        public T setUri(Uri uri) {
            this.uri = uri;
            return (T) this;
        }

        public T setBody(String body) {
            this.body = body;
            return (T) this;
        }

        public T addParam(Pair<String, String> pair) {
            params.add(pair);
            return (T) this;
        }

        public T addParams(Collection<Pair<String, String>> pairs) {
            params.addAll(pairs);
            return (T) this;
        }

        public T addHeader(Pair<String, String> header) {
            headers.add(header);
            return (T) this;
        }

        public T addHeaders(Collection<Pair<String, String>> headers) {
            this.headers.addAll(headers);
            return (T) this;
        }

        public Request build() {
            validateArguments();

            return new Request(this);
        }

        protected void validateArguments() {
            if (method == null) {
                throw new IllegalArgumentException("Can't build request with no method");
            }

            if (uri == null) {
                throw new IllegalArgumentException("Can't build request without url");
            }
        }
    }
}
