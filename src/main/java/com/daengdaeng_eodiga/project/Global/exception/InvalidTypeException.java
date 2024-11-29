package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;

public class InvalidTypeException extends BusinessException {
	public InvalidTypeException(String type) {
		super(ErrorCode.INVALID_TYPE.getErrorCode(),type+ "은 " + ErrorCode.INVALID_TYPE.getMessage());
	}
}