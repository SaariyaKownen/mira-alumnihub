package com.alumni.portal.service;

import com.alumni.portal.model.Event;
import com.alumni.portal.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    public void addEvent(Event event) {
        eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAllByOrderByEventDateAsc();
    }

    public List<Event> getUpcomingEvents() {
        return eventRepository
            .findByEventDateGreaterThanEqualOrderByEventDateAsc(LocalDate.now());
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}