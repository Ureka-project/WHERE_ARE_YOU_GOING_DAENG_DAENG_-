package com.daengdaeng_eodiga.project.Global.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ApiResponse<T>(String message , T data, @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
LocalDateTime timestamp) {
	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>("success", data, LocalDateTime.now());
	}

	public static <T> ApiResponse<T> failure(String message) {
		return new ApiResponse<>(message,null, LocalDateTime.now());
	}
}
