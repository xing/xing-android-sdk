package com.xing.api.resources;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.xing.api.HttpError;
import com.xing.api.Response;
import com.xing.api.data.jobs.Job;
import com.xing.api.data.jobs.PartialJob;

import org.junit.Test;

import java.util.List;

import static com.xing.api.TestUtils.file;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author cristian.monforte
 */
@SuppressWarnings("ConstantConditions")
public class JobsResourceTest extends ResourceTestCase<JobsResource> {

    private String jobsJson;

    public JobsResourceTest() {
        super(JobsResource.class);
    }

    @Test
    public void getJobById() throws Exception {
        String userId = "1";
        jobsJson = file("jobs.json");
        server.enqueue(new MockResponse().setBody(jobsJson));

        Response<Job, HttpError> response = resource.getJobById(userId).execute();

        assertThat(response.body().id()).isEqualTo("61723_4cae01");
    }

    @Test
    public void getJobsByCriteria() throws Exception {
        String searchCriteria = "1";
        jobsJson = file("listOfJobs.json");
        server.enqueue(new MockResponse().setBody(jobsJson));

        Response<List<PartialJob>, HttpError> response = resource.getJobsByCriteria(searchCriteria).execute();

        assertThat(response.body().get(0).id()).isEqualTo("61723_4cae01");
    }


    @Test
    public void getJobsRecomendations() throws Exception {
        String userId = "1";
        jobsJson = file("recommendedJobs.json");
        server.enqueue(new MockResponse().setBody(jobsJson));

        Response<List<PartialJob>, HttpError> response = resource.getJobsRecomendationsForUser(userId).execute();

        assertThat(response.body().get(0).id()).isEqualTo("61723_4cae01");
    }
}
