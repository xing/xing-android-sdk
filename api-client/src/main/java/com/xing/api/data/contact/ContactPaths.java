/*
 * Copyright (С) 2016 XING AG (http://xing.com/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xing.api.data.contact;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.xing.api.data.profile.XingUser;
import com.xing.api.internal.json.ContactPath;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a data model for contact paths between two XING {@linkplain XingUser users}.
 *
 * @author daniel.hartwich
 * @see <a href="https://dev.xing.com/docs/get/users/:user_id/network/:other_user_id/paths">'Contact Paths' resource
 * page.</a>
 */
@AutoValue
public abstract class ContactPaths implements Serializable {
    private static final long serialVersionUID = 2L;

    public static JsonAdapter<ContactPaths> jsonAdapter(Moshi moshi) {
        return AutoValue_ContactPaths.jsonAdapter(moshi);
    }

    /** Returns a list of available paths, which are represented as a list of {@linkplain XingUser users}. */
    @Json(name = "paths")
    @ContactPath
    public abstract List<List<XingUser>> paths();

    /** Returns the smallest distance between the users. */
    @Json(name = "distance")
    public abstract int distance();

    /** Returns the total number of available paths. */
    @Json(name = "total")
    public abstract int total();
}
