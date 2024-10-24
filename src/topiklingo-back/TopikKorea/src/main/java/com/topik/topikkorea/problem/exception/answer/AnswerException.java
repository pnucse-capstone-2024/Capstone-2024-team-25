package com.topik.topikkorea.problem.exception.answer;

import com.topik.topikkorea.base.exception.BaseException;
import com.topik.topikkorea.base.exception.BaseExceptionType;

public class AnswerException extends BaseException {

    private final AnswerExceptionType exceptionType;

    public AnswerException(final AnswerExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
