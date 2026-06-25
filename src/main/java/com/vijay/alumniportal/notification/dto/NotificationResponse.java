package com.vijay.alumniportal.notification.dto;

import com.vijay.alumniportal.notification.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationResponse {

    private Long id;
    private Long userId;
    private Notification.UserRole role;
    private String title;
    private String message;
    private Boolean isRead;
    private String createdAt;
}