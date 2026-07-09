package com.vijay.alumniportal.message.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PrivateMessageResponse {

    private Long id;
    private Long mentorshipId;
    private Long senderUserId;
    private Long senderProfileId;
    private String senderRole;
    private String content;
    private LocalDateTime sentAt;
    private LocalDateTime readAt;
}