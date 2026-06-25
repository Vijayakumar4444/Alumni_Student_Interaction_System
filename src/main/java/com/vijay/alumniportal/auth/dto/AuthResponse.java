package com.vijay.alumniportal.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private Long userId;
    private Long profileId;
    private String name;
    private String email;
    private String role;
    private String redirectTo;
}