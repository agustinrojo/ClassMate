package com.example.calendar_service.repository;

import com.example.calendar_service.entity.calendar.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IEventRepository extends JpaRepository<Event, Long> {
    List<Event> findEventsByUserId(Long userId);

    List<Event> findByStartDate(LocalDate startDate);
}
