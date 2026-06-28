package com.vijay.alumniportal.job.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long alumniId;

    private String title;
    private String company;
    private String description;
    private String location;
    private String jobType;
    private String skillsRequired;

    private Integer appliedCount;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    @Builder.Default
    private List<JobApplication> applications = new ArrayList<>();
}