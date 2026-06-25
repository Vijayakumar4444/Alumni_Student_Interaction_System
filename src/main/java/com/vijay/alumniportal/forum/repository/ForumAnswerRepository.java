package com.vijay.alumniportal.forum.repository;

import com.vijay.alumniportal.forum.entity.ForumAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumAnswerRepository extends JpaRepository<ForumAnswer, Long> {

    List<ForumAnswer> findByQuestionId(Long questionId);

    List<ForumAnswer> findByAlumniId(Long alumniId);
}