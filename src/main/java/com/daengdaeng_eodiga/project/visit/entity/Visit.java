package com.daengdaeng_eodiga.project.visit.entity;

import java.time.LocalDateTime;

import com.daengdaeng_eodiga.project.Global.entity.BaseEntity;
import com.daengdaeng_eodiga.project.place.entity.Place;
import com.daengdaeng_eodiga.project.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Visit extends BaseEntity {

	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	private int id;

	private LocalDateTime visitAt;

	@ManyToOne
	@JoinColumn(name = "place_id",nullable = false)
	private Place place;

	@ManyToOne
	@JoinColumn(name = "user_id",nullable = false)
	private User user;

	@Builder
	public Visit(LocalDateTime visitAt, Place place, User user) {
		this.visitAt = visitAt;
		this.place = place;
		this.user = user;
	}



}
