package com.cloudians.domain.user.exception;

import org.springframework.http.HttpStatus;

import com.cloudians.global.exception.BaseExceptionType;

public enum NotificationExceptionType  implements BaseExceptionType{
    NOTIFICATION_NOT_FOUND(404,HttpStatus.BAD_REQUEST,"설정한 알림 내역 조회에 실패하였습니다."),
    NOTIFICATION_NOT_UPDATED(404,HttpStatus.BAD_REQUEST,"알림 업데이트에 실패하였습니다."),
    NOTIFICATION_SEND_FAILURE(500,HttpStatus.INTERNAL_SERVER_ERROR,"알림 전송 자체에 문제가 있습니다."),
    INVALID_TOKEN(401,HttpStatus.UNAUTHORIZED,"토큰이 유효하지 않습니다."),
    API_RATED_LIMIT_EXCEEDED(429, HttpStatus.TOO_MANY_REQUESTS,"api 호출 횟수가 제한을 초과했습니다."),
    UNAUTHORIZED_ACCESS(403,HttpStatus.FORBIDDEN,"요청한 작업에 대한 권한이 부족합니다."),
    NETWORK_ERROR(503,HttpStatus.SERVICE_UNAVAILABLE,"네트워크 연결로 인한 오류로 알림 수신에 실패하였습니다.")
    ;
    
    private final int statusCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;
    
    NotificationExceptionType(int statusCode, HttpStatus httpStatus, String errorMessage) {
        this.statusCode = statusCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getStatusCode() {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public HttpStatus getHttpStatus() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getErrorMessage() {
	// TODO Auto-generated method stub
	return null;
    }

}
