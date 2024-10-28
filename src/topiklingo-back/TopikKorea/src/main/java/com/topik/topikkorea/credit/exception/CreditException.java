package com.topik.topikkorea.credit.exception;

import com.topik.topikkorea.base.exception.BaseException;
import com.topik.topikkorea.base.exception.BaseExceptionType;

public class CreditException extends BaseException {
    CreditExceptionType exceptionType;

    public CreditException(final CreditExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
