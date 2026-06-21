package com.vijay.alumniportal.student.controller;

import com.vijay.alumniportal.student.dto.StudentRequest;
import com.vijay.alumniportal.student.dto.StudentResponse;
import com.vijay.alumniportal.student.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
}