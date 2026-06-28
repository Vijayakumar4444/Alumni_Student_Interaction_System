package com.vijay.alumniportal.job.dto;

import com.vijay.alumniportal.job.entity.JobApplication;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class JobApplicationResponse {

    private Long applicationId;

    private Long jobId;
    private String jobTitle;
    private String company;
    private String location;
    private String jobType;

    private Long studentId;
    private String studentName;

    private JobApplication.ApplicationStatus status;
    private LocalDateTime appliedAt;
}