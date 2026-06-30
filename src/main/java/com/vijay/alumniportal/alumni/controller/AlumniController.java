package com.vijay.alumniportal.alumni.controller;

import com.vijay.alumniportal.alumni.dto.AlumniRequest;
import com.vijay.alumniportal.alumni.dto.AlumniResponse;
import com.vijay.alumniportal.alumni.service.AlumniService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/alumni")
public class AlumniController {

    private final AlumniService service;

    public AlumniController(AlumniService service) {
        this.service = service;
    }

    @PostMapping
    public AlumniResponse createAlumni(@RequestBody AlumniRequest request) {
        return service.createAlumni(request);
    }

    @GetMapping
    public List<AlumniResponse> getAllAlumni() {
        return service.getAllAlumni();
    }

    @GetMapping("/{id}")
    public AlumniResponse getAlumniById(@PathVariable Long id) {
        return service.getAlumniById(id);
    }

    @PutMapping("/{id}")
    public AlumniResponse updateAlumni(
            @PathVariable Long id,
            @RequestBody AlumniRequest request
    ) {
        return service.updateAlumni(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteAlumni(@PathVariable Long id) {
        service.deleteAlumni(id);
        return "Alumni deleted successfully";
    }
    @PostMapping("/{id}/profile-image")
    public AlumniResponse uploadProfileImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile image
    ) {
        return service.uploadProfileImage(id, image);
    }
}