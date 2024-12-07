package com.daengdaeng_eodiga.project.event.repository;

import com.daengdaeng_eodiga.project.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query("SELECT e FROM Event e WHERE :today <= e.endDate")
    List<Event> findActiveEvents(@Param("today") LocalDate today);
}