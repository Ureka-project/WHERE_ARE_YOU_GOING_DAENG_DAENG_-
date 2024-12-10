package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;

public class UserStoryNotFoundException extends BusinessException {
	public UserStoryNotFoundException() {
		super(ErrorCode.USER_STORY_NOT_FOUND.getErrorCode(), ErrorCode.USER_STORY_NOT_FOUND.getMessage());
	}
}