package com.daengdaeng_eodiga.project.review.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daengdaeng_eodiga.project.pet.entity.Pet;
import com.daengdaeng_eodiga.project.review.entity.Review;
import com.daengdaeng_eodiga.project.review.entity.ReviewPet;
import com.daengdaeng_eodiga.project.review.repository.ReviewPetRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewPetService {
	private final ReviewPetRepository reviewPetRepository;

	public List<ReviewPet> saveReviewPet(Review review, List<Pet> pets) {
		return reviewPetRepository.saveAll(pets.stream().map(pet -> ReviewPet.builder().review(review).pet(pet).build()).toList());
	}


}
