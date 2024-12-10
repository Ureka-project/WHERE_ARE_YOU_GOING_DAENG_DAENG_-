package com.daengdaeng_eodiga.project.Global.exception;


import org.springframework.http.HttpStatus;

public class RealtimeReviewException extends BusinessException {

    public RealtimeReviewException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}