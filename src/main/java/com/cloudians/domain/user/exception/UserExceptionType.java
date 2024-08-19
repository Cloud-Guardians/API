package com.cloudians.domain.user.exception;

import org.springframework.http.HttpStatus;

import com.cloudians.global.exception.BaseExceptionType;

public enum UserExceptionType implements BaseExceptionType {
    USER_NOT_FOUND(404, HttpStatus.NOT_FOUND, "해당 이메일을 가진 사용자 조회에 실패하였습니다."),
    USER_LOCK_NOT_FOUND(404, HttpStatus.NOT_FOUND, "사용자의 암호 조회에 실패하였습니다."),
    USER_LOCK_NOT_NULL(400,HttpStatus.BAD_REQUEST,"암호를 입력해 주세요"),
    USER_LOCK_LENGTH_INVALID(400,HttpStatus.BAD_REQUEST,"암호를 제대로 입력해 주세요"),
    USER_LOCK_DEACTIVATION_FAILED(401,HttpStatus.UNAUTHORIZED,"잘못된 접근입니다."),
    TOKEN_NOT_FOUND(404, HttpStatus.NOT_FOUND, "토큰 전체 조회에 실패하였습니다."),
    FCM_TOKEN_NOT_FOUND(404, HttpStatus.NOT_FOUND, "FCM 토큰 조회에 실패하였습니다."),
    TOKEN_EXPIRED(401, HttpStatus.UNAUTHORIZED,"토큰 유효 기간이 만료되었습니다."),
    LOCK_PASSWORD_INCORRECT(401, HttpStatus.UNAUTHORIZED, "록 암호가 올바르지 않습니다.");
    



    private final int statusCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    UserExceptionType(int statusCode, HttpStatus httpStatus, String errorMessage) {
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
