package com.daengdaeng_eodiga.project.place.repository;

import com.daengdaeng_eodiga.project.place.entity.OpeningDate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OpeningDateRepository extends JpaRepository<OpeningDate, Integer> {
    List<OpeningDate> findByPlace_PlaceId(int placeId);
}