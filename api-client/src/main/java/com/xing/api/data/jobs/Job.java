package com.xing.api.data.jobs;

import com.squareup.moshi.Json;
import com.xing.api.data.Location;
import com.xing.api.data.SafeCalendar;
import com.xing.api.data.profile.CareerLevel;

import java.io.Serializable;
import java.util.List;

/**
 * @author cristian.monforte
 */
public class Job implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "id")
    private String id;
    @Json(name = "location")
    private Location location;
    @Json(name = "title")
    private String title;
    @Json(name = "level")
    private CareerLevel level;
    @Json(name = "job_type")
    private JobType jobType;
    @Json(name = "industry")
    private String industry;
    @Json(name = "company")
    private JobCompany company;
    @Json(name = "skills")
    private List<String> skills;
    @Json(name = "description")
    private String description;
    @Json(name = "tags")
    private List<String> tags;
    @Json(name = "published_at")
    private SafeCalendar publishedAt;
    @Json(name = "links")
    private JobLinks links;
    @Json(name = "contact")
    private JobContact contact;

    public String id() {
        return id;
    }

    public Job id(String id) {
        this.id = id;
        return this;
    }

    public Location location() {
        return location;
    }

    public Job location(Location location) {
        this.location = location;
        return this;
    }

    public String title() {
        return title;
    }

    public Job title(String title) {
        this.title = title;
        return this;
    }

    public CareerLevel level() {
        return level;
    }

    public Job level(CareerLevel level) {
        this.level = level;
        return this;
    }

    public JobType jobType() {
        return jobType;
    }

    public Job jobType(JobType jobType) {
        this.jobType = jobType;
        return this;
    }

    public String industry() {
        return industry;
    }

    public Job industry(String industry) {
        this.industry = industry;
        return this;
    }

    public JobCompany company() {
        return company;
    }

    public Job company(JobCompany company) {
        this.company = company;
        return this;
    }

    public List<String> skills() {
        return skills;
    }

    public Job skills(List<String> skills) {
        this.skills = skills;
        return this;
    }

    public String description() {
        return description;
    }

    public Job description(String description) {
        this.description = description;
        return this;
    }

    public List<String> tags() {
        return tags;
    }

    public Job tags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public SafeCalendar publishedAt() {
        return publishedAt;
    }

    public Job publishedAt(SafeCalendar publishedAt) {
        this.publishedAt = publishedAt;
        return this;
    }

    public JobLinks links() {
        return links;
    }

    public Job links(JobLinks links) {
        this.links = links;
        return this;
    }

    public JobContact contact() {
        return contact;
    }

    public Job contact(JobContact contact) {
        this.contact = contact;
        return this;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Job job = (Job) o;

        if (id != null ? !id.equals(job.id) : job.id != null) return false;
        if (location != null ? !location.equals(job.location) : job.location != null) return false;
        if (title != null ? !title.equals(job.title) : job.title != null) return false;
        if (level != job.level) return false;
        if (jobType != job.jobType) return false;
        if (industry != null ? !industry.equals(job.industry) : job.industry != null) return false;
        if (company != null ? !company.equals(job.company) : job.company != null) return false;
        if (skills != null ? !skills.equals(job.skills) : job.skills != null) return false;
        if (description != null ? !description.equals(job.description) : job.description != null) return false;
        if (tags != null ? !tags.equals(job.tags) : job.tags != null) return false;
        if (publishedAt != null ? !publishedAt.equals(job.publishedAt) : job.publishedAt != null) return false;
        if (links != null ? !links.equals(job.links) : job.links != null) return false;
        return !(contact != null ? !contact.equals(job.contact) : job.contact != null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (jobType != null ? jobType.hashCode() : 0);
        result = 31 * result + (industry != null ? industry.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (skills != null ? skills.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (publishedAt != null ? publishedAt.hashCode() : 0);
        result = 31 * result + (links != null ? links.hashCode() : 0);
        result = 31 * result + (contact != null ? contact.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Job{" +
              "id='" + id + '\'' +
              ", location=" + location +
              ", title='" + title + '\'' +
              ", level=" + level +
              ", jobType=" + jobType +
              ", industry=" + industry +
              ", company=" + company +
              ", skills=" + skills +
              ", description='" + description + '\'' +
              ", tags=" + tags +
              ", publishedAt=" + publishedAt +
              ", links=" + links +
              ", contact=" + contact +
              '}';
    }

    public static class JobContact implements Serializable {
        private static final long serialVersionUID = 1L;

        @Json(name = "company")
        private final JobCompany jobCompany;

        public JobContact(JobCompany jobCompany) {
            this.jobCompany = jobCompany;
        }

        public JobCompany jobCompany() {
            return jobCompany;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            JobContact that = (JobContact) o;

            return !(jobCompany != null ? !jobCompany.equals(that.jobCompany) : that.jobCompany != null);
        }

        @Override
        public int hashCode() {
            return jobCompany != null ? jobCompany.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "JobContact{" +
                  "jobCompany=" + jobCompany +
                  '}';
        }
    }
}
