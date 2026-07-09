package com.vijay.alumniportal.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendPrivateMessageRequest {

    @NotNull
    private Long mentorshipId;

    @NotBlank
    @Size(max = 2000)
    private String content;
}