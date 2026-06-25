package com.vijay.alumniportal.event.service;

import com.vijay.alumniportal.alumni.repository.AlumniRepository;
import com.vijay.alumniportal.event.dto.*;
import com.vijay.alumniportal.event.entity.Event;
import com.vijay.alumniportal.event.entity.EventRegistration;
import com.vijay.alumniportal.event.repository.EventRegistrationRepository;
import com.vijay.alumniportal.event.repository.EventRepository;
import com.vijay.alumniportal.student.entity.Student;
import com.vijay.alumniportal.student.repository.StudentRepository;
import org.springframework.stereotype.Service;
import com.vijay.alumniportal.notification.entity.Notification;
import com.vijay.alumniportal.notification.service.NotificationService;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventRegistrationRepository registrationRepository;
    private final StudentRepository studentRepository;
    private final AlumniRepository alumniRepository;
    private final NotificationService notificationService;

    public EventService(
            EventRepository eventRepository,
            EventRegistrationRepository registrationRepository,
            StudentRepository studentRepository,
            AlumniRepository alumniRepository,
            NotificationService notificationService
    ) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.studentRepository = studentRepository;
        this.alumniRepository = alumniRepository;
        this.notificationService = notificationService;
    }

    public EventResponse createEvent(EventRequest request) {

        alumniRepository.findById(request.getAlumniId())
                .orElseThrow(() -> new RuntimeException("Alumni not found with id: " + request.getAlumniId()));

        Event event = Event.builder()
                .alumniId(request.getAlumniId())
                .title(request.getTitle())
                .description(request.getDescription())
                .eventType(request.getEventType())
                .eventDate(request.getEventDate())
                .eventTime(request.getEventTime())
                .mode(request.getMode())
                .venueOrLink(request.getVenueOrLink())
                .requiredSkills(request.getRequiredSkills())
                .maxSeats(request.getMaxSeats())
                .registeredCount(0)
                .status(Event.EventStatus.OPEN)
                .build();

        return mapToEventResponse(eventRepository.save(event));
    }

    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(this::mapToEventResponse)
                .toList();
    }

    public EventResponse getEventById(Long id) {
        Event event = getEventEntity(id);
        return mapToEventResponse(event);
    }

    public List<EventResponse> getEventsByAlumniId(Long alumniId) {
        alumniRepository.findById(alumniId)
                .orElseThrow(() -> new RuntimeException("Alumni not found with id: " + alumniId));

        return eventRepository.findByAlumniId(alumniId)
                .stream()
                .map(this::mapToEventResponse)
                .toList();
    }

    public EventResponse updateEvent(Long id, EventRequest request) {
        Event event = getEventEntity(id);

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setEventType(request.getEventType());
        event.setEventDate(request.getEventDate());
        event.setEventTime(request.getEventTime());
        event.setMode(request.getMode());
        event.setVenueOrLink(request.getVenueOrLink());
        event.setRequiredSkills(request.getRequiredSkills());
        event.setMaxSeats(request.getMaxSeats());

        if (event.getRegisteredCount() >= event.getMaxSeats()) {
            event.setStatus(Event.EventStatus.FULL);
        } else {
            event.setStatus(Event.EventStatus.OPEN);
        }

        return mapToEventResponse(eventRepository.save(event));
    }

    public void deleteEvent(Long id) {
        Event event = getEventEntity(id);
        eventRepository.delete(event);
    }

    public EventRegistrationResponse registerStudent(Long eventId, Long studentId) {

        Event event = getEventEntity(eventId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        if (event.getStatus() == Event.EventStatus.FULL) {
            throw new RuntimeException("Event seats are full. Registration closed.");
        }

        if (event.getStatus() == Event.EventStatus.CLOSED) {
            throw new RuntimeException("Event is closed. Registration not allowed.");
        }

        boolean alreadyRegistered = registrationRepository.existsByEventIdAndStudentId(eventId, studentId);

        if (alreadyRegistered) {
            throw new RuntimeException("Student already registered for this event");
        }

        EventRegistration registration = EventRegistration.builder()
                .eventId(eventId)
                .studentId(studentId)
                .registeredAt(LocalDateTime.now().toString())
                .build();

        EventRegistration savedRegistration = registrationRepository.save(registration);

        event.setRegisteredCount(event.getRegisteredCount() + 1);

        if (event.getRegisteredCount() >= event.getMaxSeats()) {
            event.setStatus(Event.EventStatus.FULL);
        }
        notificationService.createNotification(
                event.getAlumniId(),
                Notification.UserRole.ALUMNI,
                "Event Full",
                "Your event '" + event.getTitle() + "' is now full."
        );

        eventRepository.save(event);
        notificationService.createNotification(
                event.getAlumniId(),
                Notification.UserRole.ALUMNI,
                "New Event Registration",
                "A student registered for your event: " + event.getTitle()
        );

        return new EventRegistrationResponse(
                savedRegistration.getId(),
                event.getId(),
                event.getTitle(),
                student.getId(),
                student.getName(),
                savedRegistration.getRegisteredAt()
        );
    }

    public List<EventRegistrationResponse> getRegisteredStudents(Long eventId) {

        Event event = getEventEntity(eventId);

        return registrationRepository.findByEventId(eventId)
                .stream()
                .map(reg -> {
                    Student student = studentRepository.findById(reg.getStudentId())
                            .orElseThrow(() -> new RuntimeException("Student not found with id: " + reg.getStudentId()));

                    return new EventRegistrationResponse(
                            reg.getId(),
                            event.getId(),
                            event.getTitle(),
                            student.getId(),
                            student.getName(),
                            reg.getRegisteredAt()
                    );
                })
                .toList();
    }

    public List<EventRegistrationResponse> getStudentRegisteredEvents(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        return registrationRepository.findByStudentId(studentId)
                .stream()
                .map(reg -> {
                    Event event = getEventEntity(reg.getEventId());

                    return new EventRegistrationResponse(
                            reg.getId(),
                            event.getId(),
                            event.getTitle(),
                            student.getId(),
                            student.getName(),
                            reg.getRegisteredAt()
                    );
                })
                .toList();
    }

    public List<EventRecommendationResponse> getRecommendedEvents(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        return eventRepository.findAll()
                .stream()
                .map(event -> buildRecommendation(event, student))
                .filter(response -> response.getMatchScore() > 0)
                .sorted((a, b) -> b.getMatchScore() - a.getMatchScore())
                .toList();
    }

    public EventAnalyticsResponse getEventAnalytics(Long eventId) {

        Event event = getEventEntity(eventId);

        Integer availableSeats = event.getMaxSeats() - event.getRegisteredCount();

        Double fillPercentage = event.getMaxSeats() == 0
                ? 0.0
                : (event.getRegisteredCount() * 100.0) / event.getMaxSeats();

        return new EventAnalyticsResponse(
                event.getId(),
                event.getTitle(),
                event.getMaxSeats(),
                event.getRegisteredCount(),
                availableSeats,
                fillPercentage,
                event.getStatus()
        );
    }

    private EventRecommendationResponse buildRecommendation(Event event, Student student) {

        Set<String> studentSkills = splitSkills(student.getSkills());
        Set<String> requiredSkills = splitSkills(event.getRequiredSkills());

        if (requiredSkills.isEmpty()) {
            return new EventRecommendationResponse(
                    event.getId(),
                    event.getTitle(),
                    event.getRequiredSkills(),
                    0,
                    "No required skills mentioned",
                    event.getStatus(),
                    canApply(event)
            );
        }

        int matched = 0;

        for (String skill : requiredSkills) {
            if (studentSkills.contains(skill)) {
                matched++;
            }
        }

        int score = (matched * 100) / requiredSkills.size();

        String reason = matched == 0
                ? "No matching skills"
                : "Matched " + matched + " out of " + requiredSkills.size() + " required skills";

        return new EventRecommendationResponse(
                event.getId(),
                event.getTitle(),
                event.getRequiredSkills(),
                score,
                reason,
                event.getStatus(),
                canApply(event)
        );
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

    private Event getEventEntity(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
    }

    private Boolean canApply(Event event) {
        return event.getStatus() == Event.EventStatus.OPEN;
    }

    private EventResponse mapToEventResponse(Event event) {

        Integer availableSeats = event.getMaxSeats() - event.getRegisteredCount();

        return new EventResponse(
                event.getId(),
                event.getAlumniId(),
                event.getTitle(),
                event.getDescription(),
                event.getEventType(),
                event.getEventDate(),
                event.getEventTime(),
                event.getMode(),
                event.getVenueOrLink(),
                event.getRequiredSkills(),
                event.getMaxSeats(),
                event.getRegisteredCount(),
                availableSeats,
                event.getStatus(),
                canApply(event)
        );
    }
}