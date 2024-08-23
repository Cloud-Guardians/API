package com.cloudians.domain.auth.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String userEmail;
    private String password;
}