package com.vijay.alumniportal.forum.repository;

import com.vijay.alumniportal.forum.dto.ForumHelpful;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumHelpfulRepository extends JpaRepository<ForumHelpful, Long> {

    boolean existsByAnswerIdAndMarkedByIdAndUserType(
            Long answerId,
            Long markedById,
            ForumHelpful.UserType userType
    );
}