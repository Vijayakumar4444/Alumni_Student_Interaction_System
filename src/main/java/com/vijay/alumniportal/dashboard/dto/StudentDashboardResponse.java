package com.vijay.alumniportal.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentDashboardResponse {

    private Long studentId;
    private String name;
    private String email;
    private String department;
    private Integer year;
    private String skills;

    private Integer totalMentorshipRequests;
    private Boolean hasAcceptedMentor;

    private Integer registeredEventsCount;
    private Integer postedQuestionsCount;
    private Integer recommendedEventsCount;
    private Integer availableJobsCount;
}