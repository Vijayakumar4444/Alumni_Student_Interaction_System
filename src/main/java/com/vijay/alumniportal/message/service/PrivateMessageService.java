package com.vijay.alumniportal.message.service;

import com.vijay.alumniportal.auth.entity.User;
import com.vijay.alumniportal.auth.repository.UserRepository;
import com.vijay.alumniportal.mentorship.entity.MentorShip;
import com.vijay.alumniportal.mentorship.repository.MentorShipRepository;
import com.vijay.alumniportal.message.dto.PrivateMessageResponse;
import com.vijay.alumniportal.message.dto.SendPrivateMessageRequest;
import com.vijay.alumniportal.message.entity.PrivateMessage;
import com.vijay.alumniportal.message.repository.PrivateMessageRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.vijay.alumniportal.message.dto.ConversationSummaryResponse;
import java.util.Comparator;

import java.util.List;

@Service
public class PrivateMessageService {

    private final PrivateMessageRepository messageRepository;
    private final MentorShipRepository mentorShipRepository;
    private final UserRepository userRepository;

    public PrivateMessageService(
            PrivateMessageRepository messageRepository,
            MentorShipRepository mentorShipRepository,
            UserRepository userRepository
    ) {
        this.messageRepository = messageRepository;
        this.mentorShipRepository = mentorShipRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public MessageDelivery sendMessage(
            SendPrivateMessageRequest request,
            Authentication authentication
    ) {
        CurrentUser currentUser = getCurrentUser(authentication);

        MentorShip mentorship = getAuthorizedMentorship(
                request.getMentorshipId(),
                currentUser
        );

        User recipient = findRecipient(mentorship, currentUser.role());

        PrivateMessage message = PrivateMessage.builder()
                .mentorshipId(mentorship.getId())
                .senderUserId(currentUser.userId())
                .senderProfileId(currentUser.profileId())
                .senderRole(currentUser.role())
                .content(request.getContent().trim())
                .build();

        PrivateMessage savedMessage = messageRepository.save(message);

        return new MessageDelivery(
                mapToResponse(savedMessage),
                recipient.getEmail()
        );
    }

    @Transactional(readOnly = true)
    public List<PrivateMessageResponse> getHistory(
            Long mentorshipId,
            Authentication authentication
    ) {
        CurrentUser currentUser = getCurrentUser(authentication);

        getAuthorizedMentorship(mentorshipId, currentUser);

        return messageRepository
                .findByMentorshipIdOrderBySentAtAsc(mentorshipId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private MentorShip getAuthorizedMentorship(
            Long mentorshipId,
            CurrentUser currentUser
    ) {
        MentorShip mentorship = mentorShipRepository
                .findById(mentorshipId)
                .orElseThrow(() ->
                        new RuntimeException("Mentorship not found")
                );

        if (mentorship.getStatus()
                != MentorShip.MentorShipStatus.ACCEPTED) {
            throw new AccessDeniedException(
                    "Private messaging is available only for accepted mentorships"
            );
        }

        boolean authorizedStudent =
                currentUser.role() == User.Role.STUDENT
                        && currentUser.profileId()
                        .equals(mentorship.getStudentId());

        boolean authorizedAlumni =
                currentUser.role() == User.Role.ALUMNI
                        && currentUser.profileId()
                        .equals(mentorship.getAlumniId());

        if (!authorizedStudent && !authorizedAlumni) {
            throw new AccessDeniedException(
                    "You are not a member of this mentorship"
            );
        }

        return mentorship;
    }

    private User findRecipient(
            MentorShip mentorship,
            User.Role senderRole
    ) {
        Long recipientProfileId;
        User.Role recipientRole;

        if (senderRole == User.Role.STUDENT) {
            recipientProfileId = mentorship.getAlumniId();
            recipientRole = User.Role.ALUMNI;
        } else {
            recipientProfileId = mentorship.getStudentId();
            recipientRole = User.Role.STUDENT;
        }

        return userRepository
                .findByProfileIdAndRole(
                        recipientProfileId,
                        recipientRole
                )
                .orElseThrow(() ->
                        new RuntimeException("Recipient user not found")
                );
    }

    private CurrentUser getCurrentUser(
            Authentication authentication
    ) {
        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            throw new AccessDeniedException("JWT authentication required");
        }

        Jwt jwt = jwtAuth.getToken();

        Number userId = jwt.getClaim("userId");
        Number profileId = jwt.getClaim("profileId");
        String role = jwt.getClaimAsString("role");

        return new CurrentUser(
                userId.longValue(),
                profileId.longValue(),
                User.Role.valueOf(role)
        );
    }
    @Transactional(readOnly = true)
    public List<ConversationSummaryResponse> getConversations(
            Authentication authentication
    ) {
        CurrentUser currentUser = getCurrentUser(authentication);

        List<MentorShip> mentorships;

        if (currentUser.role() == User.Role.STUDENT) {
            mentorships =
                    mentorShipRepository.findByStudentIdAndStatus(
                            currentUser.profileId(),
                            MentorShip.MentorShipStatus.ACCEPTED
                    );
        } else {
            mentorships =
                    mentorShipRepository.findByAlumniIdAndStatus(
                            currentUser.profileId(),
                            MentorShip.MentorShipStatus.ACCEPTED
                    );
        }

        return mentorships.stream()
                .map(mentorship ->
                        createConversationSummary(
                                mentorship,
                                currentUser
                        )
                )
                .sorted(
                        Comparator.comparing(
                                ConversationSummaryResponse
                                        ::getLatestMessageTime,
                                Comparator.nullsLast(
                                        Comparator.reverseOrder()
                                )
                        )
                )
                .toList();
    }

    @Transactional
    public void markConversationAsRead(
            Long mentorshipId,
            Authentication authentication
    ) {
        CurrentUser currentUser = getCurrentUser(authentication);

        getAuthorizedMentorship(
                mentorshipId,
                currentUser
        );

        messageRepository.markReceivedMessagesAsRead(
                mentorshipId,
                currentUser.userId()
        );
    }

    private ConversationSummaryResponse createConversationSummary(
            MentorShip mentorship,
            CurrentUser currentUser
    ) {
        Long otherProfileId;
        User.Role otherRole;

        if (currentUser.role() == User.Role.STUDENT) {
            otherProfileId = mentorship.getAlumniId();
            otherRole = User.Role.ALUMNI;
        } else {
            otherProfileId = mentorship.getStudentId();
            otherRole = User.Role.STUDENT;
        }

        User otherUser = userRepository
                .findByProfileIdAndRole(
                        otherProfileId,
                        otherRole
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Conversation user not found"
                        )
                );

        PrivateMessage latestMessage = messageRepository
                .findFirstByMentorshipIdOrderBySentAtDesc(
                        mentorship.getId()
                )
                .orElse(null);

        long unreadCount = messageRepository
                .countByMentorshipIdAndSenderUserIdNotAndReadAtIsNull(
                        mentorship.getId(),
                        currentUser.userId()
                );

        return new ConversationSummaryResponse(
                mentorship.getId(),
                otherProfileId,
                otherUser.getName(),
                otherRole.name(),
                latestMessage == null
                        ? null
                        : latestMessage.getContent(),
                latestMessage == null
                        ? null
                        : latestMessage.getSentAt(),
                unreadCount
        );
    }

    private PrivateMessageResponse mapToResponse(
            PrivateMessage message
    ) {
        return new PrivateMessageResponse(
                message.getId(),
                message.getMentorshipId(),
                message.getSenderUserId(),
                message.getSenderProfileId(),
                message.getSenderRole().name(),
                message.getContent(),
                message.getSentAt(),
                message.getReadAt()
        );
    }

    public record MessageDelivery(
            PrivateMessageResponse message,
            String recipientEmail
    ) {
    }

    private record CurrentUser(
            Long userId,
            Long profileId,
            User.Role role
    ) {
    }
}