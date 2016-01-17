package com.xing.api.resources;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.xing.api.CallSpec;
import com.xing.api.HttpError;
import com.xing.api.Response;
import com.xing.api.data.jobs.Job;
import com.xing.api.data.jobs.PartialJob;

import org.junit.Test;

import java.util.List;

import static com.xing.api.TestUtils.file;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test each resource method against a success server response. This test is a minimal safety major to ensure that each
 * method works as expected. In case if the json response changes on the server side, this test WILL NOT reflect that.
 *
 * @author cristian.monforte
 */
@SuppressWarnings("ConstantConditions")
public class JobsResourceTest extends ResourceTestCase<JobsResource> {
    public JobsResourceTest() {
        super(JobsResource.class);
    }

    @Test
    public void getJobById() throws Exception {
        server.enqueue(new MockResponse().setBody(file("jobs.json")));

        Response<Job, HttpError> response = resource.getJobById("some_id").execute();
        assertThat(response.body().id()).isEqualTo("61723_4cae01");
        assertThat(response.body().contact().jobCompany().name()).isEqualTo("Mr. Recruiter");
    }

    @Test
    public void getJobsByCriteria() throws Exception {
        server.enqueue(new MockResponse().setBody(file("list_of_jobs.json")));

        Response<List<PartialJob>, HttpError> response = resource.getJobsByCriteria("some_criteria").execute();
        assertThat(response.body().get(0).id()).isEqualTo("61723_4cae01");
        assertThat(response.body().get(0).contact().jobCompany().name()).isEqualTo("Rails Heroes");
    }

    @Test
    public void getJobsRecommendations() throws Exception {
        server.enqueue(new MockResponse().setBody(file("recommended_jobs.json")));

        Response<List<PartialJob>, HttpError> response = resource.getJobsRecommendationsForUser("some_id").execute();
        assertThat(response.body().get(0).id()).isEqualTo("61723_4cae01");
        assertThat(response.body().get(0).contact().jobCompany().name()).isEqualTo("Rails Heroes");
    }

    @Test
    public void getJobsRecommendationsWithPagination() throws Exception {
        server.enqueue(new MockResponse().setBody(file("recommended_jobs.json")));
        server.enqueue(new MockResponse().setBody(file("recommended_jobs.json")));

        CallSpec<List<PartialJob>, HttpError> spec = resource.getJobsRecommendationsForUser("some_id");
        spec.execute();

        CallSpec<List<PartialJob>, HttpError> clonedSpec = spec.clone();
        Response<List<PartialJob>, HttpError> response = clonedSpec.queryParam("limit", 10)
              .queryParam("offset", 1)
              .execute();

        assertThat(response.body().get(0).id()).isEqualTo("61723_4cae01");
    }

    @Test(expected = IllegalStateException.class)
    public void getJobsRecommendationsReusingCallSpecShouldThrowException() throws Exception {
        server.enqueue(new MockResponse().setBody(file("recommended_jobs.json")));

        CallSpec<List<PartialJob>, HttpError> spec = resource.getJobsRecommendationsForUser("some_id");
        spec.execute();
        spec.execute();
    }
}
