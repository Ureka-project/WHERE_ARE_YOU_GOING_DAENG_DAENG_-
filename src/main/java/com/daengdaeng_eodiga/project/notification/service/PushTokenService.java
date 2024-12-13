package com.daengdaeng_eodiga.project.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daengdaeng_eodiga.project.notification.entity.PushToken;
import com.daengdaeng_eodiga.project.notification.repository.PushTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PushTokenService {

	private final PushTokenRepository pushTokenRepository;
	public List<PushToken> fetchOwnerPushTokens(List<Integer> userIds, String pushType) {
		return pushTokenRepository.findByUser_UserIdInAndPushType(userIds,pushType);
	}
}
