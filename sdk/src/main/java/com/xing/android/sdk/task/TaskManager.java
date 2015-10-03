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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author david.gonzalez
 */
public class TaskManager {
    private static final int CACHE_INITIAL_CAPACITY = 10;

    final RunnableExecutor executor;
    final Map<Object, List<Future>> futures;
    final Map<Object, List<TaggedRunnable>> runnables;

    public TaskManager(@NonNull RunnableExecutor executor) {
        this.executor = executor;
        futures = new LinkedHashMap<>(CACHE_INITIAL_CAPACITY);
        runnables = new LinkedHashMap<>(CACHE_INITIAL_CAPACITY);
    }

    /**
     * Cancels tasks even if they are been executed.
     *
     * @param tasks List of tasks to be cancelled.
     */
    static void cancelTasks(@NonNull List<Future> tasks) {
        Future task;
        for (int iterator = 0, numTasks = tasks.size(); iterator < numTasks; iterator++) {
            task = tasks.get(iterator);
            if (task != null) {
                task.cancel(true);
            }
        }
    }

    public RunnableExecutor getExecutor() {
        return executor;
    }

    public void addTaggedFuture(@NonNull Object key, @NonNull Future future) {
        if (!futures.containsKey(key)) {
            futures.put(key, new ArrayList<Future>(0));
        }

        futures.get(key).add(future);
    }

    public void addTaggedRunnable(@NonNull TaggedRunnable runnable) {
        Object key = runnable.tag;

        if (!runnables.containsKey(key)) {
            runnables.put(key, new ArrayList<TaggedRunnable>(0));
        }

        runnables.get(key).add(runnable);
    }

    /**
     * Remove the listeners from all the TaggedRunnables. When a key is not listened anymore, it can not be cancelled.
     *
     * @param key Key of the tasks which the listener has to be removed.
     */
    public void stopListening(@NonNull Object key) {
        List<TaggedRunnable> subscriptions = runnables.get(key);
        if (subscriptions != null) {
            for (int counter = 0, size = subscriptions.size(); counter < size; counter++) {
                subscriptions.get(counter).stopListening();
            }
            futures.remove(key);
            runnables.remove(key);
        }
    }

    /**
     * Cancel the execution of all the tasks for the specific key.
     *
     * @param key Key of the tasks to be canceled.
     */
    public void cancelExecution(@NonNull Object key) {
        List<Future> futures = this.futures.get(key);
        if (futures != null) {
            cancelTasks(this.futures.get(key));
        }
        this.futures.remove(key);
        runnables.remove(key);
    }

    public <T> Future executeAsync(final Task<T> task) {
        final Handler handler = new Handler(Looper.getMainLooper());

        return executor.execute(new TaggedRunnable<T>(task.getTag(), task.getListener()) {
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
                            if (listener != null) {
                                listener.onSuccess(result);
                            }
                        }
                    });
                } catch (final Exception exception) {
                    // We should not notify the listener if the thread was interrupted
                    if (exception instanceof InterruptedException) {
                        return;
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) {
                                listener.onError(exception);
                            }
                        }
                    });
                }
            }
        }, this);
    }
}
