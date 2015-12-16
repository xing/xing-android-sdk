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

import com.squareup.moshi.Json;

import java.util.List;

/**
 * Generic Error Body Object.
 *
 * This is the default object that is returned when the request is successful but there was something wrong with the
 * request.
 *
 * The API will respond to invalid requests with common HTTP status code like 401, 403 and 404. In some cases the
 * status code is ambiguous so you might not be able to tell, e.g. if the error was a missing parameter or an invalid
 * value for a parameter. In order to make sure you can react to certain errors, we provide a JSON body for (almost)
 * all client error responses.
 *
 * @author daniel.hartwich
 */
public class ErrorBody {
    @Json(name = "error_name")
    private String errorName;
    @Json(name = "message")
    private String errorMessage;
    @Json(name = "errors")
    private List<Error> errors;

    public String getErrorName() {
        return errorName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public static final class Error {
        @Json(name = "field")
        private String field;
        @Json(name = "reason")
        private Reason reason;

        @SuppressWarnings("InnerClassTooDeeplyNested")
        enum Reason {
            UNEXPECTED("UNEXPECTED"),
            UNEXPECTED_VALUE("UNEXPECTED_VALUE"),
            MISSING("MISSING"),
            INVALID_FORMAT("INVALID_FORMAT"),
            NOT_UNIQUE("NOT_UNIQUE"),
            UNKNOWN_VALUE("UNKNOWN_VALUE"),
            TOO_SHORT("TOO_SHORT"),
            TOO_LONG("TOO_LONG"),
            LOWER_BOUND_EXCEEDED("LOWER_BOUND_EXCEEDED"),
            FIELD_DEPRECATED("FIELD_DEPRECATED"),
            TOO_MANY_ENTRIES("TOO_MANY_ENTRIES");

            final String text;

            Reason(final String text) {
                this.text = text;
            }

            @Override
            public String toString() {
                return text;
            }
        }
    }

    @Override
    public String toString() {
        return "ErrorBody{"
              + "errorName='" + errorName + '\''
              + ", errorMessage='" + errorMessage + '\''
              + ", errors=" + errors
              + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ErrorBody that = (ErrorBody) o;

        return (errorName != null ? errorName.equals(that.errorName) : that.errorName == null)
              && (errorMessage != null ? errorMessage.equals(that.errorMessage) : that.errorMessage == null)
              && (errors != null ? errors.equals(that.errors) : that.errors == null);
    }

    @Override
    public int hashCode() {
        int result = errorName != null ? errorName.hashCode() : 0;
        result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
        result = 31 * result + (errors != null ? errors.hashCode() : 0);
        return result;
    }
}
