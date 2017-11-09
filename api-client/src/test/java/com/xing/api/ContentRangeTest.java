/*
 * Copyright (C) 2016 XING SE (http://xing.com/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xing.api;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/** Tests the content range. */
public final class ContentRangeTest {
    @Test
    public void prettyPrinted() throws Exception {
        ContentRange range1 = new ContentRange(1, 3, 10);
        assertThat(range1.toString()).isEqualTo("Xing-Content-Range: items 1-3/10");

        ContentRange range2 = new ContentRange(1, 4, -1);
        assertThat(range2.toString()).isEqualTo("Xing-Content-Range: items 1-4/*");

        ContentRange range3 = new ContentRange(-1, -1, 0);
        assertThat(range3.toString()).isEqualTo("Xing-Content-Range: items */0");
    }

    @Test
    public void nulls() throws Exception {
        assertThat(ContentRange.parse(null)).isNull();
        assertThat(ContentRange.parse("awefss")).isNull();
        assertThat(ContentRange.parse("items   1/")).isNull();
        assertThat(ContentRange.parse("items 1/2")).isNull();
        assertThat(ContentRange.parse("items1-3/2")).isNull();
        assertThat(ContentRange.parse("items232dsf")).isNull();
    }

    @Test
    public void knownTotal() throws Exception {
        ContentRange range = ContentRange.parse("items 1-3/42");
        assertThat(range.offset()).isEqualTo(1);
        assertThat(range.last()).isEqualTo(3);
        assertThat(range.total()).isEqualTo(42);
    }

    @Test
    public void unknownTotal() throws Exception {
        ContentRange range = ContentRange.parse("items 10-13/*");
        assertThat(range.offset()).isEqualTo(10);
        assertThat(range.last()).isEqualTo(13);
        assertThat(range.total()).isEqualTo(-1);
    }

    @Test
    public void empty() throws Exception {
        ContentRange range = ContentRange.parse("items */0");
        assertThat(range.offset()).isEqualTo(-1);
        assertThat(range.last()).isEqualTo(-1);
        assertThat(range.total()).isEqualTo(0);
        assertThat(range.isEmpty()).isTrue();
    }

    @Test
    public void equals() throws Exception {
        ContentRange range1 = new ContentRange(1, 2, 3);
        ContentRange range2 = new ContentRange(1, 2, 3);
        ContentRange range3 = new ContentRange(1, 2, 4);

        assertThat(range1).isEqualTo(range2);
        assertThat(range1.hashCode()).isEqualTo(range2.hashCode());
        assertThat(range1).isNotEqualTo(range3);
        assertThat(range1.hashCode()).isNotEqualTo(range3.hashCode());
    }
}
