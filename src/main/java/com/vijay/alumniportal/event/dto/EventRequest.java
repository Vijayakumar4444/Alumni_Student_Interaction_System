package com.vijay.alumniportal.event.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventRequest {

    private Long alumniId;

    private String title;
    private String description;
    private String eventType;

    private String eventDate;
    private String eventTime;

    private String mode;
    private String venueOrLink;

    private String requiredSkills;

    private Integer maxSeats;
}