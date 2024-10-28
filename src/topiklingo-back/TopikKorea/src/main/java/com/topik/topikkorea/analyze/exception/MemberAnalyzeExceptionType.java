package com.topik.topikkorea.analyze.exception;

import com.topik.topikkorea.base.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum MemberAnalyzeExceptionType implements BaseExceptionType {
    NOT_FOUND_MEMBER_ANALYZE(HttpStatus.NOT_FOUND, "해당 회원 분석을 찾을 수 없습니다.", 404501),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다.", 500502);

    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final int errorCode;

    MemberAnalyzeExceptionType(final HttpStatus httpStatus, final String errorMessage, final int errorCode) {
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
