package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;

public class OwnerHistoryNotFoundException extends BusinessException {
	public OwnerHistoryNotFoundException() {
		super(ErrorCode.OWNER_HISTORY_NOT_FOUND.getErrorCode(), ErrorCode.OWNER_HISTORY_NOT_FOUND.getMessage());
	}
}