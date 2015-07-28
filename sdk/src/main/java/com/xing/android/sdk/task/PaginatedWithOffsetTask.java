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

package com.xing.android.sdk.task;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;

public abstract class PaginatedWithOffsetTask<T extends Collection> extends Task<T> {
    protected int mOffset;
    protected int mLimit;

    public PaginatedWithOffsetTask(@Nullable Integer limit,
                                   @Nullable Integer offset,
                                   @NonNull Object tag,
                                   @NonNull OnTaskFinishedListener<T> listener) {
        this(offset, limit, tag, listener, null);
    }

    public PaginatedWithOffsetTask(@Nullable Integer limit,
                                   @Nullable Integer offset,
                                   @NonNull Object tag,
                                   @NonNull OnTaskFinishedListener<T> listener,
                                   @Nullable PrioritizedRunnable.Priority priority) {
        super(tag, listener, priority);
        mLimit = limit == null || limit < 0 ? 10 : limit;
        mOffset = offset == null || offset < 0 ? 0 : offset;
    }

    public void reset() {
        mOffset = 0;
    }

    public void increaseOffset(int numElements) {
        mOffset += numElements;
    }
}
