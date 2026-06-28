package com.vijay.alumniportal.job.repository;

import com.vijay.alumniportal.job.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    boolean existsByJobIdAndStudentId(Long jobId, Long studentId);

    List<JobApplication> findByStudentId(Long studentId);

    List<JobApplication> findByJobId(Long jobId);
}