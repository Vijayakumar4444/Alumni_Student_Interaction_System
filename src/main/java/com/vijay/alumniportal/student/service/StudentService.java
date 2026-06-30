package com.vijay.alumniportal.student.service;

import com.vijay.alumniportal.student.dto.StudentRequest;
import com.vijay.alumniportal.student.dto.StudentResponse;
import com.vijay.alumniportal.student.entity.Student;
import com.vijay.alumniportal.student.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public StudentResponse createStudent(StudentRequest request) {
        Student student = Student.builder()
                .name(request.getName())
                .email(request.getEmail())
                .department(request.getDepartment())
                .year(request.getYear())
                .skills(request.getSkills())
                .build();

        Student savedStudent = repository.save(student);

        return mapToResponse(savedStudent);
    }

    public List<StudentResponse> getAllStudents() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public StudentResponse getStudentById(Long id) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        return mapToResponse(student);
    }

    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setDepartment(request.getDepartment());
        student.setYear(request.getYear());
        student.setSkills(request.getSkills());

        Student updatedStudent = repository.save(student);

        return mapToResponse(updatedStudent);
    }

    public void deleteStudent(Long id) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        repository.delete(student);
    }

    private StudentResponse mapToResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getDepartment(),
                student.getYear(),
                student.getSkills(),
                student.getProfileImage()
        );
    }
    public StudentResponse uploadProfileImage(Long id, MultipartFile image) {
        try {
            Student student = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

            String folderPath = "uploads/profile-images/";
            File folder = new File(folderPath);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            String fileName = "student_" + id + "_" + System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(folderPath + fileName);

            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            student.setProfileImage("/uploads/profile-images/" + fileName);

            return mapToResponse(repository.save(student));

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload profile image");
        }
    }
}