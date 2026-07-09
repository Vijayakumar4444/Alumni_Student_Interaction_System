package com.vijay.alumniportal.message.entity;

import com.vijay.alumniportal.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "private_messages",
        indexes = {
                @Index(
                        name = "idx_message_mentorship_time",
                        columnList = "mentorship_id, sent_at"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrivateMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mentorship_id", nullable = false)
    private Long mentorshipId;

    @Column(name = "sender_user_id", nullable = false)
    private Long senderUserId;

    @Column(name = "sender_profile_id", nullable = false)
    private Long senderProfileId;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_role", nullable = false)
    private User.Role senderRole;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @PrePersist
    public void beforeInsert() {
        if (sentAt == null) {
            sentAt = LocalDateTime.now();
        }
    }
}