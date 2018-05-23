/*
 * Copyright (C) 2018 XING SE (http://xing.com/)
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
package com.xing.api.data.contact

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.xing.api.data.profile.XingUser
import com.xing.api.internal.json.ContactPath
import java.io.Serializable

/**
 * Represents a data model for contact paths between two [XING users][XingUser].
 *
 * @see ['Contact Paths' resource page.](https://dev.xing.com/docs/get/users/:user_id/network/:other_user_id/paths)
 */
@JsonClass(generateAdapter = true)
data class ContactPaths(
        /** Returns a list of available paths, which are represented as a list of [users][XingUser].  */
        @Json(name = "paths")
        @ContactPath
        val paths: List<List<XingUser>>,

        /** Returns the smallest distance between the users.  */
        @Json(name = "distance")
        val distance: Int,

        /** Returns the total number of available paths.  */
        @Json(name = "total")
        val total: Int
) : Serializable {

    companion object {
        private const val serialVersionUID = 2L
    }
}