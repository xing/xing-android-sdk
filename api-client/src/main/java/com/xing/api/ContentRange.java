/*
 * Copyright (C) 2018 XING SE (http://xing.com/)
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

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the content range data of the response. This is useful when a paginated response is being accepted.
 * Contains information about the current content offset, range and total available values.
 * <p>
 * The XING API returns a <strong>Xing-Content-Range</strong> header for all paginated requests. The format is as follows:
 * <ul>
 * <li><i>"Xing-Content-Range: items [offset]-[last]/[total]"</i> - if the total number is <strong>known</strong>.</li>
 * <li><i>"Xing-Content-Range: items [offset]-[last]/*"</i> - if the total number is <strong>unknown</strong>.</li>
 * <li><i>"Xing-Content-Range: items &#42;/0"</i> - if the response returned an empty collection.</li>
 * </ul>
 */
public final class ContentRange implements Serializable {
    private static final long serialVersionUID = 1L;

    static final Pattern HEADER_PATTERN = Pattern.compile("^items ((\\d+)-(\\d+)|\\*)/(\\d+|\\*)\\z");
    static final String HEADER_NAME = "Xing-Content-Range";

    /** Parse the header value and return the reflecting {@linkplain ContentRange} instance. Otherwise {@code null}. */
    static ContentRange parse(String headerValue) {
        if (headerValue != null) {
            Matcher matcher = HEADER_PATTERN.matcher(headerValue);
            if (matcher.find()) {
                int offset = -1;
                int last = -1;
                int total = -1;

                // For empty collections '*' is the value for the range;
                String head = matcher.group(1);
                if (!"*".equals(head)) {
                    offset = Integer.parseInt(matcher.group(2));
                    last = Integer.parseInt(matcher.group(3));
                }

                // We need to check if we have the total value
                String tail = matcher.group(4);
                if (!"*".equals(tail)) total = Integer.parseInt(tail);

                return new ContentRange(offset, last, total);
            }
        }
        return null;
    }

    private final int offset;
    private final int last;
    private final int total;

    /** Constructor for internal use only. */
    ContentRange(int offset, int last, int total) {
        this.offset = offset;
        this.last = last;
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContentRange range = (ContentRange) o;
        return offset == range.offset && last == range.last && total == range.total;
    }

    @Override
    public int hashCode() {
        int result = offset;
        result = 31 * result + last;
        result = 31 * result + total;
        return result;
    }

    @Override
    public String toString() {
        return "Xing-Content-Range: items "
              + (offset < 0 || last < 0 ? "*" : offset + "-" + last) + '/'
              + (total >= 0 ? total : "*");
    }

    /** Returns the content rage offset. Otherwise {@code -1} if none was parsed. */
    public int offset() {
        return offset;
    }

    /** Returns the content range last element position. Otherwise {@code -1} if none was parsed. */
    public int last() {
        return last;
    }

    /**
     * Return the content total. Otherwise {@code -1} if none was parsed (may occur when the response was
     * {@code [offset]-[last]/*}).
     */
    public int total() {
        return total;
    }

    /** Returns {@code true} if the response returned an empty collection. */
    public boolean isEmpty() {
        return total == 0;
    }
}
