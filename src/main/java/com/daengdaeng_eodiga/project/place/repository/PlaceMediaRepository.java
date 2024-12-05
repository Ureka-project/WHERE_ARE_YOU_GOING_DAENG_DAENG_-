package com.daengdaeng_eodiga.project.place.repository;

import com.daengdaeng_eodiga.project.place.entity.PlaceMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceMediaRepository extends JpaRepository<PlaceMedia, Integer> {
    Optional<PlaceMedia> findByPlace_PlaceId(Integer placeId);
}