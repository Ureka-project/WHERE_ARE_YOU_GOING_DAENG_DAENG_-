package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;

public class PetNotFoundException extends BusinessException {
	public PetNotFoundException() {
		super(ErrorCode.PET_NOT_FOUND.getErrorCode(), ErrorCode.PET_NOT_FOUND.getMessage());
	}
}