package com.vijay.alumniportal.mentorship.service;

import com.vijay.alumniportal.mentorship.dto.MentorShipRequest;
import com.vijay.alumniportal.mentorship.dto.MentorShipResponse;
import com.vijay.alumniportal.mentorship.entity.MentorShip;
import com.vijay.alumniportal.mentorship.repository.MentorShipRepository;
import com.vijay.alumniportal.notification.entity.Notification;
import com.vijay.alumniportal.notification.service.NotificationService;
import com.vijay.alumniportal.student.entity.Student;
import com.vijay.alumniportal.student.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MentorShipService {

    private final MentorShipRepository mentorShipRepository;
    private final NotificationService notificationService;
    private final StudentRepository studentRepository;

    public MentorShipService(
            MentorShipRepository mentorShipRepository,
            NotificationService notificationService,
            StudentRepository studentRepository
    ) {
        this.mentorShipRepository = mentorShipRepository;
        this.notificationService = notificationService;
        this.studentRepository = studentRepository;
    }

    public MentorShipResponse createMentorship(
            MentorShipRequest request
    ) {
        Student student = studentRepository
                .findById(request.getStudentId())
                .orElseThrow(() ->
                        new RuntimeException("Student not found")
                );

        boolean alreadyHasMentor =
                mentorShipRepository
                        .existsByStudentIdAndStatus(
                                request.getStudentId(),
                                MentorShip.MentorShipStatus.ACCEPTED
                        );

        if (alreadyHasMentor) {
            throw new RuntimeException(
                    "You already have an accepted mentor"
            );
        }

        boolean alreadyPending =
                mentorShipRepository
                        .existsByStudentIdAndStatus(
                                request.getStudentId(),
                                MentorShip.MentorShipStatus.PENDING
                        );

        if (alreadyPending) {
            throw new RuntimeException(
                    "You already have a pending mentorship request"
            );
        }

        MentorShip mentorShip = MentorShip.builder()
                .studentId(request.getStudentId())
                .alumniId(request.getAlumniId())
                .message(request.getMessage())
                .status(MentorShip.MentorShipStatus.PENDING)
                .build();

        MentorShip savedMentorship =
                mentorShipRepository.save(mentorShip);

        String notificationMessage =
                "MENTORSHIP_REQUEST_ID:"
                        + savedMentorship.getId()
                        + "|STUDENT_ID:"
                        + student.getId()
                        + "|STUDENT_NAME:"
                        + student.getName();

        notificationService.createNotification(
                savedMentorship.getAlumniId(),
                Notification.UserRole.ALUMNI,
                "New Mentorship Request",
                notificationMessage
        );

        return mapToResponse(savedMentorship);
    }

    public List<MentorShipResponse> getAllMentorships() {
        return mentorShipRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MentorShipResponse getMentorshipById(Long id) {
        MentorShip mentorShip = mentorShipRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Mentorship not found with id: " + id
                        )
                );

        return mapToResponse(mentorShip);
    }

    public List<MentorShipResponse>
    getMentorshipsByStudentId(Long studentId) {

        return mentorShipRepository
                .findByStudentId(studentId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<MentorShipResponse>
    getMentorshipsByAlumniId(Long alumniId) {

        return mentorShipRepository
                .findByAlumniId(alumniId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MentorShipResponse updateMentorshipStatus(
            Long id,
            String status
    ) {
        MentorShip mentorShip = mentorShipRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Mentorship not found with id: " + id
                        )
                );

        MentorShip.MentorShipStatus newStatus =
                MentorShip.MentorShipStatus.valueOf(
                        status.toUpperCase()
                );

        mentorShip.setStatus(newStatus);

        MentorShip updatedMentorship =
                mentorShipRepository.save(mentorShip);

        return mapToResponse(updatedMentorship);
    }

    public MentorShipResponse acceptMentorship(Long id) {
        MentorShip mentorShip = mentorShipRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Mentorship not found with id: " + id
                        )
                );

        if (mentorShip.getStatus()
                != MentorShip.MentorShipStatus.PENDING) {
            throw new RuntimeException(
                    "Only pending requests can be accepted"
            );
        }

        boolean alreadyHasMentor =
                mentorShipRepository
                        .existsByStudentIdAndStatus(
                                mentorShip.getStudentId(),
                                MentorShip.MentorShipStatus.ACCEPTED
                        );

        if (alreadyHasMentor) {
            throw new RuntimeException(
                    "This student already has an accepted mentor"
            );
        }

        mentorShip.setStatus(
                MentorShip.MentorShipStatus.ACCEPTED
        );

        MentorShip updatedMentorship =
                mentorShipRepository.save(mentorShip);

        List<MentorShip> pendingRequests =
                mentorShipRepository.findByStudentIdAndStatus(
                        mentorShip.getStudentId(),
                        MentorShip.MentorShipStatus.PENDING
                );

        for (MentorShip pendingRequest : pendingRequests) {
            if (!pendingRequest.getId().equals(id)) {
                pendingRequest.setStatus(
                        MentorShip.MentorShipStatus.REJECTED
                );
            }
        }

        mentorShipRepository.saveAll(pendingRequests);

        notificationService.createNotification(
                updatedMentorship.getStudentId(),
                Notification.UserRole.STUDENT,
                "Mentorship Accepted",
                "Your mentorship request has been accepted"
        );

        return mapToResponse(updatedMentorship);
    }

    public MentorShipResponse rejectMentorship(Long id) {
        MentorShip mentorShip = mentorShipRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Mentorship not found with id: " + id
                        )
                );

        if (mentorShip.getStatus()
                == MentorShip.MentorShipStatus.ACCEPTED) {
            throw new RuntimeException(
                    "An accepted mentorship cannot be rejected"
            );
        }

        mentorShip.setStatus(
                MentorShip.MentorShipStatus.REJECTED
        );

        MentorShip updatedMentorship =
                mentorShipRepository.save(mentorShip);

        notificationService.createNotification(
                updatedMentorship.getStudentId(),
                Notification.UserRole.STUDENT,
                "Mentorship Rejected",
                "Your mentorship request was rejected"
        );

        return mapToResponse(updatedMentorship);
    }

    public void deleteMentorship(Long id) {
        MentorShip mentorShip = mentorShipRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Mentorship not found with id: " + id
                        )
                );

        mentorShipRepository.delete(mentorShip);
    }

    private MentorShipResponse mapToResponse(
            MentorShip mentorShip
    ) {
        Student student = studentRepository
                .findById(mentorShip.getStudentId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Student not found with id: "
                                        + mentorShip.getStudentId()
                        )
                );

        return new MentorShipResponse(
                mentorShip.getId(),
                mentorShip.getStudentId(),
                mentorShip.getAlumniId(),
                student.getName(),
                student.getEmail(),
                student.getDepartment(),
                student.getYear(),
                student.getSkills(),
                mentorShip.getMessage(),
                mentorShip.getStatus().name()
        );
    }
}