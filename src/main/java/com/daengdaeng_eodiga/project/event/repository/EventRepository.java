package com.daengdaeng_eodiga.project.event.repository;

import com.daengdaeng_eodiga.project.event.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("SELECT e FROM Event e WHERE :today BETWEEN e.startDate AND e.endDate")
    Page<Event> findActiveEvents(@Param("today") LocalDate today, Pageable pageable);
}