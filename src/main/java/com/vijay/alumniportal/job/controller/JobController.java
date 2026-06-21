package com.vijay.alumniportal.job.controller;

import com.vijay.alumniportal.job.dto.JobRequest;
import com.vijay.alumniportal.job.dto.JobResponse;
import com.vijay.alumniportal.job.service.JobService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService service;

    public JobController(JobService service) {
        this.service = service;
    }

    @PostMapping
    public JobResponse createJob(@RequestBody JobRequest request) {
        return service.createJob(request);
    }

    @GetMapping
    public List<JobResponse> getAllJobs() {
        return service.getAllJobs();
    }

    @GetMapping("/{id}")
    public JobResponse getJobById(@PathVariable Long id) {
        return service.getJobById(id);
    }

    @PutMapping("/{id}")
    public JobResponse updateJob(
            @PathVariable Long id,
            @RequestBody JobRequest request
    ) {
        return service.updateJob(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteJob(@PathVariable Long id) {
        service.deleteJob(id);
        return "Job deleted successfully";
    }
}