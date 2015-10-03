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
package com.xing.android.sdk.task.contact_petition;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xing.android.sdk.json.user.ContactPetitionsMapper;
import com.xing.android.sdk.network.request.ContactPetitionRequests;
import com.xing.android.sdk.task.OnTaskFinishedListener;
import com.xing.android.sdk.task.PaginatedWithOffsetTask;
import com.xing.android.sdk.task.PrioritizedRunnable.Priority;

import java.util.List;

/**
 * @author ciprian.ursu
 * @author david.gonzalez
 * @see ContactPetitionRequests#sentContactPetitions(String, Integer, Integer, String)
 */
@SuppressWarnings("unused")
public class SentContactPetitionsTasks extends PaginatedWithOffsetTask<List<String>> {
    private final String mUserId;
    private final String mRecipientId;

    /**
     * @param userId ID of the user who sent the contact petitions.
     * @param limit Restrict the number of contact requests to be returned. This must be a positive number. Default:
     * 10.
     * @param offset Offset. This must be a positive number. Default: 0.
     * @param recipientId Filter the contact petitions for a given user ID.
     * @param tag Object that allows the task manager to cancel or stop listening ths task.
     * @param listener Observer that will be notified with the de-serialized result in case of success, or with an
     * exception in case of failure.
     */
    public SentContactPetitionsTasks(@NonNull String userId, @Nullable Integer limit, @Nullable Integer offset,
            @Nullable String recipientId, @NonNull Object tag, @NonNull OnTaskFinishedListener<List<String>> listener) {
        this(userId, limit, offset, recipientId, tag, listener, null);
    }

    /**
     * @param userId ID of the user who sent the contact petitions.
     * @param limit Restrict the number of contact requests to be returned. This must be a positive number. Default:
     * 10.
     * @param offset Offset. This must be a positive number. Default: 0.
     * @param recipientId Filter the contact petitions for a given user ID.
     * @param tag Object that allows the task manager to cancel or stop listening ths task.
     * @param listener Observer that will be notified with the de-serialized result in case of success, or with an
     * exception in case of failure.
     * @param priority Determines the position of the task on the queue of execution. It is a value of {@link Priority
     * Priority}
     */
    public SentContactPetitionsTasks(@NonNull String userId, @Nullable Integer limit, @Nullable Integer offset,
            @Nullable String recipientId, @NonNull Object tag, @NonNull OnTaskFinishedListener<List<String>> listener,
            @Nullable Priority priority) {
        super(limit, offset, tag, listener, priority);
        mUserId = userId;
        mRecipientId = recipientId;
    }

    /**
     * Executes the {@link ContactPetitionRequests#sentContactPetitions(String, Integer, Integer, String)}
     * ContactPathRequests.sentContactPetitions} request and deserialize the result.
     *
     * @return List of ids of the sent contact petition.
     *
     * @throws Exception Can be {@link com.xing.android.sdk.network.oauth.OauthSigner.XingOauthException
     * XingOauthException},
     * {@link com.xing.android.sdk.network.request.exception.NetworkException NetworkException}
     * or {@link com.xing.android.sdk.json.XingJsonException XingJsonException}.
     */
    @Override
    public List<String> run() throws Exception {
        String response = ContactPetitionRequests.sentContactPetitions(mUserId, mLimit, mOffset, mRecipientId);

        return ContactPetitionsMapper.parseSentContactRequestList(response);
    }
}
