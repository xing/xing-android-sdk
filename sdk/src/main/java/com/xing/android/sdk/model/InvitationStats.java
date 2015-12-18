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
package com.xing.android.sdk.model;

import com.squareup.moshi.Json;
import com.xing.android.sdk.model.user.XingUser;

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

    public int getTotalAddresses() {
        return totalAddresses;
    }

    public void setTotalAddresses(int totalAddresses) {
        this.totalAddresses = totalAddresses;
    }

    public int getInvitationsSent() {
        return invitationsSent;
    }

    public void setInvitationsSent(int invitationsSent) {
        this.invitationsSent = invitationsSent;
    }

    public List<String> getAlreadyInvited() {
        return alreadyInvited;
    }

    public void setAlreadyInvited(List<String> alreadyInvited) {
        this.alreadyInvited = alreadyInvited;
    }

    public List<XingUser> getAlreadyMember() {
        return alreadyMember;
    }

    public void setAlreadyMember(List<XingUser> alreadyMember) {
        this.alreadyMember = alreadyMember;
    }

    public List<String> getInvalidAddresses() {
        return invalidAddresses;
    }

    public void setInvalidAddresses(List<String> invalidAddresses) {
        this.invalidAddresses = invalidAddresses;
    }
}
