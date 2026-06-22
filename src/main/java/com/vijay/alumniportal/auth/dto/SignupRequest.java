package com.vijay.alumniportal.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    private String name;
    private String email;
    private String password;
    private String role;

    // Student fields
    private String department;
    private Integer year;

    // Alumni fields
    private String company;
    private String designation;
    private Integer experience;

    // Common field
    private String skills;
}