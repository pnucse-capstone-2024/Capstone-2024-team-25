package com.topik.topikkorea.problem.exception.answer;

import com.topik.topikkorea.base.exception.BaseExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AnswerExceptionType implements BaseExceptionType {
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "정답를 찾을 수 없습니다.", 400621),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다.", 500622);


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
