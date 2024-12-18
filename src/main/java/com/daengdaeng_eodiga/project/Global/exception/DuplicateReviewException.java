package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;

public class DuplicateReviewException extends BusinessException {
    public DuplicateReviewException() {
        super(ErrorCode.DUPLICATE_REVIEW_DAY.getErrorCode(), ErrorCode.DUPLICATE_REVIEW_DAY.getMessage());
    }
}