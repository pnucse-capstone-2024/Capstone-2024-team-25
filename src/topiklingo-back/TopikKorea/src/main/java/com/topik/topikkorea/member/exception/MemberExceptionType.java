package com.topik.topikkorea.member.exception;

import com.topik.topikkorea.base.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum MemberExceptionType implements BaseExceptionType {
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 Access Token입니다.", 401102),
    NOT_FOUND_GROUP_MEMBER(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다.", 404106),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Access Token입니다.", 401103),
    UNAUTHORIZED_PERMISSION(HttpStatus.UNAUTHORIZED, "권한이 없는 유저입니다.", 401104),
    NOT_FOUND_GOOGLE_ACCESS_TOKEN_RESPONSE(HttpStatus.NOT_FOUND, "구글 AccessToken 응답이 없습니다.", 404105),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다.", 404106),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다.", 500107);

    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final int errorCode;

    MemberExceptionType(final HttpStatus httpStatus, final String errorMessage, final int errorCode) {
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
