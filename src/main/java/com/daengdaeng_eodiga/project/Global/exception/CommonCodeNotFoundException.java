package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;

public class CommonCodeNotFoundException extends BusinessException {
	public CommonCodeNotFoundException() {
		super(ErrorCode.COMMON_CODE_NOT_FOUND.getErrorCode(), ErrorCode.COMMON_CODE_NOT_FOUND.getMessage());
	}
}