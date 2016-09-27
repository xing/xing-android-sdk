/*
 * Copyright (С) 2016 XING AG (http://xing.com/)
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
package com.xing.api.resources;

import com.xing.api.CallSpec;
import com.xing.api.HttpError;
import com.xing.api.Resource;
import com.xing.api.XingApi;
import com.xing.api.data.jobs.Job;
import com.xing.api.data.jobs.PartialJob;

import java.util.List;

/**
 * Represent the <a href="https://dev.xing.com/docs/resources#jobs">'Jobs'</a> resource.
 * <p>
 * Provides methods which allow search for a {@linkplain Job}'s by id or search for a list of {@linkplain Job job's} by
 * criteria or recommended to a user by id.
 *
 * @author daniel.hartwich
 * @author cristian.monforte
 */
public class JobsResource extends Resource {
    /** Creates a resource instance. This should be the only constructor declared by child classes. */
    JobsResource(XingApi api) {
        super(api);
    }

    /**
     * Returns a full {@linkplain Job} posting.
     *
     * Full job postings contain the following fields in addition to minimal job postings (returned by jobs
     * recommendations and jobs search ): level, job_type, industry, skills, tags, description.
     * When the contact field is present, it contains either a company or a user.
     * Warning: The company field does not contain a XING company profile in all cases. This means that the company,
     * that posted the job, does not have a XING company profile. In this case the id field is null and the links field
     * contains an empty object.
     */
    public CallSpec<Job, HttpError> getJobById(String jobId) {
        return Resource.<Job, HttpError>newGetSpec(api, "/v1/jobs/{id}")
              .pathParam("id", jobId)
              .responseAs(single(Job.class, "job"))
              .build();
    }

    /**
     * Returns a list of {@linkplain PartialJob} postings that match the given criteria.
     *
     * A minimal job posting consists of the fields id, title, company, location, contact, published_at and links.
     * If you want to return the full job posting, that contains for example the description, you need to use {@link
     * #getJobById(String)}. You can also follow the links.self URL, that will also lead you to the full job posting.
     */
    public CallSpec<List<PartialJob>, HttpError> getJobsByCriteria(String criteria) {
        return Resource.<List<PartialJob>, HttpError>newGetSpec(api, "/v1/jobs/find")
              .queryParam("query", criteria)
              .responseAs(list(PartialJob.class, "jobs", "items"))
              .build();
    }

    /**
     * Returns a list of recommended {@linkplain PartialJob} postings for the specified user.
     *
     * A minimal job posting consists of the fields id, title, company, location, contact, published_at and links.
     * If you want to return the full job posting, that contains for example the description, you need to use
     * {@link #getJobById(String)}. You can also follow the links.self URL, that will also lead you to the full job
     * posting.
     */
    public CallSpec<List<PartialJob>, HttpError> getJobsRecommendationsForUser(String userId) {
        return Resource.<List<PartialJob>, HttpError>newGetSpec(api, "/v1/users/{user_id}/jobs/recommendations")
              .pathParam("user_id", userId)
              .responseAs(list(PartialJob.class, "job_recommendations", "items"))
              .build();
    }
}
