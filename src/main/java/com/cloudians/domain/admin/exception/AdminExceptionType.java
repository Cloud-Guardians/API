package com.cloudians.domain.admin.exception;

import com.cloudians.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum AdminExceptionType implements BaseExceptionType {
    INVALID_ACTION(400, HttpStatus.BAD_REQUEST, "존재하지 않는 액션입니다."),
    NON_EXIST_REPORTED_COMMENT(404, HttpStatus.BAD_REQUEST, "해당 댓글이 존재하지 않습니다."),
    NON_EXIST_REPORTED_DIARY(404, HttpStatus.NOT_FOUND, "해당 일기가 존재하지 않습니다."),
    NOT_ENOUGH_REPORTS(400, HttpStatus.BAD_REQUEST, "중복되는 신고가 없습니다."),
    NO_REPORTS_FOR_COMMENT(404, HttpStatus.BAD_REQUEST, "해당 댓글에는 신고가 존재하지 않습니다."),
    NO_REPORTS_FOR_PUBLIC_DIARY(404, HttpStatus.BAD_REQUEST, "해당 일기에는 신고가 존재하지 않습니다."),
    REPORT_NOT_FOUND(404, HttpStatus.NOT_FOUND, "해당 신고가 존재하지 않습니다."),
    STATUS_NOT_FOUND(404, HttpStatus.NOT_FOUND, "해당 상태의 신고가 존재하지 않습니다.");


    private final int statusCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    AdminExceptionType(int statusCode, HttpStatus httpStatus, String errorMessage) {
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
