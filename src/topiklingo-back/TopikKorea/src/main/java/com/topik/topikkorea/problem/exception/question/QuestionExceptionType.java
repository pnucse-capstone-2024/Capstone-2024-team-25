package com.topik.topikkorea.problem.exception.question;

import com.topik.topikkorea.base.exception.BaseExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum QuestionExceptionType implements BaseExceptionType {
    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "문제를 찾을 수 없습니다.", 400611),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다.", 500612);


    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final int errorCode;

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
