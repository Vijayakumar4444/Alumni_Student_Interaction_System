package com.vijay.alumniportal.mentorship.controller;

import com.vijay.alumniportal.mentorship.dto.MentorShipRequest;
import com.vijay.alumniportal.mentorship.dto.MentorShipResponse;
import com.vijay.alumniportal.mentorship.service.MentorShipService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/mentorships")
public class MentorShipController {

    private final MentorShipService service;

    public MentorShipController(MentorShipService service) {
        this.service = service;
    }

    @PostMapping
    public MentorShipResponse createMentorship(@RequestBody MentorShipRequest request) {
        return service.createMentorship(request);
    }

    @GetMapping
    public List<MentorShipResponse> getAllMentorships() {
        return service.getAllMentorships();
    }

    @GetMapping("/{id}")
    public MentorShipResponse getMentorshipById(@PathVariable Long id) {
        return service.getMentorshipById(id);
    }

    @GetMapping("/student/{studentId}")
    public List<MentorShipResponse> getMentorshipsByStudentId(@PathVariable Long studentId) {
        return service.getMentorshipsByStudentId(studentId);
    }

    @GetMapping("/alumni/{alumniId}")
    public List<MentorShipResponse> getMentorshipsByAlumniId(@PathVariable Long alumniId) {
        return service.getMentorshipsByAlumniId(alumniId);
    }

    @PutMapping("/{id}/status")
    public MentorShipResponse updateMentorshipStatus(
            @PathVariable Long id,
            @RequestBody String status
    ) {
        return service.updateMentorshipStatus(id, status);
    }

    @PutMapping("/{id}/accept")
    public MentorShipResponse acceptMentorship(@PathVariable Long id) {
        return service.acceptMentorship(id);
    }

    @PutMapping("/{id}/reject")
    public MentorShipResponse rejectMentorship(@PathVariable Long id) {
        return service.rejectMentorship(id);
    }

    @DeleteMapping("/{id}")
    public String deleteMentorship(@PathVariable Long id) {
        service.deleteMentorship(id);
        return "Mentorship deleted successfully";
    }
}