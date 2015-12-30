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

import com.squareup.moshi.Json;

import java.util.List;

/**
 * Generic HTTP Error Object that maps the error response of XWS.
 *
 * This is the default object that is returned when the request is successful but the server returned an error request.
 *
 * The API will respond to invalid requests with common HTTP status code like 401, 403 and 404. In some cases the
 * status code is ambiguous so you might not be able to tell, e.g. if the error was a missing parameter or an invalid
 * value for a parameter. In order to make sure that a user can react to certain errors, we provide a JSON body for
 * (almost)
 * all client error responses.
 *
 * @author daniel.hartwich
 * @see <a href="https://dev.xing.com/docs/error_responses">Xing API Error Responses</a>
 */
public class HttpError {
    @Json(name = "error_name")
    private final String errorName;
    @Json(name = "message")
    private final String errorMessage;
    @Json(name = "errors")
    private final List<Error> errors;

    // This object is not to be instantiated outside this package.
    HttpError(String errorName, String errorMessage, List<Error> errors) {
        this.errorName = errorName;
        this.errorMessage = errorMessage;
        this.errors = errors;
    }

    public String getErrorName() {
        return errorName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<Error> getErrors() {
        return errors;
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

        HttpError error = (HttpError) o;

        return (errorName != null ? errorName.equals(error.errorName) : error.errorName == null)
              && (errorMessage != null ? errorMessage.equals(error.errorMessage) : error.errorMessage == null)
              && (errors != null ? errors.equals(error.errors) : error.errors == null);
    }

    @Override
    public int hashCode() {
        int result = errorName != null ? errorName.hashCode() : 0;
        result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
        result = 31 * result + (errors != null ? errors.hashCode() : 0);
        return result;
    }

    /** Represents an error for a specific form field or query parameters, with a respective reason for the error. */
    public static final class Error {
        @Json(name = "field")
        public final String field;
        @Json(name = "reason")
        public final Reason reason;

        // This object is not to be instantiated outside this package.
        Error(String field, Reason reason) {
            this.field = field;
            this.reason = reason;
        }

        @Override
        public String toString() {
            return "Error{"
                  + "field='" + field + '\''
                  + ", reason=" + reason
                  + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Error error = (Error) o;

            return (field != null ? field.equals(error.field) : error.field == null) && reason == error.reason;
        }

        @Override
        public int hashCode() {
            int result = field != null ? field.hashCode() : 0;
            result = 31 * result + (reason != null ? reason.hashCode() : 0);
            return result;
        }

        /** Possible error reason values. */
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

            Reason(String text) {
                this.text = text;
            }

            @Override
            public String toString() {
                return text;
            }
        }
    }
}
