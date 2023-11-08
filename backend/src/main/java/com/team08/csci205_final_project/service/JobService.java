/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Fall 2023
 * Instructor: Prof. Brian King / Prof. Joshua Stough
 *
 * Name: Hung Pham
 * Section: 10AM
 * Date: 11/6/2023
 * Time: 10:50 AM
 *
 * Project: csci205_final_project
 * Package: com.team08.csci205_final_project.service
 * Class: JobService
 *
 * Description:
 *
 * ****************************************
 */
package com.team08.csci205_final_project.service;

import com.team08.csci205_final_project.model.Job;
import com.team08.csci205_final_project.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    /**
     * Create a job in the database and return
     * @param job the information of the job
     * @return the job after created with added information
     */
    public Job createJob(Job job) {
        job.setCreatedDate(LocalDate.now());
        return jobRepository.save(job);
    }

}
