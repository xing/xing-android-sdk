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


import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author david.gonzalez
 */
public class TaskManager {

    private final RunnableExecutor mExecutor;
    private final Map<Object, List<Future>> mFutures = new HashMap<>();
    private final Map<Object, List<TaggedRunnable>> mRunnables = new HashMap<>();

    public TaskManager(@NonNull RunnableExecutor executor) {
        mExecutor = executor;
    }

    public RunnableExecutor getExecutor() {
        return mExecutor;
    }

    public void addTaggedFuture(@NonNull Object key, @NonNull Future future) {
        if (!mFutures.containsKey(key)) {
            mFutures.put(key, new ArrayList<Future>(0));
        }

        mFutures.get(key).add(future);
    }

    public void addTaggedRunnable(@NonNull TaggedRunnable runnable) {
        Object key = runnable.tag;

        if (!mRunnables.containsKey(key)) {
            mRunnables.put(key, new ArrayList<TaggedRunnable>(0));
        }

        mRunnables.get(key).add(runnable);
    }

    /**
     * Remove the listeners from all the TaggedRunnables. When a key is not listened anymore, it can not be cancelled.
     *
     * @param key Key of the tasks which the listener has to be removed.
     */
    public void stopListening(@NonNull Object key) {
        List<TaggedRunnable> runnables = mRunnables.get(key);
        if (runnables != null) {
            for (int runnablesIterator = 0, numRunnables = runnables.size(); runnablesIterator < numRunnables; runnablesIterator++) {
                runnables.get(runnablesIterator).stopListening();
            }
            mFutures.remove(key);
            mRunnables.remove(key);
        }
    }

    /**
     * Cancel the execution of all the tasks for the specific key.
     *
     * @param key Key of the tasks to be canceled.
     */
    public void cancelExecution(@NonNull Object key) {
        List<Future> futures = mFutures.get(key);
        if (futures != null) {
            cancelTasks(mFutures.get(key));
        }
        mFutures.remove(key);
        mRunnables.remove(key);
    }

    public <T> Future executeAsync(final Task<T> task) {
        final Handler handler = new Handler(Looper.getMainLooper());

        return mExecutor.execute(new TaggedRunnable<T>(task.getTag(), task.getListener()) {
            @Override
            public void run() {
                try {
                    final T result = task.run();
                    if (task instanceof PaginatedWithOffsetTask) {
                        ((PaginatedWithOffsetTask) task).increaseOffset(((Collection) result).size());
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener != null) {
                                mListener.onSuccess(result);
                            }
                        }
                    });
                } catch (final Exception exception) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener != null) {
                                mListener.onError(exception);
                            }
                        }
                    });
                }

            }
        }, this);
    }

    /**
     * Cancels tasks even if they are been executed.
     *
     * @param tasks List of tasks to be cancelled.
     */
    private static void cancelTasks(@NonNull List<Future> tasks) {
        Future task;
        for (int iterator = 0, numTasks = tasks.size(); iterator < numTasks; iterator++) {
            task = tasks.get(iterator);
            if (task != null) {
                task.cancel(true);
            }
        }
    }
}
