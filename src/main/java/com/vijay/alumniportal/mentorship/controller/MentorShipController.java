package com.vijay.alumniportal.mentorship.controller;

import com.vijay.alumniportal.mentorship.dto.MentorShipRequest;
import com.vijay.alumniportal.mentorship.dto.MentorShipResponse;
import com.vijay.alumniportal.mentorship.service.MentorShipService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentorships")
public class MentorShipController {
    private MentorShipService service;

    public MentorShipController(MentorShipService service){
        this.service=service;
    }
    @PostMapping
    public MentorShipResponse createMentorship(@RequestBody MentorShipRequest request){
        return this.service.createMentorship(request);
    }
    @GetMapping
    public List<MentorShipResponse> getAllMentorships(){
        return this.service.getAllMentorships();
    }
    @GetMapping("/{id}")
    public MentorShipResponse getMentorshipById(@PathVariable Long id){
        return this.service.getMentorshipById(id);
    }
    @GetMapping("/student/{studentId}")
    public List<MentorShipResponse> getMentorshipsByStudentId(@PathVariable Long studentId) {
        return service.getMentorshipsByStudentId(studentId);
    }
    @GetMapping("/alumni/{alumniId}")
    public List<MentorShipResponse> getMentorshipsByAlumniId(@PathVariable Long alumniId) {
        return service.getMentorshipsByAlumniId(alumniId);
    }
    @PutMapping("/{id}")
    public MentorShipResponse updateMentorshipStatus(@PathVariable Long id,@RequestBody String status){
        return this.service.updateMentorshipStatus(id,status);
    }
    @DeleteMapping("/{id}")
    public String deleteMentorship(@PathVariable Long id){
        this.service.deleteMentorship(id);
        return "Mentorship deleted successfully";
    }


}
