package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;

public class PlaceNotFoundException extends BusinessException {
	public PlaceNotFoundException() {
		super(ErrorCode.PLACE_NOT_FOUND.getErrorCode(), ErrorCode.PLACE_NOT_FOUND.getMessage());
	}

	public PlaceNotFoundException(String message) {
		super(ErrorCode.PLACE_NOT_FOUND.getErrorCode(), message);
	}
}
