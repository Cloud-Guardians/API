package com.cloudians.domain.publicdiary.exception;

import org.springframework.http.HttpStatus;

import com.cloudians.global.exception.BaseExceptionType;

public enum PublicDiaryExceptionType implements BaseExceptionType {
    PUBLIC_DIARY_ALREADY_EXIST(400, HttpStatus.BAD_REQUEST, "오늘 공유한 일기가 이미 존재합니다."),
    NON_EXIST_PUBLIC_DIARY(400, HttpStatus.BAD_REQUEST, "존재하지 않는 일기입니다."),
    NON_EXIST_PUBLIC_DIARY_COMMENT(400, HttpStatus.BAD_REQUEST, "존재하지 않는 댓글입니다."),
    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "잘못된 요청건입니다."),
    FORBIDDEN_USER(403, HttpStatus.FORBIDDEN, "접근 권한이 없는 사용자입니다."),
    SELF_REPORT(400, HttpStatus.BAD_REQUEST, "신고 대상이 올바르지 않습니다."),
    ALREADY_REPORT(400, HttpStatus.BAD_REQUEST, "이미 신고한 게시글입니다."),
    PUBLIC_DIARY_LIST_NOT_FOUND(404,HttpStatus.NOT_FOUND,"작성 일기가 존재하지 않습니다."),
    WRONG_SEARCH_TYPE(400, HttpStatus.BAD_REQUEST, "존재하지 않는 검색조건 입니다.");


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