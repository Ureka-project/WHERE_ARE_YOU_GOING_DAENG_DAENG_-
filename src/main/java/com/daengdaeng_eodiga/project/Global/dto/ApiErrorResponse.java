package com.daengdaeng_eodiga.project.Global.dto;

public record ApiErrorResponse(String error, String message) {
	public static ApiErrorResponse error(String error, String message) {
		return new ApiErrorResponse(error, message);
	}
}
