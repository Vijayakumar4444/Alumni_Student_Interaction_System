package com.vijay.alumniportal.alumni.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlumniResponse {

    private Long id;
    private String name;
    private String email;
    private String company;
    private String designation;
    private Integer experience;
    private String skills;
}