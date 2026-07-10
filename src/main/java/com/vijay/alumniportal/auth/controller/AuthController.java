package com.vijay.alumniportal.auth.controller;

import com.vijay.alumniportal.auth.dto.AuthResponse;
import com.vijay.alumniportal.auth.dto.LoginRequest;
import com.vijay.alumniportal.auth.dto.SignupRequest;
import com.vijay.alumniportal.auth.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://alumni-student-interaction-system-f.vercel.app"
})
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/signup")
    public AuthResponse signup(@RequestBody SignupRequest request) {
        return service.signup(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return service.login(request);
    }
}