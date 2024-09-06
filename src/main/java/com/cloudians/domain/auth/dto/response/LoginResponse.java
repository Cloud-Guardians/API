package com.cloudians.domain.auth.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public static LoginResponse of(String accessToken, String refreshToken) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}

