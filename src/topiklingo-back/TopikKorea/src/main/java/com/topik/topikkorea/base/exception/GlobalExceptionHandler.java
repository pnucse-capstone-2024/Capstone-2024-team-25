package com.topik.topikkorea.base.exception;

import com.topik.topikkorea.base.exception.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
        return createErrorResponse(ex.exceptionType().httpStatus(), ex.exceptionType().errorMessage(),
                ex.exceptionType().errorCode());
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus originalStatus, String errorMessage,
                                                              int errorCode) {
        HttpStatus statusToReturn = determineHttpStatus(originalStatus);
        return ResponseEntity
                .status(statusToReturn)
                .body(new ErrorResponse(errorMessage, errorCode, true));
    }

    private HttpStatus determineHttpStatus(HttpStatus originalStatus) {
        return switch (originalStatus.series()) {
            case CLIENT_ERROR -> HttpStatus.BAD_REQUEST;
            case SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
            default -> originalStatus;
        };
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception e) {
        return ResponseEntity.internalServerError().build();
    }
}
