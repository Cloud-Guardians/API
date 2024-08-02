package com.cloudians.global.exception;

import org.springframework.http.HttpStatus;

public interface BaseExceptionType {
    int getStatusCode();

    HttpStatus getHttpStatus();

    String getErrorMessage();
}
