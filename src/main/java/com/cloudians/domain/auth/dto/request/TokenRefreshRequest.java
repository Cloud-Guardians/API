package com.cloudians.domain.auth.dto.request;


import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class TokenRefreshRequest {
    @NotBlank(message = "리프레시 토큰을 넣어 주세요.")
    private String refreshToken;
}

