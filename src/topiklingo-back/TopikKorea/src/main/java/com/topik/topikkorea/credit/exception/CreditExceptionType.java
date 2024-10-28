package com.topik.topikkorea.credit.exception;

import com.topik.topikkorea.base.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum CreditExceptionType implements BaseExceptionType {
    GET_UNUSED_CREDITS_ERROR(HttpStatus.NOT_FOUND, "사용하지 않은 크레딧 존재하지 않습니다.", 404901);

    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final int errorCode;

    CreditExceptionType(final HttpStatus httpStatus, final String errorMessage, final int errorCode) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }

    @Override
    public int errorCode() {
        return errorCode;
    }
}
