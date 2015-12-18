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
package com.xing.android.sdk.model.user;

/**
 * The User object returned by the Find By Email Call.
 *
 * @author daniel.hartwich
 * @see <a href="https://dev.xing.com/docs/get/users/find_by_emails">Find users by email address</a>
 */
@SuppressWarnings("unused") // Public api
public class FoundXingUser {

    private String hash;
    private String email;
    private XingUser user;

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
