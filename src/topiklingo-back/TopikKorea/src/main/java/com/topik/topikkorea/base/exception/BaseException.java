package com.topik.topikkorea.base.exception;

public abstract class BaseException extends RuntimeException {

    protected BaseException(final String message) {
        super(message);
    }

    public abstract BaseExceptionType exceptionType();
}
