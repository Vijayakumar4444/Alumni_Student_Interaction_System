package com.vijay.alumniportal.forum.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForumLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long questionId;

    private Long likedById;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    public enum UserType {
        STUDENT,
        ALUMNI
    }
}