package com.cloudians.domain.auth.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {
    private String userEmail;
    private String password;

    @Builder
    private LoginRequest(String userEmail, String password) {
        this.userEmail = userEmail;
        this.password = password;
    }

}

