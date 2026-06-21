package com.vijay.alumniportal.mentorship.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MentorShipRequest {
    private Long studentId;
    private Long alumniId;
    private String message;
    private String status;
}
