package com.vijay.alumniportal.forum.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForumQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;

    private String title;

    @Column(length = 2000)
    private String description;

    private Integer likeCount;

    @Enumerated(EnumType.STRING)
    private QuestionStatus status;

    public enum QuestionStatus {
        OPEN,
        ANSWERED
    }
}