package com.cloudians.domain.home.exception;

import com.cloudians.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum WhisperExceptionType implements BaseExceptionType {
    NON_EXIST_WHISPER_QUESTION(400, HttpStatus.BAD_REQUEST, "존재하지 않는 위스퍼 질문입니다."),
    ALREADY_EXIST_MESSAGE(400, HttpStatus.BAD_REQUEST, "해당 질문에 이미 답변하셨습니다."),
    NO_MORE_DATA(400, HttpStatus.BAD_REQUEST, "더이상 가져올 데이터가 없습니다.");


    private final int statusCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    WhisperExceptionType(int statusCode, HttpStatus httpStatus, String errorMessage) {
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