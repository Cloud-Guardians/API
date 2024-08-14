package com.cloudians.global.exception;

import org.springframework.http.HttpStatus;

public enum FirebaseExceptionType  implements BaseExceptionType{
    TOKEN_EXPIRED(401, HttpStatus.UNAUTHORIZED, "파이어베이스 토큰이 만료되었습니다."),
    
   AUTHENTICATION_FAILED(401, HttpStatus.UNAUTHORIZED,"파이어베이스 인증에 실패하였습니다."),
   AUTHORIZATION_FAILED(401, HttpStatus.UNAUTHORIZED,"파이어베이스 인가에 실패하였습니다."),
   
   PHOTO_UPLOAD_FAILED(500, HttpStatus.INTERNAL_SERVER_ERROR, "사진 업로드에 실패하였습니다."),
   PHOTO_DELETION_FAILED(500, HttpStatus.INTERNAL_SERVER_ERROR, "사진 제거에 실패하였습니다."),
    PHOTO_VALUE_NOT_FOUND(400, HttpStatus.BAD_REQUEST, "사진 조회에 실패하였습니다."),
    PHOTO_VALUE_WRONG_INPUT(400, HttpStatus.BAD_REQUEST, "사진을 추가해 주세요.");
    
    private final int statusCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;
    
    FirebaseExceptionType(int statusCode, HttpStatus httpStatus, String errorMessage) {
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
