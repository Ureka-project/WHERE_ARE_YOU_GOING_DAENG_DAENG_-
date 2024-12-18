package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;
/**
 * 방문 예정 일정이 겹치면 발생하는 예외
 *
 * @author 김가은
 * */
public class DuplicateVisitException extends BusinessException {
    public DuplicateVisitException() {
        super(ErrorCode.DUPLICATE_VISIT.getErrorCode(), ErrorCode.DUPLICATE_VISIT.getMessage());
    }
}