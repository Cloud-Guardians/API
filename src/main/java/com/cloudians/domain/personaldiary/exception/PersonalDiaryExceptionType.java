package com.cloudians.domain.personaldiary.exception;

import com.cloudians.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum PersonalDiaryExceptionType implements BaseExceptionType {
    WRONG_INPUT(400, HttpStatus.BAD_REQUEST, "값을 10단위로 입력해주세요.");


    private final int statusCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    PersonalDiaryExceptionType(int statusCode, HttpStatus httpStatus, String errorMessage) {
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
