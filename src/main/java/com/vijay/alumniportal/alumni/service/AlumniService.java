package com.vijay.alumniportal.alumni.service;

import com.vijay.alumniportal.alumni.dto.AlumniRequest;
import com.vijay.alumniportal.alumni.dto.AlumniResponse;
import com.vijay.alumniportal.alumni.entity.Alumni;
import com.vijay.alumniportal.alumni.repository.AlumniRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlumniService {

    private final AlumniRepository repository;

    public AlumniService(AlumniRepository repository) {
        this.repository = repository;
    }

    public AlumniResponse createAlumni(AlumniRequest request) {
        Alumni alumni = Alumni.builder()
                .name(request.getName())
                .email(request.getEmail())
                .company(request.getCompany())
                .designation(request.getDesignation())
                .experience(request.getExperience())
                .skills(request.getSkills())
                .build();

        Alumni savedAlumni = repository.save(alumni);

        return mapToResponse(savedAlumni);
    }

    public List<AlumniResponse> getAllAlumni() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AlumniResponse getAlumniById(Long id) {
        Alumni alumni = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumni not found with id: " + id));

        return mapToResponse(alumni);
    }

    public AlumniResponse updateAlumni(Long id, AlumniRequest request) {
        Alumni alumni = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumni not found with id: " + id));

        alumni.setName(request.getName());
        alumni.setEmail(request.getEmail());
        alumni.setCompany(request.getCompany());
        alumni.setDesignation(request.getDesignation());
        alumni.setExperience(request.getExperience());
        alumni.setSkills(request.getSkills());

        Alumni updatedAlumni = repository.save(alumni);

        return mapToResponse(updatedAlumni);
    }

    public void deleteAlumni(Long id) {
        Alumni alumni = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumni not found with id: " + id));

        repository.delete(alumni);
    }

    private AlumniResponse mapToResponse(Alumni alumni) {
        return new AlumniResponse(
                alumni.getId(),
                alumni.getName(),
                alumni.getEmail(),
                alumni.getCompany(),
                alumni.getDesignation(),
                alumni.getExperience(),
                alumni.getSkills()
        );
    }
}