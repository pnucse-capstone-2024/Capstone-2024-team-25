package com.topik.topikkorea.center.exception;

import com.topik.topikkorea.base.exception.BaseException;
import com.topik.topikkorea.base.exception.BaseExceptionType;

public class CenterException extends BaseException {
    CenterExceptionType exceptionType;

    public CenterException(final CenterExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
