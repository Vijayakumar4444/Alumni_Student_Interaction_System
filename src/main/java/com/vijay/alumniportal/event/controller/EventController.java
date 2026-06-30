package com.vijay.alumniportal.event.controller;

import com.vijay.alumniportal.event.dto.*;
import com.vijay.alumniportal.event.service.EventService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/events")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public EventResponse createEvent(
            @RequestPart("event") EventRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return service.createEvent(request, image);
    }

    @GetMapping
    public List<EventResponse> getAllEvents() {
        return service.getAllEvents();
    }

    @GetMapping("/{id}")
    public EventResponse getEventById(@PathVariable Long id) {
        return service.getEventById(id);
    }

    @GetMapping("/alumni/{alumniId}")
    public List<EventResponse> getEventsByAlumniId(@PathVariable Long alumniId) {
        return service.getEventsByAlumniId(alumniId);
    }

    @PutMapping("/{id}")
    public EventResponse updateEvent(@PathVariable Long id, @RequestBody EventRequest request) {
        return service.updateEvent(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteEvent(@PathVariable Long id) {
        service.deleteEvent(id);
        return "Event deleted successfully";
    }

    @PostMapping("/{eventId}/register/{studentId}")
    public EventRegistrationResponse registerStudent(
            @PathVariable Long eventId,
            @PathVariable Long studentId
    ) {
        return service.registerStudent(eventId, studentId);
    }

    @GetMapping("/{eventId}/students")
    public List<EventRegistrationResponse> getRegisteredStudents(@PathVariable Long eventId) {
        return service.getRegisteredStudents(eventId);
    }

    @GetMapping("/student/{studentId}")
    public List<EventRegistrationResponse> getStudentRegisteredEvents(@PathVariable Long studentId) {
        return service.getStudentRegisteredEvents(studentId);
    }

    @GetMapping("/recommended/{studentId}")
    public List<EventRecommendationResponse> getRecommendedEvents(@PathVariable Long studentId) {
        return service.getRecommendedEvents(studentId);
    }

    @GetMapping("/{eventId}/analytics")
    public EventAnalyticsResponse getEventAnalytics(@PathVariable Long eventId) {
        return service.getEventAnalytics(eventId);
    }
}