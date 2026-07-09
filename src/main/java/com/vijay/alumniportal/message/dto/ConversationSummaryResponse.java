package com.vijay.alumniportal.message.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ConversationSummaryResponse {

    private Long mentorshipId;
    private Long otherPersonProfileId;
    private String otherPersonName;
    private String otherPersonRole;
    private String latestMessage;
    private LocalDateTime latestMessageTime;
    private long unreadCount;
}