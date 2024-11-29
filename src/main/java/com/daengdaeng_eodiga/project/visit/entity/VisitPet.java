package com.daengdaeng_eodiga.project.visit.entity;

import com.daengdaeng_eodiga.project.pet.entity.Pet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "visit_pet")
@NoArgsConstructor
public class VisitPet {
	@Id
	@Column(name = "visit_pet_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int visitPetId;

	@ManyToOne
	@JoinColumn(name = "pet_id", nullable = false)
	private Pet pet;

	@ManyToOne
	@JoinColumn(name = "visit_id", nullable = false)
	private Visit visit;

	@Builder
	public VisitPet(Pet pet, Visit visit) {
		this.pet = pet;
		this.visit = visit;
	}

}
