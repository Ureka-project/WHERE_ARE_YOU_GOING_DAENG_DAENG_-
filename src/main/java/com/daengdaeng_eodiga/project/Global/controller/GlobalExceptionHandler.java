package com.daengdaeng_eodiga.project.Global.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.daengdaeng_eodiga.project.Global.dto.ApiErrorResponse;
import com.daengdaeng_eodiga.project.Global.exception.BusinessException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException e) {
		return ResponseEntity.status(e.getStatus()).body(ApiErrorResponse.error(e.getStatus().name(),e.getMessage()));
	}
}
