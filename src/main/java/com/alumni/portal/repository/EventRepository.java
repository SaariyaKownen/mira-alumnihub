package com.alumni.portal.repository;

import com.alumni.portal.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventDateGreaterThanEqualOrderByEventDateAsc(LocalDate date);
    List<Event> findAllByOrderByEventDateAsc();
}