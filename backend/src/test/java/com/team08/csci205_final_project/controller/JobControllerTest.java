package com.team08.csci205_final_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team08.csci205_final_project.model.Job;
import com.team08.csci205_final_project.service.JobService;

import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit Test for JobController
 * The test ensures the controller calls
 * the correct services and responses the correct HTTP responses
 */
@WebMvcTest(value = JobController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ExtendWith(SpringExtension.class)
public class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobService jobService;

    @Autowired
    private ObjectMapper objectMapper;

    private Job randomJob, randomJob1;

    @BeforeEach
    public void setup() {
        randomJob = new Job();
        randomJob.setUserId("user1");
        randomJob.setCategory("Food");
        randomJob.setDescription("Deliver food to my cat");
        randomJob.setReceiverName("John Doe");
        randomJob.setReceiverAddress("123 Main St");
        randomJob.setReceiverPhone("555-1234");

        randomJob1 = new Job();
        randomJob1.setUserId("user1");
        randomJob1.setCategory("Pet");
        randomJob1.setDescription("Deliver cat to me");
        randomJob1.setReceiverName("John Doe");
        randomJob1.setReceiverAddress("123 Main St");
        randomJob1.setReceiverPhone("555-1234");

    }

    @Test
    public void createJob() throws Exception {

        String requestBody = objectMapper.writeValueAsString(randomJob);

        randomJob.setId("job1");

        String expectedResponseBody = objectMapper.writeValueAsString(randomJob);

        given(jobService.createJob(any(Job.class))).willReturn(randomJob);

        given(jobService.findJobById("job1")).willReturn(Optional.of(randomJob));

        mockMvc.perform(post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpectAll(
                        status().isCreated(),
                        header().string("Location", new StringContains("/api/jobs/")),
                        content().json(expectedResponseBody)
                );
    }

    @Test
    public void getJobById() throws Exception {
        Job expectedJob = randomJob;

        String expectedResponseBody = objectMapper.writeValueAsString(expectedJob);

        given(jobService.findJobById("job1")).willReturn(Optional.of(randomJob));
        given(jobService.findJobById(argThat(arg -> !arg.equals("job1")))).willReturn(Optional.empty());

        // Successful request
        mockMvc.perform(get("/api/jobs/{id}", "job1"))
                .andExpectAll(
                        status().isOk(),
                        content().json(expectedResponseBody)
                );

        // Failed request
        mockMvc.perform(get("/api/jobs/{id}", "job2"))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @Test
    public void getJobByUser() throws Exception {
        String userId = randomJob.getUserId();

        List<Job> expectedJobs = Arrays.asList(
                randomJob, randomJob1
        );

        String expectedResponseBody = objectMapper.writeValueAsString(expectedJobs);

        given(jobService.findJobByUser("user1")).willReturn(expectedJobs);

        mockMvc.perform(get("/api/jobs/user/{id}", userId))
                .andExpectAll(
                        status().isOk(),
                        content().json(expectedResponseBody)
                );
    }

    @Test
    public void updateJobById() throws Exception {

        randomJob.setId("job1");
        randomJob1.setId("job1");

        String updatedJobJson = objectMapper.writeValueAsString(randomJob1);

        given(jobService.findJobById("job1")).willReturn(Optional.of(randomJob));
        given(jobService.findJobById(argThat(arg -> !arg.equals("job1")))).willReturn(Optional.empty());
        given(jobService.updateJob(any(Job.class))).willReturn(randomJob1);

        mockMvc.perform(put("/api/jobs/{id}", "job1") // Replace with your actual endpoint
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJobJson))
                .andExpectAll(
                        status().isOk(),
                        content().json(updatedJobJson)
                );
    }

    @Test
    public void deleteJob() throws Exception {
        given(jobService.deleteJob("job1")).willReturn(true);

        mockMvc.perform(delete("/api/jobs/{id}", "job1"))
                .andExpect(status().isNoContent());
    }

}
