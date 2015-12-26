/*
 * Copyright (С) 2015 XING AG (http://xing.com/)
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
package com.xing.api.model.user;

import com.squareup.moshi.Json;
import com.xing.api.model.SafeCalendar;

import java.io.Serializable;

/**
 * Represents the profile message of a user.
 *
 * @author serj.lotutovici
 * @see <a href="https://dev.xing.com/docs/get/users/:user_id/profile_message">User Profile Message</a>
 */
public class ProfileMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Date message was updated. */
    @Json(name = "updated_at")
    private SafeCalendar updatedAt;
    /** Message. */
    @Json(name = "message")
    private String message;

    @Override
    public String toString() {
        return "ProfileMessage{"
              + "updatedAt=" + updatedAt
              + ", message='" + message + '\''
              + '}';
    }

    /**
     * Return date message was updated.
     *
     * @return date message updated.
     */
    public SafeCalendar getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Set date message is updated.
     *
     * @param updatedAt date message updated.
     */
    public void setUpdatedAt(SafeCalendar updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Return message.
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set message.
     *
     * @param message message.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
