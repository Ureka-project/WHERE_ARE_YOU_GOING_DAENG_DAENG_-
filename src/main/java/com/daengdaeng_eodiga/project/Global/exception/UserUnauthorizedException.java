package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;

public class UserUnauthorizedException extends BusinessException {
	public UserUnauthorizedException() {
		super(ErrorCode.USER_UNAUTHORIZED.getErrorCode(), ErrorCode.USER_UNAUTHORIZED.getMessage());
	}
}