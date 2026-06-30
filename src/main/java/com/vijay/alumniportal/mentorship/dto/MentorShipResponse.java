package com.vijay.alumniportal.mentorship.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MentorShipResponse {

    private Long id;
    private Long studentId;
    private Long alumniId;

    private String studentName;
    private String studentEmail;
    private String studentDepartment;
    private Integer studentYear;
    private String studentSkills;

    private String message;
    private String status;
}