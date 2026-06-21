package com.vijay.alumniportal.alumni.repository;

import com.vijay.alumniportal.alumni.entity.Alumni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlumniRepository extends JpaRepository<Alumni, Long> {
}