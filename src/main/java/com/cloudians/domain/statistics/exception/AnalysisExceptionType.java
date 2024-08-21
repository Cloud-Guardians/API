package com.cloudians.domain.statistics.exception;

import org.springframework.http.HttpStatus;

import com.cloudians.global.exception.BaseExceptionType;

public enum AnalysisExceptionType implements BaseExceptionType {
    MONTHLY_ANALYSIS_NOT_FOUND(404, HttpStatus.NOT_FOUND, "사용자의 월간 통계 내역 조회에 실패하였습니다."),
    WEEKLY_ANALYSIS_NOT_FOUND(404, HttpStatus.NOT_FOUND, "사용자의 주간 통계 내역 조회에 실패하였습니다."),
    ;
    



    private final int statusCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    AnalysisExceptionType(int statusCode, HttpStatus httpStatus, String errorMessage) {
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
