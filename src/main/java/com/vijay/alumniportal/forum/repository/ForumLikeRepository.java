package com.vijay.alumniportal.forum.repository;

import com.vijay.alumniportal.forum.entity.ForumLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumLikeRepository extends JpaRepository<ForumLike, Long> {

    boolean existsByQuestionIdAndLikedByIdAndUserType(
            Long questionId,
            Long likedById,
            ForumLike.UserType userType
    );
}