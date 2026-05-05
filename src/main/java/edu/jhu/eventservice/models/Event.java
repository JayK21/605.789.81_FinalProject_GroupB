package edu.jhu.eventservice.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer eventId;

    @Column(name = "title", unique = true, nullable = false)
	private String title;
    
    @Column(name = "description", unique = false, nullable = true)
	private String description;
    
    @Column(name = "location", unique = false, nullable = false)
	private String location;
    
    @Column(name = "date", unique = false, nullable = false)
	private LocalDate date;
    
    @Column(name = "time", unique = false, nullable = false)
	private LocalTime time;
    
    @Column(name = "maxCapacity", unique = false, nullable = true)
	private Integer maxCapacity;

    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @ManyToMany
    @JoinTable(
        name = "event_attendees",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
	private List<User> attendees;
    
    public Event() {}

	public Event(Integer eventId, String title, String description, String location, LocalDate date, LocalTime time,
			Integer maxCapacity, User organizer, List<User> attendees) {
		this.eventId = eventId;
		this.title = title;
		this.description = description;
		this.location = location;
		this.date = date;
		this.time = time;
		this.maxCapacity = maxCapacity;
		this.organizer = organizer;
		this.attendees = attendees;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public Integer getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(Integer maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	public User getOrganizer() {
		return organizer;
	}

	public void setOrganizer(User organizer) {
		this.organizer = organizer;
	}

	public List<User> getAttendees() {
		return attendees;
	}

	public void setAttendees(List<User> attendees) {
		this.attendees = attendees;
	}

	@Override
	public String toString() {
		return "Event [eventId=" + eventId + ", title=" + title + ", description=" + description + ", location="
				+ location + ", date=" + date + ", time=" + time + ", maxCapacity=" + maxCapacity
				+ ", organizer=" + (organizer != null ? organizer.getUserId() : null) + "]";
	}
    
}
