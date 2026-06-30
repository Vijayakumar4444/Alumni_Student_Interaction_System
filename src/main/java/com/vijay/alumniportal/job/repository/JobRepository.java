package com.vijay.alumniportal.job.repository;

import com.vijay.alumniportal.job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByAlumniId(Long alumniId);
}