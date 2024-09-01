package com.cloudians.domain.user.dto.response;

import com.cloudians.domain.user.dto.request.FcmNotificationRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

// 실제로 전달될 알림
@Getter
@Builder
public class FcmNotificationResponse {
    
    // 유효성 확인
    private boolean validateOnly;
    
    // 알림 메시지 담는 내부 클래스의 메시지 객체 
    private FcmNotificationResponse.Message message;

    // 내부 클래스의 메시지
    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
	// 내부 클래스의 알림: 알림의 세부 사항 정의
        private FcmNotificationResponse.Notification notification;
        // 사용자에게 전송될 토큰
        private String token;
    }

    // 내부 클래스 알림
    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
	// 알림 제목
        private String title;
        // 알림 내용
        private String body;
        // 알림에 포함될 이미지 url 
        private String image;
    }

    @Builder
    public static FcmNotificationResponse of(FcmNotificationRequest request) {
	FcmNotificationResponse.Notification notification = FcmNotificationResponse.Notification.builder()
		.title(request.getTitle())
		.body(request.getBody())
		.image(null)
		.build();
	FcmNotificationResponse.Message message = FcmNotificationResponse.Message.builder()
		.token(request.getToken())
		.notification(notification)
		.build();
	return FcmNotificationResponse.builder()
		.validateOnly(false)
		.message(message)
		.build();
    }
 
}
