package com.vijay.alumniportal.notification.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String title;

    @Column(length = 1000)
    private String message;

    private Boolean isRead;

    private String createdAt;

    public enum UserRole {
        STUDENT,
        ALUMNI
    }
}