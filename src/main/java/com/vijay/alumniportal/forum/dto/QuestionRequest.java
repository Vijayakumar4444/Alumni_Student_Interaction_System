package com.vijay.alumniportal.forum.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionRequest {

    private Long studentId;

    private String title;

    private String description;
}