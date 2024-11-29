package com.daengdaeng_eodiga.project.notification.dto;

import java.util.List;

import com.daengdaeng_eodiga.project.notification.enums.PushType;

import lombok.Builder;

import lombok.Getter;


@Getter
public class FcmRequestDto {
	private List<String> token;
	private List<Integer> userId;
	private PushType type;
	private String petName;
	private String placeName;
	private String eventName;

	@Builder
	public FcmRequestDto(List<String> token, List<Integer> userId, PushType type, String petName, String placeName, String eventName) {
		this.token = token;
		this.userId = userId;
		this.type = type;
		this.petName = petName;
		this.placeName = placeName;
		this.eventName = eventName;
	}


}

