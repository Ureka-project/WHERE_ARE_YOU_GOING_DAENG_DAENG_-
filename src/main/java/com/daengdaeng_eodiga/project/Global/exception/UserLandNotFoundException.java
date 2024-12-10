package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;

public class UserLandNotFoundException extends BusinessException {
	public UserLandNotFoundException() {
		super(ErrorCode.USER_LAND_NOT_FOUND.getErrorCode(), ErrorCode.USER_LAND_NOT_FOUND.getMessage());
	}
}