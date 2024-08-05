package com.cloudians.domain.personaldiary.exception;

import com.cloudians.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum PersonalDiaryExceptionType implements BaseExceptionType {
    WRONG_INPUT(400, HttpStatus.BAD_REQUEST, "값을 10단위로 입력해주세요."),
    NO_EMOTION_DATA(400, HttpStatus.BAD_REQUEST, "오늘의 감정을 입력하지 않았습니다."),
    PERSONAL_DIARY_ALREADY_EXIST(400, HttpStatus.BAD_REQUEST, "오늘 작성한 일기가 이미 존재합니다.");


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
