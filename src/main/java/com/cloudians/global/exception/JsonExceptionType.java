package com.cloudians.global.exception;

import org.springframework.http.HttpStatus;

public enum JsonExceptionType implements BaseExceptionType {

    INVALID_JSON_FORMAT(400, HttpStatus.BAD_REQUEST, "잘못된 JSON 형식입니다.");


    private final int statusCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    JsonExceptionType(int statusCode, HttpStatus httpStatus, String errorMessage) {
        this.statusCode = statusCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
