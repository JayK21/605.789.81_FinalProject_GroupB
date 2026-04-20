package edu.jhu.eventservice.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.jhu.eventservice.models.Event;
import edu.jhu.eventservice.models.User;
import edu.jhu.eventservice.services.EventService;

@RestController
@RequestMapping("/events")
public class EventController {
	private final EventService es;

    // Constructor
    public EventController(EventService es) {
        this.es = es;
    }
    
    // Get all events
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = es.getAllEvents();
        if (events.isEmpty()) {
        	return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(events);
    }

    // Get an event by eventId
    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable int eventId) {
    	Event event = es.getEventById(eventId);
        if (event == null) {
        	return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(event);
    }
    
    // Add a new event
    @PostMapping
    public ResponseEntity<String> addEvent(@RequestParam(value="title") String title, @RequestParam(value="description") String description,
    		@RequestParam(value="location") String location, @RequestParam(value="date") LocalDate date,
    		@RequestParam(value="time") LocalTime time, @RequestParam(value="maxCapacity") Integer maxCapacity,
    		@RequestParam(value="userIds") List<User> attendees ) {
    	List<String> errors = validateEvent(title, description, location, date, time, maxCapacity, attendees);
        
        if (!errors.isEmpty()) {
        	System.out.println(errors.toString());
        	return ResponseEntity.badRequest().build();
        }
        
        Event event = new Event(null, title, description, location, date, time, maxCapacity, attendees);
        es.addEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body("Event added successfully");
    }
    
    // Update an existing professor
//    @PutMapping("/{userId}")
//    public ResponseEntity<String> updateUser(@PathVariable int userId, @RequestParam(value="firstName") String firstName, 
//    		@RequestParam(value="lastName") String lastName,
//    		@RequestParam(value="email") String email, @RequestParam(value="password") String password) {
//    	List<String> errors = validateEvent(firstName, lastName, email, password);
//        
//        if (!errors.isEmpty()) {
//        	System.out.println(errors.toString());
//        	return ResponseEntity.badRequest().build();
//        }
//        User existingUser = es.getEventById(userId);
//        if (existingUser != null) {
//            existingUser.setFirstName(firstName);
//            existingUser.setLastName(lastName);
//            existingUser.setEmail(email);
//            existingUser.setPassword(password);
//            es.addEvent(existingUser); 
//            return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
//        } else {
//        	System.out.println("User with id " + userId + " not found");
//        	return ResponseEntity.notFound().build(); 
//        }
//    }
//    
//    // Delete a professor
//    @DeleteMapping("/{userId}")
//    public ResponseEntity<String> deleteUser(@PathVariable int userId) {
//        User user = es.getEventById(userId);
//        if (user != null) {
//            es.deleteEvent(user);
//            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
//        } else {
//        	System.out.println("User with id " + userId + " not found");
//        	return ResponseEntity.notFound().build();
//        }
//    }
    
    private List<String> validateEvent(String title, String description, String location, LocalDate date, LocalTime time, 
    		Integer maxCapacity, List<User> attendees){
    	List<String> errors = new ArrayList<>();
    	if (title == null || title.isEmpty()) {
        	errors.add("Title cannot be empty");
        }
        
        if (location == null || location.isEmpty()) {
        	errors.add("Location cannot be empty");
        }
        
        if (date == null) {
        	errors.add("Date cannot be empty");
        }
        
        if (time == null) {
        	errors.add("Time cannot be empty");
        }
        
        return errors;
    }
}
