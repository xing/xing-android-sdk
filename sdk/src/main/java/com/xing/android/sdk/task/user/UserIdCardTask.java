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
package com.xing.android.sdk.task.user;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xing.android.sdk.json.IdCardMapper;
import com.xing.android.sdk.model.IdCard;
import com.xing.android.sdk.network.request.UserProfilesRequests;
import com.xing.android.sdk.task.OnTaskFinishedListener;
import com.xing.android.sdk.task.PrioritizedRunnable.Priority;
import com.xing.android.sdk.task.Task;

/**
 * @author daniel.hartwich
 * @author david.gonzalez
 * @see UserProfilesRequests#userIdCard(String)
 */
@SuppressWarnings("unused")
public class UserIdCardTask extends Task<IdCard> {
    private final String mUserId;

    /**
     * @param userId The userId for which the IdCard should be returned
     * @param tag Object that allows the task manager to cancel or stop listening ths task.
     * @param listener Observer that will be notified with the de-serialized result in case of success, or with an
     * exception in case of failure.
     */
    public UserIdCardTask(@NonNull String userId, @NonNull Object tag,
            @NonNull OnTaskFinishedListener<IdCard> listener) {
        this(userId, tag, listener, null);
    }

    /**
     * @param userId The userId for which the IdCard should be returned
     * @param tag Object that allows the task manager to cancel or stop listening ths task.
     * @param listener Observer that will be notified with the de-serialized result in case of success, or with an
     * exception in case of failure.
     * @param priority Determines the position of the task on the queue of execution. It is a value of {@link Priority
     * Priority}
     */
    public UserIdCardTask(@NonNull String userId, @NonNull Object tag, @NonNull OnTaskFinishedListener<IdCard> listener,
            @Nullable Priority priority) {
        super(tag, listener, priority);
        mUserId = userId;
    }

    /**
     * Executes the {@link UserProfilesRequests#userIdCard(String)}  request and deserialize the result.
     *
     * @return IdCard of the userId received on the constructor.
     *
     * @throws Exception Can be {@link com.xing.android.sdk.network.oauth.OauthSigner.XingOauthException
     * XingOauthException},
     * {@link com.xing.android.sdk.network.request.exception.NetworkException NetworkException}
     * or {@link com.xing.android.sdk.json.XingJsonException XingJsonException}.
     */
    @Override
    public IdCard run() throws Exception {
        String response = UserProfilesRequests.userIdCard(mUserId);
        return IdCardMapper.parseIdCard(response);
    }
}
