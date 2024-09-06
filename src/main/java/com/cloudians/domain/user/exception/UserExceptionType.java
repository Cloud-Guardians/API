package com.cloudians.domain.user.exception;

import com.cloudians.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum UserExceptionType implements BaseExceptionType {
    USER_NOT_FOUND(404, HttpStatus.NOT_FOUND, "해당 이메일을 가진 사용자 조회에 실패하였습니다."),
    USER_LOCK_NOT_FOUND(404, HttpStatus.NOT_FOUND, "사용자의 암호 조회에 실패하였습니다."),
    USER_ALREADY_EXIST(409, HttpStatus.CONFLICT, "이미 존재하는 회원 정보입니다."),
    NICKNAME_ALREADY_EXIST(409, HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    USER_LOCK_NOT_NULL(400, HttpStatus.BAD_REQUEST, "암호를 입력해 주세요"),
    USER_LOCK_LENGTH_INVALID(400, HttpStatus.BAD_REQUEST, "암호를 제대로 입력해 주세요"),
    USER_LOCK_DEACTIVATION_FAILED(401, HttpStatus.UNAUTHORIZED, "잘못된 접근입니다."),
    USER_GENDER_INVALID(400, HttpStatus.BAD_REQUEST, "잘못된 성별을 입력하였습니다."),
    USER_NOT_MATCHED(401, HttpStatus.UNAUTHORIZED, "사용자 이메일이 일치하지 않습니다."),
    TOKEN_NOT_FOUND(404, HttpStatus.NOT_FOUND, "토큰 전체 조회에 실패하였습니다."),
    FCM_TOKEN_NOT_FOUND(404, HttpStatus.NOT_FOUND, "FCM 토큰 조회에 실패하였습니다."),
    TOKEN_EXPIRED(401, HttpStatus.UNAUTHORIZED, "토큰 유효 기간이 만료되었습니다."),
    LOCK_PASSWORD_INCORRECT(401, HttpStatus.UNAUTHORIZED, "록 암호가 올바르지 않습니다."),
    WRONG_BIRTHTIME_TYPE(400, HttpStatus.BAD_REQUEST, "잘못된 생시 타입입니다."),
    TOKEN_INVALID(401, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    WRONG_PASSWORD(401, HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다."),
    NULL_TOKEN(401, HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),
    WRONG_CALENDAR_TYPE(400, HttpStatus.BAD_REQUEST, "잘못된 캘린더 타입입니다."),
    ALREADY_LOGOUT_TOKEN(401, HttpStatus.UNAUTHORIZED, "이미 로그아웃한 회원입니다."),
    PASSWORD_DO_NOT_MATCH(401, HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    UNAUTHORIZED_ACCESS(401, HttpStatus.UNAUTHORIZED, "접근 권한이 없는 사용자입니다.");


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