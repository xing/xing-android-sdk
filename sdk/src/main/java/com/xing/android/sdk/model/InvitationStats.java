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
