package com.vijay.alumniportal.forum.controller;

import com.vijay.alumniportal.forum.dto.*;
import com.vijay.alumniportal.forum.service.ForumService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/forum")
public class ForumController {

    private final ForumService service;

    public ForumController(ForumService service) {
        this.service = service;
    }

    @PostMapping("/questions")
    public QuestionResponse createQuestion(@RequestBody QuestionRequest request) {
        return service.createQuestion(request);
    }

    @GetMapping("/questions")
    public List<QuestionResponse> getAllQuestions() {
        return service.getAllQuestions();
    }

    @GetMapping("/questions/{id}")
    public QuestionResponse getQuestionById(@PathVariable Long id) {
        return service.getQuestionById(id);
    }

    @GetMapping("/questions/student/{studentId}")
    public List<QuestionResponse> getQuestionsByStudentId(@PathVariable Long studentId) {
        return service.getQuestionsByStudentId(studentId);
    }

    @GetMapping("/questions/search")
    public List<QuestionResponse> searchQuestions(@RequestParam String keyword) {
        return service.searchQuestions(keyword);
    }

    @PostMapping("/questions/{questionId}/answers/{alumniId}")
    public AnswerResponse answerQuestion(
            @PathVariable Long questionId,
            @PathVariable Long alumniId,
            @RequestBody AnswerRequest request
    ) {
        return service.answerQuestion(questionId, alumniId, request);
    }

    @GetMapping("/questions/{questionId}/answers")
    public List<AnswerResponse> getAnswersByQuestionId(@PathVariable Long questionId) {
        return service.getAnswersByQuestionId(questionId);
    }

    @GetMapping("/answers/alumni/{alumniId}")
    public List<AnswerResponse> getAnswersByAlumniId(@PathVariable Long alumniId) {
        return service.getAnswersByAlumniId(alumniId);
    }

    @PostMapping("/questions/{questionId}/like/student/{studentId}")
    public LikeResponse likeQuestionByStudent(
            @PathVariable Long questionId,
            @PathVariable Long studentId
    ) {
        return service.likeQuestionByStudent(questionId, studentId);
    }

    @PostMapping("/questions/{questionId}/like/alumni/{alumniId}")
    public LikeResponse likeQuestionByAlumni(
            @PathVariable Long questionId,
            @PathVariable Long alumniId
    ) {
        return service.likeQuestionByAlumni(questionId, alumniId);
    }

    @PostMapping("/answers/{answerId}/helpful/student/{studentId}")
    public LikeResponse markHelpfulByStudent(
            @PathVariable Long answerId,
            @PathVariable Long studentId
    ) {
        return service.markAnswerHelpfulByStudent(answerId, studentId);
    }

    @PostMapping("/answers/{answerId}/helpful/alumni/{alumniId}")
    public LikeResponse markHelpfulByAlumni(
            @PathVariable Long answerId,
            @PathVariable Long alumniId
    ) {
        return service.markAnswerHelpfulByAlumni(answerId, alumniId);
    }
}