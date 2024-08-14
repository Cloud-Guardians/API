package com.cloudians.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@NoArgsConstructor
public class UserNotificationRequest {
    private String token;

    private String title;

    private String body;
    
    @Builder
    public UserNotificationRequest(String token, String title, String body) {
        this.token = token;
        this.title = title;
        this.body = body;
    }

}
