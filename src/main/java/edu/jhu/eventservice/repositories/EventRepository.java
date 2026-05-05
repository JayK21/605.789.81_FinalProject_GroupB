package edu.jhu.eventservice.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.jhu.eventservice.models.Event;
import edu.jhu.eventservice.models.User;

public interface EventRepository extends JpaRepository<Event, Integer>{
    List<Event> findByAttendeesContaining(User user);
    List<Event> findByAttendeesContainingAndDateGreaterThanEqual(User user, LocalDate date);
    List<Event> findByDateGreaterThanEqual(LocalDate date);
}
