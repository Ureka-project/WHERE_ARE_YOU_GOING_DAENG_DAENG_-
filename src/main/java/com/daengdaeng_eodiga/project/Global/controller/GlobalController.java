package com.daengdaeng_eodiga.project.Global.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/v1/Global")
@RequiredArgsConstructor
public class GlobalController {
	@Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
	private String googleRedirectUri;

	@GetMapping()
	public String getGoogleRedirectUri() {
		return googleRedirectUri;
	}

}
