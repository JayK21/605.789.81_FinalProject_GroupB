package edu.jhu.eventservice.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.jhu.eventservice.models.Event;
import edu.jhu.eventservice.models.User;
import edu.jhu.eventservice.repositories.EventRepository;

@Service
public class EventService {
    @Autowired
    private EventRepository er;

    public Event addEvent(Event event) {
        return er.save(event);
    }

    public List<Event> getAllEvents() {
        return er.findAll();
    }

    public Event getEventById(Integer eventId) {
        return er.findById(eventId).orElse(null);
    }

    public void deleteEvent(Event event) {
        er.delete(event);
    }

    public List<Event> getUpcomingEvents() {
        return er.findByDateGreaterThanEqual(LocalDate.now());
    }

    public List<Event> getEventsByUser(User user) {
        return er.findByAttendeesContaining(user);
    }

    public List<Event> getUpcomingEventsByUser(User user) {
        return er.findByAttendeesContainingAndDateGreaterThanEqual(user, LocalDate.now());
    }
}
