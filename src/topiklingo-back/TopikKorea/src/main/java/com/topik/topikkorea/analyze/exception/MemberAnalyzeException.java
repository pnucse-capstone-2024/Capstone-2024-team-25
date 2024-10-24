package com.topik.topikkorea.analyze.exception;

import com.topik.topikkorea.base.exception.BaseException;
import com.topik.topikkorea.base.exception.BaseExceptionType;

public class MemberAnalyzeException extends BaseException {
    MemberAnalyzeExceptionType exceptionType;

    public MemberAnalyzeException(final MemberAnalyzeExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
