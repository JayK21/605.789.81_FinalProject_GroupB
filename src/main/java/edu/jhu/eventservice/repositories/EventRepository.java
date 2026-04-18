package edu.jhu.eventservice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.jhu.eventservice.models.Event;

public interface EventRepository extends JpaRepository<Event, Integer>{
    Optional<Event> findByEventId(Integer eventId);


}
