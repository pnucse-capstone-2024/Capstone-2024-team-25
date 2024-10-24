package com.topik.topikkorea.exam.exception;

import com.topik.topikkorea.base.exception.BaseException;
import com.topik.topikkorea.base.exception.BaseExceptionType;

public class ExamException extends BaseException {

    private final ExamExceptionType exceptionType;

    public ExamException(final ExamExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
