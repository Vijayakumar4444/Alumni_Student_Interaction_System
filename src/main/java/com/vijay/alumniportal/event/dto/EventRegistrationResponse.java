package com.vijay.alumniportal.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventRegistrationResponse {

    private Long id;

    private Long eventId;
    private String eventTitle;

    private Long studentId;
    private String studentName;

    private String registeredAt;
}