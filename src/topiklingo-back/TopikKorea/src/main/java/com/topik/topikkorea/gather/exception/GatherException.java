package com.topik.topikkorea.gather.exception;

import com.topik.topikkorea.base.exception.BaseException;
import com.topik.topikkorea.base.exception.BaseExceptionType;

public class GatherException extends BaseException {
    GatherExceptionType exceptionType;

    public GatherException(final GatherExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
