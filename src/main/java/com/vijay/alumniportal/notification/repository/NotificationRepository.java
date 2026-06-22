package com.vijay.alumniportal.notification.repository;

import com.vijay.alumniportal.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdAndRoleOrderByIdDesc(
            Long userId,
            Notification.UserRole role
    );

    List<Notification> findByUserIdAndRoleAndIsReadFalse(
            Long userId,
            Notification.UserRole role
    );
}