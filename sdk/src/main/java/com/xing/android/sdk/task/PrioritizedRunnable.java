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

/*  It is easier using an inheritance instead of an interface, however this allows to use other implementations of
Runnable.*/
public abstract class PrioritizedRunnable extends TaggedRunnable {
    private Priority mPriority;

    public PrioritizedRunnable(Object tag, OnTaskFinishedListener listener) {
        super(tag, listener);
        mPriority = Priority.IMMEDIATE;
    }

    public PrioritizedRunnable(Object tag, OnTaskFinishedListener listener, Priority priority) {
        super(tag, listener);
        mPriority = priority;
    }

    public Priority getPriority() {
        return mPriority;
    }

    public enum Priority {
        IMMEDIATE,
        HIGH,
        MEDIUM,
        LOW
    }
}
