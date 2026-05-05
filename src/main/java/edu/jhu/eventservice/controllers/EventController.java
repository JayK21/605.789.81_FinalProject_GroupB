package edu.jhu.eventservice.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.jhu.eventservice.dto.EventRequest;
import edu.jhu.eventservice.models.Event;
import edu.jhu.eventservice.models.User;
import edu.jhu.eventservice.security.AuthenticatedUser;
import edu.jhu.eventservice.services.EventService;
import edu.jhu.eventservice.services.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService es;
    private final UserService us;

    public EventController(EventService es, UserService us) {
        this.es = es;
        this.us = us;
    }

    // Get all events; ?upcoming=true restricts to events that have not yet occurred
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents(
            @RequestParam(value = "upcoming", required = false, defaultValue = "false") boolean upcoming) {
        List<Event> events = upcoming ? es.getUpcomingEvents() : es.getAllEvents();
        if (events.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(events);
    }

    // Get a single event
    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable int eventId) {
        Event event = es.getEventById(eventId);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(event);
    }

    // Create event — authenticated user becomes the organizer
    @PostMapping
    public ResponseEntity<String> addEvent(@Valid @RequestBody EventRequest request) {
        Integer callerId = AuthenticatedUser.currentUserId().orElse(null);
        if (callerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        }

        User organizer = us.getUserById(callerId);
        if (organizer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authenticated user not found");
        }

        Event event = new Event(null, request.getTitle(), request.getDescription(), request.getLocation(),
                request.getDate(), request.getTime(), request.getMaxCapacity(), organizer, List.of());
        es.addEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body("Event created successfully");
    }

    // Update event — organizer only
    @PutMapping("/{eventId}")
    public ResponseEntity<String> updateEvent(@PathVariable int eventId, @Valid @RequestBody EventRequest request) {
        Integer callerId = AuthenticatedUser.currentUserId().orElse(null);
        if (callerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        }

        Event existingEvent = es.getEventById(eventId);
        if (existingEvent == null) {
            return ResponseEntity.notFound().build();
        }
        if (!existingEvent.getOrganizer().getUserId().equals(callerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the event organizer may update this event");
        }

        existingEvent.setTitle(request.getTitle());
        existingEvent.setDescription(request.getDescription());
        existingEvent.setLocation(request.getLocation());
        existingEvent.setDate(request.getDate());
        existingEvent.setTime(request.getTime());
        existingEvent.setMaxCapacity(request.getMaxCapacity());
        es.addEvent(existingEvent);
        return ResponseEntity.ok("Event updated successfully");
    }

    // Cancel event — organizer only
    @DeleteMapping("/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable int eventId) {
        Integer callerId = AuthenticatedUser.currentUserId().orElse(null);
        if (callerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        }

        Event event = es.getEventById(eventId);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        if (!event.getOrganizer().getUserId().equals(callerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the event organizer may cancel this event");
        }

        es.deleteEvent(event);
        return ResponseEntity.ok("Event cancelled successfully");
    }
    
    // Register the authenticated caller for an event
    @PostMapping("/register/{eventId}")
    public ResponseEntity<String> registerUserForEvent(@PathVariable int eventId) {
        Integer callerId = AuthenticatedUser.currentUserId().orElse(null);
        if (callerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        }

        Event event = es.getEventById(eventId);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }

        User caller = us.getUserById(callerId);
        if (caller == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authenticated user not found");
        }

        List<User> attendees = event.getAttendees();
        if (attendees.contains(caller)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("You are already registered for this event");
        }

        if (event.getMaxCapacity() != null && attendees.size() >= event.getMaxCapacity()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Event is at max capacity");
        }

        attendees.add(caller);
        es.addEvent(event);
        return ResponseEntity.ok("Registered for event successfully");
    }

    // Unregister the authenticated caller from an event
    @DeleteMapping("/register/{eventId}")
    public ResponseEntity<String> unregisterUserForEvent(@PathVariable int eventId) {
        Integer callerId = AuthenticatedUser.currentUserId().orElse(null);
        if (callerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        }

        Event event = es.getEventById(eventId);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }

        if (event.getDate().isBefore(LocalDate.now())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot unregister after event has occurred");
        }

        User caller = us.getUserById(callerId);
        if (caller == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authenticated user not found");
        }

        List<User> attendees = event.getAttendees();
        if (!attendees.remove(caller)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You are not registered for this event");
        }

        es.addEvent(event);
        return ResponseEntity.ok("Unregistered from event successfully");
    }

    // Returns upcoming events the authenticated caller is registered for 
    @GetMapping("/register/me")
    public ResponseEntity<List<Event>> getMyUpcomingEvents() {
        Integer callerId = AuthenticatedUser.currentUserId().orElse(null);
        if (callerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User caller = us.getUserById(callerId);
        if (caller == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(es.getUpcomingEventsByUser(caller));
    }
}
