package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;

public class UserFailedDeleteException extends BusinessException {
    public UserFailedDeleteException() {
        super(ErrorCode.USER_FAILED_DELETE.getErrorCode(), ErrorCode.USER_FAILED_DELETE.getMessage());
    }
}
