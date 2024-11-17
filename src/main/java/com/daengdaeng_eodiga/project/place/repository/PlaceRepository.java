package com.daengdaeng_eodiga.project.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daengdaeng_eodiga.project.place.entity.Place;

public interface PlaceRepository extends JpaRepository<Place, Integer> {
}
