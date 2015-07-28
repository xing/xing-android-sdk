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
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.xing.android.sdk.task.contact_petition;

import android.support.annotation.NonNull;

import com.xing.android.sdk.network.request.ContactPetitionRequests;
import com.xing.android.sdk.task.OnTaskFinishedListener;
import com.xing.android.sdk.task.PrioritizedRunnable;
import com.xing.android.sdk.task.Task;

/**
 * Wrapper class for asynchronous revoking contact petition.
 * Executes {@link ContactPetitionRequests#revokeOrDenyContactPetition(String, String)}
 *
 * @author serj.lotutovici
 * @see <a href="https://dev.xing.com/docs/delete/users/:user_id/contact_requests/:id">https://dev.xing.com/docs/delete/users/:user_id/contact_requests/:id</a>
 */
@SuppressWarnings("unused") // Public api
public class RevokeOrDenyContactPetitionTask extends Task<Void> {

    private final String mSenderId;
    private final String mRecipientId;

    public RevokeOrDenyContactPetitionTask(@NonNull String senderId,
                                           @NonNull String recipientId,
                                           @NonNull Object tag,
                                           @NonNull OnTaskFinishedListener<Void> listener) {
        this(senderId, recipientId, tag, listener, null);
    }

    public RevokeOrDenyContactPetitionTask(@NonNull String senderId,
                                           @NonNull String recipientId,
                                           @NonNull Object tag,
                                           @NonNull OnTaskFinishedListener<Void> listener,
                                           PrioritizedRunnable.Priority priority) {
        super(tag, listener, priority);
        mSenderId = senderId;
        mRecipientId = recipientId;
    }

    @Override
    public Void run() throws Exception {
        ContactPetitionRequests.revokeOrDenyContactPetition(mSenderId, mRecipientId);
        return null;
    }
}
