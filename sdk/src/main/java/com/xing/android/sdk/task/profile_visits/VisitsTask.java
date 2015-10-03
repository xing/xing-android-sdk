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
package com.xing.android.sdk.task.profile_visits;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xing.android.sdk.json.user.ProfileVisitMapper;
import com.xing.android.sdk.model.XingCalendar;
import com.xing.android.sdk.model.user.ProfileVisit;
import com.xing.android.sdk.network.request.ProfileVisitsRequests;
import com.xing.android.sdk.task.OnTaskFinishedListener;
import com.xing.android.sdk.task.PaginatedWithOffsetTask;
import com.xing.android.sdk.task.PrioritizedRunnable.Priority;

import java.util.List;

/**
 * @author david.gonzalez
 * @see ProfileVisitsRequests#visits(String, Integer, Integer, XingCalendar, Boolean)
 */
@SuppressWarnings("unused")
public class VisitsTask extends PaginatedWithOffsetTask<List<ProfileVisit>> {
    private final String mUserId;
    private final XingCalendar mSince;
    private final Boolean mStripHtml;

    /**
     * @param userId Id of the visited user.
     * @param limit Restrict the number of attachments to be returned. This must be a positive number. Default: 10
     * @param offset Offset. This must be a positive number. Default: 0
     * @param since Only returns visits more recent than the specified time stamp (ISO 8601).
     * @param stripHtml Specifies whether the profile visit reason should be stripped of HTML (true) or not (false).
     * @param tag Object that allows the task manager to cancel or stop listening ths task.
     * @param listener Future object to allow cancel the task if necessary.
     */
    public VisitsTask(@NonNull String userId, @Nullable Integer limit, @Nullable Integer offset,
            @Nullable XingCalendar since, @Nullable Boolean stripHtml, @NonNull Object tag,
            @NonNull OnTaskFinishedListener<List<ProfileVisit>> listener) {
        this(userId, limit, offset, since, stripHtml, tag, listener, null);
    }

    /**
     * @param userId Id of the visited user.
     * @param limit Restrict the number of attachments to be returned. This must be a positive number. Default: 10
     * @param offset Offset. This must be a positive number. Default: 0
     * @param since Only returns visits more recent than the specified time stamp (ISO 8601).
     * @param stripHtml Specifies whether the profile visit reason should be stripped of HTML (true) or not (false).
     * @param tag Object that allows the task manager to cancel or stop listening ths task.
     * @param listener Future object to allow cancel the task if necessary.
     * @param priority Determines the position of the task on the queue of execution. It is a value of {@link Priority
     * Priority}
     */
    public VisitsTask(@NonNull String userId, @Nullable Integer limit, @Nullable Integer offset,
            @Nullable XingCalendar since, @Nullable Boolean stripHtml, @NonNull Object tag,
            @NonNull OnTaskFinishedListener<List<ProfileVisit>> listener, @Nullable Priority priority) {
        super(limit, offset, tag, listener, priority);
        mUserId = userId;
        mSince = since;
        mStripHtml = stripHtml;
    }

    /**
     * Obtains the list of visits to the profile of the user with the id received as parameter
     * on the constructor.
     *
     * @return List of visits to the profile of the user with the id received as parameter.
     *
     * @throws Exception Can be {@link com.xing.android.sdk.network.oauth.OauthSigner.XingOauthException
     * XingOauthException},
     * {@link com.xing.android.sdk.network.request.exception.NetworkException NetworkException}
     * or {@link com.xing.android.sdk.json.XingJsonException XingJsonException}.
     */
    @Override
    public List<ProfileVisit> run() throws Exception {
        String response = ProfileVisitsRequests.visits(mUserId, mLimit, mOffset, mSince, mStripHtml);

        return ProfileVisitMapper.parseDetailsResponse(response);
    }
}
