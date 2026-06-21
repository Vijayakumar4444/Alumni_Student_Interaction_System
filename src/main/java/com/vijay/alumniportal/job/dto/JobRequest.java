package com.vijay.alumniportal.job.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobRequest {

    private String title;
    private String company;
    private String description;
    private String location;
    private String jobType;
    private String skillsRequired;
    private String postedBy;
}