package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;

public class DuplicateFavoriteException extends BusinessException {
	public DuplicateFavoriteException() {
		super(ErrorCode.DUPLICATE_FAVORITE.getErrorCode(), ErrorCode.DUPLICATE_FAVORITE.getMessage());
	}
}
