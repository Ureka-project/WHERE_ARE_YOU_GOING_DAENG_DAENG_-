package com.daengdaeng_eodiga.project.notification.dto;

import lombok.Getter;

@Getter
public class PushTokenRequest {
	private String token;
	private String pushType;
}
