package com.vijay.alumniportal.forum.repository;

import com.vijay.alumniportal.forum.entity.ForumQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumQuestionRepository extends JpaRepository<ForumQuestion, Long> {

    List<ForumQuestion> findByStudentId(Long studentId);

    List<ForumQuestion> findByTitleContainingIgnoreCase(String keyword);
}