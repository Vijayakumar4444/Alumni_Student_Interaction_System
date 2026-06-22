package com.vijay.alumniportal.dashboard.service;

import com.vijay.alumniportal.alumni.entity.Alumni;
import com.vijay.alumniportal.alumni.repository.AlumniRepository;
import com.vijay.alumniportal.dashboard.dto.AlumniDashboardResponse;
import com.vijay.alumniportal.dashboard.dto.StudentDashboardResponse;
import com.vijay.alumniportal.event.entity.Event;
import com.vijay.alumniportal.event.repository.EventRegistrationRepository;
import com.vijay.alumniportal.event.repository.EventRepository;
import com.vijay.alumniportal.forum.repository.ForumAnswerRepository;
import com.vijay.alumniportal.forum.repository.ForumQuestionRepository;
import com.vijay.alumniportal.job.repository.JobRepository;
import com.vijay.alumniportal.mentorship.entity.MentorShip;
import com.vijay.alumniportal.mentorship.repository.MentorShipRepository;
import com.vijay.alumniportal.student.entity.Student;
import com.vijay.alumniportal.student.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class DashboardService {

    private final StudentRepository studentRepository;
    private final AlumniRepository alumniRepository;
    private final MentorShipRepository mentorShipRepository;
    private final EventRepository eventRepository;
    private final EventRegistrationRepository eventRegistrationRepository;
    private final ForumQuestionRepository forumQuestionRepository;
    private final ForumAnswerRepository forumAnswerRepository;
    private final JobRepository jobRepository;

    public DashboardService(
            StudentRepository studentRepository,
            AlumniRepository alumniRepository,
            MentorShipRepository mentorShipRepository,
            EventRepository eventRepository,
            EventRegistrationRepository eventRegistrationRepository,
            ForumQuestionRepository forumQuestionRepository,
            ForumAnswerRepository forumAnswerRepository,
            JobRepository jobRepository
    ) {
        this.studentRepository = studentRepository;
        this.alumniRepository = alumniRepository;
        this.mentorShipRepository = mentorShipRepository;
        this.eventRepository = eventRepository;
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.forumQuestionRepository = forumQuestionRepository;
        this.forumAnswerRepository = forumAnswerRepository;
        this.jobRepository = jobRepository;
    }

    public StudentDashboardResponse getStudentDashboard(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        int totalMentorshipRequests = mentorShipRepository.findByStudentId(studentId).size();

        boolean hasAcceptedMentor = mentorShipRepository.existsByStudentIdAndStatus(
                studentId,
                MentorShip.MentorShipStatus.ACCEPTED
        );

        int registeredEventsCount = eventRegistrationRepository.findByStudentId(studentId).size();

        int postedQuestionsCount = forumQuestionRepository.findByStudentId(studentId).size();

        int recommendedEventsCount = (int) eventRepository.findAll()
                .stream()
                .filter(event -> event.getStatus() == Event.EventStatus.OPEN)
                .filter(event -> calculateMatchScore(student.getSkills(), event.getRequiredSkills()) > 0)
                .count();

        int availableJobsCount = jobRepository.findAll().size();

        return new StudentDashboardResponse(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getDepartment(),
                student.getYear(),
                student.getSkills(),
                totalMentorshipRequests,
                hasAcceptedMentor,
                registeredEventsCount,
                postedQuestionsCount,
                recommendedEventsCount,
                availableJobsCount
        );
    }

    public AlumniDashboardResponse getAlumniDashboard(Long alumniId) {

        Alumni alumni = alumniRepository.findById(alumniId)
                .orElseThrow(() -> new RuntimeException("Alumni not found with id: " + alumniId));

        int postedEventsCount = eventRepository.findByAlumniId(alumniId).size();

        int totalRegisteredStudentsForEvents = eventRepository.findByAlumniId(alumniId)
                .stream()
                .mapToInt(event -> eventRegistrationRepository.findByEventId(event.getId()).size())
                .sum();

        int answeredQuestionsCount = forumAnswerRepository.findByAlumniId(alumniId).size();

        int mentorshipRequestsReceivedCount = mentorShipRepository.findByAlumniId(alumniId).size();

        int postedJobsCount = jobRepository.findByPostedBy(alumni.getName()).size();

        return new AlumniDashboardResponse(
                alumni.getId(),
                alumni.getName(),
                alumni.getEmail(),
                alumni.getCompany(),
                alumni.getDesignation(),
                alumni.getExperience(),
                alumni.getSkills(),
                postedEventsCount,
                totalRegisteredStudentsForEvents,
                answeredQuestionsCount,
                mentorshipRequestsReceivedCount,
                postedJobsCount
        );
    }

    private int calculateMatchScore(String studentSkills, String requiredSkills) {

        Set<String> studentSkillSet = splitSkills(studentSkills);
        Set<String> requiredSkillSet = splitSkills(requiredSkills);

        if (requiredSkillSet.isEmpty()) {
            return 0;
        }

        int matched = 0;

        for (String skill : requiredSkillSet) {
            if (studentSkillSet.contains(skill)) {
                matched++;
            }
        }

        return (matched * 100) / requiredSkillSet.size();
    }

    private Set<String> splitSkills(String skills) {

        Set<String> result = new HashSet<>();

        if (skills == null || skills.isBlank()) {
            return result;
        }

        String[] arr = skills.split(",");

        for (String skill : arr) {
            result.add(skill.trim().toLowerCase());
        }

        return result;
    }
}