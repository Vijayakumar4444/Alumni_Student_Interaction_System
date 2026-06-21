package com.vijay.alumniportal.alumni.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlumniRequest {

    private String name;
    private String email;
    private String company;
    private String designation;
    private Integer experience;
    private String skills;
}