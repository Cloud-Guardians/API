package com.cloudians.domain.auth.dto.response;

import lombok.Data;

@Data
public class UserTokenResponse {
    private String userEmail;
    private String tokenValue; 
    private String tokenType; 
}