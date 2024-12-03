package com.daengdaeng_eodiga.project.Global.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.daengdaeng_eodiga.project.Global.dto.ApiErrorResponse;
import com.daengdaeng_eodiga.project.Global.exception.BusinessException;


import java.util.Collections;

import java.util.List;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException e) {
		return ResponseEntity.status(e.getStatus()).body(ApiErrorResponse.error(e.getStatus().name(),e.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
		List<String> errorMessages = Collections.singletonList(ex.getBindingResult().getFieldErrors()
				.stream()
				.map(FieldError::getDefaultMessage)
				.collect(Collectors.joining(", ")));
		ApiErrorResponse response = ApiErrorResponse.error("NOT VALIDATED", errorMessages.toString());

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiErrorResponse> handleValidationExceptions(ConstraintViolationException ex) {
		String errorMessage = ex.getConstraintViolations()
				.stream().map(violation -> violation.getMessage())
				.collect(Collectors.joining(", "));
		ApiErrorResponse response = ApiErrorResponse.error("NOT VALIDATED", errorMessage);

		return ResponseEntity.badRequest().body(response);
	}
}