package com.daengdaeng_eodiga.project.global.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.daengdaeng_eodiga.project.global.dto.ApiErrorResponse;
import com.daengdaeng_eodiga.project.global.exception.BusinessException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException e) {
		return ResponseEntity.status(e.getStatus()).body(ApiErrorResponse.error(e.getStatus().name(),e.getMessage()));
	}
}
