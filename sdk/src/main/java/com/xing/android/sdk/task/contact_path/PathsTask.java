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
package com.xing.android.sdk.task.contact_path;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xing.android.sdk.json.user.ContactPathMapper;
import com.xing.android.sdk.model.user.XingUser;
import com.xing.android.sdk.model.user.field.XingUserField;
import com.xing.android.sdk.network.request.ContactPathsRequests;
import com.xing.android.sdk.task.OnTaskFinishedListener;
import com.xing.android.sdk.task.PrioritizedRunnable.Priority;
import com.xing.android.sdk.task.Task;

import java.util.List;

/**
 * Task for the contact path request.
 *
 * @author david.gonzalez
 * @see ContactPathsRequests#paths(String, String, Boolean, List)
 */
@SuppressWarnings("unused")
public class PathsTask extends Task<List<List<XingUser>>> {
    private final String mUserId;
    private final String mOtherUserId;
    private final Boolean mAllPaths;
    private final List<XingUserField> mUserFields;

    /**
     * @param userId Id of the user whose contact path(s) are to be returned.
     * @param otherUserId Id of any other XING user.
     * @param allPaths Specifies whether this call returns just one contact path (default) or all contact paths.
     * Possible values are true or false. Default: false.
     * @param userFields List of attributes to be returned for any user of the path.
     * @param tag Object that allows the task manager to cancel or stop listening ths task.
     * @param listener Observer that will be notified with the de-serialized result in case of success, or with an
     * exception in case of failure.
     */
    public PathsTask(@NonNull String userId, @NonNull String otherUserId, @Nullable Boolean allPaths,
            @Nullable List<XingUserField> userFields, @NonNull Object tag,
            @NonNull OnTaskFinishedListener<List<List<XingUser>>> listener) {
        this(userId, otherUserId, allPaths, userFields, tag, listener, null);
    }

    /**
     * @param userId Id of the user whose contact path(s) are to be returned.
     * @param otherUserId Id of any other XING user.
     * @param allPaths Specifies whether this call returns just one contact path (default) or all contact paths.
     * Possible values are true or false. Default: false.
     * @param userFields List of attributes to be returned for any user of the path.
     * @param tag Object that allows the task manager to cancel or stop listening ths task.
     * @param listener Observer that will be notified with the de-serialized result in case of success, or with an
     * exception in case of failure.
     * @param priority Determines the position of the task on the queue of execution. It is a value of {@link Priority
     * Priority}
     */
    public PathsTask(@NonNull String userId, @NonNull String otherUserId, @Nullable Boolean allPaths,
            @Nullable List<XingUserField> userFields, @NonNull Object tag,
            @NonNull OnTaskFinishedListener<List<List<XingUser>>> listener, @Nullable Priority priority) {
        super(tag, listener, priority);
        mUserId = userId;
        mOtherUserId = otherUserId;
        mAllPaths = allPaths;
        mUserFields = userFields;
    }

    /**
     * Executes the {@link ContactPathsRequests#paths(String, String, Boolean, List) ContactPathRequests.paths} request
     * and deserialize the result.
     *
     * @return Paths from user to user. Every element on the list is a path. Every path is a list of {@link XingUser
     * users}.
     *
     * @throws Exception Can be {@link com.xing.android.sdk.network.oauth.OauthSigner.XingOauthException
     * XingOauthException},
     * {@link com.xing.android.sdk.network.request.exception.NetworkException NetworkException}
     * or {@link com.xing.android.sdk.json.XingJsonException XingJsonException}.
     */
    @Override
    public List<List<XingUser>> run() throws Exception {
        String response = ContactPathsRequests.paths(mUserId, mOtherUserId, mAllPaths, mUserFields);

        return ContactPathMapper.deserializePaths(response);
    }
}
