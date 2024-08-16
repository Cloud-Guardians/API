package com.cloudians.domain.personaldiary.exception;

import com.cloudians.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum PersonalDiaryExceptionType implements BaseExceptionType {
    EMOTION_VALUE_WRONG_INPUT(400, HttpStatus.BAD_REQUEST, "값을 10단위로 입력해주세요."),
    NO_EMOTION_DATA(400, HttpStatus.BAD_REQUEST, "오늘의 감정을 입력하지 않았습니다."),
    PERSONAL_DIARY_ALREADY_EXIST(400, HttpStatus.BAD_REQUEST, "오늘 작성한 일기가 이미 존재합니다."),
    NON_EXIST_PERSONAL_DIARY(400, HttpStatus.BAD_REQUEST, "존재하지 않는 일기입니다."),
    EMOTION_VALUE_OUT_OF_RANGE(400, HttpStatus.BAD_REQUEST, "최소 0에서 최대 100사이의 값을 입력해주세요."),
    NON_EXIST_PERSONAL_DIARY_PHOTO(400, HttpStatus.BAD_REQUEST, "존재하지 않는 사진입니다."),
    WRONG_CHATGPT_ANSWER_FORMAT(500, HttpStatus.INTERNAL_SERVER_ERROR, "잘못된 gpt 응답 형식입니다."),
    COUDNT_FOUND_ELEMENT(500, HttpStatus.INTERNAL_SERVER_ERROR, "응답에 맞는 음양오행 요소가 없습니다."),
    NON_EXIST_PERSONAL_DIARY_ANALYSIS(400, HttpStatus.BAD_REQUEST, "존재하지 않는 일기 분석입니다.");


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