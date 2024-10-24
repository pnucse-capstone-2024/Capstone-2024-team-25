package com.topik.topikkorea.center.exception;

import com.topik.topikkorea.base.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum CenterExceptionType implements BaseExceptionType {
    NOT_FOUND_CENTER(HttpStatus.NOT_FOUND, "해당 센터를 찾을 수 없습니다.", 404111),
    NOT_FOUND_CENTER_OFFER(HttpStatus.NOT_FOUND, "해당 센터 오퍼을 찾을 수 없습니다.", 404112),
    ALREADY_OFFER_CENTER(HttpStatus.BAD_REQUEST, "이미 센터에 가입 오퍼를 넣었습니다.", 400113),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다.", 500110);

    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final int errorCode;

    CenterExceptionType(final HttpStatus httpStatus, final String errorMessage, final int errorCode) {
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
