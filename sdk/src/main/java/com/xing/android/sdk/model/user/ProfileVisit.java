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

package com.xing.android.sdk.model.user;

import com.xing.android.sdk.json.EnumMapper;
import com.xing.android.sdk.model.CalendarUtils;
import com.xing.android.sdk.model.XingCalendar;

/**
 * Represents an profile visit.
 * <p/>
 *
 * @author ciprian.ursu
 * @see <a href="https://dev.xing.com/docs/get/users/:user_id/visits">Profile Visit</a>
 */
@SuppressWarnings("unused")
public class ProfileVisit {
    /**
     * Company name.
     */
    private String companyName;
    /**
     * Reason for visit.
     */
    private String reason;
    /**
     * Encrypted date of visit.
     */
    private String visitedAtEncrypted;
    /**
     * User photo.
     */
    private XingPhotoUrls photoUrls;
    /**
     * User ID.
     */
    private String userId;
    /**
     * User name.
     */
    private String displayName;
    /**
     * User job title.
     */
    private String jobTitle;
    /**
     * User contact path length.
     */
    private Integer contactPathLength;
    /**
     * Date of visit.
     */
    private XingCalendar visitedAt;
    /**
     * Number of visits.
     */
    private Integer visitCount;
    /**
     * Gender of user.
     */
    private Gender gender;

    /**
     * Return company name.
     *
     * @return company name.
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Set company name.
     *
     * @param companyName company name.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Return reason for visit.
     *
     * @return reason for visit.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Set reason for visit.
     *
     * @param reason reason for visit.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Get encrypted date of visit.
     *
     * @return encrypted date of visit.
     */
    public String getVisitedAtEncrypted() {
        return visitedAtEncrypted;
    }

    /**
     * Set encrypted date of visit.
     *
     * @param visitedAtEncrypted encrypted date of visit.
     */
    public void setVisitedAtEncrypted(String visitedAtEncrypted) {
        this.visitedAtEncrypted = visitedAtEncrypted;
    }

    /**
     * Return photo URLs of user.
     *
     * @return photo URLs of user.
     */
    public XingPhotoUrls getPhotoUrls() {
        return photoUrls;
    }

    /**
     * Set photo URLs of user.
     *
     * @param photoUrls photo URLs of user.
     */
    public void setPhotoUrls(XingPhotoUrls photoUrls) {
        this.photoUrls = photoUrls;
    }

    /**
     * Return user ID.
     *
     * @return user ID.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Set user ID.
     *
     * @param userId user ID.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Return user name.
     *
     * @return user name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Set user name.
     *
     * @param displayName user name.
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Return contact path length.
     *
     * @return contact path length.
     */
    public Integer getContactPathLength() {
        return contactPathLength;
    }

    /**
     * Set contact path length.
     *
     * @param contactPathLength contact path length.
     */
    public void setContactPathLength(Integer contactPathLength) {
        this.contactPathLength = contactPathLength;
    }

    /**
     * Return date of visit.
     *
     * @return date of visit.
     */
    public XingCalendar getVisitedAt() {
        return visitedAt;
    }

    /**
     * Set date of visit.
     *
     * @param visitedAt date of visit.
     */
    public void setVisitedAt(String visitedAt) {
        this.visitedAt = CalendarUtils.parseCalendarFromString(visitedAt);
    }

    /**
     * Return visit count.
     *
     * @return visit count.
     */
    public Integer getVisitCount() {
        return visitCount;
    }

    /**
     * Set visit count.
     *
     * @param visitCount visit count.
     */
    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    /**
     * Render user gender.
     *
     * @return gender of user.
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Set gender of user.
     *
     * @param gender gender of user.
     */
    public void setGender(String gender) {
        this.gender = EnumMapper.parseEnumFromString(Gender.values(), gender);
    }

    /**
     * Return job title of user.
     *
     * @return job title.
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Set job title of user.
     *
     * @param jobTitle job title.
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}
