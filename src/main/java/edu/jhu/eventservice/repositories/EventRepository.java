package edu.jhu.eventservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.jhu.eventservice.models.Event;
import edu.jhu.eventservice.models.User;

public interface EventRepository extends JpaRepository<Event, Integer>{
    List<Event> findByAttendeesContaining(User user);
}
