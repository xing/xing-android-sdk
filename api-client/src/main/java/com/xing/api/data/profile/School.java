/*
 * Copyright (C) 2018 XING SE (http://xing.com/)
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
import com.xing.api.internal.json.CsvCollection;

import java.io.Serializable;
import java.util.List;

/**
 * A {@linkplain XingUser user} school/university.
 *
 * @see <a href="https://dev.xing.com/docs/post/users/me/educational_background/schools">School Resource</a>
 */
@SuppressWarnings("unused")
public class School implements Serializable {
    private static final long serialVersionUID = 1L;

    /** School ID. */
    @Json(name = "id")
    private String id;
    /** Name of school. */
    @Json(name = "name")
    private String name;
    /** Degree. */
    @Json(name = "degree")
    private String degree;
    /** Additional notes such as specialized subjects. */
    @CsvCollection
    @Json(name = "notes")
    private List<String> notes;
    /** Describes the field of study. */
    @Json(name = "subject")
    private String subject;
    /** Start date. */
    @Json(name = "begin_date")
    private SafeCalendar beginDate;
    /** End date. */
    @Json(name = "end_date")
    private SafeCalendar endDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        School school = (School) o;

        return (id != null ? id.equals(school.id) : school.id == null)
              && (name != null ? name.equals(school.name) : school.name == null)
              && (degree != null ? degree.equals(school.degree) : school.degree == null)
              && (notes != null ? notes.equals(school.notes) : school.notes == null)
              && (subject != null ? subject.equals(school.subject) : school.subject == null)
              && (beginDate != null ? beginDate.equals(school.beginDate) : school.beginDate == null)
              && (endDate != null ? endDate.equals(school.endDate) : school.endDate == null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (degree != null ? degree.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (beginDate != null ? beginDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "School{"
              + "id='" + id + '\''
              + ", name='" + name + '\''
              + ", degree='" + degree + '\''
              + ", notes=" + notes
              + ", subject='" + subject + '\''
              + ", beginDate=" + beginDate
              + ", endDate=" + endDate
              + '}';
    }

    /** Returns the school id. */
    public String id() {
        return id;
    }

    /** Sets the school id. */
    public School id(String id) {
        this.id = id;
        return this;
    }

    /** Returns the school name. */
    public String name() {
        return name;
    }

    /** Sets the school name. */
    public School name(String name) {
        this.name = name;
        return this;
    }

    /** Returns the {@linkplain XingUser user's} degree. */
    public String degree() {
        return degree;
    }

    /** Sets the {@linkplain XingUser user's} degree. */
    public School degree(String degree) {
        this.degree = degree;
        return this;
    }

    /** Returns a list of additional notes (example: specialized subjects, e.t.c.). */
    public List<String> notes() {
        return notes;
    }

    /** Sets the list of additional notes. */
    public School notes(List<String> notes) {
        this.notes = notes;
        return this;
    }

    /** Returns the subject of the field of study. */
    public String subject() {
        return subject;
    }

    /** Sets the subject of the field of study. */
    public School subject(String subject) {
        this.subject = subject;
        return this;
    }

    /** Returns the start date. */
    public SafeCalendar beginDate() {
        return beginDate;
    }

    /** Sets the start date. */
    public School beginDate(SafeCalendar beginDate) {
        this.beginDate = beginDate;
        return this;
    }

    /** Returns the end date. */
    public SafeCalendar endDate() {
        return endDate;
    }

    /** Sets the end date. */
    public School endDate(SafeCalendar endDate) {
        this.endDate = endDate;
        return this;
    }
}
