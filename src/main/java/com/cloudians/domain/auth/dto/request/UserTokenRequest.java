package com.cloudians.domain.auth.dto.request;

import lombok.Data;

@Data
public class UserTokenRequest {
    private String userEmail;
    private String tokenValue;
}