package com.vijay.alumniportal.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeResponse {

    private String message;

    private Integer totalLikes;
}