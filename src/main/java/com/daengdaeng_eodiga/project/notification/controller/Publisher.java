package com.daengdaeng_eodiga.project.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.stereotype.Component;

import com.daengdaeng_eodiga.project.notification.dto.FcmRequestDto;
import com.daengdaeng_eodiga.project.notification.enums.NotificationTopic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Publisher {

	private final RedisTemplate<String, String> redisTemplate;

	@Autowired
	public Publisher(@Qualifier(value = "redisTemplate")RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

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
