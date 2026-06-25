package com.vijay.alumniportal.dashboard.controller;

import com.vijay.alumniportal.dashboard.dto.AlumniDashboardResponse;
import com.vijay.alumniportal.dashboard.dto.StudentDashboardResponse;
import com.vijay.alumniportal.dashboard.service.DashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping("/student/{studentId}")
    public StudentDashboardResponse getStudentDashboard(@PathVariable Long studentId) {
        return service.getStudentDashboard(studentId);
    }

    @GetMapping("/alumni/{alumniId}")
    public AlumniDashboardResponse getAlumniDashboard(@PathVariable Long alumniId) {
        return service.getAlumniDashboard(alumniId);
    }
}