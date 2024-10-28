package com.topik.topikkorea.exam.exception;

import com.topik.topikkorea.base.exception.BaseExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ExamExceptionType implements BaseExceptionType {

    EXAM_NOT_FOUND(HttpStatus.NOT_FOUND, "시험이 존재하지 않습니다.", 400401),
    MEMBER_EXAM_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 시험 기록이 존재하지 않습니다.", 400402),
    GROUP_EXAM_NOT_FOUND(HttpStatus.NOT_FOUND, "그룹 시험 기록이 존재하지 않습니다.", 400403);

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
