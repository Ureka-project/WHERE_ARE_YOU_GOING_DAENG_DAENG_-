package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;

public class DuplicateUserException extends BusinessException {
    public DuplicateUserException() {
        super(ErrorCode.DUPLICATE_USER.getErrorCode(), ErrorCode.DUPLICATE_USER.getMessage());
    }
}