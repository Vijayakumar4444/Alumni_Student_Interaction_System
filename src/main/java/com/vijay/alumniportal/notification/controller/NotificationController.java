package com.vijay.alumniportal.notification.controller;

import com.vijay.alumniportal.notification.dto.NotificationResponse;
import com.vijay.alumniportal.notification.entity.Notification;
import com.vijay.alumniportal.notification.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://alumni-student-interaction-system-f.vercel.app"
})
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @GetMapping("/{role}/{userId}")
    public List<NotificationResponse> getNotifications(
            @PathVariable String role,
            @PathVariable Long userId
    ) {
        return service.getNotifications(
                userId,
                Notification.UserRole.valueOf(role.toUpperCase())
        );
    }

    @GetMapping("/{role}/{userId}/unread")
    public List<NotificationResponse> getUnreadNotifications(
            @PathVariable String role,
            @PathVariable Long userId
    ) {
        return service.getUnreadNotifications(
                userId,
                Notification.UserRole.valueOf(role.toUpperCase())
        );
    }

    @PutMapping("/{notificationId}/read")
    public NotificationResponse markAsRead(@PathVariable Long notificationId) {
        return service.markAsRead(notificationId);
    }

    @DeleteMapping("/{notificationId}")
    public String deleteNotification(@PathVariable Long notificationId) {
        service.deleteNotification(notificationId);
        return "Notification deleted successfully";
    }
}