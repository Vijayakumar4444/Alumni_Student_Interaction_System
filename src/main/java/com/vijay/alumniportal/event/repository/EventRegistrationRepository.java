package com.vijay.alumniportal.event.repository;

import com.vijay.alumniportal.event.entity.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {

    boolean existsByEventIdAndStudentId(Long eventId, Long studentId);

    List<EventRegistration> findByEventId(Long eventId);

    List<EventRegistration> findByStudentId(Long studentId);
}