package com.vijay.alumniportal.message.controller;

import com.vijay.alumniportal.message.dto.PrivateMessageResponse;
import com.vijay.alumniportal.message.dto.SendPrivateMessageRequest;
import com.vijay.alumniportal.message.service.PrivateMessageService;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class PrivateMessageSocketController {

    private final PrivateMessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    public PrivateMessageSocketController(
            PrivateMessageService messageService,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/messages.send")
    @SendToUser("/queue/private-messages")
    public PrivateMessageResponse sendMessage(
            @Valid SendPrivateMessageRequest request,
            Principal principal
    ) {
        if (!(principal instanceof Authentication authentication)) {
            throw new IllegalStateException(
                    "Authenticated WebSocket connection required"
            );
        }

        PrivateMessageService.MessageDelivery delivery =
                messageService.sendMessage(
                        request,
                        authentication
                );


        messagingTemplate.convertAndSendToUser(
                delivery.recipientEmail(),
                "/queue/private-messages",
                delivery.message()
        );


        return delivery.message();
    }
}