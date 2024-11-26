package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;

public class DateNotFoundException extends BusinessException {
	public DateNotFoundException() {
		super(ErrorCode.DATE_NOT_FOUND.getErrorCode(), ErrorCode.DATE_NOT_FOUND.getMessage());
	}
}