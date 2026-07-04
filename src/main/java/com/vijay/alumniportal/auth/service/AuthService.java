package com.vijay.alumniportal.auth.service;

import com.vijay.alumniportal.alumni.entity.Alumni;
import com.vijay.alumniportal.alumni.repository.AlumniRepository;
import com.vijay.alumniportal.auth.dto.AuthResponse;
import com.vijay.alumniportal.auth.dto.LoginRequest;
import com.vijay.alumniportal.auth.dto.SignupRequest;
import com.vijay.alumniportal.auth.entity.User;
import com.vijay.alumniportal.auth.repository.UserRepository;
import com.vijay.alumniportal.student.entity.Student;
import com.vijay.alumniportal.student.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.vijay.alumniportal.security.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final AlumniRepository alumniRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            StudentRepository studentRepository,
            AlumniRepository alumniRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.alumniRepository = alumniRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse signup(SignupRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User.Role role = User.Role.valueOf(request.getRole().toUpperCase());

        Long profileId;

        if (role == User.Role.STUDENT) {

            Student student = Student.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .department(request.getDepartment())
                    .year(request.getYear())
                    .skills(request.getSkills())
                    .build();

            Student savedStudent = studentRepository.save(student);
            profileId = savedStudent.getId();

        } else if (role == User.Role.ALUMNI) {

            Alumni alumni = Alumni.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .company(request.getCompany())
                    .designation(request.getDesignation())
                    .experience(request.getExperience())
                    .skills(request.getSkills())
                    .build();

            Alumni savedAlumni = alumniRepository.save(alumni);
            profileId = savedAlumni.getId();

        } else {
            throw new RuntimeException("Invalid role");
        }

        User user = User.builder()
                .profileId(profileId)
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password")
                );

        return mapToResponse(user);
    }

    private AuthResponse mapToResponse(User user) {

        String redirectTo;

        if (user.getRole() == User.Role.STUDENT) {
            redirectTo = "/student/dashboard";
        } else {
            redirectTo = "/alumni/dashboard";
        }
        String token = jwtService.generateToken(user);

        return new AuthResponse(
                user.getId(),
                user.getProfileId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                redirectTo,
                token,
                "Bearer"
        );
    }
}