package com.vijay.alumniportal.notification.service;

import com.vijay.alumniportal.notification.dto.NotificationResponse;
import com.vijay.alumniportal.notification.entity.Notification;
import com.vijay.alumniportal.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public void createNotification(
            Long userId,
            Notification.UserRole role,
            String title,
            String message
    ) {
        Notification notification = Notification.builder()
                .userId(userId)
                .role(role)
                .title(title)
                .message(message)
                .isRead(false)
                .createdAt(LocalDateTime.now().toString())
                .build();

        repository.save(notification);
    }

    public List<NotificationResponse> getNotifications(Long userId, Notification.UserRole role) {
        return repository.findByUserIdAndRoleOrderByIdDesc(userId, role)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<NotificationResponse> getUnreadNotifications(Long userId, Notification.UserRole role) {
        return repository.findByUserIdAndRoleAndIsReadFalse(userId, role)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public NotificationResponse markAsRead(Long notificationId) {
        Notification notification = repository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));

        notification.setIsRead(true);

        return mapToResponse(repository.save(notification));
    }

    public void deleteNotification(Long notificationId) {
        Notification notification = repository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));

        repository.delete(notification);
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getUserId(),
                notification.getRole(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getIsRead(),
                notification.getCreatedAt()
        );
    }
}