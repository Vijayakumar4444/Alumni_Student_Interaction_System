package com.vijay.alumniportal.message.repository;

import com.vijay.alumniportal.message.entity.PrivateMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PrivateMessageRepository
        extends JpaRepository<PrivateMessage, Long> {

    List<PrivateMessage>
    findByMentorshipIdOrderBySentAtAsc(Long mentorshipId);

    Optional<PrivateMessage>
    findFirstByMentorshipIdOrderBySentAtDesc(
            Long mentorshipId
    );

    long countByMentorshipIdAndSenderUserIdNotAndReadAtIsNull(
            Long mentorshipId,
            Long currentUserId
    );

    @Modifying
    @Query("""
        UPDATE PrivateMessage message
        SET message.readAt = CURRENT_TIMESTAMP
        WHERE message.mentorshipId = :mentorshipId
          AND message.senderUserId <> :currentUserId
          AND message.readAt IS NULL
    """)
    int markReceivedMessagesAsRead(
            @Param("mentorshipId") Long mentorshipId,
            @Param("currentUserId") Long currentUserId
    );
}