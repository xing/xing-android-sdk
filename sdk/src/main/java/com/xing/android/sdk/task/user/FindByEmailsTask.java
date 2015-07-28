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

import com.xing.android.sdk.json.user.FoundXingUserMapper;
import com.xing.android.sdk.model.user.FoundXingUser;
import com.xing.android.sdk.model.user.field.XingUserField;
import com.xing.android.sdk.network.request.UserProfilesRequests;
import com.xing.android.sdk.task.OnTaskFinishedListener;
import com.xing.android.sdk.task.PrioritizedRunnable.Priority;
import com.xing.android.sdk.task.Task;

import java.util.List;

/**
 * @author daniel.hartwich
 * @author david.gonzalez
 * @see UserProfilesRequests#findByEmail(List, List)
 */
@SuppressWarnings("unused")
public class FindByEmailsTask extends Task<List<FoundXingUser>> {

    private final List<String> mEmails;
    private final List<XingUserField> mFields;

    /**
     * @param emails   emails The list of emails to search with
     * @param fields   The requested fields for the user objects to be returned
     * @param tag      Object that allows the task manager to cancel or stop listening ths task.
     * @param listener Observer that will be notified with the de-serialized result in case of success, or with an exception in case of failure.
     */
    public FindByEmailsTask(@NonNull List<String> emails,
                            @Nullable List<XingUserField> fields,
                            @NonNull Object tag,
                            @NonNull OnTaskFinishedListener<List<FoundXingUser>> listener) {
        this(emails, fields, tag, listener, null);
    }

    /**
     * @param emails   emails The list of emails to search with
     * @param fields   The requested fields for the user objects to be returned
     * @param tag      Object that allows the task manager to cancel or stop listening ths task.
     * @param listener Observer that will be notified with the de-serialized result in case of success, or with an exception in case of failure.
     * @param priority Determines the position of the task on the queue of execution. It is a value of {@link Priority Priority}
     */
    public FindByEmailsTask(@NonNull List<String> emails,
                            @Nullable List<XingUserField> fields,
                            @NonNull Object tag,
                            @NonNull OnTaskFinishedListener<List<FoundXingUser>> listener,
                            @Nullable Priority priority) {
        super(tag, listener, priority);
        mEmails = emails;
        mFields = fields;
    }

    /**
     * Executes the {@link UserProfilesRequests#findByEmail(List, List)}   request and deserialize the result.
     *
     * @return List of xing users founded based on the emails.
     * @throws Exception Can be {@link com.xing.android.sdk.network.oauth.OauthSigner.XingOauthException XingOauthException},
     *                   {@link com.xing.android.sdk.network.request.exception.NetworkException NetworkException}
     *                   or {@link com.xing.android.sdk.json.XingJsonException XingJsonException}.
     */
    @Override
    public List<FoundXingUser> run() throws Exception {
        String response = UserProfilesRequests.findByEmail(mEmails, mFields);
        return FoundXingUserMapper.parseFoundXingUser(response);
    }
}
