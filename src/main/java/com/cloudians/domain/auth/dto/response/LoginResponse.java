package com.cloudians.domain.auth.dto.response;


import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String fcmToken;

    @Builder
    private LoginResponse(String accessToken, String refreshToken, String fcmToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.fcmToken=fcmToken;
    }

}

