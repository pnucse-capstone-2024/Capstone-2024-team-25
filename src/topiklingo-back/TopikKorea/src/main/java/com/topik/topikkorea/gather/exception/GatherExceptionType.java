package com.topik.topikkorea.gather.exception;

import com.topik.topikkorea.base.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum GatherExceptionType implements BaseExceptionType {
    NOT_FOUND_GROUP(HttpStatus.NOT_FOUND, "해당 그룹를 찾을 수 없습니다.", 404110),
    NOT_FOUND_GROUP_OFFER(HttpStatus.NOT_FOUND, "해당 그룹 오퍼을 찾을 수 없습니다.", 404111),
    ALREADY_EXIST_GROUP_OFFER(HttpStatus.BAD_REQUEST, "이미 해당 그룹 오퍼가 존재합니다.", 400112),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다.", 500113);

    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final int errorCode;

    GatherExceptionType(final HttpStatus httpStatus, final String errorMessage, final int errorCode) {
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
