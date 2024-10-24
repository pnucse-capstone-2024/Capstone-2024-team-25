package com.topik.topikkorea.base.exception;

import org.springframework.http.HttpStatus;

public interface BaseExceptionType {

    HttpStatus httpStatus();

    String errorMessage();

    int errorCode();
}
