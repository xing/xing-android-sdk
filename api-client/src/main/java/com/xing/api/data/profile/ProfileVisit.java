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
package com.xing.api.data.profile;

import com.squareup.moshi.Json;
import com.xing.api.data.SafeCalendar;

import java.io.Serializable;

/**
 * Represents an profile visit.
 * <p/>
 *
 * @author daniel.hartwich
 * @author ciprian.ursu
 * @see <a href="https://dev.xing.com/docs/get/users/:user_id/visits">'Profile Visit' resource page.</a>
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
    /** Visit type. */
    @Json(name = "type")
    private Type type;

    /** Returns the company name. */
    public String getCompanyName() {
        return companyName;
    }

    /** Set the company name. */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /** Returns the reason for visit. */
    public Reason getReason() {
        return reason;
    }

    /** Set the reason for visit. */
    public void setReason(Reason reason) {
        this.reason = reason;
    }

    /** Returns encrypted date of visit. */
    public String getVisitedAtEncrypted() {
        return visitedAtEncrypted;
    }

    /** Set the encrypted date of visit. */
    public void setVisitedAtEncrypted(String visitedAtEncrypted) {
        this.visitedAtEncrypted = visitedAtEncrypted;
    }

    /** Returns the photo URLs of user. */
    public PhotoUrls getPhotoUrls() {
        return photoUrls;
    }

    /** Set the photo URLs of user. */
    public void setPhotoUrls(PhotoUrls photoUrls) {
        this.photoUrls = photoUrls;
    }

    /** Returns the user ID. */
    public String getUserId() {
        return userId;
    }

    /** Set the user ID. */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /** Returns the user name. */
    public String getDisplayName() {
        return displayName;
    }

    /** Set the user name. */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /** Returns the <strong>contact path</strong> length. */
    public int getDistance() {
        return distance;
    }

    /** Set the <strong>contact path</strong> length. */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /** Returns the date of visit. */
    public SafeCalendar getVisitedAt() {
        return visitedAt;
    }

    /** Set the date of visit. */
    public void setVisitedAt(SafeCalendar visitedAt) {
        this.visitedAt = visitedAt;
    }

    /** Returns the visit count (total number of visits for {@link #getVisitedAt()}). */
    public int getVisitCount() {
        return visitCount;
    }

    /** Set visit count. */
    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    /** Returns the user's gender. */
    public Gender getGender() {
        return gender;
    }

    /** Set the gender of the user. */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /** Returns the job title of the user. */
    public String getJobTitle() {
        return jobTitle;
    }

    /** Sets the job title of the user. */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /** Returns the type of the visit. */
    public Type getType() {
        return type;
    }

    /** Set visit type. */
    public void setType(Type type) {
        this.type = type;
    }

    /** Reason for the profile visit. */
    public static class Reason {
        @Json(name = "text")
        private final String text;

        public Reason(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return "Reason{"
                  + "text='" + text + '\''
                  + '}';
        }

        public String getText() {
            return text;
        }
    }

    /** Possible values for profile visit type. */
    public enum Type {
        LOGGED_IN,
        LOGGED_OUT,
        INACTIVE
    }
}
