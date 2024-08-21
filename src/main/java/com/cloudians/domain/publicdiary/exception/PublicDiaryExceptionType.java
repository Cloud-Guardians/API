package com.cloudians.domain.publicdiary.exception;

import com.cloudians.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum PublicDiaryExceptionType implements BaseExceptionType {
    PUBLIC_DIARY_ALREADY_EXIST(400, HttpStatus.BAD_REQUEST, "오늘 공유한 일기가 이미 존재합니다."),
    NON_EXIST_PUBLIC_DIARY(400, HttpStatus.BAD_REQUEST, "존재하지 않는 일기입니다.");


    private final int statusCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    PublicDiaryExceptionType(int statusCode, HttpStatus httpStatus, String errorMessage) {
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