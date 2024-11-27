package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;

public class NotificationNotFoundException extends BusinessException {
	public NotificationNotFoundException() {
		super(ErrorCode.NOTI_NOT_FOUND.getErrorCode(), ErrorCode.NOTI_NOT_FOUND.getMessage());
	}
}