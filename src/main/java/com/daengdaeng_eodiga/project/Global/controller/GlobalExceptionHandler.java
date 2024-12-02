package com.daengdaeng_eodiga.project.Global.controller;

import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.daengdaeng_eodiga.project.Global.dto.ApiErrorResponse;
import com.daengdaeng_eodiga.project.Global.exception.BusinessException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException e) {
		return ResponseEntity.status(e.getStatus()).body(ApiErrorResponse.error(e.getStatus().name(),e.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
		return ResponseEntity.badRequest().body(ApiResponse.failure(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
	}
}
