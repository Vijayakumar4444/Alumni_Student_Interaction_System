package com.vijay.alumniportal.event.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private Integer maxSeats;
    private Integer registeredCount;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    public enum EventStatus {
        OPEN,
        FULL,
        CLOSED
    }
}