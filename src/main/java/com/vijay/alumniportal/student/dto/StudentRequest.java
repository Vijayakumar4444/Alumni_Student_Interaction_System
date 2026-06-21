package com.vijay.alumniportal.student.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentRequest {

    private String name;
    private String email;
    private String department;
    private Integer year;
    private String skills;
}