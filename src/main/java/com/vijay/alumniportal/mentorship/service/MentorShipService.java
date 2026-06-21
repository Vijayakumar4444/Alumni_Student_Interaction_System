package com.vijay.alumniportal.mentorship.service;

import com.vijay.alumniportal.mentorship.dto.MentorShipRequest;
import com.vijay.alumniportal.mentorship.dto.MentorShipResponse;
import com.vijay.alumniportal.mentorship.entity.MentorShip;
import com.vijay.alumniportal.mentorship.repository.MentorShipRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MentorShipService {
    private final MentorShipRepository repo;


    public MentorShipService(MentorShipRepository repo) {
        this.repo = repo;
    }
    public MentorShipResponse createMentorship(MentorShipRequest request) {

        MentorShip mentorShip = MentorShip.builder()
                .studentId(request.getStudentId())
                .alumniId(request.getAlumniId())
                .message(request.getMessage())
                .status("PENDING")
                .build();

        MentorShip savedMentorShip = repo.save(mentorShip);
        return mapToResponse(savedMentorShip);
    }
    public List<MentorShipResponse> getAllMentorships(){

        return repo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    public MentorShipResponse getMentorshipById(Long id){
        MentorShip mentorShip= repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentorship with id : "+id+"does not exist"));
        return mapToResponse(mentorShip);
    }
    public MentorShipResponse updateMentorshipStatus(Long id,String status){
        MentorShip mentorShip= repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentorship with id : "+id+" does not exist"));
        mentorShip.setStatus(status);
        MentorShip updatedMentorShip = repo.save(mentorShip);
        return mapToResponse(updatedMentorShip);
    }
    public void deleteMentorship(Long id){
        MentorShip mentorShip= repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentorship with id : "+id+" does not exist"));
        repo.delete(mentorShip);
    }
    public List<MentorShipResponse> getMentorshipsByStudentId(Long studentId) {

        List<MentorShip> mentorShips = repo.findByStudentId(studentId);
        return mentorShips.stream()
                .map(this::mapToResponse)
                .toList();
    }
    public List<MentorShipResponse> getMentorshipsByAlumniId(Long alumniId) {

        List<MentorShip> mentorShips = repo.findByAlumniId(alumniId);
        return mentorShips.stream()
                .map(this::mapToResponse)
                .toList();
    }
    private MentorShipResponse mapToResponse(MentorShip mentorShip) {
        return new MentorShipResponse(
                mentorShip.getId(),
                mentorShip.getStudentId(),
                mentorShip.getAlumniId(),
                mentorShip.getMessage(),
                mentorShip.getStatus()
        );
    }
}
