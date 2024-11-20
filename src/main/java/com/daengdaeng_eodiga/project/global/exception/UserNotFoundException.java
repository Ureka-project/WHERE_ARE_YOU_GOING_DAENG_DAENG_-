package com.daengdaeng_eodiga.project.global.exception;

import com.daengdaeng_eodiga.project.global.enums.ErrorCode;

public class UserNotFoundException extends BusinessException {
	public UserNotFoundException() {
		super(ErrorCode.USER_NOT_FOUND.getErrorCode(), ErrorCode.USER_NOT_FOUND.getMessage());
	}
}
