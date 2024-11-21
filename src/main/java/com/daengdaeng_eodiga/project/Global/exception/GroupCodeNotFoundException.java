package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;

public class GroupCodeNotFoundException extends BusinessException {
	public GroupCodeNotFoundException() {
		super(ErrorCode.GROUP_CODE_NOT_FOUND.getErrorCode(), ErrorCode.GROUP_CODE_NOT_FOUND.getMessage());
	}
}