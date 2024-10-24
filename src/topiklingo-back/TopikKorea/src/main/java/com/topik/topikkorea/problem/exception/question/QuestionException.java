package com.topik.topikkorea.problem.exception.question;

import com.topik.topikkorea.base.exception.BaseException;
import com.topik.topikkorea.base.exception.BaseExceptionType;

public class QuestionException extends BaseException {

    private final QuestionExceptionType exceptionType;

    public QuestionException(final QuestionExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
