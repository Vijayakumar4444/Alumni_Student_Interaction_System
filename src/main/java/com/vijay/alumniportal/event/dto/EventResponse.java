package com.vijay.alumniportal.event.dto;

import com.vijay.alumniportal.event.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventResponse {

    private Long id;
    private Long alumniId;

    private String title;
    private String description;
    private String eventType;

    private String eventDate;
    private String eventTime;

    private String mode;
    private String venueOrLink;

    private String requiredSkills;
    private String imageUrl;
    private Integer maxSeats;
    private Integer registeredCount;
    private Integer availableSeats;

    private Event.EventStatus status;

    private Boolean canApply;
}