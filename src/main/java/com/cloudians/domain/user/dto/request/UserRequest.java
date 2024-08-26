package com.cloudians.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Builder
@Data
@AllArgsConstructor
public class UserRequest {

    private String userEmail;
    private int signupType;
    private String password;
    private String name;
    private String profileUrl;
    private String nickname;
    private char gender;
    private String calendarType;
    private Date birthdate;
    private String birthTime;
    private int status;

    public UserRequest() {
    }
}