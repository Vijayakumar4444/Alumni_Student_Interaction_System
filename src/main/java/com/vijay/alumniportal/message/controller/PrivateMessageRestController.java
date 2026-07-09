package com.vijay.alumniportal.message.controller;

import com.vijay.alumniportal.message.dto.PrivateMessageResponse;
import com.vijay.alumniportal.message.service.PrivateMessageService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.vijay.alumniportal.message.dto.ConversationSummaryResponse;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:5173")
public class PrivateMessageRestController {

    private final PrivateMessageService messageService;

    public PrivateMessageRestController(
            PrivateMessageService messageService
    ) {
        this.messageService = messageService;
    }

    @GetMapping("/mentorship/{mentorshipId}")
    public List<PrivateMessageResponse> getHistory(
            @PathVariable Long mentorshipId,
            Authentication authentication
    ) {
        return messageService.getHistory(
                mentorshipId,
                authentication
        );
    }
    @GetMapping("/conversations")
    public List<ConversationSummaryResponse> getConversations(
            Authentication authentication
    ) {
        return messageService.getConversations(authentication);
    }

    @PutMapping("/mentorship/{mentorshipId}/read")
    public void markConversationAsRead(
            @PathVariable Long mentorshipId,
            Authentication authentication
    ) {
        messageService.markConversationAsRead(
                mentorshipId,
                authentication
        );
    }
}