package com.vijay.alumniportal.forum.service;

import com.vijay.alumniportal.alumni.entity.Alumni;
import com.vijay.alumniportal.alumni.repository.AlumniRepository;
import com.vijay.alumniportal.forum.dto.*;
import com.vijay.alumniportal.forum.entity.ForumAnswer;
import com.vijay.alumniportal.forum.entity.ForumLike;
import com.vijay.alumniportal.forum.entity.ForumQuestion;
import com.vijay.alumniportal.forum.repository.ForumAnswerRepository;
import com.vijay.alumniportal.forum.repository.ForumHelpfulRepository;
import com.vijay.alumniportal.forum.repository.ForumLikeRepository;
import com.vijay.alumniportal.forum.repository.ForumQuestionRepository;
import com.vijay.alumniportal.student.entity.Student;
import com.vijay.alumniportal.student.repository.StudentRepository;
import org.springframework.stereotype.Service;
import com.vijay.alumniportal.notification.entity.Notification;
import com.vijay.alumniportal.notification.service.NotificationService;

import java.util.List;

@Service
public class ForumService {

    private final ForumQuestionRepository questionRepository;
    private final ForumAnswerRepository answerRepository;
    private final ForumLikeRepository likeRepository;
    private final StudentRepository studentRepository;
    private final AlumniRepository alumniRepository;
    private final NotificationService notificationService;
    private final ForumHelpfulRepository helpfulRepository;

    public ForumService(
            ForumQuestionRepository questionRepository,
            ForumAnswerRepository answerRepository,
            ForumLikeRepository likeRepository,
            StudentRepository studentRepository,
            AlumniRepository alumniRepository,
            NotificationService notificationService, ForumHelpfulRepository helpfulRepository
    ) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.likeRepository = likeRepository;
        this.studentRepository = studentRepository;
        this.alumniRepository = alumniRepository;
        this.notificationService = notificationService;
        this.helpfulRepository = helpfulRepository;
    }

    public QuestionResponse createQuestion(QuestionRequest request) {

        studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getStudentId()));

        ForumQuestion question = ForumQuestion.builder()
                .studentId(request.getStudentId())
                .title(request.getTitle())
                .description(request.getDescription())
                .likeCount(0)
                .status(ForumQuestion.QuestionStatus.OPEN)
                .build();

        return mapToQuestionResponse(questionRepository.save(question));
    }

    public List<QuestionResponse> getAllQuestions() {
        return questionRepository.findAll()
                .stream()
                .map(this::mapToQuestionResponse)
                .toList();
    }

    public QuestionResponse getQuestionById(Long id) {
        ForumQuestion question = getQuestionEntity(id);
        return mapToQuestionResponse(question);
    }

    public List<QuestionResponse> getQuestionsByStudentId(Long studentId) {

        studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        return questionRepository.findByStudentId(studentId)
                .stream()
                .map(this::mapToQuestionResponse)
                .toList();
    }

    public List<QuestionResponse> searchQuestions(String keyword) {
        return questionRepository.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(this::mapToQuestionResponse)
                .toList();
    }

    public AnswerResponse answerQuestion(Long questionId, Long alumniId, AnswerRequest request) {

        ForumQuestion question = getQuestionEntity(questionId);

        alumniRepository.findById(alumniId)
                .orElseThrow(() -> new RuntimeException("Alumni not found with id: " + alumniId));
        ForumAnswer answer = ForumAnswer.builder()
                .questionId(questionId)
                .alumniId(alumniId)
                .answer(request.getAnswer())
                .helpfulCount(0)
                .build();

        ForumAnswer savedAnswer = answerRepository.save(answer);

        question.setStatus(ForumQuestion.QuestionStatus.ANSWERED);
        questionRepository.save(question);
        notificationService.createNotification(
                question.getStudentId(),
                Notification.UserRole.STUDENT,
                "New Answer Received",
                "An alumni answered your forum question."
        );

        return mapToAnswerResponse(savedAnswer);
    }

    public List<AnswerResponse> getAnswersByQuestionId(Long questionId) {

        getQuestionEntity(questionId);

        return answerRepository.findByQuestionId(questionId)
                .stream()
                .map(this::mapToAnswerResponse)
                .toList();
    }

    public List<AnswerResponse> getAnswersByAlumniId(Long alumniId) {

        alumniRepository.findById(alumniId)
                .orElseThrow(() -> new RuntimeException("Alumni not found with id: " + alumniId));

        return answerRepository.findByAlumniId(alumniId)
                .stream()
                .map(this::mapToAnswerResponse)
                .toList();
    }

    public LikeResponse likeQuestionByStudent(Long questionId, Long studentId) {

        ForumQuestion question = getQuestionEntity(questionId);

        studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        boolean alreadyLiked = likeRepository.existsByQuestionIdAndLikedByIdAndUserType(
                questionId,
                studentId,
                ForumLike.UserType.STUDENT
        );

        if (alreadyLiked) {
            throw new RuntimeException("Student already liked this question");
        }

        ForumLike like = ForumLike.builder()
                .questionId(questionId)
                .likedById(studentId)
                .userType(ForumLike.UserType.STUDENT)
                .build();

        likeRepository.save(like);

        question.setLikeCount(question.getLikeCount() + 1);
        questionRepository.save(question);

        return new LikeResponse("Question liked successfully by student", question.getLikeCount());
    }

    public LikeResponse likeQuestionByAlumni(Long questionId, Long alumniId) {

        ForumQuestion question = getQuestionEntity(questionId);

        alumniRepository.findById(alumniId)
                .orElseThrow(() -> new RuntimeException("Alumni not found with id: " + alumniId));

        boolean alreadyLiked = likeRepository.existsByQuestionIdAndLikedByIdAndUserType(
                questionId,
                alumniId,
                ForumLike.UserType.ALUMNI
        );

        if (alreadyLiked) {
            throw new RuntimeException("Alumni already liked this question");
        }

        ForumLike like = ForumLike.builder()
                .questionId(questionId)
                .likedById(alumniId)
                .userType(ForumLike.UserType.ALUMNI)
                .build();

        likeRepository.save(like);

        question.setLikeCount(question.getLikeCount() + 1);
        questionRepository.save(question);

        return new LikeResponse("Question liked successfully by alumni", question.getLikeCount());
    }

    private ForumQuestion getQuestionEntity(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));
    }

    private QuestionResponse mapToQuestionResponse(ForumQuestion question) {

        Student student = studentRepository.findById(question.getStudentId())
                .orElse(null);

        String studentName = student != null ? student.getName() : "Unknown";

        return new QuestionResponse(
                question.getId(),
                question.getStudentId(),
                question.getTitle(),
                question.getDescription(),
                question.getLikeCount(),
                question.getStatus(),
                studentName
        );
    }

    private AnswerResponse mapToAnswerResponse(ForumAnswer answer) {

        Alumni alumni = alumniRepository.findById(answer.getAlumniId())
                .orElse(null);

        String alumniName = alumni != null ? alumni.getName() : "Unknown";

        return new AnswerResponse(
                answer.getId(),
                answer.getQuestionId(),
                answer.getAlumniId(),
                answer.getAnswer(),
                answer.getHelpfulCount(),
                alumniName
        );
    }
    public LikeResponse markAnswerHelpfulByStudent(Long answerId, Long studentId) {
        ForumAnswer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found with id: " + answerId));

        studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        boolean alreadyMarked = helpfulRepository.existsByAnswerIdAndMarkedByIdAndUserType(
                answerId,
                studentId,
                ForumHelpful.UserType.STUDENT
        );

        if (alreadyMarked) {
            throw new RuntimeException("Student already marked this answer helpful");
        }

        helpfulRepository.save(
                ForumHelpful.builder()
                        .answerId(answerId)
                        .markedById(studentId)
                        .userType(ForumHelpful.UserType.STUDENT)
                        .build()
        );

        answer.setHelpfulCount(answer.getHelpfulCount() + 1);
        answerRepository.save(answer);

        return new LikeResponse("Answer marked helpful by student", answer.getHelpfulCount());
    }
    public LikeResponse markAnswerHelpfulByAlumni(Long answerId, Long alumniId) {
        ForumAnswer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found with id: " + answerId));

        alumniRepository.findById(alumniId)
                .orElseThrow(() -> new RuntimeException("Alumni not found with id: " + alumniId));

        boolean alreadyMarked = helpfulRepository.existsByAnswerIdAndMarkedByIdAndUserType(
                answerId,
                alumniId,
                ForumHelpful.UserType.ALUMNI
        );

        if (alreadyMarked) {
            throw new RuntimeException("Alumni already marked this answer helpful");
        }

        helpfulRepository.save(
                ForumHelpful.builder()
                        .answerId(answerId)
                        .markedById(alumniId)
                        .userType(ForumHelpful.UserType.ALUMNI)
                        .build()
        );

        answer.setHelpfulCount(answer.getHelpfulCount() + 1);
        answerRepository.save(answer);

        return new LikeResponse("Answer marked helpful by alumni", answer.getHelpfulCount());
    }
}