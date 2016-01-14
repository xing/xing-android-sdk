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
package com.xing.api.data.contact;

import com.squareup.moshi.Json;
import com.xing.api.data.profile.XingUser;

import java.io.Serializable;
import java.util.List;

/**
 * Represents the invitation stats response for the invite by mail call.
 *
 * @author daniel.hartwich
 * @see <a href="https://dev.xing.com/docs/post/users/invite">'Send Invitations' resource page.</a>
 */
public class InvitationStats implements Serializable {
    private static final long serialVersionUID = 1L;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvitationStats other = (InvitationStats) o;
        return totalAddresses == other.totalAddresses
              && invitationsSent == other.invitationsSent
              && (alreadyInvited != null ? alreadyInvited.equals(other.alreadyInvited) : other.alreadyInvited == null)
              && (alreadyMember != null ? alreadyMember.equals(other.alreadyMember) : other.alreadyMember == null)
              && (invalidAddresses != null ? invalidAddresses.equals(other.invalidAddresses)
              : other.invalidAddresses == null);
    }

    @Override
    public int hashCode() {
        int result = totalAddresses;
        result = 31 * result + invitationsSent;
        result = 31 * result + (alreadyInvited != null ? alreadyInvited.hashCode() : 0);
        result = 31 * result + (alreadyMember != null ? alreadyMember.hashCode() : 0);
        result = 31 * result + (invalidAddresses != null ? invalidAddresses.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "InvitationStats{"
              + "totalAddresses=" + totalAddresses
              + ", invitationsSent=" + invitationsSent
              + ", alreadyInvited=" + alreadyInvited
              + ", alreadyMember=" + alreadyMember
              + ", invalidAddresses=" + invalidAddresses
              + '}';
    }

    /** Returns the total number of addresses received by the api. */
    public int totalAddresses() {
        return totalAddresses;
    }

    /** Sets the total number of addresses received by the api. */
    public InvitationStats totalAddresses(int totalAddresses) {
        this.totalAddresses = totalAddresses;
        return this;
    }

    /** Returns the number of invitation sent. */
    public int invitationsSent() {
        return invitationsSent;
    }

    /** Sets the number of invitation sent. */
    public InvitationStats invitationsSent(int invitationsSent) {
        this.invitationsSent = invitationsSent;
        return this;
    }

    /** Returns the list of emails of users that are already invited. */
    public List<String> alreadyInvited() {
        return alreadyInvited;
    }

    /** Sets the list of emails of users that are already invited. */
    public InvitationStats alreadyInvited(List<String> alreadyInvited) {
        this.alreadyInvited = alreadyInvited;
        return this;
    }

    /** Returns the list of users that are already XING members. */
    public List<XingUser> alreadyMember() {
        return alreadyMember;
    }

    /** Sets the list of users that are already members. */
    public InvitationStats alreadyMember(List<XingUser> alreadyMember) {
        this.alreadyMember = alreadyMember;
        return this;
    }

    /** Returns the a list of invalid emails. */
    public List<String> invalidAddresses() {
        return invalidAddresses;
    }

    /** Sets the list of invalid emails. */
    public InvitationStats invalidAddresses(List<String> invalidAddresses) {
        this.invalidAddresses = invalidAddresses;
        return this;
    }
}
