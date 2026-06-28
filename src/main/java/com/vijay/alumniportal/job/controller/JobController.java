package com.vijay.alumniportal.job.controller;

import com.vijay.alumniportal.job.dto.JobApplicationResponse;
import com.vijay.alumniportal.job.dto.JobRequest;
import com.vijay.alumniportal.job.dto.JobResponse;
import com.vijay.alumniportal.job.service.JobService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
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

    @GetMapping("/alumni/{alumniId}")
    public List<JobResponse> getJobsByAlumniId(@PathVariable Long alumniId) {
        return service.getJobsByAlumniId(alumniId);
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

    @PostMapping("/{jobId}/apply/{studentId}")
    public JobApplicationResponse applyJob(
            @PathVariable Long jobId,
            @PathVariable Long studentId
    ) {
        return service.applyJob(jobId, studentId);
    }

    @GetMapping("/student/{studentId}/applications")
    public List<JobApplicationResponse> getStudentApplications(@PathVariable Long studentId) {
        return service.getStudentApplications(studentId);
    }

    @GetMapping("/{jobId}/applications")
    public List<JobApplicationResponse> getJobApplications(@PathVariable Long jobId) {
        return service.getJobApplications(jobId);
    }

    @PutMapping("/applications/{applicationId}/cancel")
    public JobApplicationResponse cancelApplication(@PathVariable Long applicationId) {
        return service.cancelApplication(applicationId);
    }

    @PutMapping("/applications/{applicationId}/status")
    public JobApplicationResponse updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam String status
    ) {
        return service.updateApplicationStatus(applicationId, status);
    }
}