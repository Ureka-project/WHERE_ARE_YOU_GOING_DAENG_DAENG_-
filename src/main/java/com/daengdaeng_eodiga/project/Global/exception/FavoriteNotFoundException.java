package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;

public class FavoriteNotFoundException extends BusinessException {
	public FavoriteNotFoundException() {
		super(ErrorCode.FAVORITE_NOT_FOUND.getErrorCode(), ErrorCode.FAVORITE_NOT_FOUND.getMessage());
	}
}