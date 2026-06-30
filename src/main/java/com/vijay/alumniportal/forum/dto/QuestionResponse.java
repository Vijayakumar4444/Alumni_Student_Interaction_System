package com.vijay.alumniportal.forum.dto;

import com.vijay.alumniportal.forum.entity.ForumQuestion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionResponse {

    private Long id;

    private Long studentId;

    private String title;

    private String description;

    private Integer likeCount;

    private ForumQuestion.QuestionStatus status;

    private String studentName;
}