package com.daengdaeng_eodiga.project.notification.controller;

import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daengdaeng_eodiga.project.notification.dto.FcmRequestDto;
import com.daengdaeng_eodiga.project.notification.enums.NotificationTopic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/publish")
@RequiredArgsConstructor
public class Publisher {

	private final RedisTemplate<String, String> redisTemplate;

	public void publish(NotificationTopic topic, FcmRequestDto request) {
		ObjectMapper objectMapper = new ObjectMapper();
		try{
			String message = objectMapper.writeValueAsString(request);
			redisTemplate.convertAndSend(topic.toString(), message);
			log.info("published topic : " + topic + " /  message : " + message);
		} catch (JsonProcessingException e) {
			log.error("push notification send failed - json error : " + e.getMessage());
		}



	}
}
