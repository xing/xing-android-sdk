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
import com.xing.api.model.Reason;
import com.xing.api.model.SafeCalendar;

import java.io.Serializable;

/**
 * TODO move this to visits package.
 * Represents an profile visit.
 * <p/>
 *
 * @author daniel.hartwich
 * @author ciprian.ursu
 * @see <a href="https://dev.xing.com/docs/get/users/:user_id/visits">Profile Visit</a>
 */
@SuppressWarnings("unused")
public class ProfileVisit implements Serializable {
    private static final long serialVersionUID = 1L;

    /** User ID. */
    @Json(name = "user_id")
    private String userId;
    /** User name. */
    @Json(name = "display_name")
    private String displayName;
    /** Company name. */
    @Json(name = "company_name")
    private String companyName;
    /** User job title. */
    @Json(name = "job_title")
    private String jobTitle;
    /** Number of visits. */
    @Json(name = "visit_count")
    private int visitCount;
    /** Gender of user. */
    @Json(name = "gender")
    private Gender gender;
    /** Date of visit. */
    @Json(name = "visited_at")
    private SafeCalendar visitedAt;
    /** Encrypted date of visit. */
    @Json(name = "visited_at_encrypted")
    private String visitedAtEncrypted;
    /** User photo. */
    @Json(name = "photo_urls")
    private PhotoUrls photoUrls;
    /** Reason for visit. */
    @Json(name = "reason")
    private Reason reason;
    /** Distance from the visitor to the user. */
    @Json(name = "distance")
    private int distance;

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
    public Reason getReason() {
        return reason;
    }

    /**
     * Set reason for visit.
     *
     * @param reason reason for visit.
     */
    public void setReason(Reason reason) {
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
    public PhotoUrls getPhotoUrls() {
        return photoUrls;
    }

    /**
     * Set photo URLs of user.
     *
     * @param photoUrls photo URLs of user.
     */
    public void setPhotoUrls(PhotoUrls photoUrls) {
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
    public Integer getDistance() {
        return distance;
    }

    /**
     * Set contact path length.
     *
     * @param distance contact path length.
     */
    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    /**
     * Return date of visit.
     *
     * @return date of visit.
     */
    public SafeCalendar getVisitedAt() {
        return visitedAt;
    }

    /**
     * Set date of visit.
     *
     * @param visitedAt date of visit.
     */
    public void setVisitedAt(SafeCalendar visitedAt) {
        this.visitedAt = visitedAt;
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
    public void setGender(Gender gender) {
        this.gender = gender;
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
