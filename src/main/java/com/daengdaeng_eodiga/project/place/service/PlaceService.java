package com.daengdaeng_eodiga.project.place.service;

import org.springframework.stereotype.Service;

import com.daengdaeng_eodiga.project.Global.exception.PlaceNotFoundException;
import com.daengdaeng_eodiga.project.place.entity.Place;
import com.daengdaeng_eodiga.project.place.repository.PlaceRepository;
import com.daengdaeng_eodiga.project.place.repository.PlaceScoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceService {
	private final PlaceRepository placeRepository;
	private final PlaceScoreRepository placeScoreRepository;

	public Place findPlace(int placeId) {
		return placeRepository.findById(placeId).orElseThrow(()-> new PlaceNotFoundException());
	}

	public Double findPlaceScore(int placeId) {
		return placeScoreRepository.findById(placeId).orElseThrow(()-> new PlaceNotFoundException()).getScore();
	}
}
