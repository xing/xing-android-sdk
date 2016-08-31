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
import com.xing.api.internal.json.NullInt;

import java.io.Serializable;
import java.util.List;

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

    /** Visit id. */
    @Json(name = "id")
    private String id;
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
    @NullInt
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
    @NullInt
    @Json(name = "distance")
    private int distance;
    /** Visit type. */
    @Json(name = "type")
    private Type type;
    @Json(name = "shared_contacts")
    private List<XingUser> sharedContacts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProfileVisit visit = (ProfileVisit) o;

        return visitCount == visit.visitCount && distance == visit.distance
              && (id != null ? id.equals(visit.id) : visit.id == null)
              && (userId != null ? userId.equals(visit.userId) : visit.userId == null)
              && (displayName != null ? displayName.equals(visit.displayName) : visit.displayName == null)
              && (companyName != null ? companyName.equals(visit.companyName) : visit.companyName == null)
              && (jobTitle != null ? jobTitle.equals(visit.jobTitle) : visit.jobTitle == null)
              && gender == visit.gender
              && (visitedAt != null ? visitedAt.equals(visit.visitedAt) : visit.visitedAt == null)
              && (visitedAtEncrypted != null ? visitedAtEncrypted.equals(visit.visitedAtEncrypted)
              : visit.visitedAtEncrypted == null)
              && (photoUrls != null ? photoUrls.equals(visit.photoUrls) : visit.photoUrls == null)
              && (reason != null ? reason.equals(visit.reason) : visit.reason == null)
              && type == visit.type
              && (sharedContacts != null ? sharedContacts.equals(visit.sharedContacts) : visit.sharedContacts == null);
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (companyName != null ? companyName.hashCode() : 0);
        result = 31 * result + (jobTitle != null ? jobTitle.hashCode() : 0);
        result = 31 * result + visitCount;
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (visitedAt != null ? visitedAt.hashCode() : 0);
        result = 31 * result + (visitedAtEncrypted != null ? visitedAtEncrypted.hashCode() : 0);
        result = 31 * result + (photoUrls != null ? photoUrls.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        result = 31 * result + distance;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (sharedContacts != null ? sharedContacts.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProfileVisit{"
              + "id='" + id + '\''
              + "userId='" + userId + '\''
              + ", displayName='" + displayName + '\''
              + ", companyName='" + companyName + '\''
              + ", jobTitle='" + jobTitle + '\''
              + ", visitCount=" + visitCount
              + ", gender=" + gender
              + ", visitedAt=" + visitedAt
              + ", visitedAtEncrypted='" + visitedAtEncrypted + '\''
              + ", photoUrls=" + photoUrls
              + ", reason=" + reason
              + ", distance=" + distance
              + ", type=" + type
              + ", sharedContacts=" + sharedContacts
              + '}';
    }

    /** Returns <strong>this</strong> visits id. */
    public String id() {
        return id;
    }

    /** Sets <strong>this</strong> visits id. */
    public ProfileVisit id(String id) {
        this.id = id;
        return this;
    }

    /** Returns the company name. */
    public String companyName() {
        return companyName;
    }

    /** Sets the company name. */
    public ProfileVisit companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    /** Returns the reason for visit. */
    public Reason reason() {
        return reason;
    }

    /** Sets the reason for visit. */
    public ProfileVisit reason(Reason reason) {
        this.reason = reason;
        return this;
    }

    /** Returns encrypted date of visit. */
    public String visitedAtEncrypted() {
        return visitedAtEncrypted;
    }

    /** Sets the encrypted date of visit. */
    public ProfileVisit visitedAtEncrypted(String visitedAtEncrypted) {
        this.visitedAtEncrypted = visitedAtEncrypted;
        return this;
    }

    /** Returns the photo URLs of user. */
    public PhotoUrls photoUrls() {
        return photoUrls;
    }

    /** Sets the photo URLs of user. */
    public ProfileVisit photoUrls(PhotoUrls photoUrls) {
        this.photoUrls = photoUrls;
        return this;
    }

    /** Returns the user ID. */
    public String userId() {
        return userId;
    }

    /** Sets the user ID. */
    public ProfileVisit userId(String userId) {
        this.userId = userId;
        return this;
    }

    /** Returns the user name. */
    public String displayName() {
        return displayName;
    }

    /** Sets the user name. */
    public ProfileVisit displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    /** Returns the <strong>contact path</strong> length. */
    public int distance() {
        return distance;
    }

    /** Sets the <strong>contact path</strong> length. */
    public ProfileVisit distance(int distance) {
        this.distance = distance;
        return this;
    }

    /** Returns the date of visit. */
    public SafeCalendar visitedAt() {
        return visitedAt;
    }

    /** Sets the date of visit. */
    public ProfileVisit visitedAt(SafeCalendar visitedAt) {
        this.visitedAt = visitedAt;
        return this;
    }

    /** Returns the visit count (total number of visits for {@link #visitedAt()}). */
    public int visitCount() {
        return visitCount;
    }

    /** Sets visit count. */
    public ProfileVisit visitCount(int visitCount) {
        this.visitCount = visitCount;
        return this;
    }

    /** Returns the user's gender. */
    public Gender gender() {
        return gender;
    }

    /** Sets the gender of the user. */
    public ProfileVisit gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    /** Returns the job title of the user. */
    public String jobTitle() {
        return jobTitle;
    }

    /** Sets the job title of the user. */
    public ProfileVisit jobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }

    /** Returns the type of the visit. */
    public Type type() {
        return type;
    }

    /** Sets visit type. */
    public ProfileVisit type(Type type) {
        this.type = type;
        return this;
    }

    /** Returns the list of shared contacts. */
    public List<XingUser> sharedContacts() {
        return sharedContacts;
    }

    /** Sets the list of shared contacts. */
    public ProfileVisit sharedContacts(List<XingUser> sharedContacts) {
        this.sharedContacts = sharedContacts;
        return this;
    }

    /** Reason for the profile visit. */
    public static class Reason implements Serializable {
        private static final long serialVersionUID = 1L;

        @Json(name = "text")
        private final String text;

        public Reason(String text) {
            this.text = text;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Reason reason = (Reason) o;
            return text != null ? text.equals(reason.text) : reason.text == null;
        }

        @Override
        public int hashCode() {
            return text != null ? text.hashCode() : 0;
        }

        @Override
        public String toString() {
            return text;
        }

        public String text() {
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
