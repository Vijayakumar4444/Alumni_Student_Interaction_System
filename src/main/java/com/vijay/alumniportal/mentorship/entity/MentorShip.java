package com.vijay.alumniportal.mentorship.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class MentorShip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;
    private Long alumniId;
    private String message;
    @Enumerated(EnumType.STRING)
    private MentorShipStatus status;

    public enum MentorShipStatus {
        PENDING,
        ACCEPTED,
        REJECTED
    }
}
