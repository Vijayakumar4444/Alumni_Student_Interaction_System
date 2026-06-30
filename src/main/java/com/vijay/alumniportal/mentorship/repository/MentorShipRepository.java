package com.vijay.alumniportal.mentorship.repository;

import com.vijay.alumniportal.mentorship.entity.MentorShip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentorShipRepository extends JpaRepository<MentorShip,Long> {

    List<MentorShip> findByStudentId(Long studentId);

    List<MentorShip> findByAlumniId(Long alumniId);

    boolean existsByStudentIdAndStatus(
            Long studentId,
            MentorShip.MentorShipStatus status
    );

    boolean existsByStudentIdAndAlumniIdAndStatus(
            Long studentId,
            Long alumniId,
            MentorShip.MentorShipStatus status
    );

    List<MentorShip> findByStudentIdAndStatus(
            Long studentId,
            MentorShip.MentorShipStatus status
    );
}