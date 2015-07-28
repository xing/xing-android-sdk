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

import com.xing.android.sdk.json.FieldUtils;
import com.xing.android.sdk.model.user.field.XingUserField;
import com.xing.android.sdk.network.XingController;
import com.xing.android.sdk.network.oauth.OauthSigner;
import com.xing.android.sdk.network.request.exception.NetworkException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Requests for the contact path section.
 *
 * @author david.gonzalez
 * @see <a href="https://dev.xing.com/docs/resources#contact-path">https://dev.xing.com/docs/resources#contact-path</a>
 */
@SuppressWarnings("unused")
public final class ContactPathsRequests {

    /**
     * Resource for the paths request.
     */
    private static final MessageFormat CONTACT_PATH_RESOURCE = new MessageFormat("/v1/users/{0}/network/{1}/paths");

    /**
     * Key for the all_paths parameter. Used in paths.
     */
    private static final String ALL_PATHS_PARAM = "all_paths";

    /**
     * Private constructor to avoid this class to be instantiated.
     */
    private ContactPathsRequests() {
    }

    /**
     * Creates and executes the paths request.
     *
     * @param userId      Id of the user whose contact path(s) are to be returned.
     * @param otherUserId Id of any other XING user.
     * @param allPaths    Specifies whether this call returns just one contact path (default) or all contact paths. Possible values are true or false. Default: false.
     * @param userFields  List of attributes to be returned for any user of the path.
     * @return Result of the execution fo the request, in Json format.
     * @throws NetworkException               Error produced during the network connection.
     * @throws OauthSigner.XingOauthException Error because of an Oauth problem.
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/network/:other_user_id/paths">https://dev.xing.com/docs/get/users/:user_id/network/:other_user_id/paths</a>
     */
    public static String paths(@NonNull String userId, @NonNull String otherUserId,
                               @Nullable Boolean allPaths,
                               @Nullable List<XingUserField> userFields)
            throws NetworkException, OauthSigner.XingOauthException {

        return XingController.getInstance().execute(buildPathsRequest(userId, otherUserId,
                allPaths, userFields));
    }

    /**
     * Creates the paths request.
     *
     * @param userId      Id of the user whose contact path(s) are to be returned.
     * @param otherUserId Id of any other XING user.
     * @param allPaths    Specifies whether this call returns just one contact path (default) or all contact paths. Possible values are true or false. Default: false.
     * @param userFields  List of attributes to be returned for any user of the path.
     * @return Request object ready to be executed.
     */
    public static Request buildPathsRequest(@NonNull String userId,
                                            @NonNull String otherUserId,
                                            @Nullable Boolean allPaths,
                                            @Nullable List<XingUserField> userFields) {

        return new Request.Builder(Request.Method.GET)
                .setUri(Uri.parse(CONTACT_PATH_RESOURCE.format(new Object[]{userId, otherUserId})))
                .addParams(buildPathsParams(allPaths, userFields))
                .build();
    }

    /**
     * Creates the list of params for the paths request.
     *
     * @param allPaths   Specifies whether this call returns just one contact path (default) or all contact paths. Possible values are true or false. Default: false.
     * @param userFields List of attributes to be returned for any user of the path.
     * @return List with the params for the path request, as name-value pair.
     */
    private static List<Pair<String, String>> buildPathsParams(@Nullable Boolean allPaths,
                                                               @Nullable List<XingUserField> userFields) {
        List<Pair<String, String>> params = new ArrayList<>(2);

        if (allPaths != null) {
            params.add(new Pair<>(ALL_PATHS_PARAM, allPaths.toString()));
        }

        if (userFields != null && !userFields.isEmpty()) {
            params.add(new Pair<>(RequestUtils.USER_FIELDS_PARAM,
                    FieldUtils.formatFieldsToString(userFields)));
        }

        return params;
    }
}
