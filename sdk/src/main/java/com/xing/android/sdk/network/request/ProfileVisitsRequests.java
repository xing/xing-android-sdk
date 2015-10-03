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
package com.xing.android.sdk.network.request;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.xing.android.sdk.model.CalendarUtils;
import com.xing.android.sdk.model.XingCalendar;
import com.xing.android.sdk.network.XingController;
import com.xing.android.sdk.network.oauth.OauthSigner;
import com.xing.android.sdk.network.request.exception.NetworkException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Requests for the profile visits section.
 *
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/resources#profile-visits">https://dev.xing
 * .com/docs/resources#profile-visits</a>
 */
public final class ProfileVisitsRequests {

    /**
     * Resource for the visits request.
     */
    private static final MessageFormat VISITS_RESOURCE = new MessageFormat("v1/users/{0}/visits");

    /**
     * Private constructor to avoid this class to be instantiated.
     */
    private ProfileVisitsRequests() {
    }

    /**
     * Creates and executes the visits request.
     *
     * @param userId Id of the user whose profile visits are to be returned.
     * @param limit Maximum number of responses.
     * @param offset Offset for the results. With an offset of 10, the results will start on the eleventh.
     * @param since Only returns visits more recent than the specified date.
     * @param stripHtml Specifies whether the profile visit reason should be stripped of HTML (true) or not (false).
     * @return Result of the execution of the request, in Json format.
     *
     * @throws NetworkException Error produced during the network connection.
     * @throws OauthSigner.XingOauthException Error because of an Oauth problem.
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/visits">https://dev.xing
     * .com/docs/get/users/:user_id/visits</a>
     */
    public static String visits(@NonNull String userId, @Nullable Integer limit, @Nullable Integer offset,
            @Nullable XingCalendar since, @Nullable Boolean stripHtml)
            throws NetworkException, OauthSigner.XingOauthException {
        return XingController.getInstance().execute(buildVisitsRequest(userId, limit, offset, since, stripHtml));
    }

    /**
     * Creates the visits request.
     *
     * @param userId Id of the user whose profile visits are to be returned.
     * @param limit Maximum number of responses.
     * @param offset Offset for the results. With an offset of 10, the results will start on the eleventh.
     * @param since Only returns visits more recent than the specified date.
     * @param stripHtml Specifies whether the profile visit reason should be stripped of HTML (true) or not (false).
     * @return Request object ready to be executed.
     */
    public static Request buildVisitsRequest(@NonNull String userId, @Nullable Integer limit, @Nullable Integer offset,
            @Nullable XingCalendar since, @Nullable Boolean stripHtml) {
        return new Request.Builder(Request.Method.GET).setUri(Uri.parse(VISITS_RESOURCE.format(new String[]{userId})))
                .addParams(buildVisitsParams(limit, offset, since, stripHtml))
                .build();
    }

    /**
     * Creates the list of params for the recommendations request.
     *
     * @param limit Maximum number of responses.
     * @param offset Offset for the results. With an offset of 10, the results will start on the eleventh.
     * @param since Only returns visits more recent than the specified date.
     * @param stripHtml Specifies whether the profile visit reason should be stripped of HTML (true) or not (false).
     * @return List with the params for the visits request, as name-value pair.
     */
    public static List<Pair<String, String>> buildVisitsParams(@Nullable Integer limit, @Nullable Integer offset,
            @Nullable XingCalendar since, @Nullable Boolean stripHtml) {
        List<Pair<String, String>> params = new ArrayList<>(4);

        if (limit != null && limit > 0) {
            params.add(new Pair<>(RequestUtils.LIMIT_PARAM, Integer.toString(limit)));
        }
        if (offset != null && offset > 0) {
            params.add(new Pair<>(RequestUtils.OFFSET_PARAM, Integer.toString(offset)));
        }
        if (since != null) {
            params.add(new Pair<>(RequestUtils.SINCE_PARAM, CalendarUtils.calendarToTimestamp(since)));
        }
        if (stripHtml != null) {
            params.add(new Pair<>(RequestUtils.STRIP_HTML_PARAM, stripHtml.toString()));
        }

        return params;
    }

    /**
     * Creates and executes the create visit request.
     *
     * @param userId Id of the visited user.
     * @return Result of the execution of the request, in Json format.
     *
     * @throws NetworkException Error produced during the network connection.
     * @throws OauthSigner.XingOauthException Error because of an Oauth problem.
     * @see <a href="https://dev.xing.com/docs/post/users/:user_id/visits">https://dev.xing
     * .com/docs/post/users/:user_id/visits</a>
     */
    public static String createVisit(@NonNull String userId) throws NetworkException, OauthSigner.XingOauthException {
        return XingController.getInstance().execute(buildCreateVisitRequest(userId));
    }

    /**
     * Creates the create visit request.
     *
     * @param userId Id of the visited user.
     * @return Request object ready to be executed.
     */
    public static Request buildCreateVisitRequest(@NonNull String userId) {
        return new Request.Builder(Request.Method.POST).setUri(Uri.parse(VISITS_RESOURCE.format(new Object[]{userId})))
                .build();
    }
}
