package edu.jhu.eventservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.jhu.eventservice.models.Event;
import edu.jhu.eventservice.models.User;
import edu.jhu.eventservice.repositories.EventRepository;

public class EventService {
    @Autowired
    private EventRepository er;
	
    // add a new event
    public Event addEvent(Event event) {
        return er.save(event);
    }
	
    // retrieve all events
    public List<Event> getAllEvents() {
        return er.findAll(); 
    }

    // retrieve an event by its event ID
    public Event getEventById(Integer eventId) {
    	if (er.findByEventId(eventId).isPresent()) {
    		return er.findByEventId(eventId).get();
    	} else {
    		return null;
    	}
    }
    
    // delete an event
    public void deleteEvent(Event event) {
        er.delete(event);
    }
}
