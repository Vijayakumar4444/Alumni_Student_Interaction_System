package com.vijay.alumniportal.event.dto;

import com.vijay.alumniportal.event.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventAnalyticsResponse {

    private Long eventId;
    private String title;

    private Integer maxSeats;
    private Integer registeredCount;
    private Integer availableSeats;

    private Double fillPercentage;

    private Event.EventStatus status;
}