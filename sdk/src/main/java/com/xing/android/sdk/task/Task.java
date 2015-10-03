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

/**
 * Abstraction for task execution.
 *
 * @author david.gonzalez
 */
public abstract class Task<T> {
    private final Object mTag;
    @Nullable
    private final OnTaskFinishedListener<T> mListener;
    @Nullable
    private final PrioritizedRunnable.Priority mPriority;

    public Task(@NonNull Object tag, @Nullable OnTaskFinishedListener<T> listener) {
        this(tag, listener, null);
    }

    public Task(@NonNull Object tag, @Nullable OnTaskFinishedListener<T> listener,
            @Nullable PrioritizedRunnable.Priority priority) {
        mTag = tag;
        mListener = listener;
        mPriority = priority;
    }

    /**
     * Run the task. This method is executed synchronously.
     * To execute this in an asynchronous fashion pass the task object to {@link TaskManager#executeAsync(Task)}
     * or {@link com.xing.android.sdk.network.XingController#executeAsync(Task)}.
     *
     * @return The result that this {@link Task} may produce (also can be null)
     *
     * @throws Exception Will not handle eny exception will pass to a higher handler class
     */
    public abstract T run() throws Exception;

    public Object getTag() {
        return mTag;
    }

    @Nullable
    public OnTaskFinishedListener<T> getListener() {
        return mListener;
    }

    @Nullable
    public PrioritizedRunnable.Priority getPriority() {
        return mPriority;
    }
}
