package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;

public class UserFailedSaveException extends BusinessException {
    public UserFailedSaveException() {
        super(ErrorCode.USER_FAILED_SAVE.getErrorCode(), ErrorCode.USER_FAILED_SAVE.getMessage());
    }
}
