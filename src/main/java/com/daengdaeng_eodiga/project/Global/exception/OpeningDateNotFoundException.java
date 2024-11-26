package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;

public class OpeningDateNotFoundException extends BusinessException {
	public OpeningDateNotFoundException() {
		super(ErrorCode.OPENING_DATE_NOT_FOUND.getErrorCode(), ErrorCode.OPENING_DATE_NOT_FOUND.getMessage());
	}
}