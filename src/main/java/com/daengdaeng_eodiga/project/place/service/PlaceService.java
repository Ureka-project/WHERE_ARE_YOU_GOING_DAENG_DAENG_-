package com.daengdaeng_eodiga.project.place.service;

import org.springframework.stereotype.Service;

import com.daengdaeng_eodiga.project.place.entity.Place;
import com.daengdaeng_eodiga.project.place.repository.PlaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceService {
	private final PlaceRepository placeRepository;

	public Place findPlace(int placeId) {
		return placeRepository.findById(placeId).orElseThrow();
	}
}
