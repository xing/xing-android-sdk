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
package com.xing.android.sdk.task.contact;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xing.android.sdk.json.user.ContactsMapper;
import com.xing.android.sdk.model.user.XingUser;
import com.xing.android.sdk.model.user.field.XingUserField;
import com.xing.android.sdk.network.request.ContactsRequests;
import com.xing.android.sdk.task.OnTaskFinishedListener;
import com.xing.android.sdk.task.PaginatedWithOffsetTask;
import com.xing.android.sdk.task.PrioritizedRunnable.Priority;

import java.util.List;

/**
 * @author declan.mccormack
 * @author david.gonzalez
 * @see ContactsRequests#sharedContacts(String, Integer, Integer, String, List)
 */
@SuppressWarnings("unused")
public class SharedContactsTask extends PaginatedWithOffsetTask<List<XingUser>> {
    private final String mUserId;
    private final String mOrderBy;
    private final List<XingUserField> mFields;

    /**
     * @param userId ID of the user who assigned the tags
     * @param limit Restrict the number of attachments to be returned. This must be a positive number. Default: 10
     * @param offset Offset. This must be a positive number. Default: 0
     * @param orderBy Field that determines the ascending order of the returned list
     * @param fields List of user attributes to return
     * @param tag Object that allows the task manager to cancel or stop listening ths task.
     * @param listener Future object to allow cancel the task if necessary.
     */
    public SharedContactsTask(@NonNull String userId, @Nullable Integer limit, @Nullable Integer offset,
            @Nullable String orderBy, @Nullable List<XingUserField> fields, @NonNull Object tag,
            @NonNull OnTaskFinishedListener<List<XingUser>> listener) {
        this(userId, limit, offset, orderBy, fields, tag, listener, null);
    }

    /**
     * @param userId ID of the user who assigned the tags
     * @param limit Restrict the number of attachments to be returned. This must be a positive number. Default: 10
     * @param offset Offset. This must be a positive number. Default: 0
     * @param orderBy Field that determines the ascending order of the returned list
     * @param fields List of user attributes to return
     * @param tag Object that allows the task manager to cancel or stop listening ths task.
     * @param listener Future object to allow cancel the task if necessary.
     * @param priority Determines the position of the task on the queue of execution. It is a value of {@link Priority
     * Priority}
     */
    public SharedContactsTask(@NonNull String userId, @Nullable Integer limit, @Nullable Integer offset,
            @Nullable String orderBy, @Nullable List<XingUserField> fields, @NonNull Object tag,
            @NonNull OnTaskFinishedListener<List<XingUser>> listener, @Nullable Priority priority) {
        super(limit, offset, tag, listener, priority);
        mUserId = userId;
        mOrderBy = orderBy;
        mFields = fields;
    }

    /**
     * Executes the {@link ContactsRequests#sharedContacts(String, Integer, Integer, String, List)} request and
     * deserialize the result.
     *
     * @return List of shared contacts between the logged user and the user received on the constructor.
     *
     * @throws Exception Can be {@link com.xing.android.sdk.network.oauth.OauthSigner.XingOauthException
     * XingOauthException},
     * {@link com.xing.android.sdk.network.request.exception.NetworkException NetworkException}
     * or {@link com.xing.android.sdk.json.XingJsonException XingJsonException}.
     */
    @Override
    public List<XingUser> run() throws Exception {
        String response = ContactsRequests.sharedContacts(mUserId, mLimit, mOffset, mOrderBy, mFields);
        return ContactsMapper.parseSharedContacts(response);
    }
}
