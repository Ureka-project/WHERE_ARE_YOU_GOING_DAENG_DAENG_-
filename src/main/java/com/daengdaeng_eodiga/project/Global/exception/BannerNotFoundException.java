package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;

public class BannerNotFoundException extends BusinessException {
	public BannerNotFoundException() {
		super(ErrorCode.BANNER_NOT_FOUND.getErrorCode(), ErrorCode.BANNER_NOT_FOUND.getMessage());
	}
}