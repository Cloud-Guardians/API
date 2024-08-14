package com.cloudians.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

// 실제로 전달될 알림
@Getter
@Builder
public class UserNotificationResponse {
    
    private boolean validateOnly;           
    private UserNotificationResponse.Message message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private UserNotificationResponse.Notification notification;
        private String token;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }

 
}
