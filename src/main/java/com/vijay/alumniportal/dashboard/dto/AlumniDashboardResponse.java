package com.vijay.alumniportal.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlumniDashboardResponse {

    private Long alumniId;
    private String name;
    private String email;
    private String company;
    private String designation;
    private Integer experience;
    private String skills;

    private Integer postedEventsCount;
    private Integer totalRegisteredStudentsForEvents;
    private Integer answeredQuestionsCount;
    private Integer mentorshipRequestsReceivedCount;
    private Integer postedJobsCount;
}