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
package com.xing.api.data;

import com.squareup.moshi.Json;
import com.xing.api.data.profile.XingUser;

import java.util.List;

/**
 * @author daniel.hartwich
 */
public class InvitationStats {
    @Json(name = "total_addresses")
    private int totalAddresses;
    @Json(name = "invitations_sent")
    private int invitationsSent;
    @Json(name = "already_invited")
    private List<String> alreadyInvited;
    @Json(name = "already_member")
    private List<XingUser> alreadyMember;
    @Json(name = "invalid_addresses")
    private List<String> invalidAddresses;

    public int totalAddresses() {
        return totalAddresses;
    }

    public InvitationStats totalAddresses(int totalAddresses) {
        this.totalAddresses = totalAddresses;
        return this;
    }

    public int invitationsSent() {
        return invitationsSent;
    }

    public InvitationStats invitationsSent(int invitationsSent) {
        this.invitationsSent = invitationsSent;
        return this;
    }

    public List<String> alreadyInvited() {
        return alreadyInvited;
    }

    public InvitationStats alreadyInvited(List<String> alreadyInvited) {
        this.alreadyInvited = alreadyInvited;
        return this;
    }

    public List<XingUser> alreadyMember() {
        return alreadyMember;
    }

    public InvitationStats alreadyMember(List<XingUser> alreadyMember) {
        this.alreadyMember = alreadyMember;
        return this;
    }

    public List<String> invalidAddresses() {
        return invalidAddresses;
    }

    public InvitationStats invalidAddresses(List<String> invalidAddresses) {
        this.invalidAddresses = invalidAddresses;
        return this;
    }
}
