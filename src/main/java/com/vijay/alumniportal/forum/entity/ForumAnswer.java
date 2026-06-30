package com.vijay.alumniportal.forum.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForumAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long questionId;

    private Long alumniId;

    @Column(length = 3000)
    private String answer;
    private Integer helpfulCount;
}