package com.daengdaeng_eodiga.project.Global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("https://localhost:5173")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
				.allowedHeaders("*")
				.allowCredentials(true)
				.maxAge(3600);
	}
}
