package com.vijay.alumniportal.mentorship.service;

import com.vijay.alumniportal.mentorship.dto.MentorShipRequest;
import com.vijay.alumniportal.mentorship.dto.MentorShipResponse;
import com.vijay.alumniportal.mentorship.entity.MentorShip;
import com.vijay.alumniportal.mentorship.repository.MentorShipRepository;
import org.springframework.stereotype.Service;
import com.vijay.alumniportal.notification.entity.Notification;
import com.vijay.alumniportal.notification.service.NotificationService;

import java.util.List;

@Service
public class MentorShipService {

    private final MentorShipRepository repo;
    private final NotificationService notificationService;

    public MentorShipService(MentorShipRepository repo,NotificationService notificationService) {
        this.repo = repo;
        this.notificationService = notificationService;
    }

    public MentorShipResponse createMentorship(MentorShipRequest request) {

        MentorShip mentorShip = MentorShip.builder()
                .studentId(request.getStudentId())
                .alumniId(request.getAlumniId())
                .message(request.getMessage())
                .status(MentorShip.MentorShipStatus.PENDING)
                .build();

        MentorShip savedMentorShip = repo.save(mentorShip);
        notificationService.createNotification(
                savedMentorShip.getAlumniId(),
                Notification.UserRole.ALUMNI,
                "New Mentorship Request",
                "A student has sent you a mentorship request."
        );

        return mapToResponse(savedMentorShip);
    }

    public List<MentorShipResponse> getAllMentorships() {

        return repo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MentorShipResponse getMentorshipById(Long id) {

        MentorShip mentorShip = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentorship with id : " + id + " does not exist"));

        return mapToResponse(mentorShip);
    }

    public MentorShipResponse updateMentorshipStatus(Long id, String status) {

        MentorShip mentorShip = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentorship with id : " + id + " does not exist"));

        MentorShip.MentorShipStatus newStatus =
                MentorShip.MentorShipStatus.valueOf(status.toUpperCase());

        mentorShip.setStatus(newStatus);

        MentorShip updatedMentorShip = repo.save(mentorShip);

        return mapToResponse(updatedMentorShip);
    }

    public MentorShipResponse acceptMentorship(Long id) {

        MentorShip mentorShip = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentorship with id : " + id + " does not exist"));

        boolean alreadyHasMentor = repo.existsByStudentIdAndStatus(
                mentorShip.getStudentId(),
                MentorShip.MentorShipStatus.ACCEPTED
        );

        if (alreadyHasMentor) {
            throw new RuntimeException("This student already has an accepted mentor");
        }

        mentorShip.setStatus(MentorShip.MentorShipStatus.ACCEPTED);

        MentorShip updatedMentorShip = repo.save(mentorShip);
        notificationService.createNotification(
                updatedMentorShip.getStudentId(),
                Notification.UserRole.STUDENT,
                "Mentorship Accepted",
                "Your mentorship request has been accepted."
        );

        return mapToResponse(updatedMentorShip);
    }

    public MentorShipResponse rejectMentorship(Long id) {

        MentorShip mentorShip = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentorship with id : " + id + " does not exist"));

        if (mentorShip.getStatus() == MentorShip.MentorShipStatus.ACCEPTED) {
            throw new RuntimeException("Accepted mentorship cannot be rejected");
        }

        mentorShip.setStatus(MentorShip.MentorShipStatus.REJECTED);

        MentorShip updatedMentorShip = repo.save(mentorShip);

        return mapToResponse(updatedMentorShip);
    }

    public void deleteMentorship(Long id) {

        MentorShip mentorShip = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentorship with id : " + id + " does not exist"));

        repo.delete(mentorShip);
    }

    public List<MentorShipResponse> getMentorshipsByStudentId(Long studentId) {

        return repo.findByStudentId(studentId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<MentorShipResponse> getMentorshipsByAlumniId(Long alumniId) {

        return repo.findByAlumniId(alumniId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private MentorShipResponse mapToResponse(MentorShip mentorShip) {

        return new MentorShipResponse(
                mentorShip.getId(),
                mentorShip.getStudentId(),
                mentorShip.getAlumniId(),
                mentorShip.getMessage(),
                mentorShip.getStatus().name()
        );
    }
}