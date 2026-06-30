package com.vijay.alumniportal.student.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentResponse {

    private Long id;
    private String name;
    private String email;
    private String department;
    private Integer year;
    private String skills;
    private String profileImage;
}