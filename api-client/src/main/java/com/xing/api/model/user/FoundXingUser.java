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

import java.io.Serializable;

/**
 * The User object returned by the Find By Email Call.
 *
 * TODO does anyone need the hash and email in this response?
 *
 * @author daniel.hartwich
 * @see <a href="https://dev.xing.com/docs/get/users/find_by_emails">Find users by email address</a>
 */
@SuppressWarnings("unused") // Public api
public class FoundXingUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private String hash;
    private String email;
    private XingUser user;

    @Override
    public String toString() {
        return "FoundXingUser{"
              + "hash='" + hash + '\''
              + ", email='" + email + '\''
              + ", user=" + user
              + '}';
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public XingUser getUser() {
        return user;
    }

    public void setUser(XingUser user) {
        this.user = user;
    }
}
