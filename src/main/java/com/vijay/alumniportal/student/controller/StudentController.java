package com.vijay.alumniportal.student.controller;

import com.vijay.alumniportal.student.dto.StudentRequest;
import com.vijay.alumniportal.student.dto.StudentResponse;
import com.vijay.alumniportal.student.service.StudentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    public StudentResponse createStudent(@RequestBody StudentRequest request) {
        return service.createStudent(request);
    }

    @GetMapping
    public List<StudentResponse> getAllStudents() {
        return service.getAllStudents();
    }

    @GetMapping("/{id}")
    public StudentResponse getStudentById(@PathVariable Long id) {
        return service.getStudentById(id);
    }

    @PutMapping("/{id}")
    public StudentResponse updateStudent(
            @PathVariable Long id,
            @RequestBody StudentRequest request
    ) {
        return service.updateStudent(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {
        service.deleteStudent(id);
        return "Student deleted successfully";
    }

    @PostMapping("/{id}/profile-image")
    public StudentResponse uploadProfileImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile image
    ) {
        return service.uploadProfileImage(id, image);
    }

    @PostMapping("/{id}/resume")
    public StudentResponse uploadResume(
            @PathVariable Long id,
            @RequestParam("resume") MultipartFile resume
    ) {
        return service.uploadResume(id, resume);
    }
}