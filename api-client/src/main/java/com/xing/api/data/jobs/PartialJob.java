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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PartialJob partialJob = (PartialJob) o;
        return id != null ? id.equals(partialJob.id) : partialJob.id == null
              && (location != null ? location.equals(partialJob.location) : partialJob.location == null
              && (title != null ? title.equals(partialJob.title) : partialJob.title == null
              && jobType == partialJob.jobType
              && (company != null ? company.equals(partialJob.company) : partialJob.company == null
              && (publishedAt != null ? publishedAt.equals(partialJob.publishedAt) : partialJob.publishedAt == null
              && (links != null ? links.equals(partialJob.links) : partialJob.links == null
              && (contact != null ? contact.equals(partialJob.contact) : partialJob.contact == null))))));
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
}
