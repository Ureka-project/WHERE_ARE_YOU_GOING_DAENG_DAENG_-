package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;

public class DuplicatePetException extends BusinessException {
	public DuplicatePetException() {
		super(ErrorCode.DUPLICATE_PET.getErrorCode(), ErrorCode.DUPLICATE_PET.getMessage());
	}
}
