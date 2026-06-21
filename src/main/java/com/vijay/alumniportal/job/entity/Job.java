package com.vijay.alumniportal.job.entity;

import com.vijay.alumniportal.student.entity.Student;
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

    private String title;
    private String company;
    private String description;
    private String location;
    private String jobType;
    private String skillsRequired;
    private String postedBy;
    private Integer appliedCount;
    //private Integer maxCount;

    @ManyToMany
    @JoinTable(
            name="job_applications",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @Builder.Default
    private List<Student> studentsApplied=new ArrayList<>();
}