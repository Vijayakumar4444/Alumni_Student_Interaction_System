package com.vijay.alumniportal.forum.dto;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForumHelpful {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long answerId;
    private Long markedById;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    public enum UserType {
        STUDENT,
        ALUMNI
    }
}
