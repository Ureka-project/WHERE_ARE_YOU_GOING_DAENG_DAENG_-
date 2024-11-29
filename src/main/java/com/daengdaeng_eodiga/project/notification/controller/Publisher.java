package com.daengdaeng_eodiga.project.notification.controller;

import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daengdaeng_eodiga.project.notification.dto.FcmRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/publish")
@RequiredArgsConstructor
public class Publisher {

	private final RedisTemplate<String, String> redisTemplate;

	@PostMapping("/{topic}")
	public void publish(@PathVariable String topic, @RequestBody FcmRequestDto request) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String message = objectMapper.writeValueAsString(request);
		redisTemplate.convertAndSend(topic, message);
	}
}
