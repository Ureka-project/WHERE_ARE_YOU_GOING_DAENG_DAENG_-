package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;

public class PetNotFoundException extends BusinessException {
	public PetNotFoundException() {
		super(ErrorCode.PLACE_NOT_FOUND.getErrorCode(), ErrorCode.PLACE_NOT_FOUND.getMessage());
	}
}