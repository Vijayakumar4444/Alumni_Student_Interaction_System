package com.vijay.alumniportal.job.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JobResponse {

    private Long id;
    private Long alumniId;
    private String title;
    private String company;
    private String description;
    private String location;
    private String jobType;
    private String skillsRequired;
    private Integer appliedCount;
}