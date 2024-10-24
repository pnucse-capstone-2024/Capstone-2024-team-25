package com.topik.topikkorea.file.exception;

import com.topik.topikkorea.base.exception.BaseException;
import com.topik.topikkorea.base.exception.BaseExceptionType;

public class FileException extends BaseException {

    private final FileExceptionType exceptionType;

    public FileException(final FileExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
