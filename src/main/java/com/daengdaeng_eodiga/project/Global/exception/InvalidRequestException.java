package com.daengdaeng_eodiga.project.Global.exception;

import java.util.function.Supplier;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;


public class InvalidRequestException extends BusinessException {
	public InvalidRequestException(String entity, String details) {
		super(
			ErrorCode.INVALID_REQUEST.getErrorCode(),
			String.format("%s : %s - %s", entity, details, ErrorCode.INVALID_REQUEST.getMessage())
		);
	}
}

