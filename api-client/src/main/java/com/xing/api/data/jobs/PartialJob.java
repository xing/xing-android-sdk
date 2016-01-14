package com.xing.api.data.jobs;

import com.squareup.moshi.Json;
import com.xing.api.data.Location;
import com.xing.api.data.SafeCalendar;
import com.xing.api.data.jobs.Job.JobContact;
import com.xing.api.data.profile.Company;

import java.io.Serializable;

/**
 * @author cristian.monforte
 */
public class PartialJob implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "id")
    private String id;
    @Json(name = "location")
    private Location location;
    @Json(name = "title")
    private String title;
    @Json(name = "job_type")
    private JobType jobType;
    @Json(name = "company")
    private Company company;
    @Json(name = "published_at")
    private SafeCalendar publishedAt;
    @Json(name = "links")
    private JobLinks links;
    @Json(name = "contact")
    private JobContact contact;

    public String id() {
        return id;
    }

    public PartialJob id(String id) {
        this.id = id;
        return this;
    }

    public Location location() {
        return location;
    }

    public PartialJob location(Location location) {
        this.location = location;
        return this;
    }

    public String title() {
        return title;
    }

    public PartialJob title(String title) {
        this.title = title;
        return this;
    }

    public JobType jobType() {
        return jobType;
    }

    public PartialJob jobType(JobType jobType) {
        this.jobType = jobType;
        return this;
    }

    public Company company() {
        return company;
    }

    public PartialJob company(Company company) {
        this.company = company;
        return this;
    }

    public SafeCalendar publishedAt() {
        return publishedAt;
    }

    public PartialJob publishedAt(SafeCalendar publishedAt) {
        this.publishedAt = publishedAt;
        return this;
    }

    public JobLinks links() {
        return links;
    }

    public PartialJob links(JobLinks links) {
        this.links = links;
        return this;
    }

    public JobContact contact() {
        return contact;
    }

    public PartialJob contact(JobContact contact) {
        this.contact = contact;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PartialJob that = (PartialJob) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (jobType != that.jobType) return false;
        if (company != null ? !company.equals(that.company) : that.company != null) return false;
        if (publishedAt != null ? !publishedAt.equals(that.publishedAt) : that.publishedAt != null) return false;
        if (links != null ? !links.equals(that.links) : that.links != null) return false;
        return !(contact != null ? !contact.equals(that.contact) : that.contact != null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (jobType != null ? jobType.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (publishedAt != null ? publishedAt.hashCode() : 0);
        result = 31 * result + (links != null ? links.hashCode() : 0);
        result = 31 * result + (contact != null ? contact.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PartialJob{"
              + "id='" + id + '\''
              + ", location=" + location
              + ", title='" + title + '\''
              + ", jobType=" + jobType
              + ", company=" + company
              + ", publishedAt=" + publishedAt
              + ", links=" + links
              + ", contact=" + contact
              + '}';
    }
}
