package com.vijay.alumniportal.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnswerResponse {

    private Long id;

    private Long questionId;

    private Long alumniId;

    private String answer;
}