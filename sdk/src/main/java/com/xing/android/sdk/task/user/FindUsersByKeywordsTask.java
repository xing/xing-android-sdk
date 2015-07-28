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
package com.xing.android.sdk.task.user;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.JsonReader;

import com.xing.android.sdk.json.user.UserProfilesMapper;
import com.xing.android.sdk.model.user.XingUser;
import com.xing.android.sdk.model.user.field.XingUserField;
import com.xing.android.sdk.network.request.UserProfilesRequests;
import com.xing.android.sdk.task.OnTaskFinishedListener;
import com.xing.android.sdk.task.PaginatedWithOffsetTask;
import com.xing.android.sdk.task.PrioritizedRunnable;

import java.io.StringReader;
import java.util.List;

/**
 * Wrapper class for asynchronous searching users by keywords.
 * Executes {@link UserProfilesRequests#find(List, Integer, Integer, List)}
 *
 * @author serj.lotutovici
 * @see <a href="https://dev.xing.com/docs/get/users/find_by_emails">https://dev.xing.com/docs/get/users/find_by_emails</a>
 */
@SuppressWarnings("unused") // Public api
public class FindUsersByKeywordsTask extends PaginatedWithOffsetTask<List<XingUser>> {

    private final List<String> mKeywords;
    private final List<XingUserField> mFields;

    public FindUsersByKeywordsTask(@Nullable List<String> keywords,
                                   @Nullable List<XingUserField> fields,
                                   @Nullable Integer limit,
                                   @Nullable Integer offset,
                                   @NonNull Object tag,
                                   @NonNull OnTaskFinishedListener<List<XingUser>> listener) {
        this(keywords, fields, limit, offset, tag, listener, null);
    }

    public FindUsersByKeywordsTask(@Nullable List<String> keywords,
                                   @Nullable List<XingUserField> fields,
                                   @Nullable Integer limit,
                                   @Nullable Integer offset,
                                   @NonNull Object tag,
                                   @NonNull OnTaskFinishedListener<List<XingUser>> listener,
                                   @Nullable PrioritizedRunnable.Priority priority) {
        super(limit, offset, tag, listener, priority);
        mKeywords = keywords;
        mFields = fields;
    }

    @SuppressWarnings({"TryFinallyCanBeTryWithResources", "resource"})
    @Override
    public List<XingUser> run() throws Exception {
        String response = UserProfilesRequests.find(mKeywords, mLimit, mOffset, mFields);
        JsonReader jsonReader = new JsonReader(new StringReader(response));
        try {
            return UserProfilesMapper.parseUsersFromFindRequest(jsonReader);
        } finally {
            jsonReader.close();
        }
    }
}
