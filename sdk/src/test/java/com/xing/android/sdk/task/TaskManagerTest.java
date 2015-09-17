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

import android.os.Build;
import android.support.annotation.Nullable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author serj.lotutovici
 */
@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = Config.NONE
)
public class TaskManagerTest {

    @Test
    public void allSettersWorkAppropriately() throws Exception {
        RunnableExecutor mockExecutor = mock(RunnableExecutor.class);
        TaskManager manager = new TaskManager(mockExecutor);

        assertEquals(mockExecutor, manager.getExecutor());
        assertTrue(manager.futures.isEmpty());
        assertTrue(manager.runnables.isEmpty());

        Object tag = new Object();

        manager.addTaggedFuture(tag, mock(Future.class));
        assertEquals(1, manager.futures.size());
        assertEquals(1, manager.futures.get(tag).size());

        manager.addTaggedFuture(tag, mock(Future.class));
        assertEquals(1, manager.futures.size());
        assertEquals(2, manager.futures.get(tag).size());


        TaggedRunnable runnable1 = new MockTaggedRunnable(tag);
        manager.addTaggedRunnable(runnable1);
        assertEquals(1, manager.runnables.size());
        assertEquals(1, manager.runnables.get(tag).size());

        TaggedRunnable runnable2 = new MockTaggedRunnable(tag);
        manager.addTaggedRunnable(runnable2);
        assertEquals(1, manager.runnables.size());
        assertEquals(2, manager.runnables.get(tag).size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void stopListeningRemovesListenerReference() throws Exception {
        RunnableExecutor mockExecutor = mock(RunnableExecutor.class);
        TaskManager manager = new TaskManager(mockExecutor);

        Object tag = new Object();

        TaggedRunnable runnable1 = spy(new MockTaggedRunnable(tag, mock(OnTaskFinishedListener.class)));
        TaggedRunnable runnable2 = spy(new MockTaggedRunnable(tag, mock(OnTaskFinishedListener.class)));
        manager.addTaggedRunnable(runnable1);
        manager.addTaggedRunnable(runnable2);
        manager.stopListening(tag);
        assertNull(manager.runnables.get(tag));
        assertNull(runnable1.listener);
        assertNull(runnable2.listener);
        verify(runnable1, times(1)).stopListening();
        verify(runnable2, times(1)).stopListening();
    }

    private static class MockTaggedRunnable extends TaggedRunnable<Object> {
        public MockTaggedRunnable(Object tag) {
            this(tag, null);
        }

        public MockTaggedRunnable(Object tag, @Nullable OnTaskFinishedListener<Object> listener) {
            super(tag, listener);
        }

        @Override
        public void run() {

        }
    }
}
