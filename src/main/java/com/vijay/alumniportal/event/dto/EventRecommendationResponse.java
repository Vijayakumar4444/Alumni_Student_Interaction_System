package com.vijay.alumniportal.event.dto;

import com.vijay.alumniportal.event.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventRecommendationResponse {

    private Long eventId;
    private String title;
    private String requiredSkills;

    private Integer matchScore;
    private String reason;

    private Event.EventStatus status;
    private Boolean canApply;
}